package com.github.stellarwind23.terrarian_banners.neoforge;

import com.github.stellarwind23.terrarian_banners.client.init.TerrarianBannersClientInit;
import com.github.stellarwind23.terrarian_banners.content.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.content.TBBlocks;
import com.github.stellarwind23.terrarian_banners.content.block.MobBannerBlockEntity;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import dev.architectury.registry.registries.DeferredRegister;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashSet;
import java.util.function.Supplier;

@Mod(TerrarianBanners.MOD_ID)
public final class TerrarianBannersNeoForge {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;

    public TerrarianBannersNeoForge(IEventBus modBus) {
        TerrarianBanners.init();

        TBBlockEntityTypes.MOB_BANNER = registerBlockEntity(
                "mob_banner",
                MobBannerBlockEntity::new,
                TBBlocks.MOB_BANNER,
                TBBlocks.MOB_WALL_BANNER
        );

        modBus.addListener((FMLClientSetupEvent e) -> {
            TerrarianBannersClientInit.init();
        });

        modBus.addListener((FMLCommonSetupEvent e) -> {
            MidnightConfig.init(TerrarianBanners.MOD_ID, TBConfig.class);
        });
    }

    @SafeVarargs
    public static <E extends BlockEntity> Supplier<BlockEntityType<E>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<E> supplier, Supplier<? extends Block>... blocks) {
        return BLOCK_ENTITY_TYPES.register(name, () -> {
            // .get() is called here, inside the supplier, which runs after registration
            HashSet<Block> blocksIn = new HashSet<>();
            for (Supplier<? extends Block> b : blocks) blocksIn.add(b.get());
            return new BlockEntityType<>(supplier, blocksIn);
        });
    }
}
