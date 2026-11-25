package com.github.stellarwind23.terrarian_banners.mixin.client;

import com.github.stellarwind23.terrarian_banners.client.effect.TBEffectGrouping;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.Optional;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {

    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getActiveEffects()Ljava/util/Collection;"))
    private Collection<MobEffectInstance> tb$mergeBanners(LocalPlayer player, Operation<Collection<MobEffectInstance>> original) {
        return TBEffectGrouping.merge(original.call(player));
    }

    @WrapOperation(
            method = "renderEffects(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/Collection;IIIII)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/EffectsInInventory;renderText(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;IIIIII)V"))
    private void tb$labelModeTooltip(EffectsInInventory instance, GuiGraphics arg, Component arg2, Component arg3, Font arg4, int i, int j, int k, int l, int m, int n, Operation<Void> original) {
        original.call(instance, arg, arg2, arg3, arg4, i, j, k, l, m, n);

        if (TBEffectGrouping.isGroupName(arg2)
                && m >= i && m <= i + k
                && n >= j && n <= j + l) {
            arg.setTooltipForNextFrame(arg4, TBEffectGrouping.tooltip(), Optional.empty(), m, n);
        }
    }
}
