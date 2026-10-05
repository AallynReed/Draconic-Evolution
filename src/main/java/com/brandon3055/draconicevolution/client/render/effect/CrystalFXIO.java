package com.brandon3055.draconicevolution.client.render.effect;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.energynet.tileentity.TileCrystalBase;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Created by brandon3055 on 29/11/2016.
 */
public class CrystalFXIO extends CrystalFXBase<TileCrystalBase> {

    public CrystalFXIO(ClientLevel worldIn, TileCrystalBase tile) {
        super(worldIn, tile);
        this.age = worldIn.getRandom().nextInt(1024);
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksTillDeath-- <= 0) {
            remove();
        }

        float[] r = {0.0F, 0.8F, 1.0F};
        float[] g = {0.8F, 0.1F, 0.7F};
        float[] b = {1F, 1F, 0.2F};

        rCol = r[tile.getTier()];
        gCol = g[tile.getTier()];
        bCol = b[tile.getTier()];
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (!renderEnabled) {
            return;
        }

        Vec3 viewVec = renderInfo.position();
        float viewX = (float) (this.x - viewVec.x());
        float viewY = (float) (this.y - viewVec.y());
        float viewZ = (float) (this.z - viewVec.z());
        Vector3f[] renderVector = getRenderVectors(renderInfo, viewX, viewY, viewZ, 0.2F);
        buffer.addVertex(renderVector[0].x(), renderVector[0].y(), renderVector[0].z()).setColor(1F, 1F, 1F, 1F).setUv(0.5F, 0.5F).setUv2(240, 240);
        buffer.addVertex(renderVector[1].x(), renderVector[1].y(), renderVector[1].z()).setColor(1F, 1F, 1F, 1F).setUv(0.5F, 0.0F).setUv2(240, 240);
        buffer.addVertex(renderVector[2].x(), renderVector[2].y(), renderVector[2].z()).setColor(1F, 1F, 1F, 1F).setUv(0.0F, 0.0F).setUv2(240, 240);
        buffer.addVertex(renderVector[3].x(), renderVector[3].y(), renderVector[3].z()).setColor(1F, 1F, 1F, 1F).setUv(0.0F, 0.5F).setUv2(240, 240);
    }

    @Override
    public RenderType renderType() {
        return FXParticleGroup.additive(tile.getTier() == 0 ? BASIC_TEXTURE : tile.getTier() == 1 ? WYVERN_TEXTURE : DRACONIC_TEXTURE);
    }

    private static final Identifier BASIC_TEXTURE = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/particle/energy_beam_basic.png");
    private static final Identifier WYVERN_TEXTURE = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/particle/energy_beam_wyvern.png");
    private static final Identifier DRACONIC_TEXTURE = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "textures/particle/energy_beam_draconic.png");
}
