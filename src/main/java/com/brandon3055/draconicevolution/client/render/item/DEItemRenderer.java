package com.brandon3055.draconicevolution.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface DEItemRenderer {

    void renderItem(ItemStack stack, ItemDisplayContext ctx, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay);

    ItemTransforms getModelState();

    boolean usesBlockLight();

    default void resolve(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity) {}

    default @Nullable TextureAtlasSprite getParticleIcon() {
        return null;
    }
}
