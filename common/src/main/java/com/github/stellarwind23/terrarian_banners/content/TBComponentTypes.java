package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.UnaryOperator;

public class TBComponentTypes {

    protected static DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES;

    public static RegistrySupplier<DataComponentType<Identifier>> MOB_BANNER;

    public static void init() {
        DATA_COMPONENT_TYPES = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.DATA_COMPONENT_TYPE);

        //Register stuff here ▼▼▼

        MOB_BANNER = registerComponent("MOB_BANNER", (builder) -> builder
                .persistent(Identifier.CODEC)
                .networkSynchronized(Identifier.STREAM_CODEC));

        //Register stuff here ▲▲▲

        DATA_COMPONENT_TYPES.register();
    }

    private static <T> RegistrySupplier<DataComponentType<T>> registerComponent(String name, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> {
            ResourceKey.create(Registries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, name));
            return unaryOperator.apply(DataComponentType.builder()).build();
        });
    }
}
