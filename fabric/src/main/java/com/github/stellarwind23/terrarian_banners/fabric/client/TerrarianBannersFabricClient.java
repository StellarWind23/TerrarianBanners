package com.github.stellarwind23.terrarian_banners.fabric.client;

import com.github.stellarwind23.terrarian_banners.client.init.TerrarianBannersClientInit;
import net.fabricmc.api.ClientModInitializer;

public final class TerrarianBannersFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TerrarianBannersClientInit.init();
    }
}
