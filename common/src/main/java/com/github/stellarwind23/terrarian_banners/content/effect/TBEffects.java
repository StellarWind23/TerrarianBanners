package com.github.stellarwind23.terrarian_banners.content.effect;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TBEffects {

    protected static DeferredRegister<MobEffect> EFFECTS;

    private static final Map<EntityType<?>, RegistrySupplier<MobEffect>> BY_ENTITY = new HashMap<>();
    private static final Map<EntityType<?>, Holder<MobEffect>> HOLDERS = new ConcurrentHashMap<>();
    public static RegistrySupplier<MobEffect> GROUP;
    private static Holder<MobEffect> groupHolder;

    public static void init() {
        EFFECTS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.MOB_EFFECT);

        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (!TerrarianBanners.isBannerMob(type)) continue;
            Identifier mob = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            BY_ENTITY.put(type, EFFECTS.register(
                    "banner/" + mob.getNamespace() + "/" + mob.getPath(),
                    () -> new TBMobBannerEffect(type)));
        }

        GROUP = EFFECTS.register("banner_group", TBBannerGroupEffect::new);

        EFFECTS.register();
    }

    public static @Nullable Holder<MobEffect> holderFor(EntityType<?> type) {
        RegistrySupplier<MobEffect> supplier = BY_ENTITY.get(type);
        if (supplier == null) return null;
        return HOLDERS.computeIfAbsent(type, t -> BuiltInRegistries.MOB_EFFECT.wrapAsHolder(supplier.get()));
    }

    public static Holder<MobEffect> groupHolder() {
        if (groupHolder == null) groupHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GROUP.get());
        return groupHolder;
    }
}