package com.github.stellarwind23.terrarian_banners.mixin.client;

import com.github.stellarwind23.terrarian_banners.client.effect.TBEffectGrouping;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Inject(method = "blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V",
            at = @At("HEAD"), cancellable = true)
    private void tb$effectIcons(RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, int m, CallbackInfo ci) {
        if (TBEffectGrouping.drawSprite((GuiGraphics) (Object) this, renderPipeline, identifier, i, j, k, l, m)) ci.cancel();
    }

    @ModifyVariable(
            method = "setTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/resources/Identifier;)V",
            at = @At("HEAD"), argsOnly = true)
    private List<Component> tb$groupTooltipOptional(List<Component> list) {
        return TBEffectGrouping.isGroupTooltip(list) ? TBEffectGrouping.tooltip() : list;
    }

    @ModifyVariable(
            method = "setComponentTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/resources/Identifier;)V",
            at = @At("HEAD"), argsOnly = true)
    private List<Component> tb$groupTooltipComponent(List<Component> list) {
        return TBEffectGrouping.isGroupTooltip(list) ? TBEffectGrouping.tooltip() : list;
    }
}