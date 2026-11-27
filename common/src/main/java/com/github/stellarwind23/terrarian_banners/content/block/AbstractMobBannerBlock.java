package com.github.stellarwind23.terrarian_banners.content.block;

import com.github.stellarwind23.terrarian_banners.content.component.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.item.TBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public abstract class AbstractMobBannerBlock extends BaseEntityBlock {

    protected AbstractMobBannerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new MobBannerBlockEntity(pos, state);
    }

    @Override
    protected @NonNull ItemStack getCloneItemStack(LevelReader level, @NonNull BlockPos pos, @NonNull BlockState state, boolean includeData) {
        ItemStack stack = new ItemStack(TBItems.MOB_BANNER.get());
        if (level.getBlockEntity(pos) instanceof MobBannerBlockEntity banner && banner.getMob() != null) {
            stack.set(TBComponentTypes.MOB_BANNER.get(), banner.getMob());
        }
        return stack;
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return level.isClientSide() ? null
                : createTickerHelper(type, TBBlockEntityTypes.MOB_BANNER.get(), MobBannerBlockEntity::serverTick);
    }
}