package com.github.stellarwind23.terrarian_banners.client.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public record TBStyleType(String folder, int size, Map<String, List<Offset>> offsets, Identifier nullStyleId) {

    public static final String TYPE_FOLDER = "tb_texture_types";

    /*private final TBTextureStyle.Layer NULL_EMBLEM = TBTextureStyle.Layer.mod("emblem", "entity/mob_banner/emblem/default");
    private static final TBTextureStyle.Offset BANNER_EMBLEM_FRONT = new TBTextureStyle.Offset(3, 12);
    private static final TBTextureStyle.Offset BANNER_EMBLEM_BACK = new TBTextureStyle.Offset(24, 12);*/

    /*EFFECT("effect", 18, TBTextureStyle.fromLayers(TBTextureStyle.Layer.mod("background", "effect/background"), NULL_EMBLEM));
    MOB_BANNER("mob_banner", 64, TBTextureStyle.fromLayers(
            TBTextureStyle.Layer.mod("base", "entity/mob_banner/base/default").withColor(TextColor.fromLegacyFormat(ChatFormatting.BLACK)),
            TBTextureStyle.Layer.mod("border", "entity/mob_banner/detail/border").withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE)),
            TBTextureStyle.Layer.mod("thin_border", "entity/mob_banner/detail/thin_border").withColor(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
            NULL_EMBLEM.at(BANNER_EMBLEM_FRONT, BANNER_EMBLEM_BACK))),*/

    public String styleFolder() { return "tb_texture_styles/" + folder; }

    public record Offset(int x, int y) {
        public static final Offset ORIGIN = new Offset(0, 0);
        public static final Codec<Offset> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("x").forGetter(Offset::x),
                Codec.INT.fieldOf("y").forGetter(Offset::y)
        ).apply(i, Offset::new));
    }

    public static final Codec<TBStyleType> CODEC = RecordCodecBuilder.create(t -> t.group(
            Codec.STRING.fieldOf("folder").forGetter(TBStyleType::folder),
            Codec.INT.fieldOf("size").forGetter(TBStyleType::size),
            Codec.unboundedMap(Codec.STRING, Offset.CODEC.listOf()).fieldOf("offsets").forGetter(TBStyleType::offsets),
            Identifier.CODEC.fieldOf("null_style_id").forGetter(TBStyleType::nullStyleId)
    ).apply(t, TBStyleType::new));
}