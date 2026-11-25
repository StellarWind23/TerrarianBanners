package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.MTranslation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
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

        // Everything hostile by default
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if(TerrarianBanners.isBannerMob(type)) {
                translationBuilder.add("item.terrarian_banners.mob_banner" + type.getDescriptionId().replaceFirst("entity", ""), type.getDescription().getString() + " Banner");
                translationBuilder.add("effect.terrarian_banners.mob_effect" + type.getDescriptionId().replaceFirst("entity", ""), type.getDescription().getString() + " Slayer");
            }
        }

        //Config
        translationBuilder.add("terrarian_banners.midnightconfig.title", "Terrarian Banners");
        translationBuilder.add("terrarian_banners.midnightconfig.kills_header", "Kills Config:");
        translationBuilder.add("terrarian_banners.midnightconfig.kills_per_Banner", "Kills per banner");
        translationBuilder.add("terrarian_banners.midnightconfig.kills_required_overrides", "Kills required override(s)");
        translationBuilder.add("terrarian_banners.midnightconfig.require_player_kills", "Require player to kill?");
        translationBuilder.add("terrarian_banners.midnightconfig.effect_header", "Effect Config:");
        translationBuilder.add("terrarian_banners.midnightconfig.effect_radius", "Radius of effect");
        translationBuilder.add("terrarian_banners.midnightconfig.damage_taken_multiplier", "Damage taken from Mob multiplier");
        translationBuilder.add("terrarian_banners.midnightconfig.damage_dealt_multiplier", "Damage dealt to Mob multiplier");
        translationBuilder.add("terrarian_banners.midnightconfig.effect_duration_seconds", "Effect duration(seconds)");


        //Tooltip
        translationBuilder.add("effect.terrarian_banners.banner_group", "Mob Banners");
        translationBuilder.add("effect.terrarian_banners.banner_group.header", "Mob Banners (%s)");
        translationBuilder.add("effect.terrarian_banners.banner_group.more", "...and %s more");
    }
}
