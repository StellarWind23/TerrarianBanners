package com.github.stellarwind23.terrarian_banners.fabric.datagen;

import com.github.stellarwind23.terrarian_banners.fabric.datagen.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

public class TerrarianBannersFabricDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        //Create
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        //Add Providers
        pack.addProvider(MModelProvider::new);
        pack.addProvider(MEnglishUSLangProvider::new);
        pack.addProvider(MTagProvider::new);
        pack.addProvider(MBlockLootProvider::new);
        pack.addProvider(MBannerStyleProvider::new);
    }
}
