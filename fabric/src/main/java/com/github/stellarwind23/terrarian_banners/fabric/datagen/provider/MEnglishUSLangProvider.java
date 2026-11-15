package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.util.MTranslation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;
import oshi.util.tuples.Pair;

import java.util.concurrent.CompletableFuture;

public class MEnglishUSLangProvider extends FabricLanguageProvider {

    public MEnglishUSLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, @NonNull TranslationBuilder translationBuilder) {
        for(MTranslation translation : MTranslation.MOD_TRANSLATIONS) {
            Pair<String, String> t = translation.createTranslation();
            translationBuilder.add(t.getA(), t.getB());
        }
    }
}
