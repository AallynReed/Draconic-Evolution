package com.brandon3055.draconicevolution.mixin;

import com.brandon3055.brandonscore.client.model.EquippedItemModelLayer;
import com.brandon3055.draconicevolution.init.DEClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by brandon3055 on 4/2/21
 */
@Mixin (CapeLayer.class)
public class CapeLayerMixin {

    @Inject (
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
            at = @At ("HEAD"),
            cancellable = true,
            remap = false
    )
    private void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot, CallbackInfo ci) {
        LivingEntity entity = state.getRenderData(EquippedItemModelLayer.ENTITY);
        if (entity != null && DEClient.deElytraVisible(state.chestEquipment, entity)) {
            ci.cancel();
        }
    }
}
