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

        styles.put(TBBannerStyle.NEUTRAL_ID, TBBannerStyle.NEUTRAL);

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
        put(out, EntityType.ALLAY, TBBannerStyle.fromLayers(b("default", 0x00daff), d("border", 0x00adff), d("thin_border", 0xFFFFFF), e(EntityType.ALLAY)));
        put(out, EntityType.ARMADILLO, TBBannerStyle.fromLayers(b("default", 0xad716d), d("border", 0x824848), d("thin_border", 0xFFFFFF), e(EntityType.ARMADILLO)));
        put(out, EntityType.AXOLOTL, TBBannerStyle.fromLayers(b("cave", 0xfbc1e3), d("border", 0xa62d74), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.AXOLOTL)));
        put(out, EntityType.BAT, TBBannerStyle.fromLayers(b("cave", 0x4c3e30), d("border", 0x0f0f0f), d("thin_border", 0xFFFFFF), e(EntityType.BAT)));
        put(out, EntityType.BEE, TBBannerStyle.fromLayers(b("default", 0xedc343), d("border", 0x43241b), d("comb", 0xFC9600), d("thin_border", 0xedc343), e(EntityType.BEE)));
        put(out, EntityType.BLAZE, TBBannerStyle.fromLayers(b("cave", 0xf6b201), d("border", 0x7c2806), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.BLAZE)));
        put(out, EntityType.BOGGED, TBBannerStyle.fromLayers(b("default", 0x8a9c72), d("border", 0x314d1b), d("thin_border", 0xFFFFFF), e(EntityType.BOGGED)));
        put(out, EntityType.BREEZE, TBBannerStyle.fromLayers(b("default", 0xaf94df), d("border", 0x9166df), d("thin_border", 0xFFFFFF), e(EntityType.BREEZE)));
        put(out, EntityType.CAMEL, TBBannerStyle.fromLayers(b("default", 0xfcc369), d("hill", 0xd7c99c), d("border", 0xcb9337), d("thin_border", 0xFFFFFF), e(EntityType.CAMEL)));
        put(out, EntityType.CAT, TBBannerStyle.fromLayers(b("default", 0xefc88e), d("border", 0x957256), d("thin_border", 0xFFFFFF), e(EntityType.CAT)));
        put(out, EntityType.CAVE_SPIDER, TBBannerStyle.fromLayers(b("cave", 0x0c424e), d("border", 0xa80e0e), d("thin_border", 0xFFFFFF), e(EntityType.CAVE_SPIDER)));
        put(out, EntityType.CHICKEN, TBBannerStyle.fromLayers(b("default", 0xa1a1a1), d("border", 0xff0000), d("thin_border", 0xFFFFFF), e(EntityType.CHICKEN)));
        put(out, EntityType.COD, TBBannerStyle.fromLayers(b("default", 0xc1a76a), d("border", 0xe5c48b), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.COD)));
        put(out, EntityType.COW, TBBannerStyle.fromLayers(b("default", 0x443626), d("border", 0xa1a1a1), d("thin_border", 0xFFFFFF), e(EntityType.COW)));
        put(out, EntityType.CREAKING, TBBannerStyle.fromLayers(b("night", 0x5f5f5f), d("border", 0xfc7812), d("thin_border", 0xFFFFFF), e(EntityType.CREAKING)));
        put(out, EntityType.CREEPER, TBBannerStyle.fromLayers(b("night", 0x0da70b), d("border", 0x000000), d("thin_border", 0xFFFFFF), e(EntityType.CREEPER)));
        put(out, EntityType.DOLPHIN, TBBannerStyle.fromLayers(b("default", 0x223b4d), d("border", 0xf9f9f9), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.DOLPHIN)));
        put(out, EntityType.DONKEY, TBBannerStyle.fromLayers(b("default", 0x534539), d("border", 0x867566), d("thin_border", 0xFFFFFF), e(EntityType.DONKEY)));
        put(out, EntityType.DROWNED, TBBannerStyle.fromLayers(b("default", 0x8ff1d7), d("border", 0x799c65), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.DROWNED)));
        put(out, EntityType.ELDER_GUARDIAN, TBBannerStyle.fromLayers(b("default", 0xceccba), d("border", 0x747693), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.ELDER_GUARDIAN)));
        put(out, EntityType.ENDER_DRAGON, TBBannerStyle.fromLayers(b("noise", 0xb42dd6), d("island", 0xaebb74), d("border", 0xe079fa), d("thin_border", 0xFFFFFF), e(EntityType.ENDER_DRAGON)));
        put(out, EntityType.ENDERMAN, TBBannerStyle.fromLayers(b("noise", 0xb42dd6), d("island", 0xaebb74), d("border", 0xcc00fa), d("thin_border", 0xFFFFFF), e(EntityType.ENDERMAN)));
        put(out, EntityType.ENDERMITE, TBBannerStyle.fromLayers(b("noise", 0xb42dd6), d("island", 0xaebb74), d("border", 0x6e6e6e), d("thin_border", 0xFFFFFF), e(EntityType.ENDERMITE)));
        put(out, EntityType.EVOKER, TBBannerStyle.fromLayers(b("default", 0x959b9b), d("border", 0x1e1c1a), d("thin_border", 0xFFFFFF), e(EntityType.EVOKER)));
        put(out, EntityType.FOX, TBBannerStyle.fromLayers(b("default", 0xd5b69f), d("border", 0xcc6920), d("thin_border", 0xFFFFFF), e(EntityType.FOX)));
        put(out, EntityType.FROG, TBBannerStyle.fromLayers(b("default", 0xd07444), d("border", 0xffc77c), d("thin_border", 0xFFFFFF), e(EntityType.FROG)));
        put(out, EntityType.GHAST, TBBannerStyle.fromLayers(b("cave", 0xf9f9f9), d("border", 0xbcbcbc), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.GHAST)));
        put(out, EntityType.GLOW_SQUID, TBBannerStyle.fromLayers(b("cave", 0x095656), d("border", 0x85f1bc), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.GLOW_SQUID)));
        put(out, EntityType.GOAT, TBBannerStyle.fromLayers(b("default", 0xa5947c), d("border", 0x55493e), d("thin_border", 0xFFFFFF), e(EntityType.GOAT)));
        put(out, EntityType.GUARDIAN, TBBannerStyle.fromLayers(b("default", 0x5a8272), d("border", 0xf17d30), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.GUARDIAN)));
        put(out, EntityType.HOGLIN, TBBannerStyle.fromLayers(b("cave", 0xc66e55), d("border", 0x5f6464), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.HOGLIN)));
        put(out, EntityType.HORSE, TBBannerStyle.fromLayers(b("default", 0xc09e7d), d("border", 0xeee500), d("thin_border", 0xFFFFFF), e(EntityType.HORSE)));
        put(out, EntityType.HUSK, TBBannerStyle.fromLayers(b("night", 0x797061), d("hill", 0xd7c99c), d("border", 0xe6cc94), d("thin_border", 0xFFFFFF), e(EntityType.HUSK)));
        put(out, EntityType.IRON_GOLEM, TBBannerStyle.fromLayers(b("default", 0xdbcdc2), d("border", 0x74a332), d("thin_border", 0xFFFFFF), e(EntityType.IRON_GOLEM)));
        put(out, EntityType.LLAMA, TBBannerStyle.fromLayers(b("default", 0xc09e7d), d("border", 0x995f40), d("thin_border", 0xFFFFFF), e(EntityType.LLAMA)));
        put(out, EntityType.MAGMA_CUBE, TBBannerStyle.fromLayers(b("cave", 0x7c2806), d("border", 0xce490d), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.MAGMA_CUBE)));
        put(out, EntityType.MOOSHROOM, TBBannerStyle.fromLayers(b("default", 0xa00f10), d("border", 0xb7b7b7), d("thin_border", 0xFFFFFF), e(EntityType.MOOSHROOM)));
        put(out, EntityType.MULE, TBBannerStyle.fromLayers(b("default", 0x1b0200), d("border", 0x51331d), d("thin_border", 0xFFFFFF), e(EntityType.MULE)));
        put(out, EntityType.OCELOT, TBBannerStyle.fromLayers(b("default", 0xefde7d), d("border", 0x564434), d("thin_border", 0xFFFFFF), e(EntityType.OCELOT)));
        put(out, EntityType.PANDA, TBBannerStyle.fromLayers(b("default", 0xe7e7e7), d("border", 0x1b1b22), d("thin_border", 0xFFFFFF), e(EntityType.PANDA)));
        put(out, EntityType.PARROT, TBBannerStyle.fromLayers(b("default", 0x0da70b), d("border", 0xff0000), d("thin_border", 0xFFFFFF), e(EntityType.PARROT)));
        put(out, EntityType.PHANTOM, TBBannerStyle.fromLayers(b("night", 0x43518a), d("border", 0x88ff00), d("thin_border", 0xFFFFFF), e(EntityType.PHANTOM)));
        put(out, EntityType.PIG, TBBannerStyle.fromLayers(b("default", 0xf0a5a2), d("border", 0xdb635f), d("thin_border", 0xFFFFFF), e(EntityType.PIG)));
        put(out, EntityType.PIGLIN, TBBannerStyle.fromLayers(b("cave", 0x995f40), d("border", 0xf9f3a4), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.PIGLIN)));
        put(out, EntityType.PIGLIN_BRUTE, TBBannerStyle.fromLayers(b("cave", 0x592a10), d("border", 0xf9f3a4), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.PIGLIN_BRUTE)));
        put(out, EntityType.PILLAGER, TBBannerStyle.fromLayers(b("default", 0x532f36), d("border", 0x959b9b), d("thin_border", 0xFFFFFF), e(EntityType.PILLAGER)));
        put(out, EntityType.POLAR_BEAR, TBBannerStyle.fromLayers(b("default", 0xeeeede), d("hill", 0xf5fdfd), d("border", 0xd5d6cd), d("thin_border", 0xFFFFFF), e(EntityType.POLAR_BEAR)));
        put(out, EntityType.PUFFERFISH, TBBannerStyle.fromLayers(b("default", 0xf6b201), d("border", 0x37c3f2), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.PUFFERFISH)));
        put(out, EntityType.RABBIT, TBBannerStyle.fromLayers(b("default", 0x995f40), d("border", 0x734831), d("thin_border", 0xFFFFFF), e(EntityType.RABBIT)));
        put(out, EntityType.RAVAGER, TBBannerStyle.fromLayers(b("default", 0x757470), d("border", 0x5b5049), d("thin_border", 0xFFFFFF), e(EntityType.RAVAGER)));
        put(out, EntityType.SALMON, TBBannerStyle.fromLayers(b("default", 0xa00f10), d("border", 0x0e8474), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.SALMON)));
        put(out, EntityType.SHEEP, TBBannerStyle.fromLayers(b("default", 0xe7e7e7), d("border", 0xffb5b5), d("thin_border", 0xFFFFFF), e(EntityType.SHEEP)));
        put(out, EntityType.SHULKER, TBBannerStyle.fromLayers(b("noise", 0xb42dd6), d("island", 0xaebb74), d("border", 0x4d3852), d("thin_border", 0xFFFFFF), e(EntityType.SHULKER)));
        put(out, EntityType.SILVERFISH, TBBannerStyle.fromLayers(b("cave", 0x6e6e6e), d("border", 0x303030), d("thin_border", 0xFFFFFF), e(EntityType.SILVERFISH)));
        put(out, EntityType.SKELETON, TBBannerStyle.fromLayers(b("night", 0xc1c1c1), d("border", 0x494949), d("thin_border", 0xFFFFFF), e(EntityType.SKELETON)));
        put(out, EntityType.SKELETON_HORSE, TBBannerStyle.fromLayers(b("night", 0x68684f), d("border", 0xe5e5d8), d("thin_border", 0xFFFFFF), e(EntityType.SKELETON_HORSE)));
        put(out, EntityType.SLIME, TBBannerStyle.fromLayers(b("cave", 0x51a03e), d("border", 0x7ebf6e), d("thin_border", 0xFFFFFF), e(EntityType.SLIME)));
        put(out, EntityType.SNIFFER, TBBannerStyle.fromLayers(b("default", 0x871e09), d("border", 0x25ab70), d("thin_border", 0xFFFFFF), e(EntityType.SNIFFER)));
        put(out, EntityType.SNOW_GOLEM, TBBannerStyle.fromLayers(b("default", 0xd9f2f2), d("border", 0x81a4a4), d("thin_border", 0xFFFFFF), e(EntityType.SNOW_GOLEM)));
        put(out, EntityType.SPIDER, TBBannerStyle.fromLayers(b("night", 0x342d27), d("border", 0xa80e0e), d("thin_border", 0xFFFFFF), e(EntityType.SPIDER)));
        put(out, EntityType.SQUID, TBBannerStyle.fromLayers(b("default", 0x223b4d), d("border", 0x708899), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.SQUID)));
        put(out, EntityType.STRAY, TBBannerStyle.fromLayers(b("night", 0x617677), d("hill", 0xf5fdfd), d("border", 0xddeaea), d("thin_border", 0xFFFFFF), e(EntityType.STRAY)));
        put(out, EntityType.STRIDER, TBBannerStyle.fromLayers(b("cave", 0x9c3436), d("border", 0x4d494d), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.STRIDER)));
        put(out, EntityType.TADPOLE, TBBannerStyle.fromLayers(b("default", 0x6d533d), d("border", 0x160a00), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.TADPOLE)));
        put(out, EntityType.TRADER_LLAMA, TBBannerStyle.fromLayers(b("default", 0xeaa430), d("border", 0x456296), d("thin_border", 0xFFFFFF), e(EntityType.TRADER_LLAMA)));
        put(out, EntityType.TROPICAL_FISH, TBBannerStyle.fromLayers(b("default", 0xef6915), d("border", 0xfff9ef), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.TROPICAL_FISH)));
        put(out, EntityType.TURTLE, TBBannerStyle.fromLayers(b("default", 0xe7e7e7), d("border", 0x00afaf), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.TURTLE)));
        put(out, EntityType.VEX, TBBannerStyle.fromLayers(b("default", 0x7a90a4), d("border", 0xe8edf1), d("thin_border", 0xFFFFFF), e(EntityType.VEX)));
        put(out, EntityType.VILLAGER, TBBannerStyle.fromLayers(b("default", 0x563c33), d("border", 0xbd8b72), d("thin_border", 0xFFFFFF), e(EntityType.VILLAGER)));
        put(out, EntityType.VINDICATOR, TBBannerStyle.fromLayers(b("default", 0x959b9b), d("border", 0x275e61), d("thin_border", 0xFFFFFF), e(EntityType.VINDICATOR)));
        put(out, EntityType.WANDERING_TRADER, TBBannerStyle.fromLayers(b("default", 0x456296), d("border", 0xeaa430), d("thin_border", 0xFFFFFF), e(EntityType.WANDERING_TRADER)));
        put(out, EntityType.WARDEN, TBBannerStyle.fromLayers(b("cave", 0x0f4649), d("border", 0x39d6e0), d("thin_border", 0xFFFFFF), e(EntityType.WARDEN)));
        put(out, EntityType.WITCH, TBBannerStyle.fromLayers(b("night", 0x340000), d("border", 0x51a03e), d("thin_border", 0xFFFFFF), e(EntityType.WITCH)));
        put(out, EntityType.WITHER, TBBannerStyle.fromLayers(b("cave", 0x141414), d("border", 0x4d72a0), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.WITHER)));
        put(out, EntityType.WITHER_SKELETON, TBBannerStyle.fromLayers(b("cave", 0x141414), d("border", 0x474d4d), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.WITHER_SKELETON)));
        put(out, EntityType.WOLF, TBBannerStyle.fromLayers(b("default", 0xd7d3d3), d("border", 0xceaf96), d("thin_border", 0xFFFFFF), e(EntityType.WOLF)));
        put(out, EntityType.ZOGLIN, TBBannerStyle.fromLayers(b("cave", 0xc66e55), d("border", 0xe6e6e6), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.ZOGLIN)));
        put(out, EntityType.ZOMBIE, TBBannerStyle.fromLayers(b("night", 0x00afaf), d("border", 0x799c65), d("thin_border", 0xFFFFFF), e(EntityType.ZOMBIE)));
        put(out, EntityType.ZOMBIE_HORSE, TBBannerStyle.fromLayers(b("night", 0x315234), d("border", 0x97c284), d("thin_border", 0xFFFFFF), e(EntityType.ZOMBIE_HORSE)));
        put(out, EntityType.ZOMBIE_VILLAGER, TBBannerStyle.fromLayers(b("night", 0x563c33), d("border", 0x799c65), d("thin_border", 0xFFFFFF), e(EntityType.ZOMBIE_VILLAGER)));
        put(out, EntityType.ZOMBIFIED_PIGLIN, TBBannerStyle.fromLayers(b("cave", 0xea9393), d("border", 0x4c7129), d("flame", 0xfff847), d("thin_border", 0xFFFFFF), e(EntityType.ZOMBIFIED_PIGLIN)));
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