package com.github.stellarwind23.terrarian_banners.mixin;

import com.github.stellarwind23.terrarian_banners.util.TBBannerCombat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void tb$bannerEffects(DamageSource damageSource, float f, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(TBBannerCombat.adjust((LivingEntity) (Object) this, damageSource, f));
    }
}