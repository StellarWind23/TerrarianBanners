package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.content.TBTags;
import com.github.stellarwind23.terrarian_banners.content.item.MobBannerItem;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.MTranslation;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.function.Supplier;

public class TBTab {

    protected static DeferredRegister<CreativeModeTab> TABS;

    public static RegistrySupplier<CreativeModeTab> TAB;

    public static void init() {
        TABS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.CREATIVE_MODE_TAB);

        //Register stuff here ▼▼▼

        TAB = registerTab("terrarian_banners", () -> MobBannerItem.createStack(EntityType.ZOMBIE), TBTab::addBanners);

        //Register stuff here ▲▲▲

        TABS.register();
    }

    private static void addBanners(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(type -> type.is(TBTags.HAS_BANNER))
                .sorted(Comparator.comparing(type -> BuiltInRegistries.ENTITY_TYPE.getKey(type).toString()))
                .forEach(type -> output.accept(MobBannerItem.createStack(type)));
    }

    private static RegistrySupplier<CreativeModeTab> registerTab(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator generator) {
        var t = new MTranslation("tab", name);
        return TABS.register(name, () -> CreativeTabRegistry.create(builder -> builder
                .title(Component.translatable(t.createTranslation().getA()))
                .icon(icon)
                .displayItems(generator)));
    }
}