package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class TBTextureStyleManager extends SimpleJsonResourceReloadListener<TBTextureStyle> {

    public static final TBTextureStyleManager MOB_BANNER = new TBTextureStyleManager(TBTextureType.MOB_BANNER);
    public static final TBTextureStyleManager EFFECT = new TBTextureStyleManager(TBTextureType.EFFECT);

    private final TBTextureType kind;

    private TBTextureStyleManager(TBTextureType kind) {
        super(TBTextureStyle.CODEC, FileToIdConverter.json(kind.styleFolder()));
        this.kind = kind;
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBTextureStyle> prepared, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        TerrarianBanners.LOGGER.info("Loaded {} {} texture styles", prepared.size(), kind.folder());
        TBTextures.reload(kind, prepared, manager);
    }
}