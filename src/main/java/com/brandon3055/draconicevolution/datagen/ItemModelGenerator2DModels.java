package com.brandon3055.draconicevolution.datagen;

import com.brandon3055.draconicevolution.init.DEContent;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

import java.util.stream.Stream;

/**
 * Created by brandon3055 on 13/11/2024.
 */
public class ItemModelGenerator2DModels extends ItemModelGenerator {

    public ItemModelGenerator2DModels(PackOutput output) {
        super(new PackOutput(output.getOutputFolder().resolve("2d_item_models")));
    }

    @Override
    protected void registerModels() {
        simpleItem(DEContent.SHOVEL_WYVERN, "item/tools");
        simpleItem(DEContent.SHOVEL_DRACONIC, "item/tools");
        simpleItem(DEContent.SHOVEL_CHAOTIC, "item/tools");
        simpleItem(DEContent.PICKAXE_WYVERN, "item/tools");
        simpleItem(DEContent.PICKAXE_DRACONIC, "item/tools");
        simpleItem(DEContent.PICKAXE_CHAOTIC, "item/tools");
        simpleItem(DEContent.HOE_WYVERN, "item/tools");
        simpleItem(DEContent.HOE_DRACONIC, "item/tools");
        simpleItem(DEContent.HOE_CHAOTIC, "item/tools");
        simpleItem(DEContent.AXE_WYVERN, "item/tools");
        simpleItem(DEContent.AXE_DRACONIC, "item/tools");
        simpleItem(DEContent.AXE_CHAOTIC, "item/tools");
        simpleItem(DEContent.BOW_WYVERN, "item/tools");
        simpleItem(DEContent.BOW_DRACONIC, "item/tools");
        simpleItem(DEContent.BOW_CHAOTIC, "item/tools");
        simpleItem(DEContent.SWORD_WYVERN, "item/tools");
        simpleItem(DEContent.SWORD_DRACONIC, "item/tools");
        simpleItem(DEContent.SWORD_CHAOTIC, "item/tools");
        simpleItem(DEContent.STAFF_DRACONIC, "item/tools");
        simpleItem(DEContent.STAFF_CHAOTIC, "item/tools");
        simpleItem(DEContent.CHESTPIECE_WYVERN, "item/tools");
        simpleItem(DEContent.CHESTPIECE_DRACONIC, "item/tools");
        simpleItem(DEContent.CHESTPIECE_CHAOTIC, "item/tools");

    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return definedItems().map(Item::builtInRegistryHolder);
    }

    @Override
    public String getName() {
        return "Draconic Evolution 2D Item Models";
    }
}
