package com.brandon3055.draconicevolution.entity.guardian;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.PartEntity;

public class DraconicGuardianPartEntity extends PartEntity<DraconicGuardianEntity> {
   public final DraconicGuardianEntity dragon;
   public final String name;
   private final EntityDimensions size;

   public DraconicGuardianPartEntity(DraconicGuardianEntity dragon, String name, float width, float height) {
      super(dragon);
      this.size = EntityDimensions.scalable(width, height);
      this.refreshDimensions();
      this.dragon = dragon;
      this.name = name;
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder p_326003_) {

   }

   @Override
   protected void readAdditionalSaveData(ValueInput compound) {

   }

   @Override
   protected void addAdditionalSaveData(ValueOutput compound) {

   }

   @Override
   public boolean isPickable() {
      return true;
   }

   @Override
   public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
      return !this.isInvulnerableToBase(source) && this.dragon.attackEntityPartFrom(level, this, source, amount);
   }

   @Override
   public boolean is(Entity entityIn) {
      return this == entityIn || this.dragon == entityIn;
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
      throw new UnsupportedOperationException();
   }

   @Override
   public EntityDimensions getDimensions(Pose poseIn) {
      return this.size;
   }

   @Override
   public boolean shouldBeSaved() {
      return false;
   }
}
