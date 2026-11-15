package com.github.stellarwind23.terrarian_banners.neoforge;

import com.github.stellarwind23.terrarian_banners.client.init.TerrarianBannersClientInit;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(TerrarianBanners.MOD_ID)
public final class TerrarianBannersNeoForge {
    public TerrarianBannersNeoForge(IEventBus modBus) {
        TerrarianBanners.init();

        modBus.addListener((FMLClientSetupEvent e) -> {
            TerrarianBannersClientInit.init();
        });

        modBus.addListener((FMLCommonSetupEvent e) -> {
            MidnightConfig.init(TerrarianBanners.MOD_ID, TBConfig.class);
        });
    }
}
