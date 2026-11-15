package com.github.stellarwind23.terrarian_banners.content.block;

import com.github.stellarwind23.terrarian_banners.content.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class MobBannerBlockEntity extends BlockEntity {

    private @Nullable Identifier mob;

    public MobBannerBlockEntity(BlockPos pos, BlockState state) {
        super(TBBlockEntityTypes.MOB_BANNER.get(), pos, state);
    }

    public @Nullable Identifier getMob() { return mob; }

    public void setMob(@Nullable Identifier mob) {
        this.mob = mob;
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("mob", Identifier.CODEC, mob);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        mob = input.read("mob", Identifier.CODEC).orElse(null);
    }

    // Item -> block entity (placing)
    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        mob = components.get(TBComponentTypes.MOB_BANNER.get());
    }

    // Block entity -> item (ctrl+pick block, loot tables)
    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        if (mob != null) builder.set(TBComponentTypes.MOB_BANNER.get(), mob);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard("mob");
    }

    // Client sync
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        return saveCustomOnly(registries);
    }
}