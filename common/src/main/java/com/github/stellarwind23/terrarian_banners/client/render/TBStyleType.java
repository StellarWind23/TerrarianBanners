package com.github.stellarwind23.terrarian_banners.client.render;

public enum TBStyleType {
    /** The 64x64 flag sheet in vanilla's banner layout. */
    MOB_BANNER("mob_banner", 64),
    /** A vanilla-sized mob effect icon. */
    EFFECT("effect", 18);

    private final String folder;
    private final int size;

    TBStyleType(String folder, int size) {
        this.folder = folder;
        this.size = size;
    }

    public String folder() { return folder; }
    public int size() { return size; }

    /** assets/<namespace>/tb_texture_styles/<folder>/<mob>.json */
    public String styleFolder() { return "tb_texture_styles/" + folder; }
}