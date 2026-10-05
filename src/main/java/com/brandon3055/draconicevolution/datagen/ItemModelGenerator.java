package com.brandon3055.draconicevolution.datagen;

import codechicken.lib.model.ClassModelLoader;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.blocks.energynet.EnergyCrystal;
import com.brandon3055.draconicevolution.client.render.item.*;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.DEModules;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.EmptyModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 28/2/20.
 */
public class ItemModelGenerator extends ModelProvider {
    private final Map<Item, Supplier<ItemModel.Unbaked>> definitions = new LinkedHashMap<>();
    private ItemModelGenerators itemModels;

    public ItemModelGenerator(PackOutput output) {
        super(output, MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.itemModels = itemModels;
        registerModels();
        definitions.forEach((item, model) -> itemModels.itemModelOutput.accept(item, model.get()));
        getKnownItems().map(Holder::value).filter(item -> !definitions.containsKey(item)).forEach(item -> itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item))));
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    protected void registerModels() {
        //region Block Items
        blockItem(DEContent.GENERATOR, modLoc("block/generator/generator"));
        blockItem(DEContent.GRINDER, modLoc("block/grinder/grinder"));
        blockItem(DEContent.ENERGY_PYLON, modLoc("block/energy_pylon_input"));
//        blockItem(DEContent.BASIC_CRAFTING_INJECTOR); //TODO Why did i have these???
//        blockItem(DEContent.WYVERN_CRAFTING_INJECTOR);
//        blockItem(DEContent.AWAKENED_CRAFTING_INJECTOR);
//        blockItem(DEContent.CHAOTIC_CRAFTING_INJECTOR);
        blockItem(DEContent.CRAFTING_CORE, modLoc("block/crafting/fusion_crafting_core"));
        blockItem(DEContent.BASIC_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_draconium"));
        blockItem(DEContent.WYVERN_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_wyvern"));
        blockItem(DEContent.AWAKENED_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_draconic"));
        blockItem(DEContent.CHAOTIC_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_chaotic"));

        blockItem(DEContent.FLUID_GATE);
        blockItem(DEContent.FLUX_GATE);
        blockItem(DEContent.POTENTIOMETER);

        blockItem(DEContent.DISENCHANTER);
        blockItem(DEContent.ENERGY_TRANSFUSER, modLoc("block/energy_transfuser"));
        blockItem(DEContent.DISLOCATOR_PEDESTAL);
        blockItem(DEContent.DISLOCATOR_RECEPTACLE, modLoc("block/dislocator_receptacle_inactive"));
        blockItem(DEContent.CREATIVE_OP_CAPACITOR);
        blockItem(DEContent.ENTITY_DETECTOR);
        blockItem(DEContent.ENTITY_DETECTOR_ADVANCED);
        blockItem(DEContent.STABILIZED_SPAWNER);
        blockItem(DEContent.CELESTIAL_MANIPULATOR);
        blockItem(DEContent.DRACONIUM_CHEST);
        blockItem(DEContent.PARTICLE_GENERATOR);
        dummyBlock(DEContent.PLACED_ITEM);
        blockItem(DEContent.PORTAL);
        blockItem(DEContent.CHAOS_CRYSTAL);
        blockItem(DEContent.ENERGY_CORE);
        blockItem(DEContent.ENERGY_CORE_STABILIZER);
        blockItem(DEContent.STRUCTURE_BLOCK);
        blockItem(DEContent.REACTOR_CORE);
        blockItem(DEContent.REACTOR_STABILIZER);
        blockItem(DEContent.REACTOR_INJECTOR);
        blockItem(DEContent.RAIN_SENSOR);
        blockItem(DEContent.DISLOCATION_INHIBITOR);
        blockItem(DEContent.OVERWORLD_DRACONIUM_ORE);
        blockItem(DEContent.NETHER_DRACONIUM_ORE);
        blockItem(DEContent.END_DRACONIUM_ORE);
        blockItem(DEContent.DEEPSLATE_DRACONIUM_ORE);
        blockItem(DEContent.DRACONIUM_BLOCK);
        blockItem(DEContent.AWAKENED_DRACONIUM_BLOCK);
        blockItem(DEContent.INFUSED_OBSIDIAN);
        dummyBlock(DEContent.BASIC_IO_CRYSTAL);
        dummyBlock(DEContent.WYVERN_IO_CRYSTAL);
        dummyBlock(DEContent.DRACONIC_IO_CRYSTAL);
//      dummyModel(DEContent.CRYSTAL_IO_CHAOTIC);
        dummyBlock(DEContent.BASIC_RELAY_CRYSTAL);
        dummyBlock(DEContent.WYVERN_RELAY_CRYSTAL);
        dummyBlock(DEContent.DRACONIC_RELAY_CRYSTAL);
//      dummyModel(DEContent.CRYSTAL_RELAY_CHAOTIC);
        dummyBlock(DEContent.BASIC_WIRELESS_CRYSTAL);
        dummyBlock(DEContent.WYVERN_WIRELESS_CRYSTAL);
        dummyBlock(DEContent.DRACONIC_WIRELESS_CRYSTAL);
//      dummyModel(DEContent.CRYSTAL_WIRELESS_CHAOTIC);
        //endregion

        //region Components
        simpleItem(DEContent.DUST_DRACONIUM, "item/components");
        simpleItem(DEContent.DUST_DRACONIUM_AWAKENED, "item/components");
        simpleItem(DEContent.INGOT_DRACONIUM, "item/components");
        simpleItem(DEContent.INGOT_DRACONIUM_AWAKENED, "item/components");
        simpleItem(DEContent.NUGGET_DRACONIUM, "item/components");
        simpleItem(DEContent.NUGGET_DRACONIUM_AWAKENED, "item/components");
        simpleItem(DEContent.CORE_DRACONIUM, "item/components");
        simpleItem(DEContent.CORE_WYVERN, "item/components");
        simpleItem(DEContent.CORE_AWAKENED, "item/components");
        simpleItem(DEContent.CORE_CHAOTIC, "item/components");
        simpleItem(DEContent.ENERGY_CORE_WYVERN, "item/components");
        simpleItem(DEContent.ENERGY_CORE_DRACONIC, "item/components");
        simpleItem(DEContent.ENERGY_CORE_CHAOTIC, "item/components");
        simpleItem(DEContent.DRAGON_HEART, "item/components");
        simpleItem(DEContent.MODULE_CORE, "item/components");
        dummyItem(DEContent.CHAOS_SHARD);
        dummyItem(DEContent.CHAOS_FRAG_SMALL);
        dummyItem(DEContent.CHAOS_FRAG_MEDIUM);
        dummyItem(DEContent.CHAOS_FRAG_LARGE);
        dummyItem(DEContent.REACTOR_PRT_STAB_FRAME);
        dummyItem(DEContent.REACTOR_PRT_IN_ROTOR);
        dummyItem(DEContent.REACTOR_PRT_OUT_ROTOR);
        dummyItem(DEContent.REACTOR_PRT_ROTOR_FULL);
        dummyItem(DEContent.REACTOR_PRT_FOCUS_RING);
        dummyBlock(DEContent.DRACONIUM_CHEST);
        dummyBlock(DEContent.REACTOR_CORE);
        dummyBlock(DEContent.REACTOR_STABILIZER);
        dummyBlock(DEContent.REACTOR_INJECTOR);
        //endregion

        //region Misc
        dummyItem(DEContent.MOB_SOUL);
        simpleItem(DEContent.MAGNET);
        simpleItem(DEContent.MAGNET_ADVANCED);
        simpleItem(DEContent.DISLOCATOR);
        simpleItem(DEContent.DISLOCATOR_ADVANCED);
        simpleItem(DEContent.DISLOCATOR_P2P, modLoc("item/bound_dislocator"));
        simpleItem(DEContent.DISLOCATOR_P2P_UNBOUND, modLoc("item/un_bound_dislocator"));
        simpleItem(DEContent.DISLOCATOR_PLAYER, modLoc("item/bound_dislocator"));
        simpleItem(DEContent.DISLOCATOR_PLAYER_UNBOUND, modLoc("item/bound_dislocator"));
        simpleItem(DEContent.CRYSTAL_BINDER);
        simpleItem(DEContent.INFO_TABLET);
        //endregion

//        File textures = new File("../BrandonsMods/Draconic-Evolution/src/main/resources/assets/draconicevolution/textures");
//        DEModules.moduleItemMap.forEach((module, item) -> {
//            String name = Objects.requireNonNull(module.getRegistryName()).getPath();
//            File moduleTexture = new File(textures, "module/" + name + ".png");
//            if (!moduleTexture.exists()) SneakyUtils.sneaky(() -> FileUtils.copyFile(new File(textures, "item/module/" + module.getModuleTechLevel().name().toLowerCase(Locale.ENGLISH) + ".png"), moduleTexture));
//        });

        DEModules.MODULES.getEntries().stream().filter(e -> e.getId().getNamespace().equals(MODID)).forEach((module) -> {
            String name = Objects.requireNonNull(module.getId()).getPath();
            Identifier baseTexture = Identifier.fromNamespaceAndPath(MODID, "item/module/" + module.get().getModuleTechLevel().name().toLowerCase(Locale.ENGLISH));
            Identifier overlay = Identifier.fromNamespaceAndPath(MODID, "module/" + name);
            multiLayerItem(module.get().getItem(), baseTexture, overlay);
        });

        //region Modular Tools
        simpleItem(DEContent.CAPACITOR_WYVERN, "item/tools");
        simpleItem(DEContent.CAPACITOR_DRACONIC, "item/tools");
        simpleItem(DEContent.CAPACITOR_CHAOTIC, "item/tools");
        simpleItem(DEContent.CAPACITOR_CREATIVE, "item/tools");

        //endregion


        //Custom Item Renderers
        clazz(DEContent.CHAOS_SHARD, RenderItemChaosShard.CHAOS_SHARD.class);
        clazz(DEContent.CHAOS_FRAG_LARGE, RenderItemChaosShard.CHAOS_FRAG_LARGE.class);
        clazz(DEContent.CHAOS_FRAG_MEDIUM, RenderItemChaosShard.CHAOS_FRAG_MEDIUM.class);
        clazz(DEContent.CHAOS_FRAG_SMALL, RenderItemChaosShard.CHAOS_FRAG_SMALL.class);
        clazz(DEContent.MOB_SOUL, RenderItemMobSoul.class);
        clazz(DEContent.ITEM_BASIC_IO_CRYSTAL, RenderItemEnergyCrystal.ITEM_BASIC_IO_CRYSTAL.class);
        clazz(DEContent.ITEM_WYVERN_IO_CRYSTAL, RenderItemEnergyCrystal.ITEM_WYVERN_IO_CRYSTAL.class);
        clazz(DEContent.ITEM_DRACONIC_IO_CRYSTAL, RenderItemEnergyCrystal.ITEM_DRACONIC_IO_CRYSTAL.class);
        clazz(DEContent.ITEM_BASIC_RELAY_CRYSTAL, RenderItemEnergyCrystal.ITEM_BASIC_RELAY_CRYSTAL.class);
        clazz(DEContent.ITEM_WYVERN_RELAY_CRYSTAL, RenderItemEnergyCrystal.ITEM_WYVERN_RELAY_CRYSTAL.class);
        clazz(DEContent.ITEM_DRACONIC_RELAY_CRYSTAL, RenderItemEnergyCrystal.ITEM_DRACONIC_RELAY_CRYSTAL.class);
        clazz(DEContent.ITEM_BASIC_WIRELESS_CRYSTAL, RenderItemEnergyCrystal.ITEM_BASIC_WIRELESS_CRYSTAL.class);
        clazz(DEContent.ITEM_WYVERN_WIRELESS_CRYSTAL, RenderItemEnergyCrystal.ITEM_WYVERN_WIRELESS_CRYSTAL.class);
        clazz(DEContent.ITEM_DRACONIC_WIRELESS_CRYSTAL, RenderItemEnergyCrystal.ITEM_DRACONIC_WIRELESS_CRYSTAL.class);

        clazz(DEContent.ITEM_DRACONIUM_CHEST, RenderItemDraconiumChest.class);

        clazz(DEContent.ITEM_REACTOR_CORE, RenderItemReactorComponent.class);
        clazz(DEContent.ITEM_REACTOR_STABILIZER, RenderItemReactorComponent.class);
        clazz(DEContent.ITEM_REACTOR_INJECTOR, RenderItemReactorComponent.class);
        clazz(DEContent.REACTOR_PRT_STAB_FRAME, RenderItemReactorComponent.class);
        clazz(DEContent.REACTOR_PRT_IN_ROTOR, RenderItemReactorComponent.class);
        clazz(DEContent.REACTOR_PRT_OUT_ROTOR, RenderItemReactorComponent.class);
        clazz(DEContent.REACTOR_PRT_ROTOR_FULL, RenderItemReactorComponent.class);
        clazz(DEContent.REACTOR_PRT_FOCUS_RING, RenderItemReactorComponent.class);

        clazz(DEContent.PICKAXE_WYVERN, RenderModularPickaxe.PICKAXE_WYVERN.class);
        clazz(DEContent.PICKAXE_DRACONIC, RenderModularPickaxe.PICKAXE_DRACONIC.class);
        clazz(DEContent.PICKAXE_CHAOTIC, RenderModularPickaxe.PICKAXE_CHAOTIC.class);

        clazz(DEContent.AXE_WYVERN, RenderModularAxe.AXE_WYVERN.class);
        clazz(DEContent.AXE_DRACONIC, RenderModularAxe.AXE_DRACONIC.class);
        clazz(DEContent.AXE_CHAOTIC, RenderModularAxe.AXE_CHAOTIC.class);

        clazz(DEContent.SHOVEL_WYVERN, RenderModularShovel.SHOVEL_WYVERN.class);
        clazz(DEContent.SHOVEL_DRACONIC, RenderModularShovel.SHOVEL_DRACONIC.class);
        clazz(DEContent.SHOVEL_CHAOTIC, RenderModularShovel.SHOVEL_CHAOTIC.class);

        clazz(DEContent.SWORD_WYVERN, RenderModularSword.SWORD_WYVERN.class);
        clazz(DEContent.SWORD_DRACONIC, RenderModularSword.SWORD_DRACONIC.class);
        clazz(DEContent.SWORD_CHAOTIC, RenderModularSword.SWORD_CHAOTIC.class);

        clazz(DEContent.BOW_WYVERN, RenderModularBow.BOW_WYVERN.class);
        clazz(DEContent.BOW_DRACONIC, RenderModularBow.BOW_DRACONIC.class);
        clazz(DEContent.BOW_CHAOTIC, RenderModularBow.BOW_CHAOTIC.class);

        clazz(DEContent.STAFF_DRACONIC, RenderModularStaff.STAFF_DRACONIC.class);
        clazz(DEContent.STAFF_CHAOTIC, RenderModularStaff.STAFF_CHAOTIC.class);

        clazz(DEContent.HOE_WYVERN, RenderModularHoe.HOE_WYVERN.class);
        clazz(DEContent.HOE_DRACONIC, RenderModularHoe.HOE_DRACONIC.class);
        clazz(DEContent.HOE_CHAOTIC, RenderModularHoe.HOE_CHAOTIC.class);

        clazz(DEContent.CHESTPIECE_WYVERN, RenderModularChestpiece.CHESTPIECE_WYVERN.class);
        clazz(DEContent.CHESTPIECE_DRACONIC, RenderModularChestpiece.CHESTPIECE_DRACONIC.class);
        clazz(DEContent.CHESTPIECE_CHAOTIC, RenderModularChestpiece.CHESTPIECE_CHAOTIC.class);
    }

    protected void simpleItem(DeferredHolder<? extends Item, ? extends Item> item) {
        simpleItem(item, "item");
    }

    protected void simpleItem(DeferredHolder<? extends Item, ? extends Item> item, String textureFolder) {
        Identifier reg = item.getId();
        simpleItem(item, Identifier.fromNamespaceAndPath(reg.getNamespace(), textureFolder + "/" + reg.getPath()));
    }

    protected void simpleItem(DeferredHolder<? extends Item, ? extends Item> item, Identifier texture) {
        Item value = item.get();
        definitions.put(value, () -> ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(value, TextureMapping.layer0(new Material(texture)), itemModels.modelOutput)));
    }

    protected void multiLayerItem(DeferredHolder<? extends Item, ? extends Item> item, Identifier texture, Identifier overlay) {
        multiLayerItem(item.get(), texture, overlay);
    }

    protected void multiLayerItem(Item item, Identifier texture, Identifier overlay) {
        definitions.put(item, () -> ItemModelUtils.plainModel(ModelTemplates.TWO_LAYERED_ITEM.create(item, TextureMapping.layered(new Material(texture), new Material(overlay)), itemModels.modelOutput)));
    }

    protected void blockItem(DeferredHolder<? extends Block, ? extends Block> block) {
        if (block == null) return;
        Identifier reg = block.getId();
        blockItem(block, Identifier.fromNamespaceAndPath(reg.getNamespace(), "block/" + reg.getPath()));
    }

    protected void blockItem(DeferredHolder<? extends Block, ? extends Block> block, Identifier blockModel) {
        if (block == null) return;
        Item item = block.get().asItem();
        if (item == Items.AIR) return;
        definitions.put(item, () -> ItemModelUtils.plainModel(blockModel));
    }

    protected void dummyBlock(DeferredHolder<? extends Block, ? extends Block> block) {
        Item item = block.get().asItem();
        if (item == Items.AIR) return;
        definitions.put(item, EmptyModel.Unbaked::new);
    }

    protected void dummyItem(DeferredHolder<? extends Item, ? extends Item> item) {
        definitions.put(item.get(), EmptyModel.Unbaked::new);
    }

    protected void clazz(DeferredHolder<? extends Item, ? extends Item> item, Class<?> renderer) {
        definitions.put(item.get(), () -> new ClassModelLoader.Unbaked(renderer.getName()));
    }

    protected static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    protected Stream<Item> definedItems() {
        return definitions.keySet().stream();
    }

    @Override
    public String getName() {
        return "Draconic Evolution Item Models";
    }
}
