package com.github.stellarwind23.terrarian_banners.fabric.datagen.provider;

import com.github.stellarwind23.terrarian_banners.client.render.TBBannerStyle;
import com.github.stellarwind23.terrarian_banners.client.render.TBBannerStyle.Layer;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MBannerStyleProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public MBannerStyleProvider(FabricDataOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "tb_banner_styles");
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        Map<Identifier, TBBannerStyle> styles = new LinkedHashMap<>();

        //Populate with default
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (TerrarianBanners.isBannerMob(type)) styles.put(id(type), TBBannerStyle.NEUTRAL);
        }

        //Hand written styles replace the generated ones
        addStyles(styles);

        List<CompletableFuture<?>> writes = new ArrayList<>();
        styles.forEach((id, style) -> {
            JsonElement json = TBBannerStyle.CODEC.encodeStart(JsonOps.INSTANCE, style).getOrThrow();
            writes.add(DataProvider.saveStable(cache, json, pathProvider.json(id)));
        });
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private void addStyles(Map<Identifier, TBBannerStyle> out) {
        put(out, EntityType.ALLAY, TBBannerStyle.fromLayers(e(EntityType.ALLAY), b("default", 0x00daff), d("border", 0x00adff), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ARMADILLO, TBBannerStyle.fromLayers(e(EntityType.ARMADILLO), b("default", 0xad716d), d("border", 0x824848), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.AXOLOTL, TBBannerStyle.fromLayers(e(EntityType.AXOLOTL), b("default", 0xfbc1e3), d("border", 0xa62d74), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.BAT, TBBannerStyle.fromLayers(e(EntityType.BAT), b("default", 0x4c3e30), d("border", 0x0f0f0f), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.BEE, TBBannerStyle.fromLayers(e(EntityType.BEE), b("default", 0xedc343), d("border", 0x43241b), d("comb", 0xFC9600)));
        put(out, EntityType.BLAZE, TBBannerStyle.fromLayers(e(EntityType.BLAZE), b("default", 0xf6b201), d("border", 0xfff87e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.BOGGED, TBBannerStyle.fromLayers(e(EntityType.BOGGED), b("default", 0x8a9c72), d("border", 0x314d1b), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.BREEZE, TBBannerStyle.fromLayers(e(EntityType.BREEZE), b("default", 0xaf94df), d("border", 0x9166df), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.CAMEL, TBBannerStyle.fromLayers(e(EntityType.CAMEL), b("default", 0xfcc369), d("border", 0xcb9337), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.CAT, TBBannerStyle.fromLayers(e(EntityType.CAT), b("default", 0xefc88e), d("border", 0x957256), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.CAVE_SPIDER, TBBannerStyle.fromLayers(e(EntityType.CAVE_SPIDER), b("default", 0x0c424e), d("border", 0xa80e0e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.CHICKEN, TBBannerStyle.fromLayers(e(EntityType.CHICKEN), b("default", 0xa1a1a1), d("border", 0xff0000), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.COD, TBBannerStyle.fromLayers(e(EntityType.COD), b("default", 0xc1a76a), d("border", 0xe5c48b), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.COW, TBBannerStyle.fromLayers(e(EntityType.COW), b("default", 0x443626), d("border", 0xa1a1a1), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.CREAKING, TBBannerStyle.fromLayers(e(EntityType.CREAKING), b("default", 0x5f5f5f), d("border", 0xfc7812), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.CREEPER, TBBannerStyle.fromLayers(e(EntityType.CREEPER), b("default", 0x0da70b), d("border", 0x000000), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.DOLPHIN, TBBannerStyle.fromLayers(e(EntityType.DOLPHIN), b("default", 0x223b4d), d("border", 0xf9f9f9), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.DONKEY, TBBannerStyle.fromLayers(e(EntityType.DONKEY), b("default", 0x534539), d("border", 0x867566), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.DROWNED, TBBannerStyle.fromLayers(e(EntityType.DROWNED), b("default", 0x8ff1d7), d("border", 0x799c65), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ELDER_GUARDIAN, TBBannerStyle.fromLayers(e(EntityType.ELDER_GUARDIAN), b("default", 0xceccba), d("border", 0x747693), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ENDER_DRAGON, TBBannerStyle.fromLayers(e(EntityType.ENDER_DRAGON), b("default", 0x1c1c1c), d("border", 0xe079fa), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ENDERMAN, TBBannerStyle.fromLayers(e(EntityType.ENDERMAN), b("default", 0x161616), d("border", 0x000000), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ENDERMITE, TBBannerStyle.fromLayers(e(EntityType.ENDERMITE), b("default", 0x161616), d("border", 0x6e6e6e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.EVOKER, TBBannerStyle.fromLayers(e(EntityType.EVOKER), b("default", 0x959b9b), d("border", 0x1e1c1a), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.FOX, TBBannerStyle.fromLayers(e(EntityType.FOX), b("default", 0xd5b69f), d("border", 0xcc6920), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.FROG, TBBannerStyle.fromLayers(e(EntityType.FROG), b("default", 0xd07444), d("border", 0xffc77c), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.GHAST, TBBannerStyle.fromLayers(e(EntityType.GHAST), b("default", 0xf9f9f9), d("border", 0xbcbcbc), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.GLOW_SQUID, TBBannerStyle.fromLayers(e(EntityType.GLOW_SQUID), b("default", 0x095656), d("border", 0x85f1bc), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.GOAT, TBBannerStyle.fromLayers(e(EntityType.GOAT), b("default", 0xa5947c), d("border", 0x55493e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.GUARDIAN, TBBannerStyle.fromLayers(e(EntityType.GUARDIAN), b("default", 0x5a8272), d("border", 0xf17d30), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.HOGLIN, TBBannerStyle.fromLayers(e(EntityType.HOGLIN), b("default", 0xc66e55), d("border", 0x5f6464), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.HORSE, TBBannerStyle.fromLayers(e(EntityType.HORSE), b("default", 0xc09e7d), d("border", 0xeee500), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.HUSK, TBBannerStyle.fromLayers(e(EntityType.HUSK), b("default", 0x797061), d("border", 0xe6cc94), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.IRON_GOLEM, TBBannerStyle.fromLayers(e(EntityType.IRON_GOLEM), b("default", 0xdbcdc2), d("border", 0x74a332), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.LLAMA, TBBannerStyle.fromLayers(e(EntityType.LLAMA), b("default", 0xc09e7d), d("border", 0x995f40), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.MAGMA_CUBE, TBBannerStyle.fromLayers(e(EntityType.MAGMA_CUBE), b("default", 0x340000), d("border", 0xfcfc00), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.MOOSHROOM, TBBannerStyle.fromLayers(e(EntityType.MOOSHROOM), b("default", 0xa00f10), d("border", 0xb7b7b7), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.MULE, TBBannerStyle.fromLayers(e(EntityType.MULE), b("default", 0x1b0200), d("border", 0x51331d), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.OCELOT, TBBannerStyle.fromLayers(e(EntityType.OCELOT), b("default", 0xefde7d), d("border", 0x564434), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PANDA, TBBannerStyle.fromLayers(e(EntityType.PANDA), b("default", 0xe7e7e7), d("border", 0x1b1b22), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PARROT, TBBannerStyle.fromLayers(e(EntityType.PARROT), b("default", 0x0da70b), d("border", 0xff0000), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PHANTOM, TBBannerStyle.fromLayers(e(EntityType.PHANTOM), b("default", 0x43518a), d("border", 0x88ff00), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PIG, TBBannerStyle.fromLayers(e(EntityType.PIG), b("default", 0xf0a5a2), d("border", 0xdb635f), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PIGLIN, TBBannerStyle.fromLayers(e(EntityType.PIGLIN), b("default", 0x995f40), d("border", 0xf9f3a4), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PIGLIN_BRUTE, TBBannerStyle.fromLayers(e(EntityType.PIGLIN_BRUTE), b("default", 0x592a10), d("border", 0xf9f3a4), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PILLAGER, TBBannerStyle.fromLayers(e(EntityType.PILLAGER), b("default", 0x532f36), d("border", 0x959b9b), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.POLAR_BEAR, TBBannerStyle.fromLayers(e(EntityType.POLAR_BEAR), b("default", 0xeeeede), d("border", 0xd5d6cd), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.PUFFERFISH, TBBannerStyle.fromLayers(e(EntityType.PUFFERFISH), b("default", 0xf6b201), d("border", 0x37c3f2), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.RABBIT, TBBannerStyle.fromLayers(e(EntityType.RABBIT), b("default", 0x995f40), d("border", 0x734831), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.RAVAGER, TBBannerStyle.fromLayers(e(EntityType.RAVAGER), b("default", 0x757470), d("border", 0x5b5049), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SALMON, TBBannerStyle.fromLayers(e(EntityType.SALMON), b("default", 0xa00f10), d("border", 0x0e8474), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SHEEP, TBBannerStyle.fromLayers(e(EntityType.SHEEP), b("default", 0xe7e7e7), d("border", 0xffb5b5), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SHULKER, TBBannerStyle.fromLayers(e(EntityType.SHULKER), b("default", 0x946794), d("border", 0x4d3852), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SILVERFISH, TBBannerStyle.fromLayers(e(EntityType.SILVERFISH), b("default", 0x6e6e6e), d("border", 0x303030), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SKELETON, TBBannerStyle.fromLayers(e(EntityType.SKELETON), b("default", 0xc1c1c1), d("border", 0x494949), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SKELETON_HORSE, TBBannerStyle.fromLayers(e(EntityType.SKELETON_HORSE), b("default", 0x68684f), d("border", 0xe5e5d8), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SLIME, TBBannerStyle.fromLayers(e(EntityType.SLIME), b("default", 0x51a03e), d("border", 0x7ebf6e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SNIFFER, TBBannerStyle.fromLayers(e(EntityType.SNIFFER), b("default", 0x871e09), d("border", 0x25ab70), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SNOW_GOLEM, TBBannerStyle.fromLayers(e(EntityType.SNOW_GOLEM), b("default", 0xd9f2f2), d("border", 0x81a4a4), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SPIDER, TBBannerStyle.fromLayers(e(EntityType.SPIDER), b("default", 0x342d27), d("border", 0xa80e0e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.SQUID, TBBannerStyle.fromLayers(e(EntityType.SQUID), b("default", 0x223b4d), d("border", 0x708899), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.STRAY, TBBannerStyle.fromLayers(e(EntityType.STRAY), b("default", 0x617677), d("border", 0xddeaea), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.STRIDER, TBBannerStyle.fromLayers(e(EntityType.STRIDER), b("default", 0x9c3436), d("border", 0x4d494d), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.TADPOLE, TBBannerStyle.fromLayers(e(EntityType.TADPOLE), b("default", 0x6d533d), d("border", 0x160a00), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.TRADER_LLAMA, TBBannerStyle.fromLayers(e(EntityType.TRADER_LLAMA), b("default", 0xeaa430), d("border", 0x456296), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.TROPICAL_FISH, TBBannerStyle.fromLayers(e(EntityType.TROPICAL_FISH), b("default", 0xef6915), d("border", 0xfff9ef), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.TURTLE, TBBannerStyle.fromLayers(e(EntityType.TURTLE), b("default", 0xe7e7e7), d("border", 0x00afaf), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.VEX, TBBannerStyle.fromLayers(e(EntityType.VEX), b("default", 0x7a90a4), d("border", 0xe8edf1), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.VILLAGER, TBBannerStyle.fromLayers(e(EntityType.VILLAGER), b("default", 0x563c33), d("border", 0xbd8b72), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.VINDICATOR, TBBannerStyle.fromLayers(e(EntityType.VINDICATOR), b("default", 0x959b9b), d("border", 0x275e61), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.WANDERING_TRADER, TBBannerStyle.fromLayers(e(EntityType.WANDERING_TRADER), b("default", 0x456296), d("border", 0xeaa430), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.WARDEN, TBBannerStyle.fromLayers(e(EntityType.WARDEN), b("default", 0x0f4649), d("border", 0x39d6e0), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.WITCH, TBBannerStyle.fromLayers(e(EntityType.WITCH), b("default", 0x340000), d("border", 0x51a03e), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.WITHER, TBBannerStyle.fromLayers(e(EntityType.WITHER), b("default", 0x141414), d("border", 0x4d72a0), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.WITHER_SKELETON, TBBannerStyle.fromLayers(e(EntityType.WITHER_SKELETON), b("default", 0x141414), d("border", 0x474d4d), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.WOLF, TBBannerStyle.fromLayers(e(EntityType.WOLF), b("default", 0xd7d3d3), d("border", 0xceaf96), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ZOGLIN, TBBannerStyle.fromLayers(e(EntityType.ZOGLIN), b("default", 0xc66e55), d("border", 0xe6e6e6), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ZOMBIE, TBBannerStyle.fromLayers(e(EntityType.ZOMBIE), b("default", 0x00afaf), d("border", 0x799c65), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ZOMBIE_HORSE, TBBannerStyle.fromLayers(e(EntityType.ZOMBIE_HORSE), b("default", 0x315234), d("border", 0x97c284), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ZOMBIE_VILLAGER, TBBannerStyle.fromLayers(e(EntityType.ZOMBIE_VILLAGER), b("default", 0x563c33), d("border", 0x799c65), d("thin_border", 0xFFFFFF)));
        put(out, EntityType.ZOMBIFIED_PIGLIN, TBBannerStyle.fromLayers(e(EntityType.ZOMBIFIED_PIGLIN), b("default", 0xea9393), d("border", 0x4c7129), d("thin_border", 0xFFFFFF)));
    }

    // ---- Layer helpers ----
    private static Layer e(EntityType<?> type) { return Layer.asset("emblem", "entity/mob_banner/emblem/" + id(type).getPath()); }
    private static Layer b(String name, int rgb) { return Layer.asset("base","entity/mob_banner/base/" + name).withColor(rgb); }
    private static Layer d(String name, int rgb) { return Layer.asset(name, "entity/mob_banner/detail/" + name).withColor(rgb); }

    private static void put(Map<Identifier, TBBannerStyle> out, EntityType<?> type, TBBannerStyle style) {
        out.put(id(type), style);
    }

    private static Identifier id(EntityType<?> type) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(type);
    }

    @Override
    public @NonNull String getName() { return "Banner Styles"; }
}