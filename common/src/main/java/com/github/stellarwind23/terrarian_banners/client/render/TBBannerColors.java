package com.github.stellarwind23.terrarian_banners.client.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;

public record TBBannerColors(TextColor base, TextColor secondary, TextColor accent) {

    public static final Codec<Integer> COLOR = TextColor.CODEC.xmap(TextColor::getValue, TextColor::fromRgb);

    public static final Codec<TBBannerColors> CODEC = RecordCodecBuilder.create(i -> i.group(
            TextColor.CODEC.fieldOf("base").forGetter(TBBannerColors::base),
            TextColor.CODEC.fieldOf("secondary").forGetter(TBBannerColors::secondary),
            TextColor.CODEC.fieldOf("accent").forGetter(TBBannerColors::accent)
    ).apply(i, TBBannerColors::new));

    public static final TBBannerColors NEUTRAL = new TBBannerColors(
            TextColor.fromLegacyFormat(ChatFormatting.BLACK),
            TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE),
            TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)
    );

    /** Opaque ARGB for vertex colors. */
    public static int argb(int rgb) { return 0xFF000000 | rgb; }
}