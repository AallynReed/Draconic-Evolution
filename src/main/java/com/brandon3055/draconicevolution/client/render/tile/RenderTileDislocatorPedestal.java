package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.math.MathHelper;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.DEOldConfig;
import com.brandon3055.draconicevolution.blocks.tileentity.TileDislocatorPedestal;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.items.tools.DislocatorAdvanced;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandon3055 on 27/09/2016.
 */
public class RenderTileDislocatorPedestal implements DETileRenderer<TileDislocatorPedestal> {

    public static List<BakedQuad> modelQuads = null;

    public RenderTileDislocatorPedestal(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileDislocatorPedestal tile, float partialTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedoverlay, CameraRenderState camera) {
        if (modelQuads == null) {
            List<BlockStateModelPart> parts = new ArrayList<>();
            Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(DEContent.DISLOCATOR_PEDESTAL.get().defaultBlockState()).collectParts(tile.getLevel().getRandom(), parts);
            modelQuads = parts.stream().flatMap(part -> part.getQuads(null).stream()).toList();
        }

        mStack.pushPose();
        mStack.translate(0.5, 0.5, 0.5);
        mStack.mulPose(Axis.YP.rotationDegrees(-tile.rotation.get() * 22.5F));
        mStack.translate(-0.5, -0.5, -0.5);

        collector.submitCustomGeometry(mStack, RenderTypes.solidMovingBlock(), (pose, builder) -> {
            QuadInstance instance = new QuadInstance();
            instance.setLightCoords(packedLight);
            int i = 0;
            for (int j = modelQuads.size(); i < j; ++i) {
                BakedQuad bakedquad = modelQuads.get(i);
                builder.putBakedQuad(pose, bakedquad, instance);
            }
        });

        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = tile.itemHandler.getStackInSlot(0);
        if (!stack.isEmpty()) {
            mStack.pushPose();
            mStack.translate(0.5, 0.79, 0.52);
            mStack.scale(0.5F, 0.5F, 0.5F);
            mStack.mulPose(Axis.XP.rotationDegrees(-67.5F));
            DETileRenderer.renderItem(stack, ItemDisplayContext.FIXED, packedLight, packedoverlay, mStack, collector, tile.getLevel(), tile.posSeed());
            mStack.popPose();
        }
        mStack.popPose();
        if (!stack.isEmpty()) {
            drawName(tile, stack, mStack, collector, partialTicks);
        }
    }

    private void drawName(TileDislocatorPedestal tile, ItemStack item, PoseStack mStack, SubmitNodeCollector collector, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        HitResult hitResult = player.pick(10, partialTicks, true);
        boolean isCursorOver = hitResult.getType() == HitResult.Type.BLOCK && ((BlockHitResult) hitResult).getBlockPos().equals(tile.getBlockPos());
        boolean isSneaking = player.isShiftKeyDown();

        if (!isCursorOver && (isSneaking != DEOldConfig.invertDPDSB)) {
            return;
        }

        String name = item.getOrDefault(DataComponents.CUSTOM_NAME, Component.empty()).getString();
        if (item.getItem() instanceof DislocatorAdvanced) {
            DislocatorAdvanced.DislocatorTarget location = ((DislocatorAdvanced) item.getItem()).getSelected(item);
            if (location != null) {
                name = location.getName();
            }
        }
        if (name.isEmpty()) {
            return;
        }

        mStack.pushPose();
        mStack.translate(0.5, 1.125, 0.5);
        mStack.scale(0.02F, 0.02F, 0.02F);
        mStack.mulPose(new Quaternionf().rotationXYZ(0, -90 * (float) MathHelper.torad, 180 * (float) MathHelper.torad));

        double xDiff = player.getX() - (tile.getBlockPos().getX() + 0.5);
        double yDiff = (player.getY() + player.getEyeHeight()) - (tile.getBlockPos().getY() + 1.125);
        double zDiff = player.getZ() - (tile.getBlockPos().getZ() + 0.5);
        double yawAngle = Math.toDegrees(Math.atan2(zDiff, xDiff));
        double pitchAngle = Math.toDegrees(Math.atan2(yDiff, Utils.getDistance(player.getX(), player.getY(), player.getZ(), tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5)));

        mStack.mulPose(Axis.YP.rotationDegrees((float) yawAngle));
        mStack.mulPose(Axis.XP.rotationDegrees((float) -pitchAngle));
        mStack.scale(1, 1, -1);

        int textWidth = mc.font.width(name);
        mStack.translate(0, 0, -0.0125);
        collector.submitText(mStack, -(textWidth / 2F), 0, Component.literal(name).getVisualOrderText(), true, Font.DisplayMode.NORMAL, 15728880, 0xFFFFFFFF, 0, 0);
        mStack.popPose();
    }
}
