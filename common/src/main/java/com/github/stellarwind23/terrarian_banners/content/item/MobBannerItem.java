package com.github.stellarwind23.terrarian_banners.content.item;

import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.TBItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public class MobBannerItem extends StandingAndWallBlockItem {

    public MobBannerItem(Block standing, Block wall, Properties props) {
        super(standing, wall, Direction.DOWN, props);
    }

    public static ItemStack createStack(EntityType<?> type) {
        ItemStack stack = new ItemStack(TBItems.MOB_BANNER.get());
        stack.set(TBComponentTypes.MOB_BANNER.get(), BuiltInRegistries.ENTITY_TYPE.getKey(type));
        return stack;
    }

    @Override
    public @NonNull Component getName(ItemStack stack) {
        Identifier id = stack.get(TBComponentTypes.MOB_BANNER.get());
        if (id != null) {
            var type = BuiltInRegistries.ENTITY_TYPE.getOptional(id);
            if (type.isPresent()) {
                return Component.translatable(getDescriptionId() + type.get().getDescriptionId().replaceFirst("entity", ""));
            }
        }
        return super.getName(stack);
    }
}