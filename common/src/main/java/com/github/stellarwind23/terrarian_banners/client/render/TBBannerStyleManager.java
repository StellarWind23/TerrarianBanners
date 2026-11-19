package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class TBBannerStyleManager extends SimpleJsonResourceReloadListener<TBBannerStyle> {

    public static final TBBannerStyleManager INSTANCE = new TBBannerStyleManager();

    private Map<Identifier, TBBannerStyle> styles = Map.of();

    private TBBannerStyleManager() {
        super(TBBannerStyle.CODEC, FileToIdConverter.json("tb_banner_styles"));
    }

    @Override
    protected void apply(
            @NonNull Map<Identifier, TBBannerStyle> prepared,
            @NonNull ResourceManager manager,
            @NonNull ProfilerFiller profiler
    ) {
        styles = Map.copyOf(prepared);

        TerrarianBanners.LOGGER.info(
                "Loaded {} banner styles: {}",
                styles.size(),
                styles.keySet()
        );

        TBBannerTextures.clear();
    }

    public TBBannerStyle get(@Nullable Identifier mob) {
        if (mob == null) return TBBannerStyle.NEUTRAL;
        TBBannerStyle defined = styles.get(mob);
        return defined != null ? defined : TBBannerStyle.NEUTRAL;
    }
}