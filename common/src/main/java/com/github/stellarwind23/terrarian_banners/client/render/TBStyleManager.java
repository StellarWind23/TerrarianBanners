package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class TBStyleManager extends SimpleJsonResourceReloadListener<TBStyle> {

    public static Map<TBStyleType, TBStyleManager> STYLE_MANAGERS = new HashMap<>();
    private record Bank(Map<Identifier, Identifier> byMob, Identifier fallback, List<Identifier> owned) { }

    //Default texture(needs to load no matter what)
    public static final Identifier PLACEHOLDER = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "textures/entity/mob_banner/base/default.png");
    private final Bank EMPTY = new Bank(Map.of(), PLACEHOLDER, List.of());
    private Map<TBStyleType, Bank> BANKS = new HashMap<>();

    private TBStyleManager(TBStyleType kind) {
        super(TBStyle.CODEC, FileToIdConverter.json(kind.styleFolder()));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBStyle> prepared, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        TerrarianBanners.LOGGER.info("Loaded {} {} texture styles", prepared.size(), kind.folder());
        
        TextureManager textures = Minecraft.getInstance().getTextureManager();
        Bank old = BANKS.remove(kind);
        if (old != null) old.owned().forEach(textures::release);

        try (TBTextures.Baker baker = new TBTextures.Baker(kind, manager)) {
            Identifier fallback = kind.nullStyleId();
            Map<Identifier, Identifier> byMob = new HashMap<>();
            styles.forEach((mob, style) -> byMob.put(mob, baker.get(style)));
            BANKS.put(kind, new Bank(Map.copyOf(byMob), fallback, List.copyOf(baker.baked.values())));
        }
    }
}