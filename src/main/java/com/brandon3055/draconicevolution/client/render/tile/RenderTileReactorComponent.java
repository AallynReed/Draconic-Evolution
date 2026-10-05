package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorComponent;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorInjector;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorStabilizer;
import com.brandon3055.draconicevolution.init.DEContent;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import net.covers1624.quack.collection.FastStream;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.Map;

import static codechicken.lib.math.MathHelper.torad;
import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 20/01/2017.
 */
public class RenderTileReactorComponent implements DETileRenderer<TileReactorComponent> {

    private static final RenderType STAB_FRAME_TYPE = RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(MODID, "textures/block/reactor/reactor_stabilizer.png"));
    private static final RenderType INJECTOR_FRAME_TYPE = RenderTypes.entitySolid(Identifier.fromNamespaceAndPath(MODID, "textures/block/reactor/reactor_injector.png"));

    private static final RenderPipeline GLOW_PIPELINE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(MODID, "pipeline/reactor_component_glow"))
            .withSampler("Sampler1")
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .build();

    private static final RenderType STAB_GLOW_TYPE = RenderType.create(MODID + ":stab_glow", RenderSetup.builder(GLOW_PIPELINE)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/block/reactor/reactor_stabilizer.png"))
            .useLightmap()
            .useOverlay()
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private static final RenderType INJECTOR_GLOW_TYPE = RenderType.create(MODID + ":injector_glow", RenderSetup.builder(GLOW_PIPELINE)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/block/reactor/reactor_injector.png"))
            .useLightmap()
            .useOverlay()
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup()
    );

    private static CCModel modelInjectorBase;
    private static CCModel modelInjectorEmitters;

    private static CCModel modelStabFrame;
    private static CCModel modelStabInnerRotor;
    private static CCModel modelStabInnerRotorArm;
    private static CCModel modelStabOuterRotor;
    private static CCModel modelStabOuterRotorArm;
    private static CCModel modelStabRing;
    private static CCModel modelStabRingEmitter;

    private static CCModel modelInnerRotorPart;
    private static CCModel modelOuterRotorPart;

    static {
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(MODID, "models/block/reactor/reactor_injector.obj")).quads().ignoreMtl().parse();
        modelInjectorBase = CCModel.combine(FastStream.of(map.entrySet())
                .filter(e -> !e.getKey().startsWith("emitter"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();
        modelInjectorEmitters = CCModel.combine(FastStream.of(map.entrySet())
                .filter(e -> e.getKey().startsWith("emitter"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        Map<String, CCModel> stabMap = new OBJParser(Identifier.fromNamespaceAndPath(MODID, "models/block/reactor/reactor_stabilizer.obj")).quads().ignoreMtl().parse();
        modelStabFrame = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("frame"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelStabInnerRotor = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("inner_rotor_blade"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelStabInnerRotorArm = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("inner_rotor_arm"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelStabOuterRotor = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("outer_rotor_blade"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelStabOuterRotorArm = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("outer_rotor_arm"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelStabRing = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("ring"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelStabRingEmitter = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("focus_panel"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelInnerRotorPart = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("inner_rotor_blade_a"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();

        modelOuterRotorPart = CCModel.combine(FastStream.of(stabMap.entrySet())
                .filter(e -> e.getKey().startsWith("outer_rotor_blade_a"))
                .map(Map.Entry::getValue)
                .toLinkedList()).backfacedCopy();
    }

    public RenderTileReactorComponent(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileReactorComponent te, float partialTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        Matrix4 mat = new Matrix4(mStack);
        mat.translate(0.5, 0, 0.5);

        switch (te.facing.get()) {
            case SOUTH -> mat.rotate(180 * torad, Vector3.Y_POS);
            case EAST -> mat.rotate(-90 * torad, Vector3.Y_POS);
            case WEST -> mat.rotate(90 * torad, Vector3.Y_POS);
            case UP -> mat.apply(new Rotation(90 * torad, Vector3.X_POS).at(new Vector3(0, 0.5, 0)));
            case DOWN -> mat.apply(new Rotation(-90 * torad, Vector3.X_POS).at(new Vector3(0, 0.5, 0)));
        }

        if (te instanceof TileReactorStabilizer) {
            float coreRotation = te.animRotation + (partialTicks * te.animRotationSpeed);//Remember Partial Ticks here
            renderStabilizer(mat, collector, coreRotation, te.animRotationSpeed / 15F, packedLight, packedOverlay);
        } else if (te instanceof TileReactorInjector) {
            renderInjector(mat, collector, te.animRotationSpeed / 15F, packedLight, packedOverlay);
        }
    }

    public static void renderStabilizer(Matrix4 mat, SubmitNodeCollector collector, float coreRotation, float brightness, int packedLight, int packedOverlay) {
        float ringRotation = coreRotation * -0.5F;//Remember Partial Ticks here

        Matrix4 innerRotorMat = mat.copy();
        innerRotorMat.apply(new Rotation(coreRotation * torad, Vector3.Z_POS).at(new Vector3(0, 0.5, 0)));
        Matrix4 outerRotorMat = mat.copy();
        outerRotorMat.apply(new Rotation(coreRotation * torad * -2, Vector3.Z_POS).at(new Vector3(0, 0.5, 0)));

        collector.cc$submitCCRS(mat, STAB_FRAME_TYPE, (m, ccrs) -> {
            ccrs.brightness = packedLight;
            ccrs.overlay = packedOverlay;
            modelStabFrame.render(ccrs, mat);
            modelStabInnerRotorArm.render(ccrs, innerRotorMat);
            modelStabOuterRotorArm.render(ccrs, outerRotorMat);

            for (int i = 0; i < 4; i++) {
                ccrs.brightness = packedLight;
                Matrix4 ringMat = mat.copy();
                ringMat.apply(new Rotation(((90 * i) + ringRotation) * torad, Vector3.Z_POS).at(new Vector3(0, 0.5, 0)));
                modelStabRing.render(ccrs, ringMat);

                ccrs.brightness = (int) (brightness * 240);
                Matrix4 emitterMat = ringMat.copy();
                emitterMat.apply(new Rotation(45 * torad, Vector3.X_NEG).at(new Vector3(0, 15 / 16F, -((8 + 1.5) / 16F))));
                modelStabRingEmitter.render(ccrs, emitterMat);
            }

            ccrs.brightness = (int) (brightness * 240);
            modelStabInnerRotor.render(ccrs, innerRotorMat);
            modelStabOuterRotor.render(ccrs, outerRotorMat);
        });

        if (brightness >= 1) {
            collector.cc$submitCCRS(mat, STAB_GLOW_TYPE, (m, ccrs) -> {
                ccrs.brightness = (int) (brightness * 240);
                ccrs.overlay = packedOverlay;
                modelStabInnerRotor.render(ccrs, innerRotorMat);
                modelStabOuterRotor.render(ccrs, outerRotorMat);

                for (int i = 0; i < 4; i++) {
                    Matrix4 emitterMat = mat.copy();
                    emitterMat.apply(new Rotation(((90 * i) + ringRotation) * torad, Vector3.Z_POS).at(new Vector3(0, 0.5, 0)));
                    emitterMat.apply(new Rotation(45 * torad, Vector3.X_NEG).at(new Vector3(0, 15 / 16F, -((8 + 1.5) / 16F))));
                    modelStabRingEmitter.render(ccrs, emitterMat);
                }
            });
        }
    }

    public static void renderInjector(Matrix4 mat, SubmitNodeCollector collector, float brightness, int packedLight, int packedOverlay) {
        collector.cc$submitCCRS(mat, INJECTOR_FRAME_TYPE, (m, ccrs) -> {
            ccrs.brightness = packedLight;
            ccrs.overlay = packedOverlay;
            modelInjectorBase.render(ccrs, mat);

            ccrs.brightness = (int) (brightness * 240);
            modelInjectorEmitters.render(ccrs, mat);
        });

        if (brightness >= 1) {
            collector.cc$submitCCRS(mat, INJECTOR_GLOW_TYPE, (m, ccrs) -> {
                ccrs.brightness = (int) (brightness * 240);
                ccrs.overlay = packedOverlay;
                modelInjectorEmitters.render(ccrs, mat);
            });
        }
    }

    public static void renderComponent(Item item, Matrix4 mat, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        if (item == DEContent.REACTOR_PRT_STAB_FRAME.get()) {
            mat.translate(0.5, 0, 0.5);
            collector.cc$submitCCRS(mat, STAB_FRAME_TYPE, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                modelStabFrame.render(ccrs, m);
            });

        } else if (item == DEContent.REACTOR_PRT_IN_ROTOR.get()) {
            mat.translate(0.3, 0, 0.5);
            mat.scale(1.5F, 1.5F, 1.5F);
            collector.cc$submitCCRS(mat, STAB_FRAME_TYPE, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                modelInnerRotorPart.render(ccrs, m);
            });

        } else if (item == DEContent.REACTOR_PRT_OUT_ROTOR.get()) {
            mat.translate(0.3, 0, 0.5);
            mat.scale(1.5F, 1.5F, 1.5F);
            collector.cc$submitCCRS(mat, STAB_FRAME_TYPE, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                modelOuterRotorPart.render(ccrs, m);
            });

        } else if (item == DEContent.REACTOR_PRT_ROTOR_FULL.get()) {
            mat.translate(0.5, -0.2, 0.5);
            mat.scale(1.5F, 1.5F, 1.5F);
            collector.cc$submitCCRS(mat, STAB_FRAME_TYPE, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;

                modelStabInnerRotor.render(ccrs, m);
                modelStabInnerRotorArm.render(ccrs, m);
                m.apply(new Rotation(60 * torad, Vector3.Z_NEG).at(new Vector3(0, 0.5, 0)));
                modelStabOuterRotor.render(ccrs, m);
                modelStabOuterRotorArm.render(ccrs, m);
            });

        } else if (item == DEContent.REACTOR_PRT_FOCUS_RING.get()) {
            mat.translate(0.5, -0.2, 1.25);
            mat.scale(1.5F, 1.5F, 1.5F);
            collector.cc$submitCCRS(mat, STAB_FRAME_TYPE, (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;

                for (int i = 0; i < 4; i++) {
                    Matrix4 ringMat = m.copy();
                    ringMat.apply(new Rotation((90 * i) * torad, Vector3.Z_POS).at(new Vector3(0, 0.5, 0)));
                    modelStabRing.render(ccrs, ringMat);

                    Matrix4 emitterMat = ringMat.copy();
                    emitterMat.apply(new Rotation(45 * torad, Vector3.X_NEG).at(new Vector3(0, 15 / 16F, -((8 + 1.5) / 16F))));
                    modelStabRingEmitter.render(ccrs, emitterMat);
                }
            });
        }
    }
}
