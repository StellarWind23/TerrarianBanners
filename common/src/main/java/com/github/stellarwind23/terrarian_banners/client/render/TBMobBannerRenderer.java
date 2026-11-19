package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.content.block.MobBannerBlock;
import com.github.stellarwind23.terrarian_banners.content.block.MobBannerBlockEntity;
import com.github.stellarwind23.terrarian_banners.content.block.MobWallBannerBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TBMobBannerRenderer implements BlockEntityRenderer<MobBannerBlockEntity, TBMobBannerRenderState> {

    public TBMobBannerRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public @NonNull TBMobBannerRenderState createRenderState() { return new TBMobBannerRenderState(); }

    @Override
    public void extractRenderState(@NonNull MobBannerBlockEntity be, @NonNull TBMobBannerRenderState state, float partialTick, @NonNull Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
        assert crumbling != null;
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPos, crumbling);

        BlockState bs = be.getBlockState();
        state.standing = bs.getBlock() instanceof MobBannerBlock;
        state.yaw = state.standing
                ? -RotationSegment.convertToDegrees(bs.getValue(MobBannerBlock.ROTATION))
                : -bs.getValue(MobWallBannerBlock.FACING).toYRot();

        // Same sway formula vanilla banners use
        long time = be.getLevel() != null ? be.getLevel().getGameTime() : 0L;
        BlockPos p = be.getBlockPos();
        float f = ((float) Math.floorMod((long) (p.getX() * 7L + p.getY() * 9L + p.getZ() * 13L) + time, 100L) + partialTick) / 100.0F;
        state.sway = (-0.0125F + 0.01F * Mth.cos(Mth.TWO_PI * f)) * Mth.PI;

        state.face = TBMobFaceTextures.resolve(be.getMob());
    }

    @Override
    public void submit(TBMobBannerRenderState state, PoseStack ps, @NonNull SubmitNodeCollector out, @NonNull CameraRenderState camera) {
        ps.pushPose();
        ps.translate(0.5, 0.0, 0.5);
        ps.mulPose(Axis.YP.rotationDegrees(state.yaw));
        TBMobBannerDrawer.submit(ps, out, state.standing, state.face, TBBannerColorManager.INSTANCE.get(state.face), state.sway, state.lightCoords);
        ps.popPose();
    }
}