package com.github.stellarwind23.terrarian_banners.content.block;

import com.github.stellarwind23.terrarian_banners.content.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.content.TBEffects;
import com.github.stellarwind23.terrarian_banners.content.TBTags;
import com.github.stellarwind23.terrarian_banners.util.TBConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class MobBannerBlockEntity extends BlockEntity {

    private @Nullable Identifier mob;

    public MobBannerBlockEntity(BlockPos pos, BlockState state) {
        super(TBBlockEntityTypes.MOB_BANNER.get(), pos, state);
    }

    public @Nullable Identifier getMob() { return mob; }

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

    public static void serverTick(Level level, BlockPos pos, BlockState state, MobBannerBlockEntity be) {
        if (be.mob == null || Math.floorMod(level.getGameTime() + pos.hashCode(), 40L) != 0) return;

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(be.mob).orElse(null);
        if (type == null || !type.is(TBTags.HAS_BANNER)) return;   // the tag disables mobs via datapack

        Holder<MobEffect> effect = TBEffects.holderFor(type);
        if (effect == null) return;

        AABB area = new AABB(pos).inflate(TBConfig.auraRadius);
        for (Player player : level.getEntitiesOfClass(Player.class, area)) {
            player.addEffect(new MobEffectInstance(effect, (int) (TBConfig.effectDurationSeconds * 20D), 0, true, false, true));
        }
    }
}