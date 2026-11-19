package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record TBBannerStyle(Layer icon, Layer base, Layer secondary, Layer accent) {

    public record Layer(Identifier texture, Optional<TextColor> color) {

        private static final Codec<Layer> FULL = RecordCodecBuilder.create(i -> i.group(
                Identifier.CODEC.fieldOf("texture").forGetter(Layer::texture),
                TextColor.CODEC.optionalFieldOf("color").forGetter(Layer::color)
        ).apply(i, Layer::new));

        // A bare "namespace:path" string is shorthand for an untinted layer
        public static final Codec<Layer> CODEC = Codec.withAlternative(FULL,
                Identifier.CODEC.xmap(id -> new Layer(id, Optional.empty()), Layer::texture));

        /** A texture in this mod's namespace, e.g. asset("entity/mob_banner/base/default"). */
        public static Layer asset(String path) {
            return new Layer(Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path), Optional.empty());
        }

        public Layer withColor(TextColor color) { return new Layer(texture, Optional.of(color)); }
        public Layer withColor(int rgb) { return withColor(TextColor.fromRgb(rgb)); }

        /** Vertex colour: opaque ARGB, or white (no tint) if no colour is set. */
        public int tint() {
            return color.map(c -> 0xFF000000 | c.getValue()).orElse(-1);
        }
    }

    public static final Layer DEFAULT_ICON      = Layer.asset("entity/mob_banner/icon/default");
    public static final Layer DEFAULT_BASE      = Layer.asset("entity/mob_banner/base/default");
    public static final Layer DEFAULT_SECONDARY = Layer.asset("entity/mob_banner/secondary/default");
    public static final Layer DEFAULT_ACCENT    = Layer.asset("entity/mob_banner/accent/default");

    public static final Codec<TBBannerStyle> CODEC = RecordCodecBuilder.create(i -> i.group(
            Layer.CODEC.fieldOf("icon").forGetter(TBBannerStyle::icon),
            Layer.CODEC.optionalFieldOf("base", DEFAULT_BASE).forGetter(TBBannerStyle::base),
            Layer.CODEC.optionalFieldOf("secondary", DEFAULT_SECONDARY).forGetter(TBBannerStyle::secondary),
            Layer.CODEC.optionalFieldOf("accent", DEFAULT_ACCENT).forGetter(TBBannerStyle::accent)
    ).apply(i, TBBannerStyle::new));

    public static final TBBannerStyle NEUTRAL = new TBBannerStyle(
            DEFAULT_ICON,
            DEFAULT_BASE.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLACK)),
            DEFAULT_SECONDARY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE)),
            DEFAULT_ACCENT.withColor(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)));
}