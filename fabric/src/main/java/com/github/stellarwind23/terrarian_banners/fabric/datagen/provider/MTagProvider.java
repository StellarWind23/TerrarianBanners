package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.content.TBTags;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class MTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public MTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        var tag = valueLookupBuilder(TBTags.HAS_BANNER);

        // Everything hostile by default
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (TerrarianBanners.isBannerMob(type)) tag.add(type);
        }
    }
}
