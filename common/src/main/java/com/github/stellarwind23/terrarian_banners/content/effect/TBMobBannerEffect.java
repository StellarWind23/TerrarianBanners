package com.github.stellarwind23.terrarian_banners.content.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NonNull;

public class TBMobBannerEffect extends MobEffect {

    private final EntityType<?> type;

    public TBMobBannerEffect(EntityType<?> type) {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        this.type = type;
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("effect.terrarian_banners.", type.getDescriptionId().replaceFirst("entity", ""));
    }
}