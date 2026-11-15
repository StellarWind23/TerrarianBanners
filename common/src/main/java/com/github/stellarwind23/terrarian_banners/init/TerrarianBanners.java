package com.github.stellarwind23.terrarian_banners.init;

import com.github.stellarwind23.terrarian_banners.content.TBBlocks;
import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.TBItems;
import com.github.stellarwind23.terrarian_banners.content.TBTab;
import com.github.stellarwind23.terrarian_banners.event.BannerDropHandler;
import dev.architectury.event.events.common.EntityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TerrarianBanners {

    public static final String MOD_ID = "terrarian_banners";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Write common init code here.
        TBTab.init();
        TBBlocks.init();
        TBItems.init();
        TBComponentTypes.init();

        EntityEvent.LIVING_DEATH.register(BannerDropHandler::onDeath);
    }
}
