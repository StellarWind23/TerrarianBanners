package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.content.TBComponentTypes;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class TBMobBannerSpecialRenderer implements SpecialModelRenderer<Identifier> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "mob_banner");

    @Override
    public @Nullable Identifier extractArgument(ItemStack stack) {
        return TBBannerTextures.forMob(stack.get(TBComponentTypes.MOB_BANNER.get()));
    }

    @Override
    public void submit(@Nullable Identifier texture, @NonNull ItemDisplayContext context, @NonNull PoseStack ps, @NonNull SubmitNodeCollector out, int light, int overlay, boolean foil, int outlineColor) {
        if (texture == null) return;
        ps.pushPose();
        ps.translate(0.5, 0.0, 0.5);   // block-centre convention, same as the block entity renderer
        TBMobBannerDrawer.submit(ps, out, true, texture, 0.0F, light);
        ps.popPose();
    }

    @Override
    public void getExtents(@NonNull Consumer<Vector3fc> output) {
        // Bounding box corners in blocks: about 0.8 wide, 1.83 tall, 0.2 deep
        for (float x : new float[]{0.1F, 0.9F})
            for (float y : new float[]{0.0F, 1.83F})
                for (float z : new float[]{0.4F, 0.6F})
                    output.accept(new Vector3f(x, y, z));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @NonNull MapCodec<Unbaked> type() { return MAP_CODEC; }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.@NonNull BakingContext context) {
            return new TBMobBannerSpecialRenderer();
        }
    }
}