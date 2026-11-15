package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.MBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Optional;

public class TBBlocks {

    protected static DeferredRegister<Block> BLOCKS;

    public static RegistrySupplier<Block> TEMPLATE_BLOCK;

    public static void init() {

        BLOCKS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.BLOCK);

        //Register stuff here ▼▼▼

        TEMPLATE_BLOCK = registerBlock("template_block", new MBlock<>("simple", Block::new, Optional.empty()));

        //Register stuff here ▲▲▲

        BLOCKS.register();
    }

    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, MBlock<T> mBlock) {
        Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, name);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);

        T block;
        if(mBlock.feedInProps().isPresent()) {
            block = mBlock.blockConstructor().apply(mBlock.feedInProps().orElseThrow().setId(key));
        } else {
            block = mBlock.blockConstructor().apply(BlockBehaviour.Properties.of().setId(key));
        }

        MBlock.REGISTERED_MOD_BLOCKS.put(mBlock, block);
        return BLOCKS.register(id, () -> block);
    }
}
