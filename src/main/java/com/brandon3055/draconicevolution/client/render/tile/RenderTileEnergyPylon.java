package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.colour.Colour;
import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.client.shader.BCShaders;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;

import java.util.Map;

/**
 * Created by brandon3055 on 20/05/2016.
 */
public class RenderTileEnergyPylon implements DETileRenderer<TileEnergyPylon> {

    private static RenderType modelType = RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/pylon_sphere_texture.png"));

    private static RenderType shellType = RenderType.create("pylon_sphere", RenderSetup.builder(RenderPipeline.builder(BCShaders.POS_COLOUR_TEX_ALPHA0)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/pylon_sphere"))
                    .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
                    .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                    .build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/pylon_sphere_texture.png"))
            .bufferSize(256)
            .createRenderSetup()
    );
    private final CCModel model;

    public RenderTileEnergyPylon(BlockEntityRendererProvider.Context context) {
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/pylon_sphere.obj")).quads().ignoreMtl().parse();
        model = CCModel.combine(map.values());
        model.apply(new Scale(-0.35, -0.35, -0.35));
        model.computeNormals();
    }

    @Override
    public void render(TileEnergyPylon te, float partialTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        if (!te.structureValid.get()) return;

        Matrix4 mat = new Matrix4(mStack);

        int modelColour = 0x005efaFF;
        if (te.colour.notNull()) {
            modelColour = te.colour.get().rgba();
        }

        mat.translate(te.direction.get().getUnitVec3i());
        mat.translate(0.5, 0.5, 0.5);
        mat.rotate(((ClientEventHandler.elapsedTicks + partialTicks) * 2F) * MathHelper.torad, new Vector3(0, 1, 0.5).normalize());
        int finalModelColour = modelColour;
        collector.cc$submitCCRS(mat, modelType, (m, ccrs) -> {
            ccrs.brightness = 240;
            ccrs.overlay = packedOverlay;
            ccrs.baseColour = finalModelColour;
            model.render(ccrs, m);
        });

        float f = MathHelper.clip(((ClientEventHandler.elapsedTicks + partialTicks) % 35F) / 30F, 0, 1);
        if (te.ioMode.get().canExtract()) {
            f = 1F - f;
        }

        int shellColour = Colour.packRGBA(1F - f, 0xF5 / 255F, 0xfa / 255F, 1F - f);
        if (te.colour.notNull()) {
            shellColour = Colour.packRGBA(te.colour.get().rF() * (1F - f), te.colour.get().gF(), te.colour.get().bF(), 1F - (f * f));
        }

        mat.scale(1 + f);
        int finalShellColour = shellColour;
        collector.cc$submitCCRS(mat, shellType, (m, ccrs) -> {
            ccrs.brightness = 240;
            ccrs.overlay = packedOverlay;
            ccrs.baseColour = finalShellColour;
            model.render(ccrs, m);
        });
    }

    @Override
    public AABB getRenderBoundingBox(TileEnergyPylon blockEntity) {
        return DETileRenderer.super.getRenderBoundingBox(blockEntity).inflate(1);
    }
}
