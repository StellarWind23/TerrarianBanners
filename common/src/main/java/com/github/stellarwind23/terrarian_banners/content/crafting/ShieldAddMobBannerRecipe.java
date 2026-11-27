package com.github.stellarwind23.terrarian_banners.content.crafting;

import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.TBItems;
import com.github.stellarwind23.terrarian_banners.content.TBShields;
import com.github.stellarwind23.terrarian_banners.content.component.ShieldMobBanner;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class ShieldAddMobBannerRecipe extends CustomRecipe {

    public ShieldAddMobBannerRecipe(CraftingBookCategory category) { super(category); }

    @Override
    public boolean matches(CraftingInput input, Level level) { return !assemble(input).isEmpty(); }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack shield = ItemStack.EMPTY, banner = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getItem(i);
            if (s.isEmpty()) continue;
            if (s.is(TBItems.MOB_BANNER.get()) && banner.isEmpty()) banner = s;
            else if (TBShields.typeOf(s) != null && shield.isEmpty()) shield = s;
            else return ItemStack.EMPTY;
        }
        Identifier mob = banner.isEmpty() ? null : banner.get(TBComponentTypes.MOB_BANNER.get());
        if (shield.isEmpty() || mob == null) return ItemStack.EMPTY;
        if (shield.has(TBComponentTypes.SHIELD_MOB_BANNER.get())
                || shield.has(DataComponents.BANNER_PATTERNS) || shield.has(DataComponents.BASE_COLOR)) {
            return ItemStack.EMPTY;
        }

        ItemStack out = shield.copyWithCount(1);
        out.set(TBComponentTypes.SHIELD_MOB_BANNER.get(), new ShieldMobBanner(mob, TBShields.typeOf(shield).id()));
        return out;
    }

    @Override
    public @NonNull RecipeSerializer<? extends CustomRecipe> getSerializer() { return TBRecipes.ADD_MOB_BANNER.get(); }
}