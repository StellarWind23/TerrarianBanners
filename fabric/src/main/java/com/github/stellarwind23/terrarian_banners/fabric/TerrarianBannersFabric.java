package com.github.stellarwind23.terrarian_banners.fabric;

import com.github.stellarwind23.terrarian_banners.content.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.content.TBBlocks;
import com.github.stellarwind23.terrarian_banners.content.block.MobBannerBlockEntity;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public final class TerrarianBannersFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        TerrarianBanners.init();

        TBBlockEntityTypes.MOB_BANNER = registerBlockEntity(
                "mob_banner",
                MobBannerBlockEntity::new,
                TBBlocks.MOB_BANNER.get(),
                TBBlocks.MOB_WALL_BANNER.get());

        MidnightConfig.init(TerrarianBanners.MOD_ID, TBConfig.class);
    }

    public static <E extends BlockEntity> Supplier<BlockEntityType<E>> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<? extends E> factory, Block... blocks) {
        var type = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, name),
                FabricBlockEntityTypeBuilder.<E>create(factory, blocks).build()
        );
        return () -> type;
    }
}
