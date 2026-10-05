package com.brandon3055.draconicevolution.client;

import codechicken.lib.util.ClientUtils;
import com.brandon3055.brandonscore.api.TimeKeeper;
import com.brandon3055.brandonscore.client.render.RenderUtils;
import com.brandon3055.brandonscore.client.shader.BCUniform;
import com.brandon3055.draconicevolution.client.shader.DEShader;
import com.brandon3055.draconicevolution.client.shader.ShieldShader;
import com.brandon3055.draconicevolution.client.shader.ToolShader;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.covers1624.quack.util.CrashLock;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 15/05/2022
 */
public class DEShaders {

    private static final CrashLock LOCK = new CrashLock("Already Initialized");

    public static final VertexFormat BLOCK_FORMAT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV0", VertexFormatElement.UV0)
            .add("UV2", VertexFormatElement.UV2)
            .add("Normal", VertexFormatElement.NORMAL)
            .padding(1)
            .build();

    public static final DEShader reactorShader = new DEShader("reactor", DefaultVertexFormat.POSITION_TEX);
    public static final BCUniform reactorTime = reactorShader.getTimeUniform();
    public static final BCUniform reactorIntensity = reactorShader.uniform("intensity", BCUniform.Type.FLOAT);

    public static final DEShader reactorShieldShader = new DEShader("reactor_shield", DefaultVertexFormat.POSITION_TEX).withVertexShader("reactor");
    public static final BCUniform reactorShieldTime = reactorShieldShader.getTimeUniform();
    public static final BCUniform reactorShieldIntensity = reactorShieldShader.uniform("intensity", BCUniform.Type.FLOAT);

    public static final DEShader chaosBlockShader = new DEShader("chaos_block", BLOCK_FORMAT).withSamplers("Sampler0", "Sampler2");
    public static final BCUniform chaosBlockTime = chaosBlockShader.getTimeUniform();
    public static final BCUniform chaosBlockYaw = chaosBlockShader.uniform("Yaw", BCUniform.Type.FLOAT);
    public static final BCUniform chaosBlockPitch = chaosBlockShader.uniform("Pitch", BCUniform.Type.FLOAT);
    public static final BCUniform chaosBlockAlpha = chaosBlockShader.uniform("Alpha", BCUniform.Type.FLOAT);

    public static final ToolShader TOOL_BASE_SHADER = new ToolShader("tools/tool_base", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler0", "Sampler1", "Sampler2")
            .withDefaults(e -> {
                e.getDisableLightUniform().glUniform1b(false);
                e.getDisableOverlayUniform().glUniform1b(false);
                e.getUv1OverrideUniform().glUniform2i(0, 0);
                e.getUv2OverrideUniform().glUniform2i(0, 0);
            });
    public static final ToolShader TOOL_GEM_SHADER = new ToolShader("tools/tool_gem", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler1", "Sampler2")
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((float) (ClientUtils.getRenderTime() / 20)));
    public static final ToolShader TOOL_TRACE_SHADER = new ToolShader("tools/tool_trace", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler1", "Sampler2")
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((float) (ClientUtils.getRenderTime() / 20)));
    public static final ToolShader TOOL_BLADE_SHADER = new ToolShader("tools/tool_blade", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler1", "Sampler2")
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((float) (ClientUtils.getRenderTime() / 20)));
    public static final ToolShader BOW_STRING_SHADER = new ToolShader("tools/bow_string", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler1", "Sampler2")
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((float) (ClientUtils.getRenderTime() / 20)));
    public static final ToolShader CHESTPIECE_GEM_SHADER = new ToolShader("tools/chestpiece_gem", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler1", "Sampler2")
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((float) (ClientUtils.getRenderTime() / 20)));
    public static final ShieldShader CHESTPIECE_SHIELD_SHADER = new ShieldShader("tools/chestpiece_shield", DefaultVertexFormat.ENTITY)
            .withSamplers("Sampler1", "Sampler2")
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((float) (ClientUtils.getRenderTime() / 20)));

    public static final DEShader shieldShader = new DEShader("shield", DefaultVertexFormat.POSITION_TEX)
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((TimeKeeper.getClientTick() + RenderUtils.partialTick()) / 20F));
    public static final BCUniform shieldTime = shieldShader.getTimeUniform();
    public static final BCUniform shieldActivation = shieldShader.uniform("Activation", BCUniform.Type.FLOAT);
    public static final BCUniform shieldColour = shieldShader.uniform("BaseColour", BCUniform.Type.VEC4);
    public static final BCUniform shieldBarMode = shieldShader.uniform("BarMode", BCUniform.Type.INT);

    public static final DEShader energyCrystalShader = new DEShader("energy_crystal", DefaultVertexFormat.POSITION_TEX)
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((TimeKeeper.getClientTick() + RenderUtils.partialTick()) / 50F));
    public static final BCUniform energyCrystalTime = energyCrystalShader.getTimeUniform();
    public static final BCUniform energyCrystalColour = energyCrystalShader.uniform("Colour", BCUniform.Type.VEC3);
    public static final BCUniform energyCrystalMipmap = energyCrystalShader.uniform("Mipmap", BCUniform.Type.FLOAT);

//    public static CCShaderInstance testShader;
//    public static CCUniform testTime;
//    public static CCUniform testColour;
//    public static CCUniform testInB;
//    public static CCUniform testInC;
//    public static CCUniform testInD;

    public static final DEShader energyCoreShader = new DEShader("energy_core", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((TimeKeeper.getClientTick() + RenderUtils.partialTick()) / 20F));
    public static final BCUniform energyCoreTime = energyCoreShader.getTimeUniform();
    public static final BCUniform energyCoreActivation = energyCoreShader.uniform("Activation", BCUniform.Type.FLOAT);
    public static final BCUniform energyCoreEffectColour = energyCoreShader.uniform("EffectColour", BCUniform.Type.VEC3);
    public static final BCUniform energyCoreFrameColour = energyCoreShader.uniform("FrameColour", BCUniform.Type.VEC3);
    public static final BCUniform energyCoreRotTriColour = energyCoreShader.uniform("InnerTriColour", BCUniform.Type.VEC3);

    public static final DEShader reactorBeamShader = new DEShader("reactor_beam", DefaultVertexFormat.POSITION_TEX_COLOR)
            .onShaderApplied(e -> e.getTimeUniform().glUniform1f((TimeKeeper.getClientTick() + RenderUtils.partialTick()) * 0.02F));
    public static final BCUniform reactorBeamTime = reactorBeamShader.getTimeUniform();
    public static final BCUniform reactorBeamPower = reactorBeamShader.uniform("Power", BCUniform.Type.FLOAT);
    public static final BCUniform reactorBeamFade = reactorBeamShader.uniform("Fade", BCUniform.Type.FLOAT);
    public static final BCUniform reactorBeamStartup = reactorBeamShader.uniform("Startup", BCUniform.Type.FLOAT);
    public static final BCUniform reactorBeamType = reactorBeamShader.uniform("Type", BCUniform.Type.INT);

    public static final RenderPipeline EXPLOSION_FLASH = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(MODID, "pipeline/explosion_flash"))
            .withVertexShader(Identifier.fromNamespaceAndPath(MODID, "core/explosion_flash"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(MODID, "core/explosion_flash"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build();

    public static final DEShader explosionShader = new DEShader("explosion", DefaultVertexFormat.POSITION_TEX);
    public static final BCUniform explosionTime = explosionShader.getTimeUniform();
    public static final BCUniform explosionScale = explosionShader.uniform("Scale", BCUniform.Type.FLOAT);
    public static final BCUniform explosionAlpha = explosionShader.uniform("Alpha", BCUniform.Type.FLOAT);
    public static final BCUniform explosionType = explosionShader.uniform("Type", BCUniform.Type.INT);


    public static void init(IEventBus modBus) {
        LOCK.lock();
        reactorShader.register(modBus);
        reactorShieldShader.register(modBus);
        chaosBlockShader.register(modBus);
        TOOL_BASE_SHADER.register(modBus);
        TOOL_GEM_SHADER.register(modBus);
        TOOL_TRACE_SHADER.register(modBus);
        TOOL_BLADE_SHADER.register(modBus);
        BOW_STRING_SHADER.register(modBus);
        CHESTPIECE_GEM_SHADER.register(modBus);
        CHESTPIECE_SHIELD_SHADER.register(modBus);
        shieldShader.register(modBus);
        energyCrystalShader.register(modBus);
        energyCoreShader.register(modBus);
        reactorBeamShader.register(modBus);
        explosionShader.register(modBus);
        modBus.addListener(DEShaders::onRegisterPipelines);
    }

    private static void onRegisterPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(EXPLOSION_FLASH);
    }

}
