package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class TBTextureStyleManager extends SimpleJsonResourceReloadListener<TBTextureStyle> {

    public static final TBTextureStyleManager MOB_BANNER = new TBTextureStyleManager(TBStyleType.MOB_BANNER);
    public static final TBTextureStyleManager EFFECT = new TBTextureStyleManager(TBStyleType.EFFECT);

    private final TBStyleType type;

    public TBTextureStyleManager(TBStyleType type) {
        super(TBTextureStyle.CODEC, FileToIdConverter.json(type.styleFolder()));
        this.type = type;
    }

    public static void registerAll() {
        for (TBStyleType type : TBStyleType.all()) {
            ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new TBTextureStyleManager(type), Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "texture_styles/" + type.id()));
        }
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBTextureStyle> prepared, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        TerrarianBanners.LOGGER.info("Loaded {} {} texture styles", prepared.size(), type.id());
        TBTextures.reload(type, prepared, manager);
    }
}