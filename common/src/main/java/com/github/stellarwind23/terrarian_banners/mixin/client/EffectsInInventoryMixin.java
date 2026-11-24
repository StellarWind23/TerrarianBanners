package com.github.stellarwind23.terrarian_banners.mixin.client;

import com.github.stellarwind23.terrarian_banners.client.effect.TBEffectGrouping;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {

    @ModifyVariable(
            method = "renderEffects(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/Collection;IIIII)V",
            at = @At("HEAD"),
            argsOnly = true)
    private Collection<MobEffectInstance> tb$mergeBanners(Collection<MobEffectInstance> collection) {
        return TBEffectGrouping.merge(collection);
    }
}
