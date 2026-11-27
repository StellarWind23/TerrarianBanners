package com.github.stellarwind23.terrarian_banners.content.effect;

import com.github.stellarwind23.terrarian_banners.content.TBTags;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public final class TBBannerAura {
    private TBBannerAura() { }

    public static void grant(Player player, Identifier mob) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(mob).orElse(null);
        if (type == null || !type.is(TBTags.HAS_BANNER)) return;   // the tag still disables mobs via datapack

        Holder<MobEffect> effect = TBEffects.holderFor(type);
        if (effect != null) {
            // use your duration accessor; the lang keys suggest the config is in seconds now, so seconds * 20
            player.addEffect(new MobEffectInstance(effect, (int) (TBConfig.effect_duration_seconds * 20D), 0, true, false, true));
        }
    }
}
