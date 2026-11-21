package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class TBTags {
    public static final TagKey<EntityType<?>> HAS_BANNER = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "has_banner"));
}
