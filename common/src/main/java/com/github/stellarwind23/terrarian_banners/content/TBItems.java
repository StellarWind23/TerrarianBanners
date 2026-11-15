package com.github.stellarwind23.terrarian_banners.content;

import com.github.stellarwind23.terrarian_banners.object.MobBannerItem;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.github.stellarwind23.terrarian_banners.util.MItem;
import com.github.stellarwind23.terrarian_banners.util.MTranslation;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class TBItems {

    protected static DeferredRegister<Item> ITEMS;

    public static RegistrySupplier<BlockItem> MOB_BANNER;

    public static void init() {

        ITEMS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.ITEM);

        //Register stuff here ▼▼▼

        MOB_BANNER = registerItem("mob_banner", new MItem<>("none",
                props -> new MobBannerItem(TBBlocks.MOB_BANNER.get(), TBBlocks.MOB_WALL_BANNER.get(), props),
                Optional.of(new Item.Properties().stacksTo(16))));

        //Register stuff here ▲▲▲

        ITEMS.register();
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <T extends Item> RegistrySupplier<T> registerItem(String name, MItem<T> mItem) {
        Identifier id = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);

        T item;
        if(mItem.feedInProps().isPresent()) {
            item = mItem.itemConstructor().apply(mItem.feedInProps().orElseThrow().setId(key).arch$tab(TBTab.TAB));
        } else {
            item = mItem.itemConstructor().apply(new Item.Properties().setId(key).arch$tab(TBTab.TAB));
        }

        //Generate model if not block
        if(!mItem.modelType().equals("block")) MItem.REGISTERED_MOD_ITEMS.put(mItem, item);

        //Generate translation
        new MTranslation("item", name);

        return ITEMS.register(id, () -> item);
    }
}
