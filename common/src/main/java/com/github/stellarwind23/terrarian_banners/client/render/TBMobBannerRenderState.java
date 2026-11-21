package com.github.stellarwind23.terrarian_banners.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;

public class TBMobBannerRenderState extends BlockEntityRenderState {
    public boolean standing;
    public float yaw;
    public float sway;
    public Identifier texture = TBBannerStyle.NEUTRAL_ID;
}