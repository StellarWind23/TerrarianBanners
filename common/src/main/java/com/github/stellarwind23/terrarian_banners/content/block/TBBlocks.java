package com.github.stellarwind23.terrarian_banners.content.block;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.MBlock;
import com.github.stellarwind23.terrarian_banners.util.MBlockGroup;
import com.github.stellarwind23.terrarian_banners.util.MBlockProps;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;

public class TBBlocks {

    protected static DeferredRegister<Block> BLOCKS;

    public static MBlockGroup BANNER_GROUP;

    public static RegistrySupplier<MobBannerBlock> MOB_BANNER;
    public static RegistrySupplier<MobWallBannerBlock> MOB_WALL_BANNER;

    private static Optional<BlockBehaviour.Properties> bannerProps() {
        return Optional.of(new MBlockProps()
                .noCollision()
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .getCopy());
    }

    public static void init() {

        BLOCKS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.BLOCK);

        //Register stuff here ▼▼▼
        BANNER_GROUP = new MBlockGroup("banner");
        MOB_BANNER = registerBlock("mob_banner", BANNER_GROUP, new MBlock<>("standing",MobBannerBlock::new, bannerProps()));
        MOB_WALL_BANNER = registerBlock("mob_wall_banner", BANNER_GROUP, new MBlock<>("wall", MobWallBannerBlock::new, bannerProps()));

        //Register stuff here ▲▲▲

        BLOCKS.register();
    }

    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, MBlock<T> mBlock) {
        return registerBlock(name, new MBlockGroup(name), mBlock);
    }

    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, MBlockGroup group, MBlock<T> mBlock) {
        Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, name);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);

        BlockBehaviour.Properties props = mBlock.feedInProps().orElseGet(BlockBehaviour.Properties::of).setId(key);
        T block = mBlock.blockConstructor().apply(props);

        group.put(mBlock.role(), block);
        return BLOCKS.register(id, () -> block);
    }
}
