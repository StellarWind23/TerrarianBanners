package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class TBStyleTypeManager extends SimpleJsonResourceReloadListener<TBStyleType> {

    public static final TBStyleTypeManager INSTANCE = new TBStyleTypeManager();
    private static Map<Identifier, TBStyleType> types = new HashMap<>();


    private TBStyleTypeManager() {
        super(TBStyleType.CODEC, FileToIdConverter.json(TBStyleType.TYPE_FOLDER));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBStyleType> loadedTypes, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
        types = loadedTypes;
        TerrarianBanners.LOGGER.info("Loaded {} texture types", types.size());
    }

    public TBStyleType get(Identifier identifier) {
        return types.get(identifier);
    }
}
