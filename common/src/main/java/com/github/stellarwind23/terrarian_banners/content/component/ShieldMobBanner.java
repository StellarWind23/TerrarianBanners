package com.github.stellarwind23.terrarian_banners.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record ShieldMobBanner(Identifier mob, String style) {
    public static final Codec<ShieldMobBanner> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("mob").forGetter(ShieldMobBanner::mob),
            Codec.STRING.fieldOf("style").forGetter(ShieldMobBanner::style)
    ).apply(i, ShieldMobBanner::new));

    public static final StreamCodec<ByteBuf, ShieldMobBanner> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ShieldMobBanner::mob,
            ByteBufCodecs.STRING_UTF8, ShieldMobBanner::style,
            ShieldMobBanner::new);
}
