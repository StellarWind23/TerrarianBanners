package com.github.stellarwind23.terrarian_banners.util;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TBConfig extends MidnightConfig {

    @Comment public static Comment killsHeader;
    @Entry(min = 1, max = 10000) public static int defaultKillsPerBanner = 25;

    @Entry public static List<String> killOverrides = new ArrayList<>(List.of("minecraft:wither=1", "minecraft:ender_dragon=1"));

    @Entry public static boolean requirePlayerKill = true;

    @Comment public static Comment effectHeader;
    @Entry(min = 1, max = 128)  public static int auraRadius = 32;
    @Entry(min = 0, max = 100)  public static int damageTakenReductionPercent = 25;
    @Entry(min = 0, max = 300)  public static int damageDealtBonusPercent = 50;
    @Entry(min = 220, max = 6000) public static int effectDurationTicks = 220;

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