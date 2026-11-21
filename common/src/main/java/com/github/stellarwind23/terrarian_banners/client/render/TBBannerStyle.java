package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.*;

/** Layers are drawn in list order: the first is the bottom layer, the last is on top. */
public record TBBannerStyle(List<Layer> layers) {

    public static TBBannerStyle fromLayers(Layer... layers) {
        return new TBBannerStyle(Arrays.stream(layers).toList());
    }

    public TBBannerStyle {
        layers = List.copyOf(layers);
    }

    /** One full 64x64 sheet in vanilla's banner layout, optionally tinted. */
    public record Layer(String name, Identifier texture, Optional<TextColor> color) {

        public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("name").forGetter(Layer::name),
                Identifier.CODEC.fieldOf("texture").forGetter(Layer::texture),
                TextColor.CODEC.optionalFieldOf("color").forGetter(Layer::color)
        ).apply(i, Layer::new));

        /** A texture in this mod's namespace, e.g. asset("base", "entity/mob_banner/base/default"). */
        public static Layer asset(String name, String path) {
            return new Layer(name, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path), Optional.empty());
        }

        public Layer withColor(TextColor color) { return new Layer(name, texture, Optional.of(color)); }
        public Layer withColor(int rgb) { return withColor(TextColor.fromRgb(rgb)); }

        /** Opaque ARGB, or white (no tint) if no color is set. */
        public int tint() {
            return color.map(c -> 0xFF000000 | c.getValue()).orElse(-1);
        }
    }

    public static final Codec<TBBannerStyle> CODEC = RecordCodecBuilder.create(i -> i.group(
            Layer.CODEC.listOf().validate(TBBannerStyle::checkUniqueNames)
                    .fieldOf("layers").forGetter(TBBannerStyle::layers)
    ).apply(i, TBBannerStyle::new));

    public static Identifier NEUTRAL_ID = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "null");
    public static final TBBannerStyle NEUTRAL = TBBannerStyle.fromLayers(
            Layer.asset("base", "entity/mob_banner/base/default").withColor(TextColor.fromLegacyFormat(ChatFormatting.BLACK)),
            Layer.asset("border", "entity/mob_banner/detail/border").withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE)),
            Layer.asset("thin_border", "entity/mob_banner/detail/thin_border").withColor(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
            Layer.asset("emblem", "entity/mob_banner/emblem/default")
    );

    private static DataResult<List<Layer>> checkUniqueNames(List<Layer> layers) {
        Set<String> seen = new HashSet<>();
        for (Layer layer : layers) {
            if (!seen.add(layer.name())) {
                return DataResult.error(() -> "Duplicate banner layer name: " + layer.name());
            }
        }
        return DataResult.success(layers);
    }
}