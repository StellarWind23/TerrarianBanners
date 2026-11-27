package com.github.stellarwind23.terrarian_banners.client.init;

import com.github.stellarwind23.terrarian_banners.client.render.TBTextureStyleManager;
import com.github.stellarwind23.terrarian_banners.client.render.TBMobBannerRenderer;
import com.github.stellarwind23.terrarian_banners.content.block.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class TerrarianBannersClientInit {

    public static void init() {
        BlockEntityRendererRegistry.register(TBBlockEntityTypes.MOB_BANNER.get(), TBMobBannerRenderer::new);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TBTextureStyleManager.MOB_BANNER, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "texture_styles_mob_banner"));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TBTextureStyleManager.EFFECT, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "texture_styles_effect"));
    }
}
