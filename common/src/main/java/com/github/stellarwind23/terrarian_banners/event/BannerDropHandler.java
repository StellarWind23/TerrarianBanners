package com.github.stellarwind23.terrarian_banners.event;

import com.github.stellarwind23.terrarian_banners.util.BannerKillData;
import com.github.stellarwind23.terrarian_banners.content.item.MobBannerItem;
import com.github.stellarwind23.terrarian_banners.content.TBTags;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import dev.architectury.event.EventResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class BannerDropHandler {

    public static EventResult onDeath(LivingEntity entity, DamageSource source) {
        if (!(entity.level() instanceof ServerLevel level)) return EventResult.pass();
        if (!entity.getType().is(TBTags.HAS_BANNER)) return EventResult.pass();
        if (TBConfig.require_player_kills && !(source.getEntity() instanceof ServerPlayer)) return EventResult.pass();

        Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        int needed = TBConfig.killsFor(id);
        int count = BannerKillData.get(level).increment(id);

        if (count % needed == 0) {
            entity.spawnAtLocation(level, MobBannerItem.createStack(entity.getType()));
        }
        return EventResult.pass();
    }
}