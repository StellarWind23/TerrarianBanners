package com.github.stellarwind23.terrarian_banners.client.init;

import com.github.stellarwind23.terrarian_banners.client.render.TBTextureStyleManager;
import com.github.stellarwind23.terrarian_banners.client.render.TBMobBannerRenderer;
import com.github.stellarwind23.terrarian_banners.client.render.TBStyleType;
import com.github.stellarwind23.terrarian_banners.client.render.TBStyleTypeManager;
import com.github.stellarwind23.terrarian_banners.content.TBBlockEntityTypes;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import java.util.Map;

public class TerrarianBannersClientInit {

    public static void init() {
        BlockEntityRendererRegistry.register(TBBlockEntityTypes.MOB_BANNER.get(), TBMobBannerRenderer::new);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TBStyleTypeManager.INSTANCE, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "tb_texture_types"));

        for(Map.Entry<TBStyleType, TBTextureStyleManager> e: TBTextureStyleManager.STYLE_MANAGERS.entrySet()) {
            ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, e.getValue(), Identifier.fromNamespaceAndPath(e.getKey()., e.getKey().styleFolder()));
        }

        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TBTextureStyleManager.EFFECT, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "tb_texture_styles/effect"));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TBTextureStyleManager.MOB_BANNER, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "tb_texture_styles/mob_banner"));
    }
}
