package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class TBMobBannerDrawer {

    // ---- Textures ----
    private static final Identifier FRAME            = Identifier.withDefaultNamespace("textures/block/oak_planks.png");
    private static final Identifier CLOTH_BASE       = tex("textures/entity/mob_banner/cloth_base.png");
    private static final Identifier CLOTH_BORDER     = tex("textures/entity/mob_banner/cloth_border.png");
    private static final Identifier CLOTH_ACCENT     = tex("textures/entity/mob_banner/cloth_accent.png");

    // ---- Units: pixels (1/16 block) unless noted ----
    private static final float P = 1.0F / 16.0F;
    private static final int WHITE = -1;

    // 64x64 cloth sheet, vanilla banner layout
    private static final float TEX = 64.0F;
    private static final float FRONT_U0 = 1,  FRONT_U1 = 21;
    private static final float BACK_U0  = 22, BACK_U1  = 42;
    private static final float FLAG_V0  = 1,  FLAG_V1  = 41;

    // Vanilla draws the 20x40 flag at two-thirds scale
    private static final float FLAG_W = 20 * 2F / 3F;
    private static final float FLAG_H = 40 * 2F / 3F;

    // Distance between stacked layers; the face sits above all three
    private static final float LAYER_STEP = 0.05F;
    private static final float FACE_Z = 0.20F;

    private static final float FACE_SIZE = 8;
    private static final float FACE_CENTER_FROM_TOP = 11;

    private TBMobBannerDrawer() { }

    private static Identifier tex(String path) {
        return Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path);
    }

    /**
     * Pose stack must already be at the block's centre on the ground, rotated so local +Z is the banner's front.
     */
    public static void submit(PoseStack ps, SubmitNodeCollector out, boolean standing,
                              Identifier face, TBBannerColors colors, float sway, int light) {
        float barY  = standing ? 27 : 15;       // y of the bar's bottom edge
        float barZ  = standing ? 0 : -7;        // z-centre of the bar
        float flagZ = standing ? 1.3F : -5.7F;  // flag centre, clear of the pole/bar front face

        // Frame: pole (standing only) + bar
        out.submitCustomGeometry(ps, RenderTypes.entityCutoutNoCull(FRAME), (pose, vc) -> {
            if (standing) box(pose, vc, light, -1, 0, -1, 1, barY, 1);
            box(pose, vc, light, -FLAG_W / 2, barY, barZ - 1, FLAG_W / 2, barY + 2, barZ + 1);
        });

        // Flag hangs from the bar and sways around it
        ps.pushPose();
        ps.translate(0, barY * P, flagZ * P);
        ps.mulPose(Axis.XP.rotation(sway));

        cloth(ps, out, CLOTH_BASE, colors.main(),      0, light);
        cloth(ps, out, CLOTH_BORDER,     colors.secondary(), 1, light);
        cloth(ps, out, CLOTH_ACCENT,     colors.accent(),    2, light);

        float faceTop = -FACE_CENTER_FROM_TOP + FACE_SIZE / 2;
        out.submitCustomGeometry(ps, RenderTypes.entityCutoutNoCull(face), (pose, vc) -> {
            float x = FACE_SIZE / 2, y0 = faceTop - FACE_SIZE;
            rect(pose, vc, light, -x, y0, x, faceTop,  FACE_Z, false, WHITE, 0, 0, 1, 1);
            rect(pose, vc, light, -x, y0, x, faceTop, -FACE_Z, true,  WHITE, 0, 0, 1, 1);
        });
        ps.popPose();
    }

    // Front layers stack toward +Z, back layers toward -Z, so the accent is the nearest layer from either side
    private static void cloth(PoseStack ps, SubmitNodeCollector out, Identifier texture,
                              int rgb, int layer, int light) {
        int color = TBBannerColors.argb(rgb);
        out.submitCustomGeometry(ps, RenderTypes.entityCutoutNoCull(texture), (pose, vc) -> {
            rect(pose, vc, light, -FLAG_W / 2, -FLAG_H, FLAG_W / 2, 0,
                    layer * LAYER_STEP, false, color,
                    FRONT_U0 / TEX, FLAG_V0 / TEX, FRONT_U1 / TEX, FLAG_V1 / TEX);
            rect(pose, vc, light, -FLAG_W / 2, -FLAG_H, FLAG_W / 2, 0,
                    -(layer + 1) * LAYER_STEP, true, color,
                    BACK_U0 / TEX, FLAG_V0 / TEX, BACK_U1 / TEX, FLAG_V1 / TEX);
        });
    }

    // Flat rectangle in the z-plane (pixel coords). The back copy is mirrored in x so it reads correctly from behind.
    private static void rect(PoseStack.Pose pose, VertexConsumer vc, int light,
                             float x0, float y0, float x1, float y1, float z, boolean back, int color,
                             float u0, float v0, float u1, float v1) {
        float nz = back ? -1 : 1;
        float uL = back ? u1 : u0, uR = back ? u0 : u1;
        vertex(pose, vc, light, x0, y0, z, uL, v1, 0, 0, nz, color);
        vertex(pose, vc, light, x1, y0, z, uR, v1, 0, 0, nz, color);
        vertex(pose, vc, light, x1, y1, z, uR, v0, 0, 0, nz, color);
        vertex(pose, vc, light, x0, y1, z, uL, v0, 0, 0, nz, color);
    }

    private static void box(PoseStack.Pose pose, VertexConsumer vc, int light,
                            float x0, float y0, float z0, float x1, float y1, float z1) {
        face(pose, vc, light, 0, 0, 1,  x0,y0,z1, x1,y0,z1, x1,y1,z1, x0,y1,z1);
        face(pose, vc, light, 0, 0, -1, x1,y0,z0, x0,y0,z0, x0,y1,z0, x1,y1,z0);
        face(pose, vc, light, 1, 0, 0,  x1,y0,z1, x1,y0,z0, x1,y1,z0, x1,y1,z1);
        face(pose, vc, light, -1, 0, 0, x0,y0,z0, x0,y0,z1, x0,y1,z1, x0,y1,z0);
        face(pose, vc, light, 0, 1, 0,  x0,y1,z1, x1,y1,z1, x1,y1,z0, x0,y1,z0);
        face(pose, vc, light, 0, -1, 0, x0,y0,z0, x1,y0,z0, x1,y0,z1, x0,y0,z1);
    }

    private static void face(PoseStack.Pose pose, VertexConsumer vc, int light, float nx, float ny, float nz,
                             float ax, float ay, float az, float bx, float by, float bz,
                             float cx, float cy, float cz, float dx, float dy, float dz) {
        vertex(pose, vc, light, ax, ay, az, 0, 1, nx, ny, nz, WHITE);
        vertex(pose, vc, light, bx, by, bz, 1, 1, nx, ny, nz, WHITE);
        vertex(pose, vc, light, cx, cy, cz, 1, 0, nx, ny, nz, WHITE);
        vertex(pose, vc, light, dx, dy, dz, 0, 0, nx, ny, nz, WHITE);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer vc, int light,
                               float x, float y, float z, float u, float v,
                               float nx, float ny, float nz, int color) {
        vc.addVertex(pose, x * P, y * P, z * P)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }
}