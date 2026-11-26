package com.github.stellarwind23.terrarian_banners.client.render;

import com.github.stellarwind23.terrarian_banners.init.TerrarianBanners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;

import java.util.*;

public record TBTextureStyle(List<Layer> layers) {

    public static TBTextureStyle fromLayers(Layer... layers) {
        return new TBTextureStyle(Arrays.stream(layers).toList());
    }

    public TBTextureStyle {
        layers = List.copyOf(layers);
    }

    public record Layer(String name, Identifier texture, Vector2i dims, Optional<List<Vector2i>> offsets, Optional<TextColor> color) {

        public static final Codec<Vector2i> VECTOR_2_CODEC = RecordCodecBuilder.create(v -> v.group(
                Codec.INT.fieldOf("x").forGetter(Vector2i::x),
                Codec.INT.fieldOf("y").forGetter(Vector2i::y)
        ).apply(v, Vector2i::new));

        public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("name").forGetter(Layer::name),
                Identifier.CODEC.fieldOf("texture").forGetter(Layer::texture),
                VECTOR_2_CODEC.fieldOf("dims").forGetter(Layer::dims),
                Codec.optionalField("offsets", Codec.list(VECTOR_2_CODEC), true).forGetter(Layer::offsets),
                TextColor.CODEC.optionalFieldOf("color").forGetter(Layer::color)
        ).apply(i, Layer::new));

        /**
         * Reference a texture in TerrarianBanners namespace(ex. terrarian_banners:textures/effect/foo/bar)
         * @param name Name of the texture file, will be added to end of path, exclude "/"
         * @param path Path to texture folder
         * @param dims Expected dimensions of the texture
         * @return new Layer instance
         */
        public static Layer defaultAsset(String name, String path, Vector2i dims) {
            return asset(name, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path + "/" + name), dims);
        }

        /**
         * Reference a texture in TerrarianBanners namespace(ex. terrarian_banners:textures/effect/foo/bar)
         * @param name Name of the texture file, will be added to end of path, exclude "/"
         * @param path Path to texture folder
         * @param dims Expected dimensions of the texture
         * @param offsets Coordinate offsets for placing texture on layer below it(ex. 2 offsets means two copies placed at offsets).
         * @return new Layer instance
         */
        public static Layer defaultAsset(String name, String path, Vector2i dims, Vector2i...offsets) {
            return asset(name, Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, path + "/" + name), dims, offsets);
        }

        /**
         * Reference a texture in any namespace(ex. foo:textures/effect/bar)
         * @param name Name of the texture file, will be added to end of path, exclude "/"
         * @param path Path to texture folder
         * @param dims Expected dimensions of the texture
         * @param offsets Coordinate offsets for placing texture on layer below it(ex. 2 offsets means two copies placed at offsets).
         * @return new Layer instance
         */
        public static Layer asset(String name, Identifier path, Vector2i dims, Vector2i...offsets) {
            return new Layer(name, path.withPath(path.getPath() + "/" + name), dims, Optional.of(Arrays.stream(offsets).toList()), Optional.empty());
        }

        /**
         * Reference a texture in any namespace(ex. foo:textures/effect/bar)
         * @param name Name of the texture file, will be added to end of path, exclude "/"
         * @param path Path to texture folder
         * @param dims Expected dimensions of the texture
         * @return new Layer instance
         */
        public static Layer asset(String name, Identifier path, Vector2i dims) {
            return new Layer(name, path.withPath(path.getPath() + "/" + name), dims, Optional.empty(), Optional.empty());
        }

        public Layer withColor(TextColor color) { return new Layer(name, texture, dims, offsets, Optional.of(color)); }
        public Layer withColor(int rgb) { return withColor(TextColor.fromRgb(rgb)); }

        /** Opaque ARGB, or white (no tint) if no color is set. */
        public int tint() {
            return color.map(c -> 0xFF000000 | c.getValue()).orElse(-1);
        }
    }

    public static final Codec<TBTextureStyle> CODEC = RecordCodecBuilder.create(i -> i.group(
            Layer.CODEC.listOf().validate(TBTextureStyle::checkUniqueNames)
                    .fieldOf("layers").forGetter(TBTextureStyle::layers)
    ).apply(i, TBTextureStyle::new));

    //Placeholders if data is missing somewhere.
    public static Identifier NULL_ID = Identifier.fromNamespaceAndPath(TerrarianBanners.MOD_ID, "null");
    public static Layer NULL_EMBLEM = Layer.defaultAsset("emblem", "entity/mob_banner/emblem/default", new Vector2i(16, 16));
    public static final TBTextureStyle NULL_STYLE = TBTextureStyle.fromLayers(
            Layer.defaultAsset("base", "entity/mob_banner/base/default", new Vector2i(64, 64)).withColor(TextColor.fromLegacyFormat(ChatFormatting.BLACK)),
            Layer.defaultAsset("border", "entity/mob_banner/detail/border", new Vector2i(64, 64)).withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE)),
            Layer.defaultAsset("thin_border", "entity/mob_banner/detail/thin_border", new Vector2i(64, 64)).withColor(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
            NULL_EMBLEM
    );

    private static DataResult<List<Layer>> checkUniqueNames(List<Layer> layers) {
        Set<String> seen = new HashSet<>();
        for (Layer layer : layers) {
            if (!seen.add(layer.name())) {
                return DataResult.error(() -> "Duplicate banner Layer name: " + layer.name());
            }
        }
        return DataResult.success(layers);
    }
}