package com.github.stellarwind23.terrarian_banners.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class TBMobBannerDrawer {

    // ---- Textures ----
    // Vanilla's banner model sheet (64x64): pole at (44,0), bar at (0,42). Referenced, not shipped.
    private static final Identifier FRAME = Identifier.withDefaultNamespace("textures/entity/banner_base.png");

    // ---- Units: pixels (1/16 block) unless noted ----
    private static final float P = 1.0F / 16.0F;
    private static final float TEX = 64.0F;
    private static final int WHITE = -1;

    // Vanilla renders the banner model at two-thirds scale
    private static final float S = 2.0F / 3.0F;

    // Pole: model box 2x42x2 at texOffs (44,0), standing banners only
    private static final float POLE_HALF = 1 * S;
    private static final float POLE_HEIGHT = 42 * S;               // 28 px, ground to bar

    // Bar: model box 20x2x2 at texOffs (0,42)
    private static final float BAR_HALF_W = 10 * S;
    private static final float BAR_HALF_D = 1 * S;
    private static final float BAR_THICK = 2 * S;
    private static final float STANDING_BAR_Y = 28.0F;             // bottom edge of the bar
    private static final float WALL_BAR_Y = 37.0F / 3.0F;          // ~12.33, wall banners hang below their block
    private static final float WALL_BAR_Z = -7.0F;                 // bar sits 7 px toward the wall

    // Flag: model box 20x40x1 at texOffs (0,0), sits just in front of the bar
    private static final float FLAG_W = 20 * S;
    private static final float FLAG_H = 40 * S;
    private static final float FLAG_HALF_T = 0.5F * S;
    private static final float FLAG_Z = 1.5F * S;                  // flag centre in front of the bar centre

    private TBMobBannerDrawer() { }

    /**
     * Pose stack must be at the block's centre on the ground, rotated so local +Z is the banner's front.
     * flagTexture is the baked 64x64 sheet from TBBannerTextures, in vanilla's banner layout.
     */
    public static void submit(PoseStack ps, SubmitNodeCollector out, boolean standing,
                              Identifier flagTexture, float sway, int light) {
        float barBottom = standing ? STANDING_BAR_Y : WALL_BAR_Y;
        float barTop = barBottom + BAR_THICK;
        float barZ = standing ? 0 : WALL_BAR_Z;

        // Frame, drawn from vanilla's texture layout
        out.submitCustomGeometry(ps, RenderTypes.entityCutoutNoCull(FRAME), (pose, vc) -> {
            if (standing) {
                texBox(pose, vc, light, -POLE_HALF, 0, -POLE_HALF, POLE_HALF, POLE_HEIGHT, POLE_HALF,
                        44, 0, 2, 42, 2);
            }
            texBox(pose, vc, light, -BAR_HALF_W, barBottom, barZ - BAR_HALF_D, BAR_HALF_W, barTop, barZ + BAR_HALF_D,
                    0, 42, 20, 2, 2);
        });

        // Flag pivots at the top of the bar, on the bar's centre line, like vanilla
        ps.pushPose();
        ps.translate(0, barTop * P, barZ * P);
        ps.mulPose(Axis.XP.rotation(sway));
        ps.translate(0, 0, FLAG_Z * P);

        // One box using vanilla's flag layout: front at (1,1), back at (22,1), edges around them
        out.submitCustomGeometry(ps, RenderTypes.entityCutoutNoCull(flagTexture), (pose, vc) ->
                texBox(pose, vc, light, -FLAG_W / 2, -FLAG_H, -FLAG_HALF_T, FLAG_W / 2, 0, FLAG_HALF_T,
                        0, 0, 20, 40, 1));

        ps.popPose();
    }

    /**
     * Box with vanilla's cube UV layout. Corners are in pixels; u, v, w, h, d are the model box's texOffs
     * and size in model units (not scaled), which is what the texture layout is based on.
     */
    private static void texBox(PoseStack.Pose pose, VertexConsumer vc, int light,
                               float x0, float y0, float z0, float x1, float y1, float z1,
                               int u, int v, int w, int h, int d) {
        // +Z (vanilla's "front")
        quad(pose, vc, light, 0, 0, 1,   u + d, v + d, u + d + w, v + d + h,
                x0,y0,z1, x1,y0,z1, x1,y1,z1, x0,y1,z1);
        // -Z ("back")
        quad(pose, vc, light, 0, 0, -1,  u + 2*d + w, v + d, u + 2*d + 2*w, v + d + h,
                x1,y0,z0, x0,y0,z0, x0,y1,z0, x1,y1,z0);
        // +X
        quad(pose, vc, light, 1, 0, 0,   u + d + w, v + d, u + 2*d + w, v + d + h,
                x1,y0,z1, x1,y0,z0, x1,y1,z0, x1,y1,z1);
        // -X
        quad(pose, vc, light, -1, 0, 0,  u, v + d, u + d, v + d + h,
                x0,y0,z0, x0,y0,z1, x0,y1,z1, x0,y1,z0);
        // top
        quad(pose, vc, light, 0, 1, 0,   u + d, v, u + d + w, v + d,
                x0,y1,z1, x1,y1,z1, x1,y1,z0, x0,y1,z0);
        // bottom
        quad(pose, vc, light, 0, -1, 0,  u + d + w, v, u + d + 2*w, v + d,
                x0,y0,z0, x1,y0,z0, x1,y0,z1, x0,y0,z1);
    }

    // UVs in texels; corners a..d run bottom-left, bottom-right, top-right, top-left as seen from outside
    private static void quad(PoseStack.Pose pose, VertexConsumer vc, int light, float nx, float ny, float nz,
                             float u0, float v0, float u1, float v1,
                             float ax, float ay, float az, float bx, float by, float bz,
                             float cx, float cy, float cz, float dx, float dy, float dz) {
        u0 /= TEX; u1 /= TEX; v0 /= TEX; v1 /= TEX;
        vertex(pose, vc, light, ax, ay, az, u0, v1, nx, ny, nz);
        vertex(pose, vc, light, bx, by, bz, u1, v1, nx, ny, nz);
        vertex(pose, vc, light, cx, cy, cz, u1, v0, nx, ny, nz);
        vertex(pose, vc, light, dx, dy, dz, u0, v0, nx, ny, nz);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer vc, int light,
                               float x, float y, float z, float u, float v,
                               float nx, float ny, float nz) {
        vc.addVertex(pose, x * P, y * P, z * P)
                .setColor(WHITE)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }
}