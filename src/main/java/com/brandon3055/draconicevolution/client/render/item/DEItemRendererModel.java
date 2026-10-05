package com.brandon3055.draconicevolution.client.render.item;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Item model type {@code draconicevolution:item_renderer}: renders an item with the {@link DEItemRenderer} class named in the model.
 */
public class DEItemRendererModel implements ItemModel {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "item_renderer");
    private static final Supplier<Vector3fc[]> EXTENTS = () -> new Vector3fc[]{new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)};

    private final DEItemRenderer renderer;
    private final Matrix4fc transformation;

    public DEItemRendererModel(DEItemRenderer renderer, Matrix4fc transformation) {
        this.renderer = renderer;
        this.transformation = transformation;
    }

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        output.appendModelIdentityElement(this);
        output.setAnimated();
        ItemStackRenderState.LayerRenderState layer = output.newLayer();
        layer.setExtents(EXTENTS);
        layer.setLocalTransform(transformation);
        layer.setupSpecialModel(Submitter.INSTANCE, new Argument(renderer, item, displayContext, level, owner == null ? null : owner.asLivingEntity()));
        layer.setUsesBlockLight(renderer.usesBlockLight());
        layer.setItemTransform(renderer.getModelState().getTransform(displayContext));
        TextureAtlasSprite particle = renderer.getParticleIcon();
        if (particle != null) {
            layer.setParticleMaterial(new Material.Baked(particle, false));
        }
    }

    private record Argument(DEItemRenderer renderer, ItemStack stack, ItemDisplayContext context, @Nullable ClientLevel level, @Nullable LivingEntity entity) {}

    private static class Submitter implements SpecialModelRenderer<Argument> {
        private static final Submitter INSTANCE = new Submitter();

        @Override
        public void submit(@Nullable Argument argument, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
            if (argument != null) {
                argument.renderer().resolve(argument.stack(), argument.level(), argument.entity());
                argument.renderer().renderItem(argument.stack(), argument.context(), poseStack, collector, lightCoords, overlayCoords);
            }
        }

        @Override
        public void getExtents(Consumer<Vector3fc> output) {}

        @Override
        public @Nullable Argument extractArgument(ItemStack stack) {
            return null;
        }
    }

    public record Unbaked(String className) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = Codec.STRING.fieldOf("class").xmap(Unbaked::new, Unbaked::className);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {}

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            try {
                DEItemRenderer renderer = (DEItemRenderer) Class.forName(className).getConstructor().newInstance();
                return new DEItemRendererModel(renderer, transformation);
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException("Failed to construct item renderer " + className, ex);
            }
        }
    }
}
