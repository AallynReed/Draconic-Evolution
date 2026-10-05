package com.brandon3055.draconicevolution.client.render.tile;

import com.brandon3055.draconicevolution.blocks.tileentity.StabilizedSpawnerLogic;
import com.brandon3055.draconicevolution.blocks.tileentity.TileStabilizedSpawner;
import com.brandon3055.draconicevolution.init.DEContent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Created by brandon3055 on 20/05/2016.
 */
public class RenderTileStabilizedSpawner implements DETileRenderer<TileStabilizedSpawner> {

    private static ItemStack[] CORE_RENDER_ITEMS;

    public RenderTileStabilizedSpawner(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileStabilizedSpawner tile, float partialTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        StabilizedSpawnerLogic spawnerLogic = tile.spawnerLogic;

        mStack.pushPose();
        mStack.translate(0.5D, 0.0D, 0.5D);
        Entity entity = spawnerLogic.getOrCreateDisplayEntity(tile.getLevel(), tile.getBlockPos());
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0D) {
                f /= f1;
            }

            mStack.translate(0.0D, 0.4F, 0.0D);
            mStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp(partialTicks, spawnerLogic.getOSpin(), spawnerLogic.getSpin()) * 10.0F));
            mStack.translate(0.0D, -0.2F, 0.0D);
            mStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
            mStack.scale(f, f, f);
            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderState entityState = dispatcher.extractEntity(entity, partialTicks);
            entityState.lightCoords = packedLight;
            dispatcher.submit(entityState, camera, 0.0D, 0.0D, 0.0D, mStack, collector);
        }
        mStack.popPose();

        mStack.translate(0.5, 1F - (1F / 32F), 0.5);
        mStack.scale(0.75F, 0.75F, 0.75F);
        mStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        if (CORE_RENDER_ITEMS == null) CORE_RENDER_ITEMS = new ItemStack[]{new ItemStack(DEContent.CORE_DRACONIUM.get()), new ItemStack(DEContent.CORE_WYVERN.get()), new ItemStack(DEContent.CORE_AWAKENED.get()), new ItemStack(DEContent.CORE_CHAOTIC.get())};
        ItemStack stack = CORE_RENDER_ITEMS[tile.spawnerTier.get().ordinal()];
        DETileRenderer.renderItem(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, mStack, collector, tile.getLevel(), tile.posSeed());
    }
}
