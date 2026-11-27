package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** Layers are drawn in list order: the first is the bottom layer, the last is on top. */
public record TBTextureStyle(List<Layer> layers) {

    public TBTextureStyle {
        layers = List.copyOf(layers);
    }

    public static TBTextureStyle fromLayers(Layer... layers) {
        return new TBTextureStyle(List.of(layers));
    }

    public Optional<Layer> layer(String name) {
        return layers.stream().filter(l -> l.name().equals(name)).findFirst();
    }

    /** Where a copy of a layer's texture is placed on the canvas, in pixels from the top-left. */
    public record Offset(int x, int y) {
        public static final Offset ORIGIN = new Offset(0, 0);
        public static final Codec<Offset> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("x").forGetter(Offset::x),
                Codec.INT.fieldOf("y").forGetter(Offset::y)
        ).apply(i, Offset::new));
    }

    /** texture is the path under textures/ without the extension, e.g. terrarian_banners:entity/mob_banner/base/sky. */
    public record Layer(String name, Identifier texture, List<Offset> offsets, Optional<TextColor> color) {

        public Layer {
            offsets = List.copyOf(offsets);
        }

        public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("name").forGetter(Layer::name),
                Identifier.CODEC.fieldOf("texture").forGetter(Layer::texture),
                Offset.CODEC.listOf().optionalFieldOf("offsets", List.of(Offset.ORIGIN)).forGetter(Layer::offsets),
                TextColor.CODEC.optionalFieldOf("color").forGetter(Layer::color)
        ).apply(i, Layer::new));

        /** A texture in any namespace. */
        public static Layer of(String name, Identifier texture) {
            return new Layer(name, texture, List.of(Offset.ORIGIN), Optional.empty());
        }

        /** A texture in this mod's namespace. */
        public static Layer mod(String name, String path) {
            return of(name, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path));
        }

        /** Replaces where copies of this layer are placed. */
        public Layer at(Offset... offsets) {
            return new Layer(name, texture, List.of(offsets), color);
        }

        public Layer withColor(TextColor color) {
            return new Layer(name, texture, offsets, Optional.of(color));
        }

        public Layer withColor(int rgb) {
            if (rgb < 0 || rgb > 0xFFFFFF) {
                throw new IllegalArgumentException("Not a 24-bit RGB colour: 0x" + Integer.toHexString(rgb));
            }
            return withColor(TextColor.fromRgb(rgb));
        }

        /** Opaque ARGB, or white (no tint) if no colour is set. */
        public int tint() {
            return color.map(c -> 0xFF000000 | c.getValue()).orElse(-1);
        }
    }

    public static final Codec<TBTextureStyle> CODEC = RecordCodecBuilder.create(i -> i.group(
            Layer.CODEC.listOf().validate(TBTextureStyle::checkUniqueNames)
                    .fieldOf("layers").forGetter(TBTextureStyle::layers)
    ).apply(i, TBTextureStyle::new));

    // ---- Placement of the emblem on each canvas. Emblem PNGs are 16x16. ----
    private static final Offset BANNER_EMBLEM_FRONT = new Offset(3, 12);
    private static final Offset BANNER_EMBLEM_BACK = new Offset(24, 12);
    private static final Offset EFFECT_EMBLEM = new Offset(1, 1);

    public static final Layer NULL_EMBLEM = Layer.mod("emblem", "entity/mob_banner/emblem/default");
    public static final Layer EFFECT_BACKGROUND = Layer.mod("background", "effect/background");

    /** An emblem placed on both faces of the flag. */
    public static Layer bannerEmblem(String path) {
        return Layer.mod("emblem", path).at(BANNER_EMBLEM_FRONT, BANNER_EMBLEM_BACK);
    }

    /** The effect icon for a banner style: the effect background with the banner's emblem centred on it. */
    public static TBTextureStyle effectFrom(TBTextureStyle banner) {
        return fromLayers(EFFECT_BACKGROUND, banner.layer("emblem").orElse(NULL_EMBLEM).at(EFFECT_EMBLEM));
    }

    // Placeholders for mobs without a style
    public static final TBTextureStyle NULL_BANNER = fromLayers(
            Layer.mod("base", "entity/mob_banner/base/default").withColor(TextColor.fromLegacyFormat(ChatFormatting.BLACK)),
            Layer.mod("border", "entity/mob_banner/detail/border").withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE)),
            Layer.mod("thin_border", "entity/mob_banner/detail/thin_border").withColor(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
            NULL_EMBLEM.at(BANNER_EMBLEM_FRONT, BANNER_EMBLEM_BACK)
    );
    public static final TBTextureStyle NULL_EFFECT = effectFrom(NULL_BANNER);

    public static TBTextureStyle nullFor(TBStyleType kind) {
        return switch (kind) {
            case MOB_BANNER -> NULL_BANNER;
            case EFFECT -> NULL_EFFECT;
        };
    }

    private static DataResult<List<Layer>> checkUniqueNames(List<Layer> layers) {
        Set<String> seen = new HashSet<>();
        for (Layer layer : layers) {
            if (!seen.add(layer.name())) {
                return DataResult.error(() -> "Duplicate texture layer name: " + layer.name());
            }
        }
        return DataResult.success(layers);
    }
}