package com.github.stellarwind23.terrarian_banners.util;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TBConfig extends MidnightConfig {

    @Comment public static Comment killsHeader;
    @Entry(min = 1, max = 10000) public static int defaultKillsPerBanner = 25;

    // Format: "namespace:path=kills", e.g. "minecraft:creeper=25"
    @Entry public static List<String> killOverrides = new ArrayList<>(List.of("minecraft:creeper=25"));

    @Entry public static boolean requirePlayerKill = true;

    public static int killsFor(Identifier id) {
        String key = id.toString();
        for (String entry : killOverrides) {
            int eq = entry.indexOf('=');
            if (eq < 0 || !entry.substring(0, eq).trim().equals(key)) continue;
            try {
                return Math.max(1, Integer.parseInt(entry.substring(eq + 1).trim()));
            } catch (NumberFormatException ignored) { }
        }
        return defaultKillsPerBanner;
    }
}