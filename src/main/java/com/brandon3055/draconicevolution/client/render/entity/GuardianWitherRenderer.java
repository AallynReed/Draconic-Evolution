package com.brandon3055.draconicevolution.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.wither.WitherBossModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WitherArmorLayer;
import net.minecraft.client.renderer.entity.state.WitherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuardianWitherRenderer extends MobRenderer<WitherBoss, WitherRenderState, WitherBossModel> {
   private static final Identifier WITHER_INVULNERABLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither_invulnerable.png");
   private static final Identifier WITHER_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither.png");

   public GuardianWitherRenderer(EntityRendererProvider.Context context) {
      super(context, new WitherBossModel(context.bakeLayer(ModelLayers.WITHER)), 1.0F);
      this.addLayer(new WitherArmorLayer(this, context.getModelSet()));
   }

   protected int getBlockLightLevel(WitherBoss p_225624_1_, BlockPos p_225624_2_) {
      return 15;
   }

   public Identifier getTextureLocation(WitherRenderState state) {
      int i = Mth.floor(state.invulnerableTicks);
      return i > 0 && (i > 80 || i / 5 % 2 != 1) ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
   }

   public WitherRenderState createRenderState() {
      return new WitherRenderState();
   }

   protected void scale(WitherRenderState state, PoseStack p_225620_2_) {
      float f = 2.0F;
      if (state.invulnerableTicks > 0.0F) {
         f -= state.invulnerableTicks / 220.0F * 0.5F;
      }

      p_225620_2_.scale(f, f, f);
   }

   public void extractRenderState(WitherBoss entity, WitherRenderState state, float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      int i = entity.getInvulnerableTicks();
      state.invulnerableTicks = i > 0 ? i - partialTicks : 0.0F;
      System.arraycopy(entity.getHeadXRots(), 0, state.xHeadRots, 0, state.xHeadRots.length);
      System.arraycopy(entity.getHeadYRots(), 0, state.yHeadRots, 0, state.yHeadRots.length);
      state.isPowered = entity.isPowered();
   }
}
