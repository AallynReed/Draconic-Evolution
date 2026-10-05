package com.brandon3055.draconicevolution.client.render.entity;

import com.brandon3055.brandonscore.client.model.EquippedItemModelLayer;
import com.brandon3055.draconicevolution.init.DEClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.jetbrains.annotations.Nullable;

/**
 * Renders vanilla elytra wings while DE armour (worn or in a curio slot) can glide.
 */
public class DEWingsLayer<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private final ElytraModel elytraModel;
    private final ElytraModel elytraBabyModel;
    private final EquipmentLayerRenderer equipmentRenderer;

    public DEWingsLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer);
        this.elytraModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA));
        this.elytraBabyModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA_BABY));
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
        LivingEntity entity = state.getRenderData(EquippedItemModelLayer.ENTITY);
        ItemStack stack = state.chestEquipment;
        if (entity == null || !DEClient.deElytraVisible(stack, entity)) return;

        ElytraModel model = state.isBaby ? this.elytraBabyModel : this.elytraModel;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.125F);
        this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.WINGS, EquipmentAssets.ELYTRA, model, state, stack, poseStack, collector, lightCoords, getPlayerElytraTexture(state), state.outlineColor, 0);
        poseStack.popPose();
    }

    private static @Nullable Identifier getPlayerElytraTexture(HumanoidRenderState state) {
        if (state instanceof AvatarRenderState playerState) {
            PlayerSkin skin = playerState.skin;
            if (skin.elytra() != null) {
                return skin.elytra().texturePath();
            }

            if (skin.cape() != null && playerState.showCape) {
                return skin.cape().texturePath();
            }
        }

        return null;
    }
}
