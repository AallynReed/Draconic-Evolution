package com.brandon3055.draconicevolution.client.render.entity;

import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class GuardianCrystalRenderer extends EntityRenderer<GuardianCrystalEntity, GuardianCrystalRenderer.RenderState> {
    private static Identifier ENDER_CRYSTAL_TEXTURES = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/entity/guardian_crystal.png");
    private static RenderType RENDER_TYPE = RenderTypes.entityCutout(ENDER_CRYSTAL_TEXTURES);
    private final EndCrystalModel model;

    public GuardianCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new EndCrystalModel(context.bakeLayer(ModelLayers.END_CRYSTAL));
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(GuardianCrystalEntity crystal, RenderState state, float partialTicks) {
        super.extractRenderState(crystal, state, partialTicks);
        state.ageInTicks = (float) crystal.time + partialTicks;
        state.showsBottom = crystal.showsBottom();
        state.yBob = getY(crystal, partialTicks);
        state.time = crystal.time;
        state.partialTicks = partialTicks;
        state.shieldPower = crystal.getShieldPower() / (float) Math.max(20, DEConfig.guardianCrystalShield);
        state.beamPower = crystal.getBeamPower();
        state.beamTarget = crystal.getBeamTarget();
        if (state.beamTarget != null) {
            state.xRel = (float) ((double) ((float) state.beamTarget.getX() + 0.5F) - crystal.getX());
            state.yRel = (float) ((double) ((float) state.beamTarget.getY() + 0.5F) - crystal.getY());
            state.zRel = (float) ((double) ((float) state.beamTarget.getZ() + 0.5F) - crystal.getZ());
        }
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int packedLight = state.lightCoords;
        poseStack.pushPose();
        poseStack.scale(2.0F, 2.0F, 2.0F);
        poseStack.translate(0.0D, -0.5D, 0.0D);
        collector.submitModel(this.model, state, poseStack, RENDER_TYPE, packedLight, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        poseStack.popPose();

        float shieldPower = state.shieldPower;
        if (shieldPower > 0) {
            DEShaders.shieldBarMode.glUniform1i(0);
            DEShaders.shieldColour.glUniform4f(1F, 0F, 0F, 1.5F * shieldPower);
            DEShaders.shieldActivation.glUniform1f(1F);

            poseStack.pushPose();
            poseStack.scale(2.0F, 2.0F, 2.0F);
            poseStack.translate(0.0D, -0.5D, 0.0D);
            collector.submitModel(this.model, state, poseStack, DraconicGuardianRenderer.SHIELD_TYPE.withCurrentUniforms(), packedLight, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
            poseStack.popPose();
        }

        if (state.beamTarget != null) {
            float xRel = state.xRel;
            float yRel = state.yRel;
            float zRel = state.zRel;
            poseStack.translate(xRel, yRel - 2, zRel);

            float beamPower = state.beamPower;
            if (beamPower < 1) {
                DraconicGuardianRenderer.renderBeam(-xRel, -yRel + state.yBob + 2, -zRel, state.partialTicks, state.time, poseStack, collector, packedLight, beamPower);
            } else {
                DraconicGuardianRenderer.renderBeam(-xRel, -yRel + state.yBob + 2, -zRel, state.partialTicks, state.time, poseStack, collector, packedLight);
            }
        }

        super.submit(state, poseStack, collector, camera);
    }

    public static float getY(GuardianCrystalEntity crystal, float partialTicks) {
        float f = (float) crystal.time + partialTicks;
        float f1 = Mth.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    @Override
    public boolean shouldRender(GuardianCrystalEntity entity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ) || entity.getBeamTarget() != null;
    }

    public static class RenderState extends EndCrystalRenderState {
        public float yBob;
        public int time;
        public float partialTicks;
        public float shieldPower;
        public float beamPower;
        public @Nullable BlockPos beamTarget;
        public float xRel;
        public float yRel;
        public float zRel;
    }
}
