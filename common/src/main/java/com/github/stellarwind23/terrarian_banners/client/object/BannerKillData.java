package com.github.stellarwind23.terrarian_banners.client.object;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

public class BannerKillData extends SavedData {

    public static final Codec<BannerKillData> CODEC = Codec.unboundedMap(Identifier.CODEC, Codec.INT)
            .xmap(BannerKillData::new, d -> d.kills);

    public static final SavedDataType<BannerKillData> TYPE = new SavedDataType<>(
            TerrarianBanners.MOD_ID + "_banner_kills",
            BannerKillData::new,
            CODEC,
            DataFixTypes.LEVEL);

    private final Map<Identifier, Integer> kills;

    public BannerKillData() { this.kills = new HashMap<>(); }
    private BannerKillData(Map<Identifier, Integer> map) { this.kills = new HashMap<>(map); }

    public static BannerKillData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public int increment(Identifier id) {
        int n = kills.merge(id, 1, Integer::sum);
        setDirty();
        return n;
    }
}