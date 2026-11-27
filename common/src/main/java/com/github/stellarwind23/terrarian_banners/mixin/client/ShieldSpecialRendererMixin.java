package com.github.stellarwind23.terrarian_banners.mixin.client;

import com.github.stellarwind23.terrarian_banners.client.render.TBStyleType;
import com.github.stellarwind23.terrarian_banners.client.render.TBTextures;
import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.component.ShieldMobBanner;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Unit;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShieldSpecialRenderer.class)
public abstract class ShieldSpecialRendererMixin {

    @Shadow
    @Final
    private ShieldModel model;

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private void tb$mobBanner(@Nullable DataComponentMap dataComponentMap, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, boolean bl, int k, CallbackInfo ci) {
        Identifier texture = terrarian_banners$textureFor(dataComponentMap);
        if (texture == null) return;

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, model.renderType(texture), i, j, -1, null, k, null);
        poseStack.popPose();
        ci.cancel();
    }

    @Unique
    private static @Nullable Identifier terrarian_banners$textureFor(@Nullable DataComponentMap components) {
        if (components == null) return null;
        ShieldMobBanner sb = components.get(TBComponentTypes.SHIELD_MOB_BANNER.get());
        TBStyleType type = sb == null ? null : TBStyleType.byId(sb.style());
        return type == null ? null : TBTextures.get(type, sb.mob());
    }
}