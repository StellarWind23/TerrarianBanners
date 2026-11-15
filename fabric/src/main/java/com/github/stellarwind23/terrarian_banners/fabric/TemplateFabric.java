package com.github.stellarwind23.terrarian_banners.fabric;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

public final class TemplateFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        TerrarianBanners.init();

        MidnightConfig.init(TerrarianBanners.MOD_ID, TBConfig.class);
    }
}
