package com.brandon3055.draconicevolution.client.render.particle;

import com.brandon3055.brandonscore.client.particle.IntParticleData;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by brandon3055 on 2/5/2016.
 * The particle used to render the beams on the Energy Core
 */
public class ParticleLineIndicator extends SingleQuadParticle {
    public static final SingleQuadParticle.Layer PARTICLE_NO_DEPTH_NO_LIGHT = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "pipeline/line_indicator"))
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .build());

    public ParticleLineIndicator(Level worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, TextureAtlasSprite sprite) {
        super((ClientLevel)worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, sprite);
        this.xd = xSpeedIn;
        this.yd = ySpeedIn;
        this.zd = zSpeedIn;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return PARTICLE_NO_DEPTH_NO_LIGHT;
    }

    @Override
    protected int getLightCoords(float p_107249_) {
        return 255;
    }


    public static class Factory implements ParticleProvider<IntParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet p_i50823_1_) {
            this.spriteSet = p_i50823_1_;
        }

        @Override
        public Particle createParticle(IntParticleData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            ParticleLineIndicator particle = new ParticleLineIndicator(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(random));

            List<Integer> list = data.get();
            if (list.size() >= 3) {
                particle.setColor(list.get(0) / 255F, list.get(1) / 255F, list.get(2) / 255F);
            }
            if (list.size() >= 4) {
                particle.setLifetime(list.get(3));
            }

            return particle;
        }
    }
}
