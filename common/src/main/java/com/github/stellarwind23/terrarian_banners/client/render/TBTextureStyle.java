package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

    /** texture is the path under textures/ without the extension, e.g. terrarian_banners:entity/mob_banner/base/sky. */
    public record Layer(String name, Identifier texture, Optional<TextColor> color) {

        public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("name").forGetter(Layer::name),
                Identifier.CODEC.fieldOf("texture").forGetter(Layer::texture),
                TextColor.CODEC.optionalFieldOf("color").forGetter(Layer::color)
        ).apply(i, Layer::new));

        /** A texture in any namespace. */
        public static Layer of(String name, Identifier texture) {
            return new Layer(name, texture, Optional.empty());
        }

        /** A texture in this mod's namespace. */
        public static Layer mod(String name, String path) {
            return of(name, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path));
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