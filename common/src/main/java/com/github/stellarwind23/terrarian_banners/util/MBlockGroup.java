package com.github.stellarwind23.terrarian_banners.util;

import net.minecraft.world.level.block.Block;

import java.util.*;

public final class MBlockGroup {

    public static final List<MBlockGroup> MOD_GROUPS = new ArrayList<>();

    private final String modelType;
    private final Map<String, Block> blocks = new LinkedHashMap<>();

    public MBlockGroup(String modelType) {
        this.modelType = modelType;
        MOD_GROUPS.add(this);
    }

    public String modelType() { return modelType; }

    public void put(String role, Block block) {
        if (blocks.putIfAbsent(role, block) != null) {
            throw new IllegalStateException("Duplicate role '" + role + "' in block group '" + modelType + "'");
        }
    }

    public Block get(String role) {
        Block block = blocks.get(role);
        if (block == null) {
            throw new IllegalStateException("Block group '" + modelType + "' has no role '" + role + "'");
        }
        return block;
    }

    public Collection<Block> all() { return blocks.values(); }
}