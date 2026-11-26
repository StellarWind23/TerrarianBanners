package com.github.stellarwind23.terrarian_banners.client.effect;

import com.github.stellarwind23.terrarian_banners.client.render.TBStyleTypeManager;
import com.github.stellarwind23.terrarian_banners.client.render.TBTextures;
import com.github.stellarwind23.terrarian_banners.content.TBEffects;
import com.github.stellarwind23.terrarian_banners.content.effect.TBMobBannerEffect;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class TBEffectGrouping {

    public static final Identifier GROUP_SPRITE =
            Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "mob_effect/banner_group");
    private static final String SPRITE_PREFIX = "mob_effect/banner/";   // + <mob namespace>/<mob path>
    private static final String GROUP_NAME_KEY = "effect.terrarian_banners.banner_group";
    private static final int MIN_GROUP_SIZE = 3;
    private static final int MAX_TOOLTIP_MOBS = 12;

    private TBEffectGrouping() { }

    // ---- Which effects are banner effects ----
    public static boolean isBanner(MobEffectInstance instance) {
        return instance.getEffect().value() instanceof TBMobBannerEffect;
    }

    private static List<MobEffectInstance> bannerEffects(Collection<MobEffectInstance> effects) {
        return effects.stream()
                .filter(TBEffectGrouping::isBanner)
                .sorted(Comparator.comparing((MobEffectInstance e) -> e.getEffect().getRegisteredName()))
                .toList();
    }

    private static List<MobEffectInstance> activeBanners() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player == null ? List.of() : bannerEffects(player.getActiveEffects());
    }

    private static List<Identifier> activeMobs() {
        return activeBanners().stream()
                .map(e -> mobOf(Gui.getMobEffectSprite(e.getEffect())))
                .filter(Objects::nonNull)
                .toList();
    }

    private static @Nullable Identifier mobOf(Identifier sprite) {
        if (!sprite.getNamespace().equals(TerrarianBanners.MOD_ID)) return null;
        String path = sprite.getPath();
        if (!path.startsWith(SPRITE_PREFIX)) return null;

        String rest = path.substring(SPRITE_PREFIX.length());
        int slash = rest.indexOf('/');
        return slash < 0 ? null : Identifier.fromNamespaceAndPath(rest.substring(0, slash), rest.substring(slash + 1));
    }

    // ---- List merging ----

    public static Collection<MobEffectInstance> merge(Collection<MobEffectInstance> effects) {
        List<MobEffectInstance> banners = bannerEffects(effects);
        if (banners.size() < MIN_GROUP_SIZE) return effects;

        List<MobEffectInstance> out = new ArrayList<>(effects.size() - banners.size() + 1);
        for (MobEffectInstance e : effects) {
            if (!isBanner(e)) out.add(e);
        }
        // Shortest remaining time, so the entry blinks when any of its banners is about to lapse
        int duration = banners.stream().mapToInt(MobEffectInstance::getDuration).min().orElse(0);
        out.add(new MobEffectInstance(TBEffects.groupHolder(), duration, 0, true, false, true));
        return out;
    }

    // ---- Tooltip ----

    public static List<Component> tooltip() {
        List<MobEffectInstance> banners = activeBanners();
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(GROUP_NAME_KEY + ".header", banners.size()));
        for (int i = 0; i < Math.min(banners.size(), MAX_TOOLTIP_MOBS); i++) {
            lines.add(banners.get(i).getEffect().value().getDisplayName().copy().withStyle(ChatFormatting.GRAY));
        }
        if (banners.size() > MAX_TOOLTIP_MOBS) {
            lines.add(Component.translatable(GROUP_NAME_KEY + ".more", banners.size() - MAX_TOOLTIP_MOBS)
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        return lines;
    }

    public static boolean isGroupName(Component name) {
        return name.getContents() instanceof TranslatableContents c && c.getKey().equals(GROUP_NAME_KEY);
    }

    public static boolean isGroupTooltip(List<Component> lines) {
        return !lines.isEmpty() && isGroupName(lines.getFirst());
    }

    // ---- Icons ----

    public static boolean drawSprite(GuiGraphics g, RenderPipeline pipeline, Identifier sprite, int x, int y, int w, int h, int color) {
        if (GROUP_SPRITE.equals(sprite)) {
            drawGroup(g, pipeline, x, y, w, h, color);
            return true;
        }
        Identifier mob = mobOf(sprite);
        if (mob == null) return false;
        drawIcon(g, pipeline, mob, x, y, w, h, color);
        return true;
    }

    private static void drawIcon(GuiGraphics g, RenderPipeline pipeline, Identifier mob, int x, int y, int w, int h, int color) {
        int s = TBStyleTypeManager.INSTANCE.getType(Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "effect")).size();
        g.blit(pipeline, TBTextures.effect(mob), x, y, 0.0F, 0.0F, w, h, s, s, s, s, color);
    }

    private static void drawGroup(GuiGraphics g, RenderPipeline pipeline, int x, int y, int w, int h, int color) {
        List<Identifier> mobs = activeMobs();
        int n = mobs.size();
        if (n < MIN_GROUP_SIZE) return;   // merge() never produces a group below the threshold

        int sw = w / 2, sh = h / 2;
        drawIcon(g, pipeline, mobs.get(0), x, y, sw, sh, color);
        drawIcon(g, pipeline, mobs.get(1), x + sw, y, sw, sh, color);

        if (n == 3) {   // two on top, the third centred underneath
            drawIcon(g, pipeline, mobs.get(2), x + (w - sw) / 2, y + sh, sw, sh, color);
        } else {        // four or more: the third bottom-left, then a plus in the last quadrant
            drawIcon(g, pipeline, mobs.get(2), x, y + sh, sw, sh, color);
            drawPlus(g, x + sw, y + sh, sw, color);
        }
    }

    private static void drawPlus(GuiGraphics g, int x, int y, int size, int color) {
        int alpha = color & 0xFF000000;   // keeps the HUD's blink alpha
        int white = alpha | 0xFFFFFF;
        int arm = size - 4, off = (size - arm) / 2, mid = size / 2;
        g.fill(x + off - 1, y + mid - 1, x + off + arm + 1, y + mid + 2, alpha);
        g.fill(x + mid - 1, y + off - 1, x + mid + 2, y + off + arm + 1, alpha);
        g.fill(x + off, y + mid, x + off + arm, y + mid + 1, white);
        g.fill(x + mid, y + off, x + mid + 1, y + off + arm, white);
    }
}