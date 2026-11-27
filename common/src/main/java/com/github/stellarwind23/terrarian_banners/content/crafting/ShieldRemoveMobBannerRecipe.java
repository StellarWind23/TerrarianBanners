package com.github.stellarwind23.terrarian_banners.content.crafting;

import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.TBItems;
import com.github.stellarwind23.terrarian_banners.content.component.ShieldMobBanner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class ShieldRemoveMobBannerRecipe extends CustomRecipe {

    public ShieldRemoveMobBannerRecipe(CraftingBookCategory category) { super(category); }

    @Override
    public boolean matches(@NonNull CraftingInput input, @NonNull Level level) { return !assemble(input).isEmpty(); }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack only = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getItem(i);
            if (s.isEmpty()) continue;
            if (!only.isEmpty()) return ItemStack.EMPTY;   // must be alone in the grid
            only = s;
        }
        ShieldMobBanner sb = only.isEmpty() ? null : only.get(TBComponentTypes.SHIELD_MOB_BANNER.get());
        if (sb == null) return ItemStack.EMPTY;

        ItemStack banner = new ItemStack(TBItems.MOB_BANNER.get());
        banner.set(TBComponentTypes.MOB_BANNER.get(), sb.mob());
        return banner;
    }

    @Override
    public @NonNull RecipeSerializer<? extends CustomRecipe> getSerializer() { return TBRecipes.REMOVE_MOB_BANNER.get(); }
}
