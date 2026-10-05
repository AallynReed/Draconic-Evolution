package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.model.OBJParser;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCoreStabilizer;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;

import java.util.Map;

/**
 * Created by brandon3055 on 19/4/2016.
 */
public class RenderEnergyCoreStabilizer implements DETileRenderer<TileEnergyCoreStabilizer> {

    private static final RenderType MODEL_TYPE = RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/stabilizer_large.png"));

    private static final RenderType MODEL_TYPE_ACTIVE = RenderType.create("stab_type_a", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/stab_type_a"))
                    .withVertexShader("core/rendertype_text")
                    .withFragmentShader("core/rendertype_text")
                    .withSampler("Sampler0")
                    .withSampler("Sampler2")
                    .withColorTargetState(ColorTargetState.DEFAULT)
                    .build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/stabilizer_large.png"))
            .useLightmap()
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private CCModel model;

    public RenderEnergyCoreStabilizer(BlockEntityRendererProvider.Context context) {
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/energy_core/stabilizer_large.obj")).quads().ignoreMtl().parse();
        model = CCModel.combine(map.values()).backfacedCopy();
    }

//    Ask covers about this...

    @Override
    public void render(TileEnergyCoreStabilizer tile, float partialTicks, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        if (!tile.isValidMultiBlock.get()) return;

        boolean coreActive = tile.isCoreActive.get();
        Direction facing;
        if (coreActive) {
            facing = tile.coreDirection.get();
        } else {
            facing = Direction.get(Direction.AxisDirection.POSITIVE, tile.multiBlockAxis.get());
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        if (facing.getAxis() == Direction.Axis.X || facing.getAxis() == Direction.Axis.Y) {
            poseStack.mulPose(new Quaternionf().rotationXYZ(facing.getStepY() * 90 * (float)MathHelper.torad, facing.getStepX() * -90 * (float)MathHelper.torad, 0));
        } else if (facing == Direction.SOUTH) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180F));
        }
        poseStack.mulPose(Axis.ZP.rotationDegrees(tile.rotation + (coreActive ? partialTicks : 0)));
        poseStack.translate(0, -1.5, 0);
        collector.cc$submitCCRS(poseStack, coreActive ? MODEL_TYPE_ACTIVE : MODEL_TYPE, (mat, ccrs) -> {
            ccrs.brightness = 240;
            ccrs.overlay = packedOverlay;
            model.render(ccrs, mat);
        });
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(TileEnergyCoreStabilizer blockEntity) {
        return DETileRenderer.super.getRenderBoundingBox(blockEntity).inflate(1);
    }
}
