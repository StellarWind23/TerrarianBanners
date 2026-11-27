package com.github.stellarwind23.terrarian_banners.init;

import com.github.stellarwind23.terrarian_banners.content.*;
import com.github.stellarwind23.terrarian_banners.content.component.ShieldMobBanner;
import com.github.stellarwind23.terrarian_banners.content.effect.TBBannerAura;
import com.github.stellarwind23.terrarian_banners.event.BannerDropHandler;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public final class TerrarianBanners {

    public static final String MOD_ID = "terrarian_banners";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Write common init code here.
        TBTab.init();
        TBBlocks.init();
        TBItems.init();
        TBComponentTypes.init();
        TBEffects.init();

        TickEvent.PLAYER_POST.register(player -> {
            if (player.level().isClientSide() || (player.tickCount % 40) != 0) return;
            ShieldMobBanner sb = player.getOffhandItem().get(TBComponentTypes.SHIELD_MOB_BANNER.get());
            if (sb != null) TBBannerAura.grant(player, sb.mob());
        });

        EntityEvent.LIVING_DEATH.register(BannerDropHandler::onDeath);
    }

    private static final Set<EntityType<?>> NOT_BANNER_MOBS = Set.of(
            EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.MANNEQUIN);

    @SuppressWarnings("unchecked")
    public static boolean isBannerMob(EntityType<?> type) {
        if (NOT_BANNER_MOBS.contains(type)) return false;
        return DefaultAttributes.hasSupplier((EntityType<? extends LivingEntity>) type);
    }
}
