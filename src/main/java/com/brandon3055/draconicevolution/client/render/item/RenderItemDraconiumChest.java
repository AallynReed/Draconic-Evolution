package com.brandon3055.draconicevolution.client.render.item;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.draconicevolution.client.render.tile.DraconiumChestTileRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Created by brandon3055 on 18/04/2017.
 */
public class RenderItemDraconiumChest implements DEItemRenderer {
    public DraconiumChestTileRenderer renderer = new DraconiumChestTileRenderer(null);

    public RenderItemDraconiumChest() {}

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, Identifier.parse("draconicevolution:blocks/draconium_block")));
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext context, PoseStack mStack, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        int colour = 0x640096;
        if (stack.has(BlockBCore.BC_TILE_DATA_TAG)) {
            CompoundTag tag = stack.get(BlockBCore.BC_TILE_DATA_TAG).copyTag();
            if (tag.contains("bc_managed_data")) {
                colour = tag.getCompoundOrEmpty("bc_managed_data").getIntOr("colour", 0);
            }
        }
        renderer.renderChest(mStack, collector, 180, 0, packedLight, packedOverlay, colour);
    }

    @Override
    public ItemTransforms getModelState() {
        return DEItemTransforms.DEFAULT_BLOCK;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }
}
