package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.DeferredItem;
import com.github.stellarwind23.terrarian_banners.util.MTranslation;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TBTab {

    protected static DeferredRegister<CreativeModeTab> TABS;

    public static RegistrySupplier<CreativeModeTab> TAB;

    public static void init(){

        TABS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.CREATIVE_MODE_TAB);

        //Register stuff here ▼▼▼

        TAB = registerTab("terrarian_banners", () -> new ItemStack(new DeferredItem<>(TBItems.TEMPLATE_BLOCK)));

        //Register stuff here ▲▲▲

        TABS.register();
    }

    private static RegistrySupplier<CreativeModeTab> registerTab(String name, Supplier<ItemStack> itemStackSupplier) {
        var t = new MTranslation("tab", name);
        return TABS.register(name, () -> CreativeTabRegistry.create(
                Component.translatable(t.createTranslation().getA()),
                itemStackSupplier
        ));
    }
}
