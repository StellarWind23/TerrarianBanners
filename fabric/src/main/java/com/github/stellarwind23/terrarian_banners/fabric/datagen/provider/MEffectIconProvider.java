package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.client.render.TBTextureStyle;
import com.github.stellarwind23.terrarian_banners.client.render.TBBannerTextures;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.google.common.hash.Hashing;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class MEffectIconProvider implements DataProvider {

    private static final int ICON = 18;   // vanilla effect icon size
    // The emblem lives in the front region of the 64x64 banner sheet
    private static final int FRONT_X0 = 1, FRONT_Y0 = 1, FRONT_X1 = 21, FRONT_Y1 = 41;

    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "effect/background");

    private final PackOutput.PathProvider paths;

    public MEffectIconProvider(FabricDataOutput output) {
        this.paths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures/gui/sprites/mob_effect/banner");
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        BufferedImage background = read(BACKGROUND);
        if (background == null) throw new IllegalStateException("Missing effect icon background: " + BACKGROUND);
        BufferedImage fallback = read(TBTextureStyle.NULL_EMBLEM.texture());

        MBannerStyleProvider.buildStyles().forEach((mob, style) ->
                write(cache, mob, compose(background, emblemFor(mob, style, fallback))));
        return CompletableFuture.completedFuture(null);
    }

    private static @Nullable BufferedImage emblemFor(Identifier mob, TBTextureStyle style, @Nullable BufferedImage fallback) {
        for (TBTextureStyle.Layer layer : style.layers()) {
            if (!layer.name().equals("emblem")) continue;
            BufferedImage image = read(layer.texture());
            if (image != null) return image;
            TerrarianBanners.LOGGER.warn("No emblem texture for {}: {}", mob, layer.texture());
        }
        return fallback;
    }

    private static BufferedImage compose(BufferedImage background, @Nullable BufferedImage emblem) {
        BufferedImage out = new BufferedImage(ICON, ICON, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < Math.min(ICON, background.getHeight()); y++)
            for (int x = 0; x < Math.min(ICON, background.getWidth()); x++)
                out.setRGB(x, y, background.getRGB(x, y));

        Rectangle box = emblem == null ? null : opaqueBounds(emblem);
        if (box == null) return out;
        if (box.width > ICON || box.height > ICON) {
            TerrarianBanners.LOGGER.warn("Emblem is {}x{}, larger than the {}x{} icon", box.width, box.height, ICON, ICON);
        }

        int ox = (ICON - box.width) / 2, oy = (ICON - box.height) / 2;
        for (int y = 0; y < box.height; y++) {
            for (int x = 0; x < box.width; x++) {
                int dx = ox + x, dy = oy + y;
                if (dx < 0 || dy < 0 || dx >= ICON || dy >= ICON) continue;
                int src = emblem.getRGB(box.x + x, box.y + y);
                if ((src >>> 24) == 0) continue;
                out.setRGB(dx, dy, TBBannerTextures.over(src, out.getRGB(dx, dy)));
            }
        }
        return out;
    }

    // Tight bounds of the painted pixels inside the front region, so the emblem centres itself
    private static @Nullable Rectangle opaqueBounds(BufferedImage img) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
        for (int y = FRONT_Y0; y < Math.min(FRONT_Y1, img.getHeight()); y++) {
            for (int x = FRONT_X0; x < Math.min(FRONT_X1, img.getWidth()); x++) {
                if ((img.getRGB(x, y) >>> 24) == 0) continue;
                minX = Math.min(minX, x); minY = Math.min(minY, y);
                maxX = Math.max(maxX, x); maxY = Math.max(maxY, y);
            }
        }
        return maxX < 0 ? null : new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    // Textures come from the classpath: common's resources are on the datagen run's classpath
    private static @Nullable BufferedImage read(Identifier texture) {
        String path = "/assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png";
        try (InputStream in = MEffectIconProvider.class.getResourceAsStream(path)) {
            return in == null ? null : ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    private void write(CachedOutput cache, Identifier mob, BufferedImage icon) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ImageIO.write(icon, "png", bytes);
            byte[] data = bytes.toByteArray();
            Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID,
                    mob.getNamespace() + "/" + mob.getPath());
            cache.writeIfNeeded(paths.file(id, "png"), data, Hashing.sha256().hashBytes(data));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public @NonNull String getName() { return "Banner Effect Icons"; }
}