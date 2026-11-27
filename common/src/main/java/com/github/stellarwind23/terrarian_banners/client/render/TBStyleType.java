package com.github.stellarwind23.terrarian_banners.client.render;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class TBStyleType {

    private static final Map<String, TBStyleType> TYPES = new LinkedHashMap<>();

    // Emblem placement on the vanilla shield sheet's front face (12x22 at 1,1). Estimates: tune in-game.
    private static final TBTextureStyle.Offset SHIELD_EMBLEM = new TBTextureStyle.Offset(-1, 4);
    // Vanilla's shield sheet, kept underneath so the handle and rim still show. Verify this path in the client jar.
    private static final TBTextureStyle.Layer VANILLA_SHIELD_FRAME = TBTextureStyle.Layer.of("frame", Identifier.withDefaultNamespace("entity/shield_base_nopattern"));

    /** The authored type: the datagen table writes these, everything else is derived from them. */
    public static final TBStyleType MOB_BANNER = register("mob_banner", 64, 64, UnaryOperator.identity());
    public static final TBStyleType EFFECT = register("effect", 18, 18, TBTextureStyle::effectFrom);
    public static final TBStyleType SHIELD = shield("vanilla", 64, 64, SHIELD_EMBLEM, VANILLA_SHIELD_FRAME);

    private final String id;
    private final int width, height;
    private final UnaryOperator<TBTextureStyle> derive;

    private TBStyleType(String id, int width, int height, UnaryOperator<TBTextureStyle> derive) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.derive = derive;
    }

    /** id may contain '/', e.g. "shield_mob_banner/round". Namespace it by convention if you're another mod. */
    public static synchronized TBStyleType register(String id, int width, int height, UnaryOperator<TBTextureStyle> derive) {
        if (TYPES.containsKey(id)) throw new IllegalStateException("Duplicate texture style type: " + id);
        TBStyleType type = new TBStyleType(id, width, height, derive);
        TYPES.put(id, type);
        return type;
    }

    /**
     * A shield whose sheet mirrors the banner layout but with its own textures:
     * base/detail layers move from entity/mob_banner/... to entity/shield_mob_banner/<shape>/...,
     * emblems are shared, and the emblem lands at the given offset.
     */
    public static TBStyleType shield(String shape, int width, int height,
                                     TBTextureStyle.Offset emblem, TBTextureStyle.Layer... leading) {
        return register("shield_mob_banner/" + shape, width, height, TBTextureStyle.remapper(
                "entity/mob_banner/", "entity/shield_mob_banner/" + shape + "/", List.of(emblem), List.of(leading)));
    }

    public static synchronized Collection<TBStyleType> all() { return List.copyOf(TYPES.values()); }
    public static synchronized @Nullable TBStyleType byId(String id) { return TYPES.get(id); }

    public String id() { return id; }
    public int width() { return width; }
    public int height() { return height; }
    public String styleFolder() { return "tb_texture_styles/" + id; }

    public TBTextureStyle derive(TBTextureStyle banner) { return derive.apply(banner); }
    public TBTextureStyle nullStyle() { return derive.apply(TBTextureStyle.NULL_BANNER); }
}