package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.client.render.TBStyleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class TBShields {
    private static final Map<Item, TBStyleType> SHIELDS = new HashMap<>();

    public static void register(Item shield, TBStyleType type) { SHIELDS.put(shield, type); }
    public static @Nullable TBStyleType typeOf(ItemStack stack) { return SHIELDS.get(stack.getItem()); }
}
