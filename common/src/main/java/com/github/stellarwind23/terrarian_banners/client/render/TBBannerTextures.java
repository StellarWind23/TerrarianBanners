package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TBBannerTextures {

    private static final int SIZE = 64;

    // A real, loadable path for the moments before the first reload has finished
    private static final Identifier FALLBACK = TBBannerStyle.NEUTRAL.layers().get(1).texture().withPrefix("textures/").withSuffix(".png");

    private static Map<Identifier, Identifier> byMob = Map.of();
    private static Identifier neutral = FALLBACK;
    private static List<Identifier> owned = List.of();   // dynamic textures to release on the next reload
    private static int nextId = 0;

    private TBBannerTextures() { }

    /** Render thread. Unknown mobs and mobs without a style JSON get the neutral banner. */
    public static Identifier forMob(@Nullable Identifier mob) {
        return mob == null ? neutral : byMob.getOrDefault(mob, neutral);
    }

    /** Called from the style manager's apply(), which runs on the main thread. Rebuilds every banner texture. */
    public static void reload(Map<Identifier, TBBannerStyle> styles, ResourceManager manager) {
        TextureManager textures = Minecraft.getInstance().getTextureManager();
        owned.forEach(textures::release);
        owned = List.of();
        byMob = Map.of();
        neutral = FALLBACK;

        Map<Identifier, Identifier> mobs = new HashMap<>();
        try (Baker baker = new Baker(manager)) {
            neutral = baker.get(TBBannerStyle.NEUTRAL);
            styles.forEach((mob, style) -> mobs.put(mob, baker.get(style)));
            owned = List.copyOf(baker.baked.values());
        }
        byMob = Map.copyOf(mobs);
    }

    /** One per reload. Decodes each source PNG once, and styles that compare equal share one texture. */
    private static final class Baker implements AutoCloseable {

        private final ResourceManager manager;
        private final Map<Identifier, NativeImage> sources = new HashMap<>();
        private final Set<Identifier> missing = new HashSet<>();
        private final Map<TBBannerStyle, Identifier> baked = new HashMap<>();

        Baker(ResourceManager manager) { this.manager = manager; }

        Identifier get(TBBannerStyle style) {
            return baked.computeIfAbsent(style, this::bake);
        }

        private Identifier bake(TBBannerStyle style) {
            NativeImage image = new NativeImage(SIZE, SIZE, true);

            for (TBBannerStyle.Layer layer : style.layers()) {
                composite(image, layer);
            }

            Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "dynamic/banner/" + nextId++);
            Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(id::toString, image));
            return id;
        }

        private void composite(NativeImage dst, TBBannerStyle.Layer layer) {
            NativeImage src = source(layer);
            if (src == null) return;

            int tint = layer.tint();
            for (int y = 0; y < Math.min(src.getHeight(), SIZE); y++) {
                for (int x = 0; x < Math.min(src.getWidth(), SIZE); x++) {
                    int s = ARGB.multiply(src.getPixel(x, y), tint);
                    if (ARGB.alpha(s) == 0) continue;
                    dst.setPixel(x, y, over(s, dst.getPixel(x, y)));
                }
            }
        }

        private @Nullable NativeImage source(TBBannerStyle.Layer layer) {
            Identifier path = layer.texture().withPrefix("textures/").withSuffix(".png");

            NativeImage cached = sources.get(path);
            if (cached != null) return cached;
            if (missing.contains(path)) return null;

            var resource = manager.getResource(path);
            if (resource.isEmpty()) {
                missing.add(path);
                TerrarianBanners.LOGGER.warn("Missing banner layer texture: {}", path);
                return null;
            }
            try (InputStream in = resource.get().open()) {
                NativeImage image = NativeImage.read(in);
                if (image.getWidth() != SIZE || image.getHeight() != SIZE) {
                    TerrarianBanners.LOGGER.warn("Banner layer {} is {}x{}, expected {}x{}",
                            path, image.getWidth(), image.getHeight(), SIZE, SIZE);
                }
                sources.put(path, image);
                return image;
            } catch (IOException e) {
                missing.add(path);
                TerrarianBanners.LOGGER.warn("Failed to read banner layer texture: {}", path, e);
                return null;
            }
        }

        @Override
        public void close() { sources.values().forEach(NativeImage::close); }
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
}