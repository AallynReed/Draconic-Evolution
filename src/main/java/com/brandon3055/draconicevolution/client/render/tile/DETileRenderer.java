package com.brandon3055.draconicevolution.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface DETileRenderer<T extends BlockEntity> extends BlockEntityRenderer<T, DETileRenderer.State<T>> {

    @Override
    default State<T> createRenderState() {
        return new State<>();
    }

    @Override
    default void extractRenderState(T tile, State<T> state, float partialTicks, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
        state.tile = tile;
        state.partialTicks = partialTicks;
    }

    @Override
    default void submit(State<T> state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        render(state.tile, state.partialTicks, poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, camera);
    }

    void render(T tile, float partialTicks, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera);

    static void renderItem(ItemStack stack, ItemDisplayContext context, int packedLight, int packedOverlay, PoseStack poseStack, SubmitNodeCollector collector, @Nullable Level level, int seed) {
        ItemStackRenderState itemState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(itemState, stack, context, level, null, seed);
        itemState.submit(poseStack, collector, packedLight, packedOverlay, 0);
    }

    class State<T extends BlockEntity> extends BlockEntityRenderState {
        public T tile;
        public float partialTicks;
    }
}
