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

public class TBTextureStyleManager extends SimpleJsonResourceReloadListener<TBTextureStyle> {

    public static final TBTextureStyleManager INSTANCE = new TBTextureStyleManager();

    private Map<Identifier, TBTextureStyle> styles = Map.of();

    private TBTextureStyleManager() {
        super(TBTextureStyle.CODEC, FileToIdConverter.json("tb_banner_styles"));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBTextureStyle> prepared, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        styles = Map.copyOf(prepared);
        TerrarianBanners.LOGGER.info("Loaded {} banner styles", styles.size());
        TBBannerTextures.reload(styles, manager);
    }

    public TBTextureStyle get(@Nullable Identifier mob) {
        return mob == null ? TBTextureStyle.NULL_STYLE : styles.getOrDefault(mob, TBTextureStyle.NULL_STYLE);
    }
}