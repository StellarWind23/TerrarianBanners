package com.github.stellarwind23.terrarian_banners.util;

import com.github.stellarwind23.terrarian_banners.content.TBEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class TBBannerCombat {

    private TBBannerCombat() { }

    public static float adjust(LivingEntity victim, DamageSource source, float damage) {
        Entity attacker = source.getEntity();   // projectiles resolve to their owner
        if (attacker == null) return damage;

        if (victim instanceof Player player && hasEffectFor(player, attacker.getType())) {
            damage = (float) (damage * (1.0F * TBConfig.damage_taken_multiplier));
        }
        if (attacker instanceof Player player && hasEffectFor(player, victim.getType())) {
            damage = (float) (damage * (1.0F * TBConfig.damage_dealt_multiplier));
        }
        return damage;
    }

    private static boolean hasEffectFor(Player player, EntityType<?> mob) {
        Holder<MobEffect> effect = TBEffects.holderFor(mob);
        return effect != null && player.hasEffect(effect);
    }
}