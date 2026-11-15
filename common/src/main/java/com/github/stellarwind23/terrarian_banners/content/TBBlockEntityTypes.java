package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.content.block.MobBannerBlockEntity;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class TBBlockEntityTypes {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;

    public static RegistrySupplier<BlockEntityType<MobBannerBlockEntity>> MOB_BANNER;

    public static void init() {

        BLOCK_ENTITIES = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

        MOB_BANNER = BLOCK_ENTITIES.register("mob_banner",
                () -> new BlockEntityType<>(MobBannerBlockEntity::new,
                        Set.of(TBBlocks.MOB_BANNER.get(), TBBlocks.MOB_WALL_BANNER.get())));


    }
}
