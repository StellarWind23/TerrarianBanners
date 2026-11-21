package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.util.MBlockGroup;
import com.github.stellarwind23.terrarian_banners.util.MItem;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

public class MModelProvider extends FabricModelProvider {

    public MModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators gen) {
        for (MBlockGroup group : MBlockGroup.MOD_GROUPS) {
            switch (group.modelType()) {
                case "none" -> { }
                case "banner" -> {
                    gen.createParticleOnlyBlock(group.get("standing"), Blocks.WHITE_WOOL);
                    gen.createParticleOnlyBlock(group.get("wall"), Blocks.WHITE_WOOL);
                }
                default -> group.all().forEach(gen::createTrivialCube);
            }
        }
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {
        for(MItem<?> mItem : MItem.MOD_ITEMS) {
            if(MItem.REGISTERED_MOD_ITEMS.get(mItem) == null) continue;
            switch (mItem.modelType()) {
                case "none" -> { }
                case "handheld" -> itemModelGenerator.generateFlatItem(MItem.REGISTERED_MOD_ITEMS.get(mItem), ModelTemplates.FLAT_HANDHELD_ITEM);
                default -> itemModelGenerator.generateFlatItem(MItem.REGISTERED_MOD_ITEMS.get(mItem), ModelTemplates.FLAT_ITEM);
            }
        }
    }
}
