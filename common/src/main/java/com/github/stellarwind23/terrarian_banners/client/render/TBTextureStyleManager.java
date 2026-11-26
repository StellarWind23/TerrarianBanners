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
import org.jspecify.annotations.Nullable;

import java.util.*;

public class TBTextureStyleManager extends SimpleJsonResourceReloadListener<TBTextureStyle> {

    //Statics
    public static Map<Identifier, TBTextureStyle> STYLES = new HashMap<>();
    public static final Identifier PLACEHOLDER = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "textures/entity/mob_banner/base/default.png");

    //Bank
    private record Bank(Map<Identifier, Identifier> byMob, Identifier fallback, List<Identifier> owned) { }

    //Default texture(needs to load no matter what)
    public final String name;
    private final Bank EMPTY = new Bank(Map.of(), PLACEHOLDER, List.of());
    private final Map<TBStyleType, Bank> BANKS = new HashMap<>();

    protected TBTextureStyleManager(TBStyleType type) {
        super(TBTextureStyle.CODEC, FileToIdConverter.json(type.styleFolder()));
        name = type.folder();
    }

    @Override
    protected void apply(@NonNull Map<Identifier, TBTextureStyle> loadedStyles, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        STYLES = loadedStyles;
        TerrarianBanners.LOGGER.info("Loaded {} texture styles for {}", loadedStyles.size(), name);
        
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

    public Identifier get(TBStyleType kind, @Nullable Identifier mob) {
        Bank bank = BANKS.getOrDefault(kind, EMPTY);
        return mob == null ? bank.fallback() : bank.byMob().getOrDefault(mob, bank.fallback());
    }
}