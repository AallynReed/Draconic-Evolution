package com.brandon3055.draconicevolution.client.render.effect;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FXParticleGroup extends ParticleGroup<Particle> {
    public static final ParticleRenderType GROUP = new ParticleRenderType(DraconicEvolution.MODID + ":fx");
    public static final RenderPipeline ADDITIVE_PIPELINE = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/fx_additive"))
            .withVertexShader("core/rendertype_text")
            .withFragmentShader("core/rendertype_text")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .withCull(false)
            .build();
    private static final Function<Identifier, RenderType> ADDITIVE = Util.memoize(texture -> RenderType.create(DraconicEvolution.MODID + ":fx_additive", RenderSetup.builder(ADDITIVE_PIPELINE)
            .withTexture("Sampler0", texture)
            .useLightmap()
            .createRenderSetup()));

    public FXParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    public static RenderType additive(Identifier texture) {
        return ADDITIVE.apply(texture);
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTicks) {
        List<FXParticle> visible = new ArrayList<>();
        for (Particle particle : particles) {
            if (frustum.isVisible(particle.getBoundingBox())) {
                visible.add((FXParticle) particle);
            }
        }
        return (collector, cameraState) -> {
            PoseStack poseStack = new PoseStack();
            for (FXParticle particle : visible) {
                collector.submitCustomGeometry(poseStack, particle.renderType(), (pose, buffer) -> particle.render(buffer, camera, partialTicks));
            }
        };
    }

    public interface FXParticle {
        RenderType renderType();

        void render(VertexConsumer buffer, Camera camera, float partialTicks);
    }
}
