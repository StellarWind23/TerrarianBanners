package com.github.stellarwind23.terrarian_banners.client.render;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TBBannerColorManager extends SimpleJsonResourceReloadListener<TBBannerColors> {

    public static final TBBannerColorManager INSTANCE = new TBBannerColorManager();

    private Map<Identifier, TBBannerColors> colors = Map.of();
    private final Map<Identifier, TBBannerColors> fallbacks = new HashMap<>();

    private TBBannerColorManager() {
        super(TBBannerColors.CODEC, FileToIdConverter.json("tb_banner_colors"));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBBannerColors> prepared, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        colors = Map.copyOf(prepared);
        fallbacks.clear();
        TBMobFaceTextures.clearCache();   // this also fixes the face cache not reloading on F3+T
    }

    public TBBannerColors get(@Nullable Identifier mob) {
        if (mob == null) return TBBannerColors.NEUTRAL;
        TBBannerColors defined = colors.get(mob);
        return defined != null ? defined : TBBannerColors.NEUTRAL;
    }
}