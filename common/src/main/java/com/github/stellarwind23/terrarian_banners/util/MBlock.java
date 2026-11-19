package com.github.stellarwind23.terrarian_banners.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.*;
import java.util.function.Function;

public record MBlock<T extends Block>(String role, Function<BlockBehaviour.Properties, T> blockConstructor, Optional<BlockBehaviour.Properties> feedInProps) {
}