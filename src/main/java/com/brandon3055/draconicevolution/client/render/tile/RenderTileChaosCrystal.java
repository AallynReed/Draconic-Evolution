package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.util.ClientUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.client.shader.BCRenderType;
import com.brandon3055.draconicevolution.blocks.tileentity.TileChaosCrystal;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.Map;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 24/9/2015.
 */
public class RenderTileChaosCrystal implements DETileRenderer<TileChaosCrystal> {

    private static final BCRenderType CHAOS_CRYSTAL_INNER = DEShaders.chaosBlockShader.renderType(MODID + ":chaos_crystal_inner", RenderSetup.builder(DEShaders.chaosBlockShader.pipeline("chaos_crystal_inner", builder -> builder
                    .withCull(false)
                    .withVertexFormat(DEShaders.BLOCK_FORMAT, Mode.TRIANGLES)))
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/chaos_shader.png"), () -> RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR))
            .useLightmap()
            .bufferSize(256)
            .createRenderSetup());

    private static final RenderType CHAOS_CRYSTAL = RenderType.create(MODID + ":chaos_crystal", RenderSetup.builder(RenderPipeline.builder(RenderPipelines.BLOCK_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(MODID, "pipeline/chaos_crystal"))
                    .withShaderDefine("ALPHA_CUTOUT", 0.5F)
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .withVertexFormat(DefaultVertexFormat.BLOCK, Mode.TRIANGLES)
                    .build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/block/chaos_crystal.png"))
            .useLightmap()
            .bufferSize(256)
            .createRenderSetup());

    private static final BCRenderType CHAOS_CRYSTAL_SHIELD = DEShaders.shieldShader.renderType(MODID + ":chaos_shield_type", RenderSetup.builder(DEShaders.shieldShader.pipeline("chaos_shield", builder -> builder
                    .withCull(false)
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, Mode.TRIANGLES)))
            .bufferSize(256)
            .createRenderSetup());

    private final CCModel model;

    public RenderTileChaosCrystal(BlockEntityRendererProvider.Context context) {
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(MODID, "models/block/chaos_crystal.obj"))
                .ignoreMtl()
                .parse();
        model = CCModel.combine(map.values())
                .backfacedCopy()
                .computeNormals();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public void render(TileChaosCrystal te, float partialTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        if (te.parentPos.notNull()) return;
        Matrix4 mat = new Matrix4(mStack);

        mat.translate(0.5, 0.5, 0.5);
        mat.rotate((ClientEventHandler.elapsedTicks + partialTicks) / 180F, Vector3.Y_POS);
        mat.scale(0.75F);

        Player player = Minecraft.getInstance().player;
        DEShaders.chaosBlockTime.glUniform1f((float) ClientUtils.getRenderTime());
        DEShaders.chaosBlockYaw.glUniform1f((float) (player.getYRot() * MathHelper.torad));
        DEShaders.chaosBlockPitch.glUniform1f((float) -(player.getXRot() * MathHelper.torad));
        collector.cc$submitCCRS(mat, CHAOS_CRYSTAL_INNER.withCurrentUniforms(), (m, ccrs) -> {
            ccrs.brightness = packedLight;
            ccrs.overlay = packedOverlay;
            model.render(ccrs, m);
        });

        collector.cc$submitCCRS(mat, CHAOS_CRYSTAL, (m, ccrs) -> {
            ccrs.brightness = packedLight;
            ccrs.overlay = packedOverlay;
            ccrs.baseColour = 0xFFFFFFF0;
            model.render(ccrs, m);
        });

        if (!te.guardianDefeated.get()) {
            DEShaders.shieldBarMode.glUniform1i(0);
            DEShaders.shieldActivation.glUniform1f(1F);
            DEShaders.shieldColour.glUniform4f(1F, 0F, 0F, 1F);
            collector.cc$submitCCRS(mat, CHAOS_CRYSTAL_SHIELD.withCurrentUniforms(), (m, ccrs) -> {
                ccrs.brightness = packedLight;
                ccrs.overlay = packedOverlay;
                ccrs.baseColour = 0xFFFFFFF0;
                model.render(ccrs, m);
            });
        }
    }

    @Override
    public AABB getRenderBoundingBox(TileChaosCrystal blockEntity) {
        return DETileRenderer.super.getRenderBoundingBox(blockEntity).inflate(3);
    }
}
