package com.brandon3055.draconicevolution.mixin;

import com.brandon3055.brandonscore.client.model.EquippedItemModelLayer;
import com.brandon3055.draconicevolution.client.handler.ModularItemRenderOverrideHandler;
import com.brandon3055.draconicevolution.client.render.item.RenderModularStaff;
import net.covers1624.quack.util.SneakyUtils;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by brandon3055 on 4/2/21
 */
@Mixin(PlayerModel.class)
public class PlayerModelMixin {

    private PlayerModel getThis() {
        return (PlayerModel) (Object) this;
    }

    @Inject(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V",
            at = @At("RETURN"),
            remap = false
    )
    public void afterSetupAnim(AvatarRenderState state, CallbackInfo ci) {
        LivingEntity entity = state.getRenderData(EquippedItemModelLayer.ENTITY);
        if (entity == null) return;
        RenderModularStaff.doMixinStuff(entity, getThis());
        ModularItemRenderOverrideHandler.modifyPlayerPose(entity, getThis());
    }
}
