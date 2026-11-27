package com.github.stellarwind23.terrarian_banners.content.crafting;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TBRecipes {

    private static DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
    public static RegistrySupplier<RecipeSerializer<ShieldAddMobBannerRecipe>> ADD_MOB_BANNER;
    public static RegistrySupplier<RecipeSerializer<ShieldRemoveMobBannerRecipe>> REMOVE_MOB_BANNER;

    public static void init() {
        SERIALIZERS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.RECIPE_SERIALIZER);

        ADD_MOB_BANNER = registerCustomRecipe("shield_add_mob_banner", ShieldAddMobBannerRecipe::new);
        REMOVE_MOB_BANNER = registerCustomRecipe("shield_remove_mob_banner", ShieldRemoveMobBannerRecipe::new);

        SERIALIZERS.register();
    }

    private static <T extends CustomRecipe> RegistrySupplier<RecipeSerializer<T>> registerCustomRecipe(String name, CustomRecipe.Serializer.Factory<T> customRecipe) {
        Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, name);
        return SERIALIZERS.register(id, () -> new CustomRecipe.Serializer<T>(customRecipe));
    }
}