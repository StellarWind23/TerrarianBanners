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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Bakes every texture style into one dynamic texture, once per resource reload. */
public final class TBTextures {

    /** Loadable stand-in for the moments before the first reload has finished. */
    public static final Identifier PLACEHOLDER = Identifier.fromNamespaceAndPath(
            TerrarianBanners.MOD_ID, "textures/entity/mob_banner/base/plain.png");

    private record Bank(Map<Identifier, Identifier> byMob, Identifier fallback, List<Identifier> owned) { }

    private static final Bank EMPTY = new Bank(Map.of(), PLACEHOLDER, List.of());
    private static final Map<TBStyleType, Bank> BANKS = new EnumMap<>(TBStyleType.class);
    private static int nextId = 0;

    private TBTextures() { }

    public static Identifier banner(@Nullable Identifier mob) { return get(TBStyleType.MOB_BANNER, mob); }
    public static Identifier effect(@Nullable Identifier mob) { return get(TBStyleType.EFFECT, mob); }

    /** Render thread. Mobs without a style get the kind's null texture. */
    public static Identifier get(TBStyleType kind, @Nullable Identifier mob) {
        Bank bank = BANKS.getOrDefault(kind, EMPTY);
        return mob == null ? bank.fallback() : bank.byMob().getOrDefault(mob, bank.fallback());
    }

    /** Main thread, from a style manager's apply(). Rebuilds every texture of one kind. */
    public static void reload(TBStyleType kind, Map<Identifier, TBTextureStyle> styles, ResourceManager manager) {
        TextureManager textures = Minecraft.getInstance().getTextureManager();
        Bank old = BANKS.remove(kind);
        if (old != null) old.owned().forEach(textures::release);

        try (Baker baker = new Baker(kind, manager)) {
            Identifier fallback = baker.get(TBTextureStyle.nullFor(kind));
            Map<Identifier, Identifier> byMob = new HashMap<>();
            styles.forEach((mob, style) -> byMob.put(mob, baker.get(style)));
            BANKS.put(kind, new Bank(Map.copyOf(byMob), fallback, List.copyOf(baker.baked.values())));
        }
    }

    /** One per reload and kind. Decodes each source PNG once; styles that compare equal share one texture. */
    private static final class Baker implements AutoCloseable {

        private final TBStyleType kind;
        private final ResourceManager manager;
        private final Map<Identifier, NativeImage> sources = new HashMap<>();
        private final Set<Identifier> missing = new HashSet<>();
        private final Map<TBTextureStyle, Identifier> baked = new HashMap<>();

        Baker(TBStyleType kind, ResourceManager manager) {
            this.kind = kind;
            this.manager = manager;
        }

        Identifier get(TBTextureStyle style) {
            return baked.computeIfAbsent(style, this::bake);
        }

        private Identifier bake(TBTextureStyle style) {
            NativeImage image = new NativeImage(kind.size(), kind.size(), true);   // zero-filled = transparent
            for (TBTextureStyle.Layer layer : style.layers()) composite(image, layer);

            Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "dynamic/" + kind.folder() + "/" + nextId++);
            Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(id::toString, image));
            return id;
        }

        private void composite(NativeImage dst, TBTextureStyle.Layer layer) {
            NativeImage src = source(layer);
            if (src == null && layer.name().equals("emblem")) src = source(TBTextureStyle.NULL_EMBLEM);
            if (src == null) return;

            int tint = layer.tint();
            for (TBTextureStyle.Offset offset : layer.offsets()) blend(dst, src, offset, tint);
        }

        private static void blend(NativeImage dst, NativeImage src, TBTextureStyle.Offset offset, int tint) {
            for (int y = 0; y < src.getHeight(); y++) {
                int dy = offset.y() + y;
                if (dy < 0 || dy >= dst.getHeight()) continue;
                for (int x = 0; x < src.getWidth(); x++) {
                    int dx = offset.x() + x;
                    if (dx < 0 || dx >= dst.getWidth()) continue;

                    int s = ARGB.multiply(src.getPixel(x, y), tint);
                    if (ARGB.alpha(s) == 0) continue;
                    dst.setPixel(dx, dy, over(s, dst.getPixel(dx, dy)));
                }
            }
        }

        private @Nullable NativeImage source(TBTextureStyle.Layer layer) {
            Identifier path = layer.texture().withPrefix("textures/").withSuffix(".png");

            NativeImage cached = sources.get(path);
            if (cached != null) return cached;
            if (missing.contains(path)) return null;

            var resource = manager.getResource(path);
            if (resource.isEmpty()) {
                missing.add(path);
                if (layer.name().equals("emblem")) {
                    TerrarianBanners.LOGGER.debug("No emblem texture yet: {}", path);   // expected while art is unfinished
                } else {
                    TerrarianBanners.LOGGER.warn("Missing texture layer: {}", path);
                }
                return null;
            }
            try (InputStream in = resource.get().open()) {
                NativeImage image = NativeImage.read(in);
                if (image.getWidth() > kind.size() || image.getHeight() > kind.size()) {
                    TerrarianBanners.LOGGER.warn("Texture layer {} is {}x{}, larger than the {}x{} {} canvas",
                            path, image.getWidth(), image.getHeight(), kind.size(), kind.size(), kind.folder());
                }
                sources.put(path, image);
                return image;
            } catch (IOException e) {
                missing.add(path);
                TerrarianBanners.LOGGER.warn("Failed to read texture layer: {}", path, e);
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