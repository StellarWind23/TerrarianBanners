import com.github.stellarwind23.terrarian_banners.content.crafting.ShieldAddMobBannerRecipe;
import com.github.stellarwind23.terrarian_banners.content.crafting.ShieldRemoveMobBannerRecipe;
import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TBRecipes {
    private static DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
    public static RegistrySupplier<RecipeSerializer<ShieldAddMobBannerRecipe>> ADD_MOB_BANNER;
    public static RegistrySupplier<RecipeSerializer<ShieldRemoveMobBannerRecipe>> REMOVE_MOB_BANNER;

    public static void init() {
        SERIALIZERS = DeferredRegister.create(TerrarianBanners.MOD_ID, Registries.RECIPE_SERIALIZER);
        ADD_MOB_BANNER = SERIALIZERS.register("shield_add_mob_banner",
                () -> new CustomRecipe.Serializer<>(ShieldAddMobBannerRecipe::new));
        REMOVE_MOB_BANNER = SERIALIZERS.register("shield_remove_mob_banner",
                () -> new CustomRecipe.Serializer<>(ShieldRemoveMobBannerRecipe::new));
        SERIALIZERS.register();
    }
}