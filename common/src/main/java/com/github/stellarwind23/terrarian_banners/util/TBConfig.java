package com.github.stellarwind23.terrarian_banners.util;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TBConfig extends MidnightConfig {

    @Comment public static Comment kills_header;
    @Entry(min = 1, max = 10000) public static int kills_per_banner = 25;

    @Entry public static List<String> kills_required_overrides = new ArrayList<>(List.of("minecraft:wither=1", "minecraft:ender_dragon=1"));

    @Entry public static boolean require_player_kills = true;

    @Comment public static Comment effect_header;
    @Entry(min = 1, max = 128)  public static int effect_radius = 32;
    @Entry(min = 0, max = 3, isSlider = true)  public static double damage_taken_multiplier = 0.75;
    @Entry(min = 0, max = 3, isSlider = true)  public static double damage_dealt_multiplier = 1.50;
    @Entry(min = 0, max = 300) public static double effect_duration_seconds = 10;

    public static int killsFor(Identifier id) {
        String key = id.toString();
        for (String entry : kills_required_overrides) {
            int eq = entry.indexOf('=');
            if (eq < 0 || !entry.substring(0, eq).trim().equals(key)) continue;
            try {
                return Math.max(1, Integer.parseInt(entry.substring(eq + 1).trim()));
            } catch (NumberFormatException ignored) { }
        }
        return kills_per_banner;
    }
}