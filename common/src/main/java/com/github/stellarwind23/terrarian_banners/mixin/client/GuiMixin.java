package com.github.stellarwind23.terrarian_banners.mixin.client;

import com.github.stellarwind23.terrarian_banners.client.effect.TBEffectGrouping;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @SuppressWarnings("unchecked")
    @WrapOperation(
            method = "renderEffects(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getActiveEffects()Ljava/util/Collection;"))
    private Collection<?> tb$mergeBanners(LocalPlayer instance, Operation<Collection<?>> original) {
        return TBEffectGrouping.merge((Collection<MobEffectInstance>) original.call(instance));
    }
}