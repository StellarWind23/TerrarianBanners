package com.github.stellarwind23.terrarian_banners.client.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.TextColor;

public record TBBannerColors(int main, int secondary, int accent) {

    public static final Codec<Integer> COLOR = TextColor.CODEC.xmap(TextColor::getValue, TextColor::fromRgb);

    public static final Codec<TBBannerColors> CODEC = RecordCodecBuilder.create(i -> i.group(
            COLOR.fieldOf("main").forGetter(TBBannerColors::main),
            COLOR.fieldOf("secondary").forGetter(TBBannerColors::secondary),
            COLOR.fieldOf("accent").forGetter(TBBannerColors::accent)
    ).apply(i, TBBannerColors::new));

    public static final TBBannerColors NEUTRAL = new TBBannerColors(0x555555, 0xAAAAAA, 0x222222);

    /** Opaque ARGB for vertex colors. */
    public static int argb(int rgb) { return 0xFF000000 | rgb; }
}