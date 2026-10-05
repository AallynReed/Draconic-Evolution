package com.brandon3055.draconicevolution.client.render.entity;

import com.brandon3055.brandonscore.client.shader.BCRenderType;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.brandon3055.draconicevolution.entity.guardian.control.ChargeUpPhase;
import com.brandon3055.draconicevolution.entity.guardian.control.IPhase;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Random;

public class DraconicGuardianRenderer extends EntityRenderer<DraconicGuardianEntity, DraconicGuardianRenderer.RenderState> {
    public static final Identifier ENDERCRYSTAL_BEAM_TEXTURES = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/entity/guardian_crystal_beam.png");
    private static final Identifier DRAGON_EXPLODING_TEXTURES = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_exploding.png");
    private static final Identifier DRAGON_TEXTURE = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
    private static final Identifier GUARDIAN_TEXTURE = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/entity/chaos_guardian.png");
    private static final Identifier EYES_TEXTURE = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_eyes.png");
    private static final RenderType dragonCutoutType = RenderTypes.entityCutout(GUARDIAN_TEXTURE);
    private static final RenderType dragonDeathType = RenderTypes.entityCutoutDissolve(GUARDIAN_TEXTURE, DRAGON_EXPLODING_TEXTURES);
    private static final RenderType eyesType = RenderTypes.eyes(EYES_TEXTURE);
    private static final RenderType beamType = RenderTypes.endCrystalBeam(ENDERCRYSTAL_BEAM_TEXTURES);
    private static RenderType BEAM_TYPE2 = RenderType.create("beam_type_2", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/guardian_beam"))
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("NO_OVERLAY")
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withCull(false)
                    .build())
            .withTexture("Sampler0", ENDERCRYSTAL_BEAM_TEXTURES)
            .useLightmap()
            .bufferSize(256)
            .sortOnUpload()
            .createRenderSetup());

    public static BCRenderType SHIELD_TYPE = DEShaders.shieldShader.renderType(DraconicEvolution.MODID + ":guardian_shield", RenderSetup.builder(DEShaders.shieldShader.pipeline("guardian_shield", builder -> builder
                    .withCull(false)
                    .withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, true))))
            .bufferSize(256)
            .createRenderSetup());

    private static final float sqrt3div2 = (float) (Math.sqrt(3.0D) / 2.0D);
    private final DraconicGuardianRenderer.DragonModel model;


    public DraconicGuardianRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new DraconicGuardianRenderer.DragonModel(context.bakeLayer(ModelLayers.ENDER_DRAGON));
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(DraconicGuardianEntity guardian, RenderState state, float partialTicks) {
        super.extractRenderState(guardian, state, partialTicks);
        state.guardian = guardian;
        state.partialTicks = partialTicks;
    }

    private void submitModel(SubmitNodeCollector collector, PoseStack poseStack, RenderType type, DraconicGuardianEntity guardian, float partialTicks, int packedLight, int packedOverlay, int colour) {
        collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
            PoseStack modelStack = new PoseStack();
            modelStack.last().set(pose);
            this.model.prepareMobModel(guardian, 0.0F, 0.0F, partialTicks);
            this.model.renderToBuffer(modelStack, buffer, packedLight, packedOverlay, colour);
        });
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        DraconicGuardianEntity guardian = state.guardian;
        float partialTicks = state.partialTicks;
        int packedLight = state.lightCoords;
        poseStack.pushPose();
        float f = (float) guardian.getLatencyPos(7, partialTicks)[0];
        float f1 = (float) (guardian.getLatencyPos(5, partialTicks)[1] - guardian.getLatencyPos(10, partialTicks)[1]);
        poseStack.mulPose(Axis.YP.rotationDegrees(-f));
        poseStack.mulPose(Axis.XP.rotationDegrees(f1 * 10.0F));
        poseStack.translate(0.0D, 0.0D, 1.0D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.501F, 0.0D);
        boolean flag = guardian.hurtTime > 0;

        if (guardian.deathTicks > 0) {
            float progress = (float)guardian.deathTicks / 200.0F;
            submitModel(collector, poseStack, dragonDeathType, guardian, partialTicks, packedLight, OverlayTexture.NO_OVERLAY, ARGB.white(1.0F - progress));
        } else {
            submitModel(collector, poseStack, dragonCutoutType, guardian, partialTicks, packedLight, OverlayTexture.pack(0.0F, flag), -1);
        }

        boolean isImmune = guardian.getPhaseManager().getCurrentPhase().isInvulnerable();
        float shieldState = guardian.getEntityData().get(DraconicGuardianEntity.SHIELD_POWER) / (float) DEConfig.guardianShield;
        if (shieldState > 0 || isImmune) {
            if (isImmune) {
                DEShaders.shieldColour.glUniform4f(0F, 1F, 1F, 2);
            } else {
                DEShaders.shieldColour.glUniform4f(1F, 0F, 0F, 1.5F * shieldState);
            }
            DEShaders.shieldBarMode.glUniform1i(0);
            DEShaders.shieldActivation.glUniform1f(1F);

            submitModel(collector, poseStack, SHIELD_TYPE.withCurrentUniforms(), guardian, partialTicks, packedLight, OverlayTexture.pack(0.0F, flag), -1);
        }

        submitModel(collector, poseStack, eyesType, guardian, partialTicks, packedLight, OverlayTexture.NO_OVERLAY, -1);
        if (guardian.deathTicks > 0) {
            float f5 = ((float) guardian.deathTicks + partialTicks) / 200.0F;
            float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
            poseStack.pushPose();
            poseStack.translate(0.0D, -1.0D, -2.0D);
            collector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, builder) -> {
                PoseStack rayStack = new PoseStack();
                rayStack.last().set(pose);
                Random random = new Random(432L);

                for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
                    rayStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
                    rayStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
                    rayStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
                    rayStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
                    rayStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
                    rayStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
                    float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                    float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                    Matrix4f matrix4f = rayStack.last().pose();
                    int j = (int) (255.0F * (1.0F - f7));
                    deathAnimA(builder, matrix4f, j);
                    deathAnimB(builder, matrix4f, f3, f4);
                    deathAnimC(builder, matrix4f, f3, f4);
                    deathAnimA(builder, matrix4f, j);
                    deathAnimC(builder, matrix4f, f3, f4);
                    deathAnimD(builder, matrix4f, f3, f4);
                    deathAnimA(builder, matrix4f, j);
                    deathAnimD(builder, matrix4f, f3, f4);
                    deathAnimB(builder, matrix4f, f3, f4);
                }
            });

            poseStack.popPose();
        }

        poseStack.popPose();
        if (guardian.closestGuardianCrystal != null) {
            poseStack.pushPose();
            float relX = (float) (guardian.closestGuardianCrystal.getX() - Mth.lerp(partialTicks, guardian.xo, guardian.getX()));
            float relY = (float) (guardian.closestGuardianCrystal.getY() - Mth.lerp(partialTicks, guardian.yo, guardian.getY()));
            float relZ = (float) (guardian.closestGuardianCrystal.getZ() - Mth.lerp(partialTicks, guardian.zo, guardian.getZ()));
            renderBeam(relX, relY + GuardianCrystalRenderer.getY(guardian.closestGuardianCrystal, partialTicks), relZ, partialTicks, guardian.tickCount, poseStack, collector, packedLight);
            poseStack.popPose();
        }

        IPhase iPhase = guardian.getPhaseManager().getCurrentPhase();
        if (iPhase instanceof ChargeUpPhase && guardian.getArenaOrigin() != null) {
            ChargeUpPhase phase = (ChargeUpPhase) iPhase;
            if (phase.animState() != 0) {
                BlockPos origin = guardian.getArenaOrigin();
//                float beamSin = MathHelper.sin((Math.min(1, phase.animState() + 0.3F)) * (float) Math.PI);
                float beamSin = Mth.sin(phase.animState() * (float) Math.PI);
                poseStack.pushPose();
                float relX = (float) ((origin.getX() + 0.5) - Mth.lerp(partialTicks, guardian.xo, guardian.getX()));
                float relY = (float) ((origin.getY() + 0.5) - Mth.lerp(partialTicks, guardian.yo, guardian.getY()));
                float relZ = (float) ((origin.getZ() + 0.5) - Mth.lerp(partialTicks, guardian.zo, guardian.getZ()));
                renderChargingBeam(relX, relY, relZ, partialTicks, guardian.tickCount, poseStack, collector, packedLight, beamSin);
                poseStack.popPose();
            }
        }

        super.submit(state, poseStack, collector, camera);
    }

    private static void deathAnimA(VertexConsumer builder, Matrix4f mat, int alpha) {
        builder.addVertex(mat, 0.0F, 0.0F, 0.0F).setColor(255, 0, 0, alpha);
        builder.addVertex(mat, 0.0F, 0.0F, 0.0F).setColor(255, 0, 0, alpha);
    }

    private static void deathAnimB(VertexConsumer builder, Matrix4f mat, float p_229060_2_, float p_229060_3_) {
        builder.addVertex(mat, -sqrt3div2 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).setColor(255, 0, 0, 0);
    }

    private static void deathAnimC(VertexConsumer builder, Matrix4f mat, float p_229062_2_, float p_229062_3_) {
        builder.addVertex(mat, sqrt3div2 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).setColor(255, 0, 255, 0);
    }

    private static void deathAnimD(VertexConsumer builder, Matrix4f mat, float p_229063_2_, float p_229063_3_) {
        builder.addVertex(mat, 0.0F, p_229063_2_, 1.0F * p_229063_3_).setColor(255, 0, 0, 0);
    }

    public static void renderBeam(float crystalRelX, float crystalRelY, float crystalRelZ, float partialTicks, int animTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight) {
        float xzDistance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelZ * crystalRelZ);
        float distance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ);
        mStack.pushPose();
        mStack.translate(0.0D, 2.0D, 0.0D);
        mStack.mulPose(Axis.YP.rotation((float) (-Math.atan2(crystalRelZ, crystalRelX)) - ((float) Math.PI / 2F)));
        mStack.mulPose(Axis.XP.rotation((float) (-Math.atan2(xzDistance, crystalRelY)) - ((float) Math.PI / 2F)));
        float f2 = 0.0F - ((float) animTicks + partialTicks) * 0.01F;
        float f3 = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ) / 32.0F - ((float) animTicks + partialTicks) * 0.01F;
        collector.submitCustomGeometry(mStack, beamType, (stackLast, builder) -> {
            Matrix4f lastMatrix = stackLast.pose();
            float f4 = 0.0F;
            float f5 = 0.75F;
            float f6 = 0.0F;

            for (int j = 1; j <= 8; ++j) {
                float rSin = Mth.sin((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
                float rCos = Mth.cos((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
                float indexDecimal = (float) j / 8.0F;
                builder.addVertex(lastMatrix, f4 * 0.2F, f5 * 0.2F, 0.0F).setColor(0, 0, 0, 255).setUv(f6, f2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, f4, f5, distance).setColor(255, 255, 255, 255).setUv(f6, f3).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin, rCos, distance).setColor(255, 255, 255, 255).setUv(indexDecimal, f3).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin * 0.2F, rCos * 0.2F, 0.0F).setColor(0, 0, 0, 255).setUv(indexDecimal, f2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                f4 = rSin;
                f5 = rCos;
                f6 = indexDecimal;
            }
        });
        mStack.popPose();
    }

    public static void renderBeam(float crystalRelX, float crystalRelY, float crystalRelZ, float partialTicks, int animTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, float alpha) {
        float xzDistance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelZ * crystalRelZ);
        float distance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ);
        mStack.pushPose();
        mStack.translate(0.0D, 2.0D, 0.0D);
        mStack.mulPose(Axis.YP.rotation((float) (-Math.atan2(crystalRelZ, crystalRelX)) - ((float) Math.PI / 2F)));
        mStack.mulPose(Axis.XP.rotation((float) (-Math.atan2(xzDistance, crystalRelY)) - ((float) Math.PI / 2F)));
        float f2 = 0.0F - ((float) animTicks + partialTicks) * 0.01F;
        float f3 = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ) / 32.0F - ((float) animTicks + partialTicks) * 0.01F;
        collector.submitCustomGeometry(mStack, BEAM_TYPE2, (stackLast, builder) -> {
            Matrix4f lastMatrix = stackLast.pose();
            float f4 = 0.0F;
            float f5 = 0.75F;
            float f6 = 0.0F;

            for (int j = 1; j <= 8; ++j) {
                float rSin = Mth.sin((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
                float rCos = Mth.cos((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
                float indexDecimal = (float) j / 8.0F;
                builder.addVertex(lastMatrix, f4 * 0.2F, f5 * 0.2F, 0.0F).setColor(1F, 1F, 1F, alpha).setUv(f6, f2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, f4, f5, distance).setColor(1F, 1F, 1F, alpha).setUv(f6, f3).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin, rCos, distance).setColor(1F, 1F, 1F, alpha).setUv(indexDecimal, f3).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin * 0.2F, rCos * 0.2F, 0.0F).setColor(1F, 1F, 1F, alpha).setUv(indexDecimal, f2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                f4 = rSin;
                f5 = rCos;
                f6 = indexDecimal;
            }
        });
        mStack.popPose();
    }

    public static void renderChargingBeam(float crystalRelX, float crystalRelY, float crystalRelZ, float partialTicks, int animTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, float alpha) {
        float xzDistance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelZ * crystalRelZ);
        float distance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ);
        mStack.pushPose();
        mStack.translate(0.0D, 2.0D, 0.0D);
        mStack.mulPose(Axis.YP.rotation((float) (-Math.atan2(crystalRelZ, crystalRelX)) - ((float) Math.PI / 2F)));
        mStack.mulPose(Axis.XP.rotation((float) (-Math.atan2(xzDistance, crystalRelY)) - ((float) Math.PI / 2F)));
        float vMin = ((float) animTicks + partialTicks) * 0.01F;
        float vMax = (Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ) / 32.0F) + (((float) animTicks + partialTicks) * 0.01F);
        collector.submitCustomGeometry(mStack, BEAM_TYPE2, (stackLast, builder) -> {
            Matrix4f lastMatrix = stackLast.pose();
            float f4 = 0.0F;
            float f5 = 0.1F;
            float texU = 0.0F;

            float taperOffset = 10F;//0.2F;

            for (int j = 1; j <= 8; ++j) {
    //            int shell = j / 8;
                float rSin = Mth.sin((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.1F;
                float rCos = Mth.cos((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.1F;
                float indexDecimal = (float) j / 8.0F;
                builder.addVertex(lastMatrix, f4 * taperOffset, f5 * taperOffset, 0.0F).setColor(1F, 1F, 1F, alpha).setUv(texU, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, f4, f5, distance).setColor(1F, 1F, 1F, alpha).setUv(texU, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin, rCos, distance).setColor(1F, 1F, 1F, alpha).setUv(indexDecimal, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin * taperOffset, rCos * taperOffset, 0.0F).setColor(1F, 1F, 1F, alpha).setUv(indexDecimal, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                f4 = rSin;
                f5 = rCos;
                texU = indexDecimal;
            }
        });
        mStack.popPose();
    }

    //Original
    public static void renderShaderBeam(float crystalRelX, float crystalRelY, float crystalRelZ, float partialTicks, int animTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight) {
        float xzDistance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelZ * crystalRelZ);
        float distance = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ);
        mStack.pushPose();
        mStack.translate(0.0D, 2.0D, 0.0D);
        mStack.mulPose(Axis.YP.rotation((float) (-Math.atan2(crystalRelZ, crystalRelX)) - ((float) Math.PI / 2F)));
        mStack.mulPose(Axis.XP.rotation((float) (-Math.atan2(xzDistance, crystalRelY)) - ((float) Math.PI / 2F)));
        float f2 = 0.0F - ((float) animTicks + partialTicks) * 0.01F;
        float f3 = Mth.sqrt(crystalRelX * crystalRelX + crystalRelY * crystalRelY + crystalRelZ * crystalRelZ) / 32.0F - ((float) animTicks + partialTicks) * 0.01F;
        collector.submitCustomGeometry(mStack, beamType, (stackLast, builder) -> {
            Matrix4f lastMatrix = stackLast.pose();
            float f4 = 0.0F;
            float f5 = 0.75F;
            float f6 = 0.0F;

            for (int j = 1; j <= 8; ++j) {
                float rSin = Mth.sin((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
                float rCos = Mth.cos((float) j * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
                float indexDecimal = (float) j / 8.0F;
                builder.addVertex(lastMatrix, f4 * 0.2F, f5 * 0.2F, 0.0F).setColor(0, 0, 0, 255).setUv(f6, f2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, f4, f5, distance).setColor(255, 255, 255, 255).setUv(f6, f3).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin, rCos, distance).setColor(255, 255, 255, 255).setUv(indexDecimal, f3).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                builder.addVertex(lastMatrix, rSin * 0.2F, rCos * 0.2F, 0.0F).setColor(0, 0, 0, 255).setUv(indexDecimal, f2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(stackLast, 0.0F, -1.0F, 0.0F);
                f4 = rSin;
                f5 = rCos;
                f6 = indexDecimal;
            }
        });
        mStack.popPose();
    }

    public Identifier getTextureLocation(DraconicGuardianEntity entity) {
        return DRAGON_TEXTURE;
    }

    public static class RenderState extends EntityRenderState {
        public DraconicGuardianEntity guardian;
        public float partialTicks;
    }

    public static class DragonModel {
        private final ModelPart head;
        private final ModelPart neck;
        private final ModelPart jaw;
        private final ModelPart body;
        private final ModelPart leftWing;
        private final ModelPart leftWingTip;
        private final ModelPart leftFrontLeg;
        private final ModelPart leftFrontLegTip;
        private final ModelPart leftFrontFoot;
        private final ModelPart leftRearLeg;
        private final ModelPart leftRearLegTip;
        private final ModelPart leftRearFoot;
        private final ModelPart rightWing;
        private final ModelPart rightWingTip;
        private final ModelPart rightFrontLeg;
        private final ModelPart rightFrontLegTip;
        private final ModelPart rightFrontFoot;
        private final ModelPart rightRearLeg;
        private final ModelPart rightRearLegTip;
        private final ModelPart rightRearFoot;
        @Nullable
        private DraconicGuardianEntity entity;
        private float a;

        public DragonModel(ModelPart modelPart) {
            this.head = modelPart.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.neck = modelPart.getChild("neck");
            this.body = modelPart.getChild("body");
            this.leftWing = modelPart.getChild("left_wing");
            this.leftWingTip = this.leftWing.getChild("left_wing_tip");
            this.leftFrontLeg = modelPart.getChild("left_front_leg");
            this.leftFrontLegTip = this.leftFrontLeg.getChild("left_front_leg_tip");
            this.leftFrontFoot = this.leftFrontLegTip.getChild("left_front_foot");
            this.leftRearLeg = modelPart.getChild("left_hind_leg");
            this.leftRearLegTip = this.leftRearLeg.getChild("left_hind_leg_tip");
            this.leftRearFoot = this.leftRearLegTip.getChild("left_hind_foot");
            this.rightWing = modelPart.getChild("right_wing");
            this.rightWingTip = this.rightWing.getChild("right_wing_tip");
            this.rightFrontLeg = modelPart.getChild("right_front_leg");
            this.rightFrontLegTip = this.rightFrontLeg.getChild("right_front_leg_tip");
            this.rightFrontFoot = this.rightFrontLegTip.getChild("right_front_foot");
            this.rightRearLeg = modelPart.getChild("right_hind_leg");
            this.rightRearLegTip = this.rightRearLeg.getChild("right_hind_leg_tip");
            this.rightRearFoot = this.rightRearLegTip.getChild("right_hind_foot");
        }

        public void prepareMobModel(DraconicGuardianEntity p_114269_, float p_114270_, float p_114271_, float p_114272_) {
            this.entity = p_114269_;
            this.a = p_114272_;
        }

        public void renderToBuffer(PoseStack p_114281_, VertexConsumer p_114282_, int p_114283_, int p_114284_, int colour) {
            p_114281_.pushPose();
            float f = Mth.lerp(this.a, this.entity.oFlapTime, this.entity.flapTime);
            this.jaw.xRot = (float) (Math.sin(f * ((float) Math.PI * 2F)) + 1.0D) * 0.2F;
            float f1 = (float) (Math.sin(f * ((float) Math.PI * 2F) - 1.0F) + 1.0D);
            f1 = (f1 * f1 + f1 * 2.0F) * 0.05F;
            p_114281_.translate(0.0D, f1 - 2.0F, -3.0D);
            p_114281_.mulPose(Axis.XP.rotationDegrees(f1 * 2.0F));
            float f2 = 0.0F;
            float f3 = 20.0F;
            float f4 = -12.0F;
            float f5 = 1.5F;
            double[] adouble = this.entity.getLatencyPos(6, this.a);
            float f6 = Mth.wrapDegrees((float) this.entity.getLatencyPos(5, this.a)[0] - (float) this.entity.getLatencyPos(10, this.a)[0]);
            float f7 = Mth.wrapDegrees((float) this.entity.getLatencyPos(5, this.a)[0] + (f6 / 2.0F));
            float f8 = f * ((float) Math.PI * 2F);

            for (int i = 0; i < 5; ++i) {
                double[] adouble1 = this.entity.getLatencyPos(5 - i, this.a);
                float f9 = (float) Math.cos((float) i * 0.45F + f8) * 0.15F;
                this.neck.yRot = Mth.wrapDegrees((float) (adouble1[0] - adouble[0])) * ((float) Math.PI / 180F) * 1.5F;
                this.neck.xRot = f9 + this.entity.getHeadPartYOffset(i, adouble, adouble1) * ((float) Math.PI / 180F) * 1.5F * 5.0F;
                this.neck.zRot = -Mth.wrapDegrees((float) (adouble1[0] - (double) f7)) * ((float) Math.PI / 180F) * 1.5F;
                this.neck.y = f3;
                this.neck.z = f4;
                this.neck.x = f2;
                f3 += Mth.sin(this.neck.xRot) * 10.0F;
                f4 -= Mth.cos(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                f2 -= Mth.sin(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                this.neck.render(p_114281_, p_114282_, p_114283_, p_114284_, colour);
            }

            this.head.y = f3;
            this.head.z = f4;
            this.head.x = f2;
            double[] adouble2 = this.entity.getLatencyPos(0, this.a);
            this.head.yRot = Mth.wrapDegrees((float) (adouble2[0] - adouble[0])) * ((float) Math.PI / 180F);
            this.head.xRot = Mth.wrapDegrees(this.entity.getHeadPartYOffset(6, adouble, adouble2)) * ((float) Math.PI / 180F) * 1.5F * 5.0F;
            this.head.zRot = -Mth.wrapDegrees((float) (adouble2[0] - (double) f7)) * ((float) Math.PI / 180F);
            this.head.render(p_114281_, p_114282_, p_114283_, p_114284_, colour);
            p_114281_.pushPose();
            p_114281_.translate(0.0D, 1.0D, 0.0D);
            p_114281_.mulPose(Axis.ZP.rotationDegrees(-f6 * 1.5F));
            p_114281_.translate(0.0D, -1.0D, 0.0D);
            this.body.zRot = 0.0F;
            this.body.render(p_114281_, p_114282_, p_114283_, p_114284_, colour);
            float f10 = f * ((float) Math.PI * 2F);
            this.leftWing.xRot = 0.125F - (float) Math.cos(f10) * 0.2F;
            this.leftWing.yRot = -0.25F;
            this.leftWing.zRot = -((float) (Math.sin(f10) + 0.125D)) * 0.8F;
            this.leftWingTip.zRot = (float) (Math.sin(f10 + 2.0F) + 0.5D) * 0.75F;
            this.rightWing.xRot = this.leftWing.xRot;
            this.rightWing.yRot = -this.leftWing.yRot;
            this.rightWing.zRot = -this.leftWing.zRot;
            this.rightWingTip.zRot = -this.leftWingTip.zRot;
            this.renderSide(p_114281_, p_114282_, p_114283_, p_114284_, f1, this.leftWing, this.leftFrontLeg, this.leftFrontLegTip, this.leftFrontFoot, this.leftRearLeg, this.leftRearLegTip, this.leftRearFoot, colour);
            this.renderSide(p_114281_, p_114282_, p_114283_, p_114284_, f1, this.rightWing, this.rightFrontLeg, this.rightFrontLegTip, this.rightFrontFoot, this.rightRearLeg, this.rightRearLegTip, this.rightRearFoot, colour);
            p_114281_.popPose();
            float f11 = -Mth.sin(f * ((float) Math.PI * 2F)) * 0.0F;
            f8 = f * ((float) Math.PI * 2F);
            f3 = 10.0F;
            f4 = 60.0F;
            f2 = 0.0F;
            adouble = this.entity.getLatencyPos(11, this.a);

            for (int j = 0; j < 12; ++j) {
                adouble2 = this.entity.getLatencyPos(12 + j, this.a);
                f11 += Mth.sin((float) j * 0.45F + f8) * 0.05F;
                this.neck.yRot = (Mth.wrapDegrees((float) (adouble2[0] - adouble[0])) * 1.5F + 180.0F) * ((float) Math.PI / 180F);
                this.neck.xRot = f11 + (float) (adouble2[1] - adouble[1]) * ((float) Math.PI / 180F) * 1.5F * 5.0F;
                this.neck.zRot = Mth.wrapDegrees((float) (adouble2[0] - (double) f7)) * ((float) Math.PI / 180F) * 1.5F;
                this.neck.y = f3;
                this.neck.z = f4;
                this.neck.x = f2;
                f3 += Mth.sin(this.neck.xRot) * 10.0F;
                f4 -= Mth.cos(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                f2 -= Mth.sin(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                this.neck.render(p_114281_, p_114282_, p_114283_, p_114284_, colour);
            }

            p_114281_.popPose();
        }

        private void renderSide(PoseStack p_173978_, VertexConsumer p_173979_, int p_173980_, int p_173981_, float p_173982_, ModelPart p_173983_, ModelPart p_173984_, ModelPart p_173985_, ModelPart p_173986_, ModelPart p_173987_, ModelPart p_173988_, ModelPart p_173989_, int colour) {
            p_173987_.xRot = 1.0F + p_173982_ * 0.1F;
            p_173988_.xRot = 0.5F + p_173982_ * 0.1F;
            p_173989_.xRot = 0.75F + p_173982_ * 0.1F;
            p_173984_.xRot = 1.3F + p_173982_ * 0.1F;
            p_173985_.xRot = -0.5F - p_173982_ * 0.1F;
            p_173986_.xRot = 0.75F + p_173982_ * 0.1F;
            p_173983_.render(p_173978_, p_173979_, p_173980_, p_173981_, colour);
            p_173984_.render(p_173978_, p_173979_, p_173980_, p_173981_, colour);
            p_173987_.render(p_173978_, p_173979_, p_173980_, p_173981_, colour);
        }
    }
}
