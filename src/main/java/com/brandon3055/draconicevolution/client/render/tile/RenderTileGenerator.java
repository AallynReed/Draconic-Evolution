package com.brandon3055.draconicevolution.client.render.tile;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.lighting.LightModel;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.IconTransformation;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.machines.Generator;
import com.brandon3055.draconicevolution.blocks.tileentity.TileGenerator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

import java.util.Map;

/**
 * Created by brandon3055 on 1/3/20.
 */
public class RenderTileGenerator implements DETileRenderer<TileGenerator> {

    private static final RenderType MODEL_TYPE = RenderTypes.solidMovingBlock();
    private static final Identifier GEN_TEXTURE = Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "block/generator/generator_2");
    private final CCModel fanModel;

    public RenderTileGenerator(BlockEntityRendererProvider.Context context) {
        Map<String, CCModel> map = new OBJParser(Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/generator/generator_fan.obj")).quads().ignoreMtl().parse();
        fanModel = CCModel.combine(map.values()).backfacedCopy();
    }

    @Override
    public void render(TileGenerator tile, float partialTicks, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, CameraRenderState camera) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, GEN_TEXTURE));
        IconTransformation icon = new IconTransformation(sprite);
        if (icon.icon == null) return;
        Matrix4 mat = new Matrix4(mStack);
        mat.translate(Vector3.CENTER);
        mat.apply(new Rotation(tile.getBlockState().getValue(Generator.FACING).getOpposite().toYRot() * -MathHelper.torad, 0, 1, 0));
        mat.apply(new Scale(0.0625));
        mat.apply(new Rotation((tile.rotation + (tile.rotationSpeed * partialTicks)), 1, 0, 0).at(new Vector3(0, -1.5, -4.5)));

        collector.cc$submitCCRS(mat, MODEL_TYPE, (m, ccrs) -> {
            ccrs.brightness = packedLight;
            ccrs.overlay = packedOverlay;
            fanModel.render(ccrs, LightModel.standardLightModel, icon, m);
        });
    }
}
