package com.brandon3055.draconicevolution.client.render.item;

import codechicken.lib.math.MathHelper;
import codechicken.lib.model.PerspectiveModelState;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.shader.BCRenderType;
import com.brandon3055.brandonscore.client.shader.BCShader;
import com.brandon3055.brandonscore.client.shader.BCShaders;
import com.brandon3055.brandonscore.client.shader.ChaosEntityShader;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.client.shader.ToolShader;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 22/5/20.
 */
public abstract class ToolRenderBase implements IItemRenderer {

    private static final PoseStack IDENTITY = new PoseStack();
    private static final RenderPipeline BASE_PIPELINE = DEShaders.TOOL_BASE_SHADER.pipeline("tool_base", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline CHAOS_PIPELINE = BCShaders.CHAOS_ENTITY_SHADER.pipeline("de_tool_chaos", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline GEM_PIPELINE = DEShaders.TOOL_GEM_SHADER.pipeline("tool_gem", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline TRACE_PIPELINE = DEShaders.TOOL_TRACE_SHADER.pipeline("tool_trace", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline BLADE_PIPELINE = DEShaders.TOOL_BLADE_SHADER.pipeline("tool_blade", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static int submitOrder;

    protected final TechLevel techLevel;
    protected final String tool;

    public ToolRenderBase(TechLevel techLevel, String tool) {
        this.techLevel = techLevel;
        this.tool = tool;
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        Matrix4 mat = new Matrix4(mStack);
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();
        ccrs.brightness = packedLight;
        ccrs.overlay = packedOverlay;

        DEShaders.TOOL_BASE_SHADER.getUv1OverrideUniform().glUniform2i(packedOverlay & 0xFFFF, (packedOverlay >> 16) & 0xFFFF);
        DEShaders.TOOL_BASE_SHADER.getUv2OverrideUniform().glUniform2i(packedLight & 0xFFFF, (packedLight >> 16) & 0xFFFF);

        submitOrder = 1;
        renderTool(ccrs, stack, transformType, mat, collector, transformType == ItemDisplayContext.GUI);
    }

    public abstract void renderTool(CCRenderState ccrs, ItemStack stack, ItemDisplayContext transform, Matrix4 mat, SubmitNodeCollector collector, boolean gui);

    public void transform(Matrix4 mat, double x, double y, double z, double scale) {
        mat.translate(x, y, z);
        mat.rotate(MathHelper.torad * 90, Vector3.Y_NEG);
        mat.rotate(MathHelper.torad * 45, Vector3.X_POS);
        mat.scale(scale);
    }

    public PerspectiveModelState getModelState() {
        return TransformUtils.DEFAULT_TOOL;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    protected static float[][] baseColours = {
            { 0.0F, 0.5F, 0.8F, 1F },
            { 0.55F, 0.0F, 0.65F, 1F },
            { 0.8F, 0.5F, 0.1F, 1F },
            { 0.75F, 0.05F, 0.05F, 0.2F }
    };

    public static void glUniformBaseColor(BCShader<?> shader, TechLevel techLevel, float pulse) {
        if (!(shader instanceof ToolShader toolShader) || !toolShader.hasBaseColorUniform()) return;
        float[] baseColour = baseColours[techLevel.index];
        float r = baseColour[0];
        float g = baseColour[1];
        float b = baseColour[2];
        float a = baseColour[3];
        switch (techLevel) {
            case DRACONIUM, WYVERN, DRACONIC -> a *= 1F + pulse;
            case CHAOTIC -> {
                r += pulse * 0.2F;
                g += pulse * 0.2F;
                b += pulse * 0.2F;
            }
        }
        toolShader.getBaseColorUniform().glUniform4f(r, g, b, a);
    }

    protected static OrderedSubmitNodeCollector nextOrder(SubmitNodeCollector collector) {
        return collector.order(submitOrder++);
    }

    protected static void submitModel(SubmitNodeCollector collector, BCRenderType type, CCModel model) {
        nextOrder(collector).submitCustomGeometry(IDENTITY, type.withCurrentUniforms(), (pose, consumer) -> {
            CCRenderState ccrs = CCRenderState.instance();
            ccrs.reset();
            ccrs.bind(consumer, DefaultVertexFormat.ENTITY);
            model.render(ccrs);
        });
    }

    //These parts will always be rendered solid using the model texture.
    protected ToolPart basePart(CCModel model) {
        String levelName = techLevel.name().toLowerCase(Locale.ROOT);
        BCRenderType baseType = DEShaders.TOOL_BASE_SHADER.renderType(MODID + ":base", RenderSetup.builder(BASE_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/" + levelName + "_" + tool + ".png"))
                .useLightmap()
                .useOverlay()
                .affectsCrumbling()
                .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                .bufferSize(256)
                .createRenderSetup());

        BCRenderType guiType = DEShaders.TOOL_BASE_SHADER.renderType(MODID + ":base_gui", RenderSetup.builder(BASE_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/" + levelName + "_" + tool + ".png"))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        return new BaseToolPart(model, baseType, guiType, DEShaders.TOOL_BASE_SHADER);
    }

    //These are parts like the head that are made out of the base material and will have the chaos shader applied if tech level is chaos.
    protected ToolPart materialPart(CCModel model) {
        if (techLevel != TechLevel.CHAOTIC) return basePart(model);

        BCRenderType chaoticType = BCShaders.CHAOS_ENTITY_SHADER.renderType(MODID + ":tool_chaos", RenderSetup.builder(CHAOS_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/chaos_shader.png"), () -> RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );
        return new ChaoticToolPart(model, chaoticType, BCShaders.CHAOS_ENTITY_SHADER);
    }

    protected ToolPart gemPart(CCModel model) {
        String levelName = techLevel.name().toLowerCase(Locale.ROOT);
        BCRenderType gemType = DEShaders.TOOL_GEM_SHADER.renderType(MODID + ":tool_gem", RenderSetup.builder(GEM_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/shader_fallback_" + levelName + ".png"))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        return new SimpleToolPart(model, gemType, DEShaders.TOOL_GEM_SHADER);
    }

    //These are the shaded model "inlays" on the handles of most tools
    protected ToolPart tracePart(CCModel model) {
        String levelName = techLevel.name().toLowerCase(Locale.ROOT);
        BCRenderType gemType = DEShaders.TOOL_TRACE_SHADER.renderType(MODID + ":tool_trace", RenderSetup.builder(TRACE_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/shader_fallback_" + levelName + ".png"))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        return new SimpleToolPart(model, gemType, DEShaders.TOOL_TRACE_SHADER);
    }

    protected ToolPart bladePart(CCModel model) {
        String levelName = techLevel.name().toLowerCase(Locale.ROOT);
        BCRenderType gemType = DEShaders.TOOL_BLADE_SHADER.renderType(MODID + ":tool_blade", RenderSetup.builder(BLADE_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/shader_fallback_" + levelName + ".png"))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        return new SimpleToolPart(model, gemType, DEShaders.TOOL_BLADE_SHADER);
    }

    protected abstract static class ToolPart {

        protected final BCShader<?> shader;

        protected ToolPart(BCShader<?> shader) {
            this.shader = shader;
        }

        public final void render(ItemDisplayContext transformType, SubmitNodeCollector collector, Matrix4 mat) {
            render(transformType, collector, mat, 1F);
        }

        public abstract void render(ItemDisplayContext transformType, SubmitNodeCollector collector, Matrix4 mat, float pulse);
    }

    protected static class BaseToolPart extends ToolPart {

        private final CCModel model;
        private final BCRenderType type;
        private final BCRenderType guiType;

        public BaseToolPart(CCModel model, BCRenderType type, BCRenderType guiType, BCShader<?> shader) {
            super(shader);
            this.model = model;
            this.type = type;
            this.guiType = guiType;
        }

        @Override
        public void render(ItemDisplayContext transformType, SubmitNodeCollector collector, Matrix4 mat, float pulse) {
            shader.getModelMatUniform().glUniformMatrix4f(mat);
            submitModel(collector, transformType == ItemDisplayContext.GUI ? guiType : type, model);
        }
    }

    protected class SimpleToolPart extends ToolPart {

        protected final CCModel model;
        protected final BCRenderType type;

        public SimpleToolPart(CCModel model, BCRenderType baseType, BCShader<?> shader) {
            super(shader);
            this.model = model;
            this.type = baseType;
        }

        @Override
        public void render(ItemDisplayContext transformType, SubmitNodeCollector collector, Matrix4 mat, float pulse) {
            glUniformBaseColor(shader, techLevel, pulse);
            shader.getModelMatUniform().glUniformMatrix4f(mat);
            submitModel(collector, type, model);
        }

    }

    protected class ChaoticToolPart extends SimpleToolPart {

        private final ChaosEntityShader shader;

        public ChaoticToolPart(CCModel model, BCRenderType baseType, ChaosEntityShader shader) {
            super(model, baseType, shader);
            this.shader = shader;
        }

        @Override
        public void render(ItemDisplayContext transformType, SubmitNodeCollector collector, Matrix4 mat, float pulse) {
            shader.getDisableLightUniform().glUniform1b(true);
            shader.getDisableOverlayUniform().glUniform1b(true);
            shader.getAlphaUniform().glUniform1f(0.7F);
            shader.getModelMatUniform().glUniformMatrix4f(mat);
            submitModel(collector, type, model);
        }
    }
}
