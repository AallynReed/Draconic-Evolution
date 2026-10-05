package com.brandon3055.draconicevolution.client.model;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Translation;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.model.ExtendedModelPart;
import com.brandon3055.brandonscore.client.render.EquippedItemModel;
import com.brandon3055.brandonscore.client.shader.BCShaders;
import com.brandon3055.brandonscore.client.shader.BCRenderType;
import com.brandon3055.brandonscore.handlers.contributor.ContributorHandler;
import com.brandon3055.brandonscore.handlers.contributor.ContributorProperties;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.client.render.item.ToolRenderBase;
import com.brandon3055.brandonscore.client.shader.BCShader;
import com.brandon3055.draconicevolution.client.shader.ShieldShader;
import com.brandon3055.draconicevolution.client.shader.ToolShader;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 13/11/2022
 */
public class ModularChestpieceModel<T extends HumanoidRenderState> extends HumanoidModel<T> implements EquippedItemModel {
    private static final PoseStack IDENTITY = new PoseStack();
    private static final RenderPipeline BASE_PIPELINE = DEShaders.TOOL_BASE_SHADER.pipeline("chestpiece_base", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline CHAOS_PIPELINE = BCShaders.CHAOS_ENTITY_SHADER.pipeline("de_chestpiece_chaos", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline GEM_PIPELINE = DEShaders.TOOL_GEM_SHADER.pipeline("chestpiece_gem", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline CORE_GEM_PIPELINE = DEShaders.CHESTPIECE_GEM_SHADER.pipeline("chestpiece_core_gem", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final RenderPipeline SHIELD_PIPELINE = DEShaders.CHESTPIECE_SHIELD_SHADER.pipeline("armor_shield", builder -> builder.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.TRIANGLES));
    private static final float BABY_Y_HEAD_OFFSET = 16.0F;
    private static final float BABY_Z_HEAD_OFFSET = 0.0F;
    private static final float BABY_HEAD_SCALE = 2.0F;
    private static final float BABY_BODY_SCALE = 2.0F;
    private static final float BODY_Y_OFFSET = 24.0F;
    private static int submitOrder;

    private final TechLevel techLevel;
    private int shieldColour;
    private float shieldState;
    private final ExtendedModelPart extHead;
    private final ExtendedModelPart extBody;
    private final ExtendedModelPart extLeftArm;
    private final ExtendedModelPart extRightArm;
    private final ExtendedModelPart extLeftLeg;
    private final ExtendedModelPart extRightLeg;

    public ModularChestpieceModel(TechLevel techLevel, boolean isOnArmor) {
        super(createMesh(new CubeDeformation(1), 0).getRoot().bake(64, 64));
        this.techLevel = techLevel;
        Map<String, CCModel> model = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/item/equipment/chestpeice.obj")).ignoreMtl().parse();
        CCModel baseModel = model.get("base_model").backfacedCopy();
        CCModel materialModel = model.get("chevrons").backfacedCopy();
        CCModel gemModel = model.get("power_crystals").backfacedCopy();
        CCModel coreGemModel = model.get("crystal_core").backfacedCopy();

        CCModel shieldHeadModel = model.get("shield_head").backfacedCopy();
        CCModel shieldBodyModel = model.get("shield_body").backfacedCopy();
        CCModel shieldRightArmModel = model.get("shield_right_arm").backfacedCopy();
        CCModel shieldLeftArmModel = model.get("shield_left_arm").backfacedCopy();
        CCModel shieldRightLegModel = model.get("shield_right_leg").backfacedCopy();
        CCModel shieldLeftLegModel = model.get("shield_left_leg").backfacedCopy();

        if (isOnArmor) {
            materialModel.apply(new Translation(0, 0, -0.0625));
            gemModel.apply(new Translation(0, 0, -0.0625 / 2));
            coreGemModel.apply(new Translation(0, 0, -0.0625 / 2));
        }

        String levelName = techLevel.name().toLowerCase(Locale.ROOT);
        BCRenderType baseType = DEShaders.TOOL_BASE_SHADER.renderType(MODID + ":base", RenderSetup.builder(BASE_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/" + levelName + "_chestpeice.png"))
                .useLightmap()
                .useOverlay()
                .affectsCrumbling()
                .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                .bufferSize(256)
                .createRenderSetup()
        );

        BCRenderType chaoticType = BCShaders.CHAOS_ENTITY_SHADER.renderType(MODID + ":tool_chaos", RenderSetup.builder(CHAOS_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/chaos_shader.png"), () -> RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        BCRenderType gemType = DEShaders.TOOL_GEM_SHADER.renderType(MODID + ":tool_gem", RenderSetup.builder(GEM_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/shader_fallback_" + levelName + ".png"))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        BCRenderType coreGemType = DEShaders.CHESTPIECE_GEM_SHADER.renderType(MODID + ":core_gem", RenderSetup.builder(CORE_GEM_PIPELINE)
                .withTexture("Sampler0", Identifier.fromNamespaceAndPath(MODID, "textures/item/equipment/shader_fallback_" + levelName + ".png"))
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        BCRenderType shieldType = DEShaders.CHESTPIECE_SHIELD_SHADER.renderType(MODID + ":armor_shield", RenderSetup.builder(SHIELD_PIPELINE)
                .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .useLightmap()
                .useOverlay()
                .bufferSize(256)
                .createRenderSetup()
        );

        ExtendedModelPart body = new ExtendedModelPart();
        if (!isOnArmor) {
            body.addChild(new ChestpieceModelPart(baseModel, baseType, DEShaders.TOOL_BASE_SHADER));
        }
        if (techLevel == TechLevel.CHAOTIC) {
            body.addChild(new ChestpieceModelPart(materialModel, chaoticType, BCShaders.CHAOS_ENTITY_SHADER));
        } else {
            body.addChild(new ChestpieceModelPart(materialModel, baseType, DEShaders.TOOL_BASE_SHADER));
        }
        body.addChild(new ChestpieceModelPart(gemModel, gemType, DEShaders.TOOL_GEM_SHADER));
        body.addChild(new CoreGemModelPart(coreGemModel, coreGemType, DEShaders.CHESTPIECE_GEM_SHADER));
        this.extBody = body;

        extHead = new ShieldModelPart(shieldHeadModel, shieldType, DEShaders.CHESTPIECE_SHIELD_SHADER);
        body.addChild(new ShieldModelPart(shieldBodyModel, shieldType, DEShaders.CHESTPIECE_SHIELD_SHADER));
        extLeftArm = new ShieldModelPart(shieldLeftArmModel, shieldType, DEShaders.CHESTPIECE_SHIELD_SHADER);
        extRightArm = new ShieldModelPart(shieldRightArmModel, shieldType, DEShaders.CHESTPIECE_SHIELD_SHADER);
        extLeftLeg = new ShieldModelPart(shieldLeftLegModel, shieldType, DEShaders.CHESTPIECE_SHIELD_SHADER);
        extRightLeg = new ShieldModelPart(shieldRightLegModel, shieldType, DEShaders.CHESTPIECE_SHIELD_SHADER);
    }

    protected Iterable<ExtendedModelPart> headParts() {
        return ImmutableList.of(extHead);
    }

    protected Iterable<ExtendedModelPart> bodyParts() {
        return ImmutableList.of(extBody, extLeftArm, extRightArm, extLeftLeg, extRightLeg);
    }

    private static void copyPose(ModelPart from, ModelPart to) {
        to.visible = from.visible;
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
    }

    private static OrderedSubmitNodeCollector nextOrder(SubmitNodeCollector collector) {
        return collector.order(submitOrder++);
    }

    @Override
    public void render(LivingEntity entity, PoseStack poseStack, SubmitNodeCollector collector, ItemStack stack, int packedLight, int packedOverlay, float partialTicks) {
        shieldColour = 0xFFFFFFFF;
        shieldState = 0;
        try (ModuleHost host = DECapabilities.getHost(stack)) {
            if (!stack.isEmpty() && host != null) {
                ShieldControlEntity shieldControl = host.getEntitiesByType(ModuleTypes.SHIELD_CONTROLLER).map(e -> (ShieldControlEntity) e).findAny().orElse(null);
                if (shieldControl != null) {
                    shieldState = shieldControl.getShieldState();
                    shieldColour = shieldControl.getShieldColour() | 0xFF000000;
                    if (entity instanceof Player player) {
                        ContributorProperties props = ContributorHandler.getProps(player);
                        if (props.hasShieldRGB() && props.getConfig().overrideShield()) {
                            shieldColour = props.getConfig().getShieldColour(partialTicks);
                        }
                    }
                }
            }
        }

        copyPose(head, extHead);
        copyPose(body, extBody);
        copyPose(leftArm, extLeftArm);
        copyPose(rightArm, extRightArm);
        copyPose(leftLeg, extLeftLeg);
        copyPose(rightLeg, extRightLeg);
        submitOrder = 1;

        if (entity.isBaby()) {
            poseStack.pushPose();
            float f = 1.5F / BABY_HEAD_SCALE;
            poseStack.scale(f, f, f);

            poseStack.translate(0.0D, BABY_Y_HEAD_OFFSET / 16.0F, BABY_Z_HEAD_OFFSET / 16.0F);
            this.headParts().forEach(part -> part.render(poseStack, collector, packedLight, packedOverlay));
            poseStack.popPose();
            poseStack.pushPose();
            float f1 = 1.0F / BABY_BODY_SCALE;
            poseStack.scale(f1, f1, f1);
            poseStack.translate(0.0D, BODY_Y_OFFSET / 16.0F, 0.0D);
            this.bodyParts().forEach(part -> part.render(poseStack, collector, packedLight, packedOverlay));
            poseStack.popPose();
        } else {
            this.headParts().forEach(part -> part.render(poseStack, collector, packedLight, packedOverlay));
            this.bodyParts().forEach(part -> part.render(poseStack, collector, packedLight, packedOverlay));
        }
    }

    public class ChestpieceModelPart extends ExtendedModelPart {
        protected final CCModel model;
        protected final BCRenderType renderType;
        protected final BCShader<?> shader;

        public ChestpieceModelPart(CCModel model, BCRenderType baseType, BCShader<?> shader) {
            this.model = model;
            this.shader = shader;
            this.renderType = baseType;
        }

        protected void submit(SubmitNodeCollector collector) {
            nextOrder(collector).submitCustomGeometry(IDENTITY, renderType.withCurrentUniforms(), (pose, consumer) -> {
                CCRenderState ccrs = CCRenderState.instance();
                ccrs.reset();
                ccrs.bind(consumer, DefaultVertexFormat.ENTITY);
                model.render(ccrs);
            });
        }

        @Override
        public void render(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, float r, float g, float b, float a) {
            if (this.visible) {
                poseStack.pushPose();
                this.translateAndRotate(poseStack);
                Matrix4 mat = new Matrix4(poseStack);
                ToolRenderBase.glUniformBaseColor(shader, techLevel, 1F);
                shader.getModelMatUniform().glUniformMatrix4f(mat);
                submit(collector);

                poseStack.popPose();
            }
        }
    }

    public class CoreGemModelPart extends ChestpieceModelPart {
        private final ToolShader shader;

        public CoreGemModelPart(CCModel model, BCRenderType baseType, ToolShader shader) {
            super(model, baseType, shader);
            this.shader = shader;
        }

        @Override
        public void render(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, float r, float g, float b, float a) {
            if (this.visible) {
                poseStack.pushPose();
                this.translateAndRotate(poseStack);
                Matrix4 mat = new Matrix4(poseStack);
                int color = shieldColour;
                shader.getBaseColorUniform().glUniform4f(((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F, ((color >> 24) & 0xFF) / 255F);
                shader.getModelMatUniform().glUniformMatrix4f(mat);
                submit(collector);

                poseStack.popPose();
            }
        }
    }

    public class ShieldModelPart extends ChestpieceModelPart {
        private final ShieldShader shader;

        public ShieldModelPart(CCModel model, BCRenderType baseType, ShieldShader shader) {
            super(model, baseType, shader);
            this.shader = shader;
        }

        @Override
        public void render(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, float r, float g, float b, float a) {
            if (shieldState > 0) {
                poseStack.pushPose();
                this.translateAndRotate(poseStack);
                Matrix4 mat = new Matrix4(poseStack);
                int color = shieldColour;
                float state = shieldState;
                shader.getBaseColourUniform().glUniform4f(((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F, ((color >> 24) & 0xFF) / 255F);
                shader.getActivationUniform().glUniform1f(state);
                shader.getModelMatUniform().glUniformMatrix4f(mat);
                submit(collector);

                poseStack.popPose();
            }
        }
    }
}
