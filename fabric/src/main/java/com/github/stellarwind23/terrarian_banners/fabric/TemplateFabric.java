package com.github.stellarwind23.terrarian_banners.fabric;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.fabricmc.api.ModInitializer;

public final class TemplateFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        TerrarianBanners.init();
    }
}
