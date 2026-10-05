package com.brandon3055.draconicevolution.mixin;

import com.brandon3055.brandonscore.network.BCoreNetwork;
import com.brandon3055.draconicevolution.entity.projectile.DraconicArrowEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


/**
 * Created by covers1624 on 18/7/21.
 */
@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

    @Final
    @Shadow
    private Entity entity;

    @Redirect(
            method = "sendChanges()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerEntity$Synchronizer;sendToTrackingPlayers(Lnet/minecraft/network/protocol/Packet;)V",
                    ordinal = 1
            )
    )
    public void onVelocity(ServerEntity.Synchronizer consumer, Packet<? super ClientGamePacketListener> t) {
        if (!(entity instanceof DraconicArrowEntity)) {
            consumer.sendToTrackingPlayers(t);
            return;
        }
        consumer.sendToTrackingPlayers((Packet<? super ClientGamePacketListener>) BCoreNetwork.sendEntityVelocity(entity, false));
    }

    @Redirect(
            method = "sendChanges()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerEntity$Synchronizer;sendToTrackingPlayers(Lnet/minecraft/network/protocol/Packet;)V",
                    ordinal = 2
            )
    )
    public void onMovePacket(ServerEntity.Synchronizer consumer, Packet<? super ClientGamePacketListener> t) {
        if (!(t instanceof ClientboundMoveEntityPacket.PosRot) || !(entity instanceof DraconicArrowEntity)) {
            consumer.sendToTrackingPlayers(t);
            return;
        }
        consumer.sendToTrackingPlayers((Packet<? super ClientGamePacketListener>) BCoreNetwork.sendEntityVelocity(entity, true));
    }
}