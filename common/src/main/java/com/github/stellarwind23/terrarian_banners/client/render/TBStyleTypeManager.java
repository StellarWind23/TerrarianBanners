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

import java.util.*;

public class TBStyleTypeManager extends SimpleJsonResourceReloadListener<TBStyleType> {

    public static final TBStyleTypeManager INSTANCE = new TBStyleTypeManager();
    private Map<Identifier, TBStyleType> types = new HashMap<>();
    private final Map<TBStyleType, TBTextureStyleManager> managers = new HashMap<>();


    private TBStyleTypeManager() {
        super(TBStyleType.CODEC, FileToIdConverter.json(TBStyleType.TYPE_FOLDER));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBStyleType> loadedTypes, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
        types = loadedTypes;
        TerrarianBanners.LOGGER.info("Loaded {} texture style types...", types.size());

        for(Map.Entry<Identifier, TBStyleType> e: types.entrySet()) {
            ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new TBTextureStyleManager(e.getValue()), Identifier.fromNamespaceAndPath(e.getKey().getNamespace(), e.getValue().styleFolder()));
        }
    }

    protected void register(Identifier id, TBTextureStyleManager manager) {
        managers.put(types.get(id), manager);
    }

    protected TBTextureStyleManager getManager(Identifier id) {
        return managers.get(types.get(id));
    }

    public TBStyleType getType(Identifier identifier) {
        return types.get(identifier);
    }
}
