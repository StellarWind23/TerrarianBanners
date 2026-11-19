package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TBBannerTextures {

    private static final int SIZE = 64;

    // Icon placement in texels: 16x16, centred in the 20-wide front (x 1..21) and back (x 22..42) regions
    private static final int ICON_Y = 9;
    private static final int FRONT_ICON_X = 3;
    private static final int BACK_ICON_X = 24;

    private static final Map<TBBannerStyle, Identifier> CACHE = new HashMap<>();
    private static final Set<Identifier> WARNED = new HashSet<>();
    private static int nextId = 0;

    private TBBannerTextures() { }

    /** Render thread only. Styles that compare equal share one texture. */
    public static Identifier get(TBBannerStyle style) {
        return CACHE.computeIfAbsent(style, TBBannerTextures::bake);
    }

    /** Call on resource reload. */
    public static void clear() {
        TextureManager textures = Minecraft.getInstance().getTextureManager();
        CACHE.values().forEach(textures::release);
        CACHE.clear();
        WARNED.clear();
    }

    private static Identifier bake(TBBannerStyle style) {
        NativeImage image = new NativeImage(SIZE, SIZE, true);   // zero-filled, so fully transparent

        composite(image, style.base(), 0, 0);
        composite(image, style.secondary(), 0, 0);
        composite(image, style.accent(), 0, 0);
        composite(image, style.icon(), FRONT_ICON_X, ICON_Y);
        composite(image, style.icon(), BACK_ICON_X, ICON_Y);

        Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "dynamic/banner/" + nextId++);
        Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(id::toString, image));
        return id;
    }

    private static void composite(NativeImage dst, TBBannerStyle.Layer layer, int ox, int oy) {
        NativeImage src = load(layer);
        try (src) {
            if (src == null) return;
            int tint = layer.tint();
            for (int y = 0; y < src.getHeight(); y++) {
                for (int x = 0; x < src.getWidth(); x++) {
                    int dx = ox + x, dy = oy + y;
                    if (dx >= SIZE || dy >= SIZE) continue;

                    int s = ARGB.multiply(src.getPixel(x, y), tint);
                    if (ARGB.alpha(s) == 0) continue;
                    dst.setPixel(dx, dy, over(s, dst.getPixel(dx, dy)));
                }
            }
        }
    }

    // Standard "source over destination" alpha blend on straight-alpha ARGB
    private static int over(int s, int d) {
        int sa = ARGB.alpha(s), da = ARGB.alpha(d);
        if (sa == 255 || da == 0) return s;

        float a = sa / 255.0F;
        float b = (da / 255.0F) * (1.0F - a);
        float out = a + b;
        return ARGB.color(
                Math.round(out * 255.0F),
                Math.round((ARGB.red(s) * a + ARGB.red(d) * b) / out),
                Math.round((ARGB.green(s) * a + ARGB.green(d) * b) / out),
                Math.round((ARGB.blue(s) * a + ARGB.blue(d) * b) / out));
    }

    private static @Nullable NativeImage load(TBBannerStyle.Layer layer) {
        Identifier path = layer.texture().withPrefix("textures/entity/mob_banner/").withSuffix(".png");
        var resource = Minecraft.getInstance().getResourceManager().getResource(path);
        if (resource.isEmpty()) {
            if (WARNED.add(path)) TerrarianBanners.LOGGER.warn("Missing banner layer texture: {}", path);
            return null;
        }
        try (InputStream in = resource.get().open()) {
            return NativeImage.read(in);
        } catch (IOException e) {
            if (WARNED.add(path)) TerrarianBanners.LOGGER.warn("Failed to read banner layer texture: {}", path, e);
            return null;
        }
    }
}