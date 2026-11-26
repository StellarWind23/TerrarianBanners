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

        styles.put(TBBannerStyle.NULL_ID, TBBannerStyle.NULL_STYLE);

        //Populate with default
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (TerrarianBanners.isBannerMob(type)) styles.put(id(type), TBBannerStyle.NULL_STYLE);
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

    public static Map<Identifier, TBBannerStyle> buildStyles() {
        Map<Identifier, TBBannerStyle> styles = new LinkedHashMap<>();
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (TerrarianBanners.isBannerMob(type)) styles.put(id(type), TBBannerStyle.NULL_STYLE);
        }
        addStyles(styles);
        return styles;
    }

    private static void addStyles(Map<Identifier, TBBannerStyle> out) {
        put(out, EntityType.BREEZE, TBBannerStyle.fromLayers(b("cave", 0xaf94df), d("border", 0x9166df), d("thin_border", 0xFFFFFF), e(EntityType.BREEZE)));

        //Sky
        put(out, EntityType.PHANTOM, TBBannerStyle.fromLayers(b("night", 0x43518a), d("border", 0x88ff00), d("thin_border", 0xFFFFFF), e(EntityType.PHANTOM)));
        put(out, EntityType.HAPPY_GHAST, TBBannerStyle.fromLayers(b("sky", 0x99ccff), d("border", 0xbcbcbc), d("thin_border", 0xFFFFFF), e(EntityType.HAPPY_GHAST)));

        //Forest
        put(out, EntityType.FOX, TBBannerStyle.fromLayers(b("sky", 0x99ccff), d("border", 0xd5b69f), d("thin_border", 0xcc6920), e(EntityType.FOX)));
        put(out, EntityType.WOLF, TBBannerStyle.fromLayers(b("default", 0xd7d3d3), d("border", 0xceaf96), d("thin_border", 0xFFFFFF), e(EntityType.WOLF)));
        put(out, EntityType.BEE, TBBannerStyle.fromLayers(b("default", 0xedc343), d("comb", 0xFC9600), d("border", 0x43241b), d("thin_border", 0xedc343), e(EntityType.BEE)));

            //Night
        put(out, EntityType.CREAKING, TBBannerStyle.fromLayers(b("night", 0x5f5f5f), d("mini_boss_border", 0xfc7812), d("thin_mini_boss_border", 0xFFFFFF), e(EntityType.CREAKING)));

        //Jungle
        int jungleSkyColor = 0x87d9ea;
        int jungleTreeColor = 0x398815;

        put(out, EntityType.SNIFFER, TBBannerStyle.fromLayers(b("sky", jungleSkyColor), d("jungle", jungleTreeColor), d("border", 0x871e09), d("thin_border", 0x25ab70), e(EntityType.SNIFFER)));
        put(out, EntityType.OCELOT, TBBannerStyle.fromLayers(b("sky", jungleSkyColor), d("jungle", jungleTreeColor), d("border", 0xefde7d), d("thin_border", 0x564434), e(EntityType.OCELOT)));
        put(out, EntityType.PANDA, TBBannerStyle.fromLayers(b("sky", jungleSkyColor), d("jungle", jungleTreeColor), d("border", 0xe7e7e7), d("thin_border", 0x1b1b22), e(EntityType.PANDA)));
        put(out, EntityType.PARROT, TBBannerStyle.fromLayers(b("sky", jungleSkyColor), d("jungle", jungleTreeColor), d("border", 0x0da70b), d("thin_border", 0xff0000), e(EntityType.PARROT)));

        //Grass
        int grassSkyColor = 0x87d9ea;
        int grassColor = 0x447e1f;
        int grassNightSkyColor = 0xffffff;
        int grassNightColor = 0x2f4720;

        put(out, EntityType.RABBIT, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0x995f40), d("thin_border", 0x734831), e(EntityType.RABBIT)));
        put(out, EntityType.CHICKEN, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0xa1a1a1), d("thin_border", 0xff0000), e(EntityType.CHICKEN)));
        put(out, EntityType.COW, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0x443626), d("thin_border", 0xe7e7e7), e(EntityType.COW)));
        put(out, EntityType.PIG, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0xf0a5a2), d("thin_border", 0xdb635f), e(EntityType.PIG)));
        put(out, EntityType.SHEEP, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0xe7e7e7), d("thin_border", 0xffb5b5), e(EntityType.SHEEP)));
        put(out, EntityType.HORSE, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("angled_border", 0xc09e7d), d("thin_angled_border", 0xeee500), e(EntityType.HORSE)));
        put(out, EntityType.DONKEY, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0x534539), d("thin_border", 0x867566), e(EntityType.DONKEY)));
        put(out, EntityType.MULE, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0x1b0200), d("thin_border", 0x51331d), e(EntityType.MULE)));
        put(out, EntityType.GOAT, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0xa5947c), d("thin_border", 0x55493e), e(EntityType.GOAT)));
        put(out, EntityType.MOOSHROOM, TBBannerStyle.fromLayers(b("sky", grassSkyColor), d("hill", grassColor), d("border", 0xa00f10), d("thin_border", 0xb7b7b7), e(EntityType.MOOSHROOM)));
            //Night
        put(out, EntityType.CREEPER, TBBannerStyle.fromLayers(b("night", grassNightSkyColor), d("hill", grassNightColor), d("border", 0x000000), d("thin_border", 0x0da70b), e(EntityType.CREEPER)));
        put(out, EntityType.SKELETON, TBBannerStyle.fromLayers(b("night", grassNightSkyColor), d("hill", grassNightColor), d("border", 0xc1c1c1), d("thin_border", 0x494949), e(EntityType.SKELETON)));
        put(out, EntityType.SKELETON_HORSE, TBBannerStyle.fromLayers(b("night", grassNightSkyColor), d("hill", grassNightColor), d("border", 0x68684f), d("thin_border", 0xe5e5d8), e(EntityType.SKELETON_HORSE)));
        put(out, EntityType.SPIDER, TBBannerStyle.fromLayers(b("night", grassNightSkyColor), d("hill", grassNightColor), d("border", 0x342d27), d("thin_border", 0xa80e0e), e(EntityType.SPIDER)));
        put(out, EntityType.ZOMBIE, TBBannerStyle.fromLayers(b("night", grassNightSkyColor), d("hill", grassNightColor), d("border", 0x799c65), d("thin_border", 0x00afaf), e(EntityType.ZOMBIE)));
        put(out, EntityType.ZOMBIE_HORSE, TBBannerStyle.fromLayers(b("night", grassNightSkyColor), d("hill", grassNightColor), d("border", 0x315234), d("thin_border", 0x97c284), e(EntityType.ZOMBIE_HORSE)));

        //Savanna
        put(out, EntityType.ARMADILLO, TBBannerStyle.fromLayers(b("sky", 0x99ccff), d("border", 0x824848), d("thin_border", 0xFFFFFF), e(EntityType.ARMADILLO)));
        put(out, EntityType.LLAMA, TBBannerStyle.fromLayers(b("default", 0xc09e7d), d("border", 0x995f40), d("thin_border", 0xFFFFFF), e(EntityType.LLAMA)));

        //Snow
        put(out, EntityType.STRAY, TBBannerStyle.fromLayers(b("night", 0x617677), d("hill", 0xf5fdfd), d("border", 0xddeaea), d("thin_border", 0xFFFFFF), e(EntityType.STRAY)));
        put(out, EntityType.POLAR_BEAR, TBBannerStyle.fromLayers(b("default", 0xeeeede), d("hill", 0xf5fdfd), d("border", 0xd5d6cd), d("thin_border", 0xFFFFFF), e(EntityType.POLAR_BEAR)));

        //Desert
        put(out, EntityType.CAMEL, TBBannerStyle.fromLayers(b("sky", 0x99ccff), d("hill", 0xd7c99c), d("angled_border", 0xcb9337), d("thin_angled_border",0xfcc369), e(EntityType.CAMEL)));
        put(out, EntityType.CAMEL_HUSK, TBBannerStyle.fromLayers(b("night", 0x5f5f5f), d("hill", 0xd7c99c), d("alt_angled_border", 0x544937), d("thin_alt_angled_border",0x1a1714), e(EntityType.CAMEL_HUSK)));
        put(out, EntityType.HUSK, TBBannerStyle.fromLayers(b("night", 0x797061), d("hill", 0xd7c99c), d("border", 0xe6cc94), d("thin_border", 0xFFFFFF), e(EntityType.HUSK)));

        //Nether
        put(out, EntityType.BLAZE, TBBannerStyle.fromLayers(b("cave", 0xf6b201), d("flame", 0xfff847), d("border", 0x7c2806), d("thin_border", 0xFFFFFF), e(EntityType.BLAZE)));
        put(out, EntityType.GHAST, TBBannerStyle.fromLayers(b("cave", 0xf9f9f9), d("flame", 0xfff847), d("border", 0xbcbcbc), d("thin_border", 0xFFFFFF), e(EntityType.GHAST)));
        put(out, EntityType.WITHER, TBBannerStyle.fromLayers(b("cave", 0x141414), d("flame", 0xfff847), d("boss_border", 0xFFFFFF), d("thin_boss_border", 0x4d72a0), e(EntityType.WITHER)));
        put(out, EntityType.WITHER_SKELETON, TBBannerStyle.fromLayers(b("cave", 0x141414), d("flame", 0xfff847), d("border", 0x474d4d), d("thin_border", 0xFFFFFF), e(EntityType.WITHER_SKELETON)));
        put(out, EntityType.MAGMA_CUBE, TBBannerStyle.fromLayers(b("cave", 0x7c2806), d("flame", 0xfff847), d("border", 0xce490d), d("thin_border", 0xFFFFFF), e(EntityType.MAGMA_CUBE)));
        put(out, EntityType.STRIDER, TBBannerStyle.fromLayers(b("cave", 0x9c3436), d("flame", 0xfff847), d("angled_border", 0x4d494d), d("thin_angled_border", 0xFFFFFF), e(EntityType.STRIDER)));
        put(out, EntityType.PIGLIN, TBBannerStyle.fromLayers(b("cave", 0x995f40), d("flame", 0xfff847), d("border", 0xf9f3a4), d("thin_border", 0xFFFFFF), e(EntityType.PIGLIN)));
        put(out, EntityType.PIGLIN_BRUTE, TBBannerStyle.fromLayers(b("cave", 0x592a10), d("flame", 0xfff847), d("border", 0xf9f3a4), d("thin_border", 0xFFFFFF), e(EntityType.PIGLIN_BRUTE)));
        put(out, EntityType.ZOMBIFIED_PIGLIN, TBBannerStyle.fromLayers(b("cave", 0xea9393), d("flame", 0xfff847), d("border", 0x4c7129), d("thin_border", 0xFFFFFF), e(EntityType.ZOMBIFIED_PIGLIN)));
        put(out, EntityType.HOGLIN, TBBannerStyle.fromLayers(b("cave", 0xc66e55), d("flame", 0xfff847), d("border", 0x5f6464), d("thin_border", 0xFFFFFF), e(EntityType.HOGLIN)));
        put(out, EntityType.ZOGLIN, TBBannerStyle.fromLayers(b("cave", 0xc66e55), d("flame", 0xfff847), d("border", 0xe6e6e6), d("thin_border", 0xFFFFFF), e(EntityType.ZOGLIN)));


        //Golem
        put(out, EntityType.ALLAY, TBBannerStyle.fromLayers(b("sky", 0x00daff), d("border", 0x00adff), d("thin_border", 0xFFFFFF), e(EntityType.ALLAY)));
        put(out, EntityType.COPPER_GOLEM, TBBannerStyle.fromLayers(b("default", 0xb05537), d("square_border", 0xbf6f54), d("thin_square_border", 0xe3826c), e(EntityType.COPPER_GOLEM)));
        put(out, EntityType.IRON_GOLEM, TBBannerStyle.fromLayers(b("sky", 0xdbcdc2), d("square_border", 0x74a332), d("thin_square_border", 0xFFFFFF), e(EntityType.IRON_GOLEM)));
        put(out, EntityType.SNOW_GOLEM, TBBannerStyle.fromLayers(b("sky", 0xd9f2f2), d("square_border", 0x81a4a4), d("thin_square_border", 0xFFFFFF), e(EntityType.SNOW_GOLEM)));

        //Cave
        put(out, EntityType.BAT, TBBannerStyle.fromLayers(b("cave", 0x4c3e30), d("border", 0x0f0f0f), d("thin_border", 0xFFFFFF), e(EntityType.BAT)));
        put(out, EntityType.CAVE_SPIDER, TBBannerStyle.fromLayers(b("cave", 0x0c424e), d("border", 0xa80e0e), d("thin_border", 0xFFFFFF), e(EntityType.CAVE_SPIDER)));
        put(out, EntityType.AXOLOTL, TBBannerStyle.fromLayers(b("cave", 0xfbc1e3), d("bubble", 0xb4ecdf), d("border", 0xa62d74), d("thin_border", 0xFFFFFF), e(EntityType.AXOLOTL)));
        put(out, EntityType.GLOW_SQUID, TBBannerStyle.fromLayers(b("cave", 0x095656),  d("bubble", 0xb4ecdf), d("border", 0x85f1bc), d("thin_border", 0xFFFFFF), e(EntityType.GLOW_SQUID)));
        put(out, EntityType.SILVERFISH, TBBannerStyle.fromLayers(b("cave", 0x6e6e6e), d("border", 0x303030), d("thin_border", 0xFFFFFF), e(EntityType.SILVERFISH)));
        put(out, EntityType.WARDEN, TBBannerStyle.fromLayers(b("cave", 0x0f4649), d("mini_boss_border", 0x39d6e0), d("thin_mini_boss_border", 0xFFFFFF), e(EntityType.WARDEN)));

        //Swamp
        put(out, EntityType.BOGGED, TBBannerStyle.fromLayers(b("sky", 0x99ccff), d("hill", 0x8a9c72), d("lake", 0x2033bb), d("border", 0x314d1b), d("thin_border", 0xFFFFFF), e(EntityType.BOGGED)));
        put(out, EntityType.FROG, TBBannerStyle.fromLayers(b("sky", 0xd07444), d("border", 0xffc77c), d("thin_border", 0xFFFFFF), e(EntityType.FROG)));
        put(out, EntityType.TADPOLE, TBBannerStyle.fromLayers(b("default", 0x6d533d), d("border", 0x160a00), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.TADPOLE)));
        put(out, EntityType.SLIME, TBBannerStyle.fromLayers(b("cave", 0x51a03e), d("border", 0x7ebf6e), d("thin_border", 0xFFFFFF), e(EntityType.SLIME)));
        put(out, EntityType.WITCH, TBBannerStyle.fromLayers(b("night", 0x340000), d("border", 0x51a03e), d("thin_border", 0xFFFFFF), e(EntityType.WITCH)));

        //Ocean
        int oceanColor = 0x2033bb;
        int oceanBubbleColor = 0xb4ecdf;

        put(out, EntityType.COD, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0xc1a76a), d("thin_border", 0xe5c48b), e(EntityType.COD)));
        put(out, EntityType.SALMON, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0xa00f10), d("thin_border", 0x0e8474), e(EntityType.SALMON)));
        put(out, EntityType.DOLPHIN, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0xf9f9f90), d("thin_border", 0x223b4d), e(EntityType.DOLPHIN)));
        put(out, EntityType.SQUID, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0x223b4d), d("thin_border", 0x708899), e(EntityType.SQUID)));
        put(out, EntityType.TROPICAL_FISH, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0xef6915), d("thin_border", 0xfff9ef), e(EntityType.TROPICAL_FISH)));
        put(out, EntityType.TURTLE, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0x00afaf), d("thin_border", 0xFFFFFF), e(EntityType.TURTLE)));
        put(out, EntityType.NAUTILUS, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("angled_border", 0xcbbea7), d("thin_angled_border", 0xd0523f), e(EntityType.NAUTILUS)));
        put(out, EntityType.ZOMBIE_NAUTILUS, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("alt_angled_border", 0xdee1c8), d("thin_alt_angled_border", 0x7ea623), e(EntityType.ZOMBIE_NAUTILUS)));
        put(out, EntityType.GUARDIAN, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("square_border",0x5a8272 ), d("thin_square_border", 0xf17d30), e(EntityType.GUARDIAN)));
        put(out, EntityType.ELDER_GUARDIAN, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("mini_boss_border", 0xceccba), d("thin_mini_boss_border", 0x747693), e(EntityType.ELDER_GUARDIAN)));
        put(out, EntityType.DROWNED, TBBannerStyle.fromLayers(b("ocean", oceanColor), d("bubble", oceanBubbleColor), d("border", 0x8ff1d7), d("thin_border", 0x799c65), e(EntityType.DROWNED)));
        put(out, EntityType.PUFFERFISH, TBBannerStyle.fromLayers(b("default", 0xf6b201), d("border", 0x37c3f2), d("bubble", 0xb4ecdf), d("thin_border", 0xFFFFFF), e(EntityType.PUFFERFISH)));

        //End
        int endColor = 0x6c5194;

        put(out, EntityType.ENDER_DRAGON, TBBannerStyle.fromLayers(b("end", endColor), d("boss_border", 0x0b0b0b), d("thin_boss_border", 0xd63cfa), e(EntityType.ENDER_DRAGON)));
        put(out, EntityType.ENDERMAN, TBBannerStyle.fromLayers(b("end", endColor), d("border", 0x0b0b0b), d("thin_border", 0xd63cfa), e(EntityType.ENDERMAN)));
        put(out, EntityType.ENDERMITE, TBBannerStyle.fromLayers(b("end", endColor), d("border", 0x442e5a), d("thin_border", 0x8c4175), e(EntityType.ENDERMITE)));
        put(out, EntityType.SHULKER, TBBannerStyle.fromLayers(b("end", endColor), d("border", 0x8e618e), d("thin_border", 0xd0d393), e(EntityType.SHULKER)));

        //Pillager
        put(out, EntityType.VEX, TBBannerStyle.fromLayers(b("default", 0x7a90a4), d("border", 0xe8edf1), d("thin_border", 0xFFFFFF), e(EntityType.VEX)));
        put(out, EntityType.VINDICATOR, TBBannerStyle.fromLayers(b("default", 0x959b9b), d("border", 0x275e61), d("thin_border", 0xFFFFFF), e(EntityType.VINDICATOR)));
        put(out, EntityType.EVOKER, TBBannerStyle.fromLayers(b("default", 0x959b9b), d("border", 0x1e1c1a), d("thin_border", 0xFFFFFF), e(EntityType.EVOKER)));
        put(out, EntityType.PILLAGER, TBBannerStyle.fromLayers(b("default", 0x532f36), d("border", 0x959b9b), d("thin_border", 0xFFFFFF), e(EntityType.PILLAGER)));
        put(out, EntityType.RAVAGER, TBBannerStyle.fromLayers(b("default", 0x757470), d("border", 0x5b5049), d("thin_border", 0xFFFFFF), e(EntityType.RAVAGER)));

        //Villager
        put(out, EntityType.VILLAGER, TBBannerStyle.fromLayers(b("sky", 0x563c33), d("angled_border", 0xbd8b72), d("thin_angled_border", 0xFFFFFF), e(EntityType.VILLAGER)));
        put(out, EntityType.WANDERING_TRADER, TBBannerStyle.fromLayers(b("sky", 0x456296), d("angled_border", 0xeaa430), d("thin_angled_border", 0xFFFFFF), e(EntityType.WANDERING_TRADER)));
        put(out, EntityType.TRADER_LLAMA, TBBannerStyle.fromLayers(b("sky", 0xeaa430), d("angled_border", 0x456296), d("thin_angled_border", 0xFFFFFF), e(EntityType.TRADER_LLAMA)));
        put(out, EntityType.CAT, TBBannerStyle.fromLayers(b("default", 0xefc88e), d("angled_border", 0x957256), d("thin_angled_border", 0xFFFFFF), e(EntityType.CAT)));
            //Night
        put(out, EntityType.ZOMBIE_VILLAGER, TBBannerStyle.fromLayers(b("night", 0x563c33), d("alt_angled_border", 0x799c65), d("thin_alt_angled_border", 0xFFFFFF), e(EntityType.ZOMBIE_VILLAGER)));
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