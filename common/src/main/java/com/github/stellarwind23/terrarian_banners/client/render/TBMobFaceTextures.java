package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class TBMobFaceTextures {

    public static final Identifier UNKNOWN = Identifier.fromNamespaceAndPath(
            TerrarianBanners.MOD_ID, "textures/mob_faces/unknown.png");

    // Render-thread only, so no concurrent map needed
    private static final Map<Identifier, Identifier> CACHE = new HashMap<>();

    public static Identifier resolve(@Nullable Identifier mob) {
        if (mob == null) return UNKNOWN;
        return CACHE.computeIfAbsent(mob, id -> {
            Identifier tex = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID,
                    "textures/mob_faces/" + id.getNamespace() + "/" + id.getPath() + ".png");
            return Minecraft.getInstance().getResourceManager().getResource(tex).isPresent() ? tex : UNKNOWN;
        });
    }

    public static void clearCache() { CACHE.clear(); }
}