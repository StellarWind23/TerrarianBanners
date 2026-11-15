package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.content.TBBlocks;
import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class MBlockLootProvider extends FabricBlockLootTableProvider {

    public MBlockLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generate() {
        add(TBBlocks.MOB_BANNER.get(), block -> LootTable.lootTable().withPool(
                applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block).apply(
                                CopyComponentsFunction.copyComponentsFromBlockEntity(new ContextKey<>(Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "mob_banner")))
                                        .include(TBComponentTypes.MOB_BANNER.get()))))));
    }
}