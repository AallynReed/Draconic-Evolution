package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.colour.Colour;
import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.buffer.TransformingVertexConsumer;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.client.render.MultiBlockRenderers;
import com.brandon3055.brandonscore.client.shader.BCRenderType;
import com.brandon3055.brandonscore.client.shader.BCShaders;
import com.brandon3055.brandonscore.lib.datamanager.ManagedPos;
import com.brandon3055.brandonscore.multiblock.MultiBlockDefinition;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCore;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;

import java.util.Map;

/**
 * Created by brandon3055 on 2/4/2016.
 */
public class RenderTileEnergyCore implements DETileRenderer<TileEnergyCore> {
    public static final double[] SCALES = {1.1, 1.7, 2.3, 3.6, 5.5, 7.1, 8.6, 10.2};

    private static final RenderPipeline ALPHA0_TRANSLUCENT = RenderPipeline.builder(BCShaders.POS_COLOUR_TEX_ALPHA0)
            .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/energy_core_translucent"))
            .build();
    private static final RenderPipeline ALPHA0_OPAQUE = RenderPipeline.builder(BCShaders.POS_COLOUR_TEX_ALPHA0)
            .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/energy_core_opaque"))
            .withColorTargetState(ColorTargetState.DEFAULT)
            .build();

    private static final RenderType innerCoreType = RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/energy_core_base.png"));

    private static final RenderType outerCoreType = RenderType.create("outer_core", RenderSetup.builder(ALPHA0_TRANSLUCENT)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/energy_core_overlay.png"))
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private static final RenderType innerStabType = RenderType.create("inner_stab", RenderSetup.builder(ALPHA0_OPAQUE)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/stabilizer_sphere.png"))
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );
    private static final RenderType outerStabType = RenderType.create("outer_stab", RenderSetup.builder(ALPHA0_TRANSLUCENT)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/stabilizer_sphere.png"))
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private static final RenderType beamType = RenderType.create("inner_beam", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/inner_beam"))
                    .withVertexShader("core/position_tex")
                    .withFragmentShader("core/position_tex")
                    .withSampler("Sampler0")
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/stabilizer_beam.png"))
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private static final RenderType outerBeamType = RenderType.create("outer_beam", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/outer_beam"))
                    .withVertexShader("core/position_tex_color")
                    .withFragmentShader("core/position_tex_color")
                    .withSampler("Sampler0")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
                    .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                    .build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/block/energy_core/stabilizer_beam.png"))
            .bufferSize(256)
            .createRenderSetup()
    );

    private static BCRenderType coreShaderType = DEShaders.energyCoreShader.renderType("test_shader", RenderSetup.builder(DEShaders.energyCoreShader.pipeline("test_shader", builder -> builder.withCull(false)))
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private final CCModel modelStabilizerSphere;
    private final CCModel modelEnergyCore;

    //TODO switch core to VBOs, Requires custom shaders to apply transforms
//    private final VBORenderType coreType;
//    private final VBORenderType shieldType;

    public RenderTileEnergyCore(BlockEntityRendererProvider.Context context) {
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/energy_core/stabilizer_sphere.obj")).quads().ignoreMtl().parse();
        modelStabilizerSphere = CCModel.combine(map.values());
        modelStabilizerSphere.computeNormals();

        map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/energy_core/energy_core_model.obj")).quads().ignoreMtl().parse();
        modelEnergyCore = CCModel.combine(map.values());
        modelEnergyCore.computeNormals();

//        coreType = new VBORenderType(innerCoreType, (format, builder) -> {
//            CCRenderState ccrs = CCRenderState.instance();
//            ccrs.reset();
//            ccrs.bind(builder, format);
//            modelEnergyCore.render(ccrs);
//        });
//
//        shieldType = new VBORenderType(outerCoreType, (format, builder) -> {
//            CCRenderState ccrs = CCRenderState.instance();
//            ccrs.reset();
//            ccrs.bind(builder, format);
//            modelEnergyCore.render(ccrs);
//        });

    }


    @Override
    public void render(TileEnergyCore te, float partialTicks, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        if (te.buildGuide.get()) {
            MultiBlockDefinition def = te.getMultiBlockDef();
            if (def != null) {
                MultiBlockRenderers.renderBuildGuide(te.getLevel(), te.getBlockPos(), poseStack, collector, def, 200, partialTicks);
            }
        }

        if (!te.active.get()) {
            return;
        }

        Matrix4 mat = new Matrix4(poseStack);

        float rotation = (ClientEventHandler.elapsedTicks + partialTicks) / 2F;
        double scale = SCALES[te.tier.get() - 1];
        int coreBrightness = 140 + (int) Math.abs(Math.sin((float) ClientEventHandler.elapsedTicks / 100f) * 100f);

        renderInnerCore(te, mat, collector, partialTicks, rotation, scale, coreBrightness, packedOverlay);
        if (te.legacyRender.get()) {
            renderLegacyOuterCore(te, mat, collector, partialTicks, rotation, scale, coreBrightness, packedOverlay);
        } else {
            renderFancyOuterCore(te, mat, collector, partialTicks, rotation, scale, coreBrightness, packedOverlay);
        }

        renderStabilizers(te, mat, collector, partialTicks, packedOverlay);
    }

    public void renderInnerCore(TileEnergyCore te, Matrix4 mat, SubmitNodeCollector collector, float partialTicks, float rotation, double scale, int brightness, int packedOverlay) {
        int colour = te.getColour();
        Matrix4 coreMat = mat.copy();
        coreMat.translate(Vector3.CENTER);
        coreMat.scale(scale * -0.65, scale * -0.65, scale * -0.65);
        coreMat.rotate(rotation * MathHelper.torad, new Vector3(0F, 1F, 0.5F).normalize());
        collector.cc$submitCCRS(coreMat, innerCoreType, (m, ccrs) -> {
            ccrs.baseColour = colour;
            ccrs.brightness = brightness;
            ccrs.overlay = packedOverlay;
            modelEnergyCore.render(ccrs, m);
        });
    }

    public void renderFancyOuterCore(TileEnergyCore te, Matrix4 mat, SubmitNodeCollector collector, float partialTicks, float rotation, double scale, int brightness, int packedOverlay) {
        DEShaders.energyCoreActivation.glUniform1f(1);
        boolean t8 = te.tier.get() == 8;

        float[] frame;
        float[] triangle;
        float[] effect;

        if (te.customColour.get()) {
            frame = unpack(te.frameColour.get());
            triangle = unpack(te.innerColour.get());
            effect = unpack(te.effectColour.get());
        } else {
            frame = unpack(t8 ? TileEnergyCore.DEFAULT_FRAME_COLOUR_T8 : TileEnergyCore.DEFAULT_FRAME_COLOUR);
            triangle = unpack(t8 ? TileEnergyCore.DEFAULT_TRIANGLE_COLOUR_T8 : TileEnergyCore.DEFAULT_TRIANGLE_COLOUR);
            effect = unpack(t8 ? TileEnergyCore.DEFAULT_EFFECT_COLOUR_T8 : TileEnergyCore.DEFAULT_EFFECT_COLOUR);
        }

        DEShaders.energyCoreFrameColour.glUniform3f(frame[0], frame[1], frame[2]);
        DEShaders.energyCoreRotTriColour.glUniform3f(triangle[0], triangle[1], triangle[2]);
        DEShaders.energyCoreEffectColour.glUniform3f(effect[0], effect[1], effect[2]);


//        DEShaders.energyCoreFrameColour.glUniform3f(0.1F, 0.1F, 0.1F);
//        DEShaders.energyCoreRotTriColour.glUniform3f(0.4F, 0F, 0.6F); //Default
////        DEShaders.energyCoreRotTriColour.glUniform3f(0.65F, 0.15F, 0F); //Default tier 8
//        DEShaders.energyCoreEffectColour.glUniform3f(0.0F, 0.95F, 0.95F); //Default
////        DEShaders.energyCoreEffectColour.glUniform3f(1F, 0.5F, 0F); //Default Tier 8
////        DEShaders.energyCoreEffectColour.glUniform3f(1F, 1F, 1F);


        Matrix4 overlayMat = mat.copy();
        overlayMat.translate(Vector3.CENTER);
        overlayMat.scale(scale * -0.7, scale * -0.7, scale * -0.7);
        overlayMat.rotate(rotation * 0.5F * MathHelper.torad, new Vector3(0F, -1F, -0.5F).normalize());
        collector.cc$submitCCRS(overlayMat, coreShaderType.withCurrentUniforms(), (m, ccrs) -> {
            ccrs.brightness = brightness;
            ccrs.overlay = packedOverlay;
            modelEnergyCore.render(ccrs, m);
        });
    }

    public void renderLegacyOuterCore(TileEnergyCore te, Matrix4 mat, SubmitNodeCollector collector, float partialTicks, float rotation, double scale, int brightness, int packedOverlay) {
        int colour;
        if (te.tier.get() == 8) {
            colour = Colour.packRGBA(0.95F, 0.45F, 0F, 1F);
        } else {
            colour = Colour.packRGBA(0.2F, 1F, 1F, 1F);
        }

        Matrix4 overlayMatRef = mat.copy();
        overlayMatRef.translate(0.5, 0.5, 0.5);
        overlayMatRef.scale(scale * -0.7, scale * -0.7, scale * -0.7);
        overlayMatRef.rotate(rotation * 0.5F * MathHelper.torad, new Vector3(0F, -1F, -0.5F).normalize());
        collector.cc$submitCCRS(overlayMatRef, outerCoreType, (m, ccrs) -> {
            ccrs.baseColour = colour;
            ccrs.brightness = brightness;
            ccrs.overlay = packedOverlay;
            modelEnergyCore.render(ccrs, m);
        });
    }

    private void renderStabilizers(TileEnergyCore te, Matrix4 matrix4, SubmitNodeCollector collector, float partialTick, int packedOverlay) {
        if (!te.stabilizersValid.get()) {
            return;
        }

        for (ManagedPos posOffset : te.stabilizerPositions) {
            Matrix4 mat = matrix4.copy();
            mat.translate(-posOffset.get().getX() + 0.5, -posOffset.get().getY() + 0.5, -posOffset.get().getZ() + 0.5);

            Direction facing = Direction.getApproximateNearest(posOffset.get().getX(), posOffset.get().getY(), posOffset.get().getZ());//Direction.getFacingFromAxis(Direction.AxisDirection.POSITIVE, te.multiBlockAxis);
            if (facing.getAxis() == Direction.Axis.X || facing.getAxis() == Direction.Axis.Y) {
                mat.rotate(-90F * MathHelper.torad, new Vector3(-facing.getStepY(), facing.getStepX(), 0).normalize());
            } else if (facing == Direction.SOUTH) {
                mat.rotate(180F * MathHelper.torad, new Vector3(0, 1, 0).normalize());
            }

            mat.rotate(90F * MathHelper.torad, new Vector3(1, 0, 0).normalize());

            renderStabilizerBeam(te, mat, collector, posOffset.get(), partialTick);
            if (te.tier.get() >= 5) {
                mat.scale(-1.2F, -0.5F, -1.2F);
            } else {
                mat.scale(-0.45, -0.45, -0.45);
            }

            Matrix4 innerMat = mat.copy();
            innerMat.scale(0.9F, 0.9F, 0.9F);
            innerMat.rotate((ClientEventHandler.elapsedTicks + partialTick) * MathHelper.torad, new Vector3(0, -1, 0));
            collector.cc$submitCCRS(innerMat, innerStabType, (m, ccrs) -> {
                ccrs.baseColour = 0x00FFFFFF;
                ccrs.brightness = 240;
                ccrs.overlay = packedOverlay;
                modelStabilizerSphere.render(ccrs, m);
            });

            mat.scale(1.1F, 1.1F, 1.1F);
            mat.rotate((ClientEventHandler.elapsedTicks + partialTick) * 0.5F * MathHelper.torad, new Vector3(0, 1, 0));
            collector.cc$submitCCRS(mat, outerStabType, (m, ccrs) -> {
                ccrs.baseColour = 0x00FFFF7F;
                ccrs.brightness = 240;
                ccrs.overlay = packedOverlay;
                modelStabilizerSphere.render(ccrs, m);
            });
        }
    }

    private void renderStabilizerBeam(TileEnergyCore te, Matrix4 matrix4, SubmitNodeCollector collector, BlockPos vec, float partialTick) {
        float beamLength = Math.abs(vec.getX() + vec.getY() + vec.getZ()) - 0.5F;
        float time = ClientEventHandler.elapsedTicks + partialTick;
        float beamMotion = -time * 0.2F - (float) MathHelper.floor(-time * 0.1F);
        int tier = te.tier.get();

        collector.cc$submitCustomGeometry(matrix4, beamType, (m, type, buffer) -> {
            Matrix4 innerMat = m.copy();
            VertexConsumer builder = new TransformingVertexConsumer(buffer, innerMat);
            innerMat.rotate(180 * MathHelper.torad, new Vector3(0, 0, 1));

            double rotation = (double) time * 0.025D * -1.5D;

            //region Render Inner Beam
            float scale = 0.2F;
            float d7 = 0.5F + (float) Math.cos(rotation + 2.356194490192345F) * scale;  //x point 1
            float d9 = 0.5F + (float) Math.sin(rotation + 2.356194490192345F) * scale;  //z point 1
            float d11 = 0.5F + (float) Math.cos(rotation + (Math.PI / 4F)) * scale;        //x point 2
            float d13 = 0.5F + (float) Math.sin(rotation + (Math.PI / 4F)) * scale;     //z point 2
            float d15 = 0.5F + (float) Math.cos(rotation + 3.9269908169872414F) * scale;//Dist from x-3
            float d17 = 0.5F + (float) Math.sin(rotation + 3.9269908169872414F) * scale;
            float d19 = 0.5F + (float) Math.cos(rotation + 5.497787143782138F) * scale;
            float d21 = 0.5F + (float) Math.sin(rotation + 5.497787143782138F) * scale;
            float texXMin = 0.0F;
            float texXMax = 1.0F;
            float d28 = (-1.0F + beamMotion);
            float texHeight = beamLength * (0.5F / scale) + d28;

            if (tier >= 5) {
                innerMat.scale(3.5, 1, 3.5);
            }
            innerMat.translate(-0.5, 0, -0.5);

            builder.addVertex(d7, beamLength, d9).setUv(texXMax, texHeight);
            builder.addVertex(d7, 0, d9).setUv(texXMax, d28);
            builder.addVertex(d11, 0, d13).setUv(texXMin, d28);
            builder.addVertex(d11, beamLength, d13).setUv(texXMin, texHeight);

            builder.addVertex(d19, beamLength, d21).setUv(texXMax, texHeight);
            builder.addVertex(d19, 0, d21).setUv(texXMax, d28);
            builder.addVertex(d15, 0, d17).setUv(texXMin, d28);
            builder.addVertex(d15, beamLength, d17).setUv(texXMin, texHeight);

            builder.addVertex(d11, beamLength, d13).setUv(texXMax, texHeight);
            builder.addVertex(d11, 0, d13).setUv(texXMax, d28);
            builder.addVertex(d19, 0, d21).setUv(texXMin, d28);
            builder.addVertex(d19, beamLength, d21).setUv(texXMin, texHeight);

            builder.addVertex(d15, beamLength, d17).setUv(texXMax, texHeight);
            builder.addVertex(d15, 0, d17).setUv(texXMax, d28);
            builder.addVertex(d7, 0, d9).setUv(texXMin, d28);
            builder.addVertex(d7, beamLength, d9).setUv(texXMin, texHeight);

            rotation += 0.77f;
            d7 = 0.5F + (float) Math.cos(rotation + 2.356194490192345F) * scale;
            d9 = 0.5F + (float) Math.sin(rotation + 2.356194490192345F) * scale;
            d11 = 0.5F + (float) Math.cos(rotation + (Math.PI / 4F)) * scale;
            d13 = 0.5F + (float) Math.sin(rotation + (Math.PI / 4F)) * scale;
            d15 = 0.5F + (float) Math.cos(rotation + 3.9269908169872414F) * scale;
            d17 = 0.5F + (float) Math.sin(rotation + 3.9269908169872414F) * scale;
            d19 = 0.5F + (float) Math.cos(rotation + 5.497787143782138F) * scale;
            d21 = 0.5F + (float) Math.sin(rotation + 5.497787143782138F) * scale;

            d28 = (-1F + (beamMotion * 1));
            texHeight = beamLength * (0.5F / scale) + d28;

            builder.addVertex(d7, beamLength, d9).setUv(texXMax, texHeight);
            builder.addVertex(d7, 0, d9).setUv(texXMax, d28);
            builder.addVertex(d11, 0, d13).setUv(texXMin, d28);
            builder.addVertex(d11, beamLength, d13).setUv(texXMin, texHeight);

            builder.addVertex(d19, beamLength, d21).setUv(texXMax, texHeight);
            builder.addVertex(d19, 0, d21).setUv(texXMax, d28);
            builder.addVertex(d15, 0, d17).setUv(texXMin, d28);
            builder.addVertex(d15, beamLength, d17).setUv(texXMin, texHeight);

            builder.addVertex(d11, beamLength, d13).setUv(texXMax, texHeight);
            builder.addVertex(d11, 0, d13).setUv(texXMax, d28);
            builder.addVertex(d19, 0, d21).setUv(texXMin, d28);
            builder.addVertex(d19, beamLength, d21).setUv(texXMin, texHeight);

            builder.addVertex(d15, beamLength, d17).setUv(texXMax, texHeight);
            builder.addVertex(d15, 0, d17).setUv(texXMax, d28);
            builder.addVertex(d7, 0, d9).setUv(texXMin, d28);
            builder.addVertex(d7, beamLength, d9).setUv(texXMin, texHeight);
            //endregion
        });

        collector.cc$submitCustomGeometry(matrix4, outerBeamType, (m, type, buffer) -> {
            Matrix4 outerMat = m.copy();
            VertexConsumer builder = new TransformingVertexConsumer(buffer, outerMat);
            outerMat.rotate(180 * MathHelper.torad, new Vector3(0, 0, 1));

            //region Render Outer Beam
            outerMat.rotate(90 * MathHelper.torad, new Vector3(-1, 0, 0));
            outerMat.rotate(45 * MathHelper.torad, new Vector3(0, 0, 1));
            outerMat.translate(0, 0, 0.4);

            int sides = 4;
            float enlarge = 0.35F;
            if (tier >= 5) {
                sides = 12;
                enlarge = 0.5F + ((tier - 5) * 0.1F);
                outerMat.rotate(time * 0.6F * MathHelper.torad, new Vector3(0, 0, -1));
                outerMat.scale(3.5, 3.5, 1);
            }

            for (int i = 0; i <= sides; i++) {
                float verX = (float) Math.sin((float) (i % sides) * (float) Math.PI * 2F / (float) sides) * 1F;
                float verY = (float) Math.cos((float) (i % sides) * (float) Math.PI * 2F / (float) sides) * 1F;
                builder.addVertex(verX * 0.35F, verY * 0.35F, 0.0F).setColor(255, 255, 255, 32).setUv(i, (beamMotion * 2));
                builder.addVertex(verX * enlarge, verY * enlarge, beamLength).setColor(255, 255, 255, 32).setUv(i, beamLength + (beamMotion * 2));
            }
        });
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    private static float[] unpack(int colour) {
        return new float[]{((colour >> 16) & 0xFF) / 255F, ((colour >> 8) & 0xFF) / 255F, (colour & 0xFF) / 255F};
    }

    @Override
    public AABB getRenderBoundingBox(TileEnergyCore blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 14, pos.getY() - 14, pos.getZ() - 14, pos.getX() + 15, pos.getY() + 15, pos.getZ() + 15);
    }
}
