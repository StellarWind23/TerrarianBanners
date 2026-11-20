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
        put(out, EntityType.ALLAY, new TBBannerStyle(i(EntityType.ALLAY), b("default", 0x00daff), s("default", 0x00adff), a("default", 0xFFFFFF)));
        put(out, EntityType.ARMADILLO, new TBBannerStyle(i(EntityType.ARMADILLO), b("default", 0xad716d), s("default", 0x824848), a("default", 0xFFFFFF)));
        put(out, EntityType.AXOLOTL, new TBBannerStyle(i(EntityType.AXOLOTL), b("default", 0xfbc1e3), s("default", 0xa62d74), a("default", 0xFFFFFF)));
        put(out, EntityType.BAT, new TBBannerStyle(i(EntityType.BAT), b("default", 0x4c3e30), s("default", 0x0f0f0f), a("default", 0xFFFFFF)));
        put(out, EntityType.BEE, new TBBannerStyle(i(EntityType.BEE), b("default", 0xedc343), s("default", 0x43241b), a("comb", 0xFC9600)));
        put(out, EntityType.BLAZE, new TBBannerStyle(i(EntityType.BLAZE), b("default", 0xf6b201), s("default", 0xfff87e), a("default", 0xFFFFFF)));
        put(out, EntityType.BOGGED, new TBBannerStyle(i(EntityType.BOGGED), b("default", 0x8a9c72), s("default", 0x314d1b), a("default", 0xFFFFFF)));
        put(out, EntityType.BREEZE, new TBBannerStyle(i(EntityType.BREEZE), b("default", 0xaf94df), s("default", 0x9166df), a("default", 0xFFFFFF)));
        put(out, EntityType.CAMEL, new TBBannerStyle(i(EntityType.CAMEL), b("default", 0xfcc369), s("default", 0xcb9337), a("default", 0xFFFFFF)));
        put(out, EntityType.CAT, new TBBannerStyle(i(EntityType.CAT), b("default", 0xefc88e), s("default", 0x957256), a("default", 0xFFFFFF)));
        put(out, EntityType.CAVE_SPIDER, new TBBannerStyle(i(EntityType.CAVE_SPIDER), b("default", 0x0c424e), s("default", 0xa80e0e), a("default", 0xFFFFFF)));
        put(out, EntityType.CHICKEN, new TBBannerStyle(i(EntityType.CHICKEN), b("default", 0xa1a1a1), s("default", 0xff0000), a("default", 0xFFFFFF)));
        put(out, EntityType.COD, new TBBannerStyle(i(EntityType.COD), b("default", 0xc1a76a), s("default", 0xe5c48b), a("default", 0xFFFFFF)));
        put(out, EntityType.COW, new TBBannerStyle(i(EntityType.COW), b("default", 0x443626), s("default", 0xa1a1a1), a("default", 0xFFFFFF)));
        put(out, EntityType.CREAKING, new TBBannerStyle(i(EntityType.CREAKING), b("default", 0x5f5f5f), s("default", 0xfc7812), a("default", 0xFFFFFF)));
        put(out, EntityType.CREEPER, new TBBannerStyle(i(EntityType.CREEPER), b("default", 0x0da70b), s("default", 0x000000), a("default", 0xFFFFFF)));
        put(out, EntityType.DOLPHIN, new TBBannerStyle(i(EntityType.DOLPHIN), b("default", 0x223b4d), s("default", 0xf9f9f9), a("default", 0xFFFFFF)));
        put(out, EntityType.DONKEY, new TBBannerStyle(i(EntityType.DONKEY), b("default", 0x534539), s("default", 0x867566), a("default", 0xFFFFFF)));
        put(out, EntityType.DROWNED, new TBBannerStyle(i(EntityType.DROWNED), b("default", 0x8ff1d7), s("default", 0x799c65), a("default", 0xFFFFFF)));
        put(out, EntityType.ELDER_GUARDIAN, new TBBannerStyle(i(EntityType.ELDER_GUARDIAN), b("default", 0xceccba), s("default", 0x747693), a("default", 0xFFFFFF)));
        put(out, EntityType.ENDER_DRAGON, new TBBannerStyle(i(EntityType.ENDER_DRAGON), b("default", 0x1c1c1c), s("default", 0xe079fa), a("default", 0xFFFFFF)));
        put(out, EntityType.ENDERMAN, new TBBannerStyle(i(EntityType.ENDERMAN), b("default", 0x161616), s("default", 0x000000), a("default", 0xFFFFFF)));
        put(out, EntityType.ENDERMITE, new TBBannerStyle(i(EntityType.ENDERMITE), b("default", 0x161616), s("default", 0x6e6e6e), a("default", 0xFFFFFF)));
        put(out, EntityType.EVOKER, new TBBannerStyle(i(EntityType.EVOKER), b("default", 0x959b9b), s("default", 0x1e1c1a), a("default", 0xFFFFFF)));
        put(out, EntityType.FOX, new TBBannerStyle(i(EntityType.FOX), b("default", 0xd5b69f), s("default", 0xcc6920), a("default", 0xFFFFFF)));
        put(out, EntityType.FROG, new TBBannerStyle(i(EntityType.FROG), b("default", 0xd07444), s("default", 0xffc77c), a("default", 0xFFFFFF)));
        put(out, EntityType.GHAST, new TBBannerStyle(i(EntityType.GHAST), b("default", 0xf9f9f9), s("default", 0xbcbcbc), a("default", 0xFFFFFF)));
        put(out, EntityType.GLOW_SQUID, new TBBannerStyle(i(EntityType.GLOW_SQUID), b("default", 0x095656), s("default", 0x85f1bc), a("default", 0xFFFFFF)));
        put(out, EntityType.GOAT, new TBBannerStyle(i(EntityType.GOAT), b("default", 0xa5947c), s("default", 0x55493e), a("default", 0xFFFFFF)));
        put(out, EntityType.GUARDIAN, new TBBannerStyle(i(EntityType.GUARDIAN), b("default", 0x5a8272), s("default", 0xf17d30), a("default", 0xFFFFFF)));
        put(out, EntityType.HOGLIN, new TBBannerStyle(i(EntityType.HOGLIN), b("default", 0xc66e55), s("default", 0x5f6464), a("default", 0xFFFFFF)));
        put(out, EntityType.HORSE, new TBBannerStyle(i(EntityType.HORSE), b("default", 0xc09e7d), s("default", 0xeee500), a("default", 0xFFFFFF)));
        put(out, EntityType.HUSK, new TBBannerStyle(i(EntityType.HUSK), b("default", 0x797061), s("default", 0xe6cc94), a("default", 0xFFFFFF)));
        put(out, EntityType.IRON_GOLEM, new TBBannerStyle(i(EntityType.IRON_GOLEM), b("default", 0xdbcdc2), s("default", 0x74a332), a("default", 0xFFFFFF)));
        put(out, EntityType.LLAMA, new TBBannerStyle(i(EntityType.LLAMA), b("default", 0xc09e7d), s("default", 0x995f40), a("default", 0xFFFFFF)));
        put(out, EntityType.MAGMA_CUBE, new TBBannerStyle(i(EntityType.MAGMA_CUBE), b("default", 0x340000), s("default", 0xfcfc00), a("default", 0xFFFFFF)));
        put(out, EntityType.MOOSHROOM, new TBBannerStyle(i(EntityType.MOOSHROOM), b("default", 0xa00f10), s("default", 0xb7b7b7), a("default", 0xFFFFFF)));
        put(out, EntityType.MULE, new TBBannerStyle(i(EntityType.MULE), b("default", 0x1b0200), s("default", 0x51331d), a("default", 0xFFFFFF)));
        put(out, EntityType.OCELOT, new TBBannerStyle(i(EntityType.OCELOT), b("default", 0xefde7d), s("default", 0x564434), a("default", 0xFFFFFF)));
        put(out, EntityType.PANDA, new TBBannerStyle(i(EntityType.PANDA), b("default", 0xe7e7e7), s("default", 0x1b1b22), a("default", 0xFFFFFF)));
        put(out, EntityType.PARROT, new TBBannerStyle(i(EntityType.PARROT), b("default", 0x0da70b), s("default", 0xff0000), a("default", 0xFFFFFF)));
        put(out, EntityType.PHANTOM, new TBBannerStyle(i(EntityType.PHANTOM), b("default", 0x43518a), s("default", 0x88ff00), a("default", 0xFFFFFF)));
        put(out, EntityType.PIG, new TBBannerStyle(i(EntityType.PIG), b("default", 0xf0a5a2), s("default", 0xdb635f), a("default", 0xFFFFFF)));
        put(out, EntityType.PIGLIN, new TBBannerStyle(i(EntityType.PIGLIN), b("default", 0x995f40), s("default", 0xf9f3a4), a("default", 0xFFFFFF)));
        put(out, EntityType.PIGLIN_BRUTE, new TBBannerStyle(i(EntityType.PIGLIN_BRUTE), b("default", 0x592a10), s("default", 0xf9f3a4), a("default", 0xFFFFFF)));
        put(out, EntityType.PILLAGER, new TBBannerStyle(i(EntityType.PILLAGER), b("default", 0x532f36), s("default", 0x959b9b), a("default", 0xFFFFFF)));
        put(out, EntityType.POLAR_BEAR, new TBBannerStyle(i(EntityType.POLAR_BEAR), b("default", 0xeeeede), s("default", 0xd5d6cd), a("default", 0xFFFFFF)));
        put(out, EntityType.PUFFERFISH, new TBBannerStyle(i(EntityType.PUFFERFISH), b("default", 0xf6b201), s("default", 0x37c3f2), a("default", 0xFFFFFF)));
        put(out, EntityType.RABBIT, new TBBannerStyle(i(EntityType.RABBIT), b("default", 0x995f40), s("default", 0x734831), a("default", 0xFFFFFF)));
        put(out, EntityType.RAVAGER, new TBBannerStyle(i(EntityType.RAVAGER), b("default", 0x757470), s("default", 0x5b5049), a("default", 0xFFFFFF)));
        put(out, EntityType.SALMON, new TBBannerStyle(i(EntityType.SALMON), b("default", 0xa00f10), s("default", 0x0e8474), a("default", 0xFFFFFF)));
        put(out, EntityType.SHEEP, new TBBannerStyle(i(EntityType.SHEEP), b("default", 0xe7e7e7), s("default", 0xffb5b5), a("default", 0xFFFFFF)));
        put(out, EntityType.SHULKER, new TBBannerStyle(i(EntityType.SHULKER), b("default", 0x946794), s("default", 0x4d3852), a("default", 0xFFFFFF)));
        put(out, EntityType.SILVERFISH, new TBBannerStyle(i(EntityType.SILVERFISH), b("default", 0x6e6e6e), s("default", 0x303030), a("default", 0xFFFFFF)));
        put(out, EntityType.SKELETON, new TBBannerStyle(i(EntityType.SKELETON), b("default", 0xc1c1c1), s("default", 0x494949), a("default", 0xFFFFFF)));
        put(out, EntityType.SKELETON_HORSE, new TBBannerStyle(i(EntityType.SKELETON_HORSE), b("default", 0x68684f), s("default", 0xe5e5d8), a("default", 0xFFFFFF)));
        put(out, EntityType.SLIME, new TBBannerStyle(i(EntityType.SLIME), b("default", 0x51a03e), s("default", 0x7ebf6e), a("default", 0xFFFFFF)));
        put(out, EntityType.SNIFFER, new TBBannerStyle(i(EntityType.SNIFFER), b("default", 0x871e09), s("default", 0x25ab70), a("default", 0xFFFFFF)));
        put(out, EntityType.SNOW_GOLEM, new TBBannerStyle(i(EntityType.SNOW_GOLEM), b("default", 0xd9f2f2), s("default", 0x81a4a4), a("default", 0xFFFFFF)));
        put(out, EntityType.SPIDER, new TBBannerStyle(i(EntityType.SPIDER), b("default", 0x342d27), s("default", 0xa80e0e), a("default", 0xFFFFFF)));
        put(out, EntityType.SQUID, new TBBannerStyle(i(EntityType.SQUID), b("default", 0x223b4d), s("default", 0x708899), a("default", 0xFFFFFF)));
        put(out, EntityType.STRAY, new TBBannerStyle(i(EntityType.STRAY), b("default", 0x617677), s("default", 0xddeaea), a("default", 0xFFFFFF)));
        put(out, EntityType.STRIDER, new TBBannerStyle(i(EntityType.STRIDER), b("default", 0x9c3436), s("default", 0x4d494d), a("default", 0xFFFFFF)));
        put(out, EntityType.TADPOLE, new TBBannerStyle(i(EntityType.TADPOLE), b("default", 0x6d533d), s("default", 0x160a00), a("default", 0xFFFFFF)));
        put(out, EntityType.TRADER_LLAMA, new TBBannerStyle(i(EntityType.TRADER_LLAMA), b("default", 0xeaa430), s("default", 0x456296), a("default", 0xFFFFFF)));
        put(out, EntityType.TROPICAL_FISH, new TBBannerStyle(i(EntityType.TROPICAL_FISH), b("default", 0xef6915), s("default", 0xfff9ef), a("default", 0xFFFFFF)));
        put(out, EntityType.TURTLE, new TBBannerStyle(i(EntityType.TURTLE), b("default", 0xe7e7e7), s("default", 0x00afaf), a("default", 0xFFFFFF)));
        put(out, EntityType.VEX, new TBBannerStyle(i(EntityType.VEX), b("default", 0x7a90a4), s("default", 0xe8edf1), a("default", 0xFFFFFF)));
        put(out, EntityType.VILLAGER, new TBBannerStyle(i(EntityType.VILLAGER), b("default", 0x563c33), s("default", 0xbd8b72), a("default", 0xFFFFFF)));
        put(out, EntityType.VINDICATOR, new TBBannerStyle(i(EntityType.VINDICATOR), b("default", 0x959b9b), s("default", 0x275e61), a("default", 0xFFFFFF)));
        put(out, EntityType.WANDERING_TRADER, new TBBannerStyle(i(EntityType.WANDERING_TRADER), b("default", 0x456296), s("default", 0xeaa430), a("default", 0xFFFFFF)));
        put(out, EntityType.WARDEN, new TBBannerStyle(i(EntityType.WARDEN), b("default", 0x0f4649), s("default", 0x39d6e0), a("default", 0xFFFFFF)));
        put(out, EntityType.WITCH, new TBBannerStyle(i(EntityType.WITCH), b("default", 0x340000), s("default", 0x51a03e), a("default", 0xFFFFFF)));
        put(out, EntityType.WITHER, new TBBannerStyle(i(EntityType.WITHER), b("default", 0x141414), s("default", 0x4d72a0), a("default", 0xFFFFFF)));
        put(out, EntityType.WITHER_SKELETON, new TBBannerStyle(i(EntityType.WITHER_SKELETON), b("default", 0x141414), s("default", 0x474d4d), a("default", 0xFFFFFF)));
        put(out, EntityType.WOLF, new TBBannerStyle(i(EntityType.WOLF), b("default", 0xd7d3d3), s("default", 0xceaf96), a("default", 0xFFFFFF)));
        put(out, EntityType.ZOGLIN, new TBBannerStyle(i(EntityType.ZOGLIN), b("default", 0xc66e55), s("default", 0xe6e6e6), a("default", 0xFFFFFF)));
        put(out, EntityType.ZOMBIE, new TBBannerStyle(i(EntityType.ZOMBIE), b("default", 0x00afaf), s("default", 0x799c65), a("default", 0xFFFFFF)));
        put(out, EntityType.ZOMBIE_HORSE, new TBBannerStyle(i(EntityType.ZOMBIE_HORSE), b("default", 0x315234), s("default", 0x97c284), a("default", 0xFFFFFF)));
        put(out, EntityType.ZOMBIE_VILLAGER, new TBBannerStyle(i(EntityType.ZOMBIE_VILLAGER), b("default", 0x563c33), s("default", 0x799c65), a("default", 0xFFFFFF)));
        put(out, EntityType.ZOMBIFIED_PIGLIN, new TBBannerStyle(i(EntityType.ZOMBIFIED_PIGLIN), b("default", 0xea9393), s("default", 0x4c7129), a("default", 0xFFFFFF)));
    }

    // ---- Layer helpers ----
    private static Layer i(EntityType<?> type) { return Layer.asset("entity/mob_banner/icon/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath()); }
    private static Layer b(String name, int rgb) { return Layer.asset("entity/mob_banner/base/" + name).withColor(rgb); }
    private static Layer s(String name, int rgb) { return Layer.asset("entity/mob_banner/secondary/" + name).withColor(rgb); }
    private static Layer a(String name, int rgb) { return Layer.asset("entity/mob_banner/accent/" + name).withColor(rgb); }

    private static void put(Map<Identifier, TBBannerStyle> out, EntityType<?> type, TBBannerStyle style) {
        out.put(id(type), style);
    }

    private static Identifier id(EntityType<?> type) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(type);
    }

    @Override
    public @NonNull String getName() { return "Banner Styles"; }
}