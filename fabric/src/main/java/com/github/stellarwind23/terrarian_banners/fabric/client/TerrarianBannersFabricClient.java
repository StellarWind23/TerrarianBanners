package com.github.stellarwind23.terrarian_banners.fabric.client;

import com.github.stellarwind23.terrarian_banners.client.init.TerrarianBannersClientInit;
import com.github.stellarwind23.terrarian_banners.client.render.TBMobBannerSpecialRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public final class TerrarianBannersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TerrarianBannersClientInit.init();
        SpecialModelRenderers.ID_MAPPER.put(TBMobBannerSpecialRenderer.ID, TBMobBannerSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
