package com.brandon3055.draconicevolution.client.render.particle;

import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.utils.MathUtils;
import com.brandon3055.draconicevolution.client.AtlasTextureHelper;
import com.brandon3055.draconicevolution.entity.guardian.control.ChargeUpPhase;
import com.brandon3055.draconicevolution.entity.guardian.control.PhaseManager;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class GuardianChargeParticle extends SingleQuadParticle {

    private Vector3 startPos;
    private Vector3 endPos;
    private double angularPos;
    private PhaseManager phaseManager;
    public TextureAtlasSprite sprite = AtlasTextureHelper.ORB_PARTICLE;

    public GuardianChargeParticle(ClientLevel world, Vector3 startPos, Vector3 endPos, double angularPos, int life, PhaseManager phaseManager) {
        super(world, startPos.x, startPos.y, startPos.z, AtlasTextureHelper.ORB_PARTICLE);
        this.startPos = startPos;
        this.endPos = endPos;
        this.angularPos = angularPos;
        this.phaseManager = phaseManager;
        lifetime = life * 4;
        setColor(0.75F, 0F, 0F);
        scale(5);
    }
//
//    @Override
//    public boolean shouldCull() {
//        return false;
//    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (!(phaseManager.getCurrentPhase() instanceof ChargeUpPhase)) {
            alpha -= 0.1;
        }
        if (this.age++ >= this.lifetime || alpha <= 0) {
            this.remove();
        }
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTicks) {
        if (age + partialTicks > lifetime) return;
        ;
        Vec3 vector3d = camera.position();
        float anim = (age + partialTicks) / lifetime;
        Vector3 pos = MathUtils.interpolateVec3(startPos, endPos, anim);
        float radius = (anim * 2) + (Mth.sin(anim * (float) Math.PI) * 5);
        float x = (float) (pos.x - vector3d.x()) + (Mth.sin((float) (angularPos * Math.PI * 2) + anim) * radius);
        float y = (float) (pos.y - vector3d.y());
        float z = (float) (pos.z - vector3d.z()) + (Mth.cos((float) (angularPos * Math.PI * 2) + anim) * radius);
        Quaternionf quaternion;
        if (this.roll == 0.0F) {
            quaternion = camera.rotation();
        } else {
            quaternion = new Quaternionf(camera.rotation());
            float f3 = Mth.lerp(partialTicks, this.oRoll, this.roll);
            quaternion.mul(Axis.ZP.rotation(f3));
        }

        this.extractRotatedQuad(state, quaternion, x, y, z, partialTicks);
    }

    @Override
    protected int getLightCoords(float partialTicks) {
        return 240;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return AtlasTextureHelper.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected float getU0() {
        return sprite.getU0();
    }

    @Override
    protected float getU1() {
        return sprite.getU1();
    }

    @Override
    protected float getV0() {
        return sprite.getV0();
    }

    @Override
    protected float getV1() {
        return sprite.getV1();
    }
}