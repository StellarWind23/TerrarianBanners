package com.github.stellarwind23.terrarian_banners.client.init;

import com.github.stellarwind23.terrarian_banners.client.render.TBBannerColorManager;
import com.github.stellarwind23.terrarian_banners.client.render.TBMobBannerRenderer;
import com.github.stellarwind23.terrarian_banners.content.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class TerrarianBannersClientInit {

    public static void init() {
        BlockEntityRendererRegistry.register(TBBlockEntityTypes.MOB_BANNER.get(), TBMobBannerRenderer::new);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TBBannerColorManager.INSTANCE, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "tb_banner_colors"));
    }
}
