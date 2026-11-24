package com.github.stellarwind23.terrarian_banners.mixin.client;

import com.github.stellarwind23.terrarian_banners.client.effect.TBEffectGrouping;
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

    @Inject(method = "blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void tb$groupIcon(RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, int m, CallbackInfo ci) {
        if (!TBEffectGrouping.GROUP_SPRITE.equals(identifier)) return;
        TBEffectGrouping.drawGroupIcon((GuiGraphics) (Object) this, renderPipeline, i, j, k, l, m);
        ci.cancel();
    }

    @Inject(method = "blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void tb$groupIconPlain(RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, CallbackInfo ci) {
        if (!TBEffectGrouping.GROUP_SPRITE.equals(identifier)) return;
        TBEffectGrouping.drawGroupIcon((GuiGraphics) (Object) this, renderPipeline, i, j, k, l, -1);
        ci.cancel();
    }

    @ModifyVariable(method = "setComponentTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At("HEAD"), argsOnly = true, require = 0)
    private List<Component> tb$groupTooltip(List<Component> list) {
        return TBEffectGrouping.isGroupTooltip(list) ? TBEffectGrouping.tooltip() : list;
    }
}