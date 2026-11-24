package com.github.stellarwind23.terrarian_banners.client.effect;

import com.github.stellarwind23.terrarian_banners.content.TBEffects;
import com.github.stellarwind23.terrarian_banners.content.effect.TBMobBannerEffect;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class TBEffectGrouping {

    public static final Identifier GROUP_SPRITE =
            Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "mob_effect/banner_group");
    private static final String GROUP_NAME_KEY = "effect.terrarian_banners.banner_group";
    private static final int MAX_TOOLTIP_MOBS = 12;

    private TBEffectGrouping() { }

    public static boolean isBanner(MobEffectInstance instance) {
        return instance.getEffect().value() instanceof TBMobBannerEffect;
    }

    /** Sorted by effect id, so the icon layout doesn't shuffle between frames. */
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

    // ---- List merging ----

    public static Collection<MobEffectInstance> merge(Collection<MobEffectInstance> effects) {
        List<MobEffectInstance> banners = bannerEffects(effects);
        if (banners.size() < 2) return effects;

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

    public static boolean isGroupTooltip(List<Component> lines) {
        return !lines.isEmpty()
                && lines.get(0).getContents() instanceof TranslatableContents contents
                && contents.getKey().equals(GROUP_NAME_KEY);
    }

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

    // ---- Icon ----

    private static Identifier spriteOf(MobEffect effect) {
        return BuiltInRegistries.MOB_EFFECT.getKey(effect).withPrefix("mob_effect/");
    }

    public static void drawGroupIcon(GuiGraphics g, RenderPipeline pipeline, int x, int y, int w, int h, int color) {
        List<Identifier> icons = activeBanners().stream()
                .map(e -> spriteOf(e.getEffect().value())).toList();
        int n = icons.size();
        if (n == 0) return;
        if (n == 1) {
            g.blitSprite(pipeline, icons.getFirst(), x, y, w, h, color);
            return;
        }

        int sw = w / 2, sh = h / 2;
        switch (n) {
            case 2 -> {   // side by side, vertically centred
                int oy = (h - sh) / 2;
                g.blitSprite(pipeline, icons.get(0), x, y + oy, sw, sh, color);
                g.blitSprite(pipeline, icons.get(1), x + sw, y + oy, sw, sh, color);
            }
            case 3 -> {   // two on top, one centred underneath
                g.blitSprite(pipeline, icons.get(0), x, y, sw, sh, color);
                g.blitSprite(pipeline, icons.get(1), x + sw, y, sw, sh, color);
                g.blitSprite(pipeline, icons.get(2), x + (w - sw) / 2, y + sh, sw, sh, color);
            }
            default -> {  // first three, then a plus in the last quadrant
                g.blitSprite(pipeline, icons.get(0), x, y, sw, sh, color);
                g.blitSprite(pipeline, icons.get(1), x + sw, y, sw, sh, color);
                g.blitSprite(pipeline, icons.get(2), x, y + sh, sw, sh, color);
                drawPlus(g, x + sw, y + sh, sw, color);
            }
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