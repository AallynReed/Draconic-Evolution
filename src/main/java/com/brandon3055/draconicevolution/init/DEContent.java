package com.brandon3055.draconicevolution.init;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.worldentity.WorldEntityHandler;
import com.brandon3055.brandonscore.worldentity.WorldEntityType;
import com.brandon3055.draconicevolution.api.DraconicAPI;
import com.brandon3055.draconicevolution.api.crafting.FusionRecipe;
import com.brandon3055.draconicevolution.api.crafting.StackIngredient;
import com.brandon3055.draconicevolution.blocks.*;
import com.brandon3055.draconicevolution.blocks.energynet.EnergyCrystal;
import com.brandon3055.draconicevolution.blocks.energynet.tileentity.TileCrystalDirectIO;
import com.brandon3055.draconicevolution.blocks.energynet.tileentity.TileCrystalRelay;
import com.brandon3055.draconicevolution.blocks.energynet.tileentity.TileCrystalWirelessIO;
import com.brandon3055.draconicevolution.blocks.machines.*;
import com.brandon3055.draconicevolution.blocks.reactor.ReactorComponent;
import com.brandon3055.draconicevolution.blocks.reactor.ReactorCore;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorInjector;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorStabilizer;
import com.brandon3055.draconicevolution.blocks.tileentity.*;
import com.brandon3055.draconicevolution.blocks.tileentity.chest.TileDraconiumChest;
import com.brandon3055.draconicevolution.blocks.tileentity.flowgate.TileFluidGate;
import com.brandon3055.draconicevolution.blocks.tileentity.flowgate.TileFluxGate;
import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.brandon3055.draconicevolution.entity.guardian.GuardianFightManager;
import com.brandon3055.draconicevolution.entity.guardian.GuardianProjectileEntity;
import com.brandon3055.draconicevolution.entity.guardian.GuardianWither;
import com.brandon3055.draconicevolution.entity.projectile.DraconicArrowEntity;
import com.brandon3055.draconicevolution.inventory.*;
import com.brandon3055.draconicevolution.items.InfoTablet;
import com.brandon3055.draconicevolution.items.ItemCore;
import com.brandon3055.draconicevolution.items.MobSoul;
import com.brandon3055.draconicevolution.items.equipment.*;
import com.brandon3055.draconicevolution.items.tools.*;
import com.brandon3055.draconicevolution.world.ChaosIslandFeature;
import com.brandon3055.draconicevolution.world.EnderCometFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

/**
 * Created by brandon3055 on 18/3/2016.
 * This class contains a reference to all blocks and items in Draconic Evolution
 */
public class DEContent {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<WorldEntityType<?>> WORLD_ENTITY_TYPES = DeferredRegister.create(WorldEntityHandler.ENTITY_TYPE, MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIAL = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, MODID);

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, MODID);


    public static void init(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TILES_ENTITIES.register(modBus);
        MENU_TYPES.register(modBus);
        ENTITY_TYPES.register(modBus);
        WORLD_ENTITY_TYPES.register(modBus);
        RECIPE_TYPES.register(modBus);
        RECIPE_SERIAL.register(modBus);
        FEATURES.register(modBus);
        INGREDIENT_TYPES.register(modBus);
        modBus.addListener(DEContent::registerAttributes);
    }

    //#################################################################
    // Blocks
    //#################################################################

    public static final Properties MACHINE = Properties.of().mapColor(MapColor.COLOR_GRAY).sound(SoundType.METAL).strength(3.0F, 8F).noOcclusion().requiresCorrectToolForDrops();
    public static final Properties MACHINE_OPAQUE = Properties.of().mapColor(MapColor.COLOR_GRAY).sound(SoundType.METAL).strength(3.0F, 8F).requiresCorrectToolForDrops();
    public static final Properties HARDENED_MACHINE = Properties.of().mapColor(MapColor.COLOR_GRAY).sound(SoundType.METAL).strength(20.0F, 600F).noOcclusion().requiresCorrectToolForDrops();
    public static final Properties STORAGE_BLOCK = Properties.of().mapColor(MapColor.COLOR_GRAY).sound(SoundType.METAL).strength(30.0F, 600F).requiresCorrectToolForDrops();
    public static final Properties STONE_PROP = Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1.5F, 6F).requiresCorrectToolForDrops();
//    public static final Properties ORE = Properties.of().mapColor(MapColor.COLOR_GRAY).strength(6.0F, 16F).requiresCorrectToolForDrops();
    public static final Properties ORE = Properties.ofFullCopy(Blocks.DIAMOND_ORE);
    //@formatter:off
    //Machines
    public static final DeferredHolder<Block, Generator> GENERATOR                             = BLOCKS.register("generator",                          id -> new Generator(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, EnergyTransfuser> ENERGY_TRANSFUSER              = BLOCKS.register("energy_transfuser",                  id -> new EnergyTransfuser(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DislocatorPedestal> DISLOCATOR_PEDESTAL          = BLOCKS.register("dislocator_pedestal",                id -> new DislocatorPedestal(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DislocatorReceptacle> DISLOCATOR_RECEPTACLE      = BLOCKS.register("dislocator_receptacle",              id -> new DislocatorReceptacle(MACHINE_OPAQUE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, CreativeOPSource> CREATIVE_OP_CAPACITOR          = BLOCKS.register("creative_op_capacitor",              id -> new CreativeOPSource(MACHINE_OPAQUE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, EntityDetector> ENTITY_DETECTOR                  = BLOCKS.register("entity_detector",                    id -> new EntityDetector(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), false));
    public static final DeferredHolder<Block, EntityDetector> ENTITY_DETECTOR_ADVANCED         = BLOCKS.register("entity_detector_advanced",           id -> new EntityDetector(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), true));
    public static final DeferredHolder<Block, StabilizedSpawner> STABILIZED_SPAWNER            = BLOCKS.register("stabilized_spawner",                 id -> new StabilizedSpawner(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DraconiumChest> DRACONIUM_CHEST                  = BLOCKS.register("draconium_chest",                    id -> new DraconiumChest(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, ParticleGenerator> PARTICLE_GENERATOR            = BLOCKS.register("particle_generator",                 id -> new ParticleGenerator(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DislocationInhibitor> DISLOCATION_INHIBITOR      = BLOCKS.register("dislocation_inhibitor",              id -> new DislocationInhibitor(MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    //Stone Type
    public static final DeferredHolder<Block, RainSensor> RAIN_SENSOR                          = BLOCKS.register("rain_sensor",                        id -> new RainSensor(STONE_PROP.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, Potentiometer> POTENTIOMETER                     = BLOCKS.register("potentiometer",                      id -> new Potentiometer(STONE_PROP.setId(ResourceKey.create(Registries.BLOCK, id))));
    //Hardened Machine
    public static final DeferredHolder<Block, Grinder> GRINDER                                 = BLOCKS.register("grinder",                            id -> new Grinder(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, Disenchanter> DISENCHANTER                       = BLOCKS.register("disenchanter",                       id -> new Disenchanter(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, CelestialManipulator> CELESTIAL_MANIPULATOR      = BLOCKS.register("celestial_manipulator",              id -> new CelestialManipulator(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, FlowGate> FLUX_GATE                              = BLOCKS.register("flux_gate",                          id -> new FlowGate(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), true));
    public static final DeferredHolder<Block, FlowGate> FLUID_GATE                             = BLOCKS.register("fluid_gate",                         id -> new FlowGate(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), false));
    //Fusion Crafting
    public static final DeferredHolder<Block, FusionCraftingCore> CRAFTING_CORE                = BLOCKS.register("crafting_core",                      id -> (FusionCraftingCore) (DraconicAPI.CRAFTING_CORE = new FusionCraftingCore(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)))));
    public static final DeferredHolder<Block, CraftingInjector> BASIC_CRAFTING_INJECTOR        = BLOCKS.register("basic_crafting_injector",            id -> new CraftingInjector(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIUM));
    public static final DeferredHolder<Block, CraftingInjector> WYVERN_CRAFTING_INJECTOR       = BLOCKS.register("wyvern_crafting_injector",           id -> new CraftingInjector(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.WYVERN));
    public static final DeferredHolder<Block, CraftingInjector> AWAKENED_CRAFTING_INJECTOR     = BLOCKS.register("awakened_crafting_injector",         id -> new CraftingInjector(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIC));
    public static final DeferredHolder<Block, CraftingInjector> CHAOTIC_CRAFTING_INJECTOR      = BLOCKS.register("chaotic_crafting_injector",          id -> new CraftingInjector(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.CHAOTIC));
    //Energy Core
    public static final DeferredHolder<Block, EnergyCore> ENERGY_CORE                          = BLOCKS.register("energy_core",                        id -> new EnergyCore(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, EnergyCoreStabilizer> ENERGY_CORE_STABILIZER     = BLOCKS.register("energy_core_stabilizer",             id -> new EnergyCoreStabilizer(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, EnergyPylon> ENERGY_PYLON                        = BLOCKS.register("energy_pylon",                       id -> new EnergyPylon(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, StructureBlock> STRUCTURE_BLOCK                  = BLOCKS.register("structure_block",                    id -> new StructureBlock(Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5.0F, 12F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id))));
    //Reactor
    public static final DeferredHolder<Block, ReactorCore> REACTOR_CORE                        = BLOCKS.register("reactor_core",                       id -> new ReactorCore(HARDENED_MACHINE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, ReactorComponent> REACTOR_STABILIZER             = BLOCKS.register("reactor_stabilizer",                 id -> new ReactorComponent(Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5.0F, 6000F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id)), false));
    public static final DeferredHolder<Block, ReactorComponent> REACTOR_INJECTOR               = BLOCKS.register("reactor_injector",                   id -> new ReactorComponent(Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5.0F, 6000F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id)), true));
    //Ore
    public static final DeferredHolder<Block, DraconiumOre> OVERWORLD_DRACONIUM_ORE            = BLOCKS.register("overworld_draconium_ore",            id -> new DraconiumOre(ORE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DraconiumOre> DEEPSLATE_DRACONIUM_ORE            = BLOCKS.register("deepslate_draconium_ore",            id -> new DraconiumOre(ORE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DraconiumOre> NETHER_DRACONIUM_ORE               = BLOCKS.register("nether_draconium_ore",               id -> new DraconiumOre(ORE.setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, DraconiumOre> END_DRACONIUM_ORE                  = BLOCKS.register("end_draconium_ore",                  id -> new DraconiumOre(ORE.setId(ResourceKey.create(Registries.BLOCK, id))));
    //Storage Blocks
    public static final DeferredHolder<Block, DraconiumBlock> DRACONIUM_BLOCK                  = BLOCKS.register("draconium_block",                    id -> (DraconiumBlock) new DraconiumBlock(STORAGE_BLOCK.setId(ResourceKey.create(Registries.BLOCK, id))).setMobResistant().setExplosionResistant());
    public static final DeferredHolder<Block, DraconiumBlock> AWAKENED_DRACONIUM_BLOCK         = BLOCKS.register("awakened_draconium_block",           id -> (DraconiumBlock) new DraconiumBlock(STORAGE_BLOCK.setId(ResourceKey.create(Registries.BLOCK, id))).setMobResistant().setExplosionResistant());
    //Special
    public static final DeferredHolder<Block, Portal> PORTAL                                   = BLOCKS.register("portal",                             id -> new Portal(Properties.of().noOcclusion().noCollision().strength(-1F).setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, ChaosCrystal> CHAOS_CRYSTAL                      = BLOCKS.register("chaos_crystal",                      id -> new ChaosCrystal(Properties.of().strength(100, 4000).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, ChaosCrystal> CHAOS_CRYSTAL_PART                 = BLOCKS.register("chaos_crystal_part",                 id -> new ChaosCrystal(Properties.of().strength(100, 4000).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, BlockBCore> INFUSED_OBSIDIAN                     = BLOCKS.register("infused_obsidian",                   id -> new BlockBCore(Properties.of().mapColor(MapColor.COLOR_BLACK).strength(100.0F, 2400.0F).setId(ResourceKey.create(Registries.BLOCK, id))).setMobResistant().setExplosionResistant());

    public static final DeferredHolder<Block, PlacedItem> PLACED_ITEM                          = BLOCKS.register("placed_item",                        id -> new PlacedItem(Properties.of().strength(5F, 12F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id))));
    public static final DeferredHolder<Block, CometSpawner> COMET_SPAWNER                      = BLOCKS.register("comet_spawner",                      id -> new CometSpawner(Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));

    //Energy Crystals
    public static final Properties CRYSTAL_B = Properties.of().mapColor(DyeColor.BLUE).strength(3.0F, 8F);
    public static final Properties CRYSTAL_W = Properties.of().mapColor(DyeColor.PURPLE).strength(5.0F, 16F);
    public static final Properties CRYSTAL_D = Properties.of().mapColor(DyeColor.ORANGE).strength(8.0F, 32F);
    public static final DeferredHolder<Block, EnergyCrystal> BASIC_IO_CRYSTAL                  = BLOCKS.register("basic_io_crystal",                   id -> new EnergyCrystal(CRYSTAL_B.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIUM, EnergyCrystal.CrystalType.CRYSTAL_IO));
    public static final DeferredHolder<Block, EnergyCrystal> WYVERN_IO_CRYSTAL                 = BLOCKS.register("wyvern_io_crystal",                  id -> new EnergyCrystal(CRYSTAL_W.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.WYVERN, EnergyCrystal.CrystalType.CRYSTAL_IO));
    public static final DeferredHolder<Block, EnergyCrystal> DRACONIC_IO_CRYSTAL               = BLOCKS.register("draconic_io_crystal",                id -> new EnergyCrystal(CRYSTAL_D.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIC, EnergyCrystal.CrystalType.CRYSTAL_IO));
    public static final DeferredHolder<Block, EnergyCrystal> BASIC_RELAY_CRYSTAL               = BLOCKS.register("basic_relay_crystal",                id -> new EnergyCrystal(CRYSTAL_B.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIUM, EnergyCrystal.CrystalType.RELAY));
    public static final DeferredHolder<Block, EnergyCrystal> WYVERN_RELAY_CRYSTAL              = BLOCKS.register("wyvern_relay_crystal",               id -> new EnergyCrystal(CRYSTAL_W.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.WYVERN, EnergyCrystal.CrystalType.RELAY));
    public static final DeferredHolder<Block, EnergyCrystal> DRACONIC_RELAY_CRYSTAL            = BLOCKS.register("draconic_relay_crystal",             id -> new EnergyCrystal(CRYSTAL_D.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIC, EnergyCrystal.CrystalType.RELAY));
    public static final DeferredHolder<Block, EnergyCrystal> BASIC_WIRELESS_CRYSTAL            = BLOCKS.register("basic_wireless_crystal",             id -> new EnergyCrystal(CRYSTAL_B.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIUM, EnergyCrystal.CrystalType.WIRELESS));
    public static final DeferredHolder<Block, EnergyCrystal> WYVERN_WIRELESS_CRYSTAL           = BLOCKS.register("wyvern_wireless_crystal",            id -> new EnergyCrystal(CRYSTAL_W.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.WYVERN, EnergyCrystal.CrystalType.WIRELESS));
    public static final DeferredHolder<Block, EnergyCrystal> DRACONIC_WIRELESS_CRYSTAL         = BLOCKS.register("draconic_wireless_crystal",          id -> new EnergyCrystal(CRYSTAL_D.setId(ResourceKey.create(Registries.BLOCK, id)), TechLevel.DRACONIC, EnergyCrystal.CrystalType.WIRELESS));

    //@formatter:on

    //#################################################################
    // Items
    //#################################################################

    //@formatter:off
    //Components
    public static final DeferredHolder<Item, Item> DUST_DRACONIUM                         = ITEMS.register("draconium_dust",                      id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> DUST_DRACONIUM_AWAKENED                = ITEMS.register("awakened_draconium_dust",             id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> INGOT_DRACONIUM                        = ITEMS.register("draconium_ingot",                     id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> INGOT_DRACONIUM_AWAKENED               = ITEMS.register("awakened_draconium_ingot",            id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> NUGGET_DRACONIUM                       = ITEMS.register("draconium_nugget",                    id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> NUGGET_DRACONIUM_AWAKENED              = ITEMS.register("awakened_draconium_nugget",           id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ItemCore> CORE_DRACONIUM                     = ITEMS.register("draconium_core",                      id -> new ItemCore(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ItemCore> CORE_WYVERN                        = ITEMS.register("wyvern_core",                         id -> new ItemCore(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ItemCore> CORE_AWAKENED                      = ITEMS.register("awakened_core",                       id -> new ItemCore(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ItemCore> CORE_CHAOTIC                       = ITEMS.register("chaotic_core",                        id -> new ItemCore(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> ENERGY_CORE_WYVERN                     = ITEMS.register("wyvern_energy_core",                  id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> ENERGY_CORE_DRACONIC                   = ITEMS.register("draconic_energy_core",                id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> ENERGY_CORE_CHAOTIC                    = ITEMS.register("chaotic_energy_core",                 id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> DRAGON_HEART                           = ITEMS.register("dragon_heart",                        id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> CHAOS_SHARD                            = ITEMS.register("chaos_shard",                         id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> CHAOS_FRAG_LARGE                       = ITEMS.register("large_chaos_frag",                    id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> CHAOS_FRAG_MEDIUM                      = ITEMS.register("medium_chaos_frag",                   id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> CHAOS_FRAG_SMALL                       = ITEMS.register("small_chaos_frag",                    id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> MODULE_CORE                            = ITEMS.register("module_core",                         id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> REACTOR_PRT_STAB_FRAME                 = ITEMS.register("reactor_prt_stab_frame",              id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> REACTOR_PRT_IN_ROTOR                   = ITEMS.register("reactor_prt_in_rotor",                id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> REACTOR_PRT_OUT_ROTOR                  = ITEMS.register("reactor_prt_out_rotor",               id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> REACTOR_PRT_ROTOR_FULL                 = ITEMS.register("reactor_prt_rotor_full",              id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, Item> REACTOR_PRT_FOCUS_RING                 = ITEMS.register("reactor_prt_focus_ring",              id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    //Misc Tools
    public static final DeferredHolder<Item, Magnet> MAGNET                               = ITEMS.register("magnet",                              id -> new Magnet(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id)), 8));
    public static final DeferredHolder<Item, Magnet> MAGNET_ADVANCED                      = ITEMS.register("advanced_magnet",                     id -> new Magnet(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id)), 32));
    public static final DeferredHolder<Item, Dislocator> DISLOCATOR                       = ITEMS.register("dislocator",                          id -> new Dislocator(new Item.Properties().stacksTo(1).durability(31).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, DislocatorAdvanced> DISLOCATOR_ADVANCED      = ITEMS.register("advanced_dislocator",                 id -> new DislocatorAdvanced(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, BoundDislocator> DISLOCATOR_P2P              = ITEMS.register("p2p_dislocator",                      id -> new BoundDislocator(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, BoundDislocator> DISLOCATOR_P2P_UNBOUND      = ITEMS.register("p2p_dislocator_unbound",              id -> new BoundDislocator(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, BoundDislocator> DISLOCATOR_PLAYER           = ITEMS.register("player_dislocator",                   id -> new BoundDislocator(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, BoundDislocator> DISLOCATOR_PLAYER_UNBOUND   = ITEMS.register("player_dislocator_unbound",           id -> new BoundDislocator(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, CrystalBinder> CRYSTAL_BINDER                = ITEMS.register("crystal_binder",                      id -> new CrystalBinder(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, InfoTablet> INFO_TABLET                      = ITEMS.register("info_tablet",                         id -> new InfoTablet(new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, MobSoul> MOB_SOUL                            = ITEMS.register("mob_soul",                            id -> new MobSoul(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    //Tools
    public static final TechProperties WYVERN_TOOLS = (TechProperties) new TechProperties(TechLevel.WYVERN).rarity(Rarity.UNCOMMON).stacksTo(1).fireResistant();
    public static final TechProperties DRACONIC_TOOLS = (TechProperties) new TechProperties(TechLevel.DRACONIC).rarity(Rarity.RARE).stacksTo(1).fireResistant();
    public static final TechProperties CHAOTIC_TOOLS = (TechProperties) new TechProperties(TechLevel.CHAOTIC).rarity(Rarity.EPIC).stacksTo(1).fireResistant();
    public static DETier WYVERN_TIER = new DETier(TechLevel.WYVERN);
    public static DETier DRACONIC_TIER = new DETier(TechLevel.DRACONIC);
    public static DETier CHAOTIC_TIER = new DETier(TechLevel.CHAOTIC);
    public static final DeferredHolder<Item, DraconiumCapacitor> CAPACITOR_WYVERN             = ITEMS.register("wyvern_capacitor",                    id -> new DraconiumCapacitor(WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, DraconiumCapacitor> CAPACITOR_DRACONIC           = ITEMS.register("draconic_capacitor",                  id -> new DraconiumCapacitor(DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, DraconiumCapacitor> CAPACITOR_CHAOTIC            = ITEMS.register("chaotic_capacitor",                   id -> new DraconiumCapacitor(CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, DraconiumCapacitor> CAPACITOR_CREATIVE           = ITEMS.register("creative_capacitor",                  id -> new DraconiumCapacitor(CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularShovel> SHOVEL_WYVERN                     = ITEMS.register("wyvern_shovel",                       id -> new ModularShovel(WYVERN_TIER, WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularShovel> SHOVEL_DRACONIC                   = ITEMS.register("draconic_shovel",                     id -> new ModularShovel(DRACONIC_TIER, DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularShovel> SHOVEL_CHAOTIC                    = ITEMS.register("chaotic_shovel",                      id -> new ModularShovel(CHAOTIC_TIER, CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularHoe> HOE_WYVERN                           = ITEMS.register("wyvern_hoe",                          id -> new ModularHoe(WYVERN_TIER, WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularHoe> HOE_DRACONIC                         = ITEMS.register("draconic_hoe",                        id -> new ModularHoe(DRACONIC_TIER, DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularHoe> HOE_CHAOTIC                          = ITEMS.register("chaotic_hoe",                         id -> new ModularHoe(CHAOTIC_TIER, CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularPickaxe> PICKAXE_WYVERN                   = ITEMS.register("wyvern_pickaxe",                      id -> new ModularPickaxe(WYVERN_TIER, WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularPickaxe> PICKAXE_DRACONIC                 = ITEMS.register("draconic_pickaxe",                    id -> new ModularPickaxe(DRACONIC_TIER, DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularPickaxe> PICKAXE_CHAOTIC                  = ITEMS.register("chaotic_pickaxe",                     id -> new ModularPickaxe(CHAOTIC_TIER, CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularAxe> AXE_WYVERN                           = ITEMS.register("wyvern_axe",                          id -> new ModularAxe(WYVERN_TIER, WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularAxe> AXE_DRACONIC                         = ITEMS.register("draconic_axe",                        id -> new ModularAxe(DRACONIC_TIER, DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularAxe> AXE_CHAOTIC                          = ITEMS.register("chaotic_axe",                         id -> new ModularAxe(CHAOTIC_TIER, CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularBow> BOW_WYVERN                           = ITEMS.register("wyvern_bow",                          id -> new ModularBow(WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularBow> BOW_DRACONIC                         = ITEMS.register("draconic_bow",                        id -> new ModularBow(DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularBow> BOW_CHAOTIC                          = ITEMS.register("chaotic_bow",                         id -> new ModularBow(CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularSword> SWORD_WYVERN                       = ITEMS.register("wyvern_sword",                        id -> new ModularSword(WYVERN_TIER, WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularSword> SWORD_DRACONIC                     = ITEMS.register("draconic_sword",                      id -> new ModularSword(DRACONIC_TIER, DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularSword> SWORD_CHAOTIC                      = ITEMS.register("chaotic_sword",                       id -> new ModularSword(CHAOTIC_TIER, CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularStaff> STAFF_DRACONIC                     = ITEMS.register("draconic_staff",                      id -> new ModularStaff(DRACONIC_TIER, DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularStaff> STAFF_CHAOTIC                      = ITEMS.register("chaotic_staff",                       id -> new ModularStaff(CHAOTIC_TIER, CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    //Armor
    public static final DeferredHolder<Item, ModularChestpiece> CHESTPIECE_WYVERN             = ITEMS.register("wyvern_chestpiece",                   id -> new ModularChestpiece(WYVERN_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularChestpiece> CHESTPIECE_DRACONIC           = ITEMS.register("draconic_chestpiece",                 id -> new ModularChestpiece(DRACONIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredHolder<Item, ModularChestpiece> CHESTPIECE_CHAOTIC            = ITEMS.register("chaotic_chestpiece",                  id -> new ModularChestpiece(CHAOTIC_TOOLS.setId(ResourceKey.create(Registries.ITEM, id))));

    //Blocks
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_GENERATOR                   = ITEMS.register("generator",                           id ->  new ItemBlockBCore(GENERATOR.get(),                     new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //Generator
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_GRINDER                     = ITEMS.register("grinder",                             id ->  new ItemBlockBCore(GRINDER.get(),                       new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //Grinder
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DISENCHANTER                = ITEMS.register("disenchanter",                        id ->  new ItemBlockBCore(DISENCHANTER.get(),                  new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //Disenchanter
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_ENERGY_TRANSFUSER           = ITEMS.register("energy_transfuser",                   id ->  new ItemBlockBCore(ENERGY_TRANSFUSER.get(),             new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyTransfuser
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DISLOCATOR_PEDESTAL         = ITEMS.register("dislocator_pedestal",                 id ->  new ItemBlockBCore(DISLOCATOR_PEDESTAL.get(),           new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DislocatorPedestal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DISLOCATOR_RECEPTACLE       = ITEMS.register("dislocator_receptacle",               id ->  new ItemBlockBCore(DISLOCATOR_RECEPTACLE.get(),         new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DislocatorReceptacle
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_CREATIVE_OP_CAPACITOR       = ITEMS.register("creative_op_capacitor",               id ->  new ItemBlockBCore(CREATIVE_OP_CAPACITOR.get(),         new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //CreativeOPSource
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_ENTITY_DETECTOR             = ITEMS.register("entity_detector",                     id ->  new ItemBlockBCore(ENTITY_DETECTOR.get(),               new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EntityDetector
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_ENTITY_DETECTOR_ADVANCED    = ITEMS.register("entity_detector_advanced",            id ->  new ItemBlockBCore(ENTITY_DETECTOR_ADVANCED.get(),      new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EntityDetector
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_STABILIZED_SPAWNER          = ITEMS.register("stabilized_spawner",                  id ->  new ItemBlockBCore(STABILIZED_SPAWNER.get(),            new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //StabilizedSpawner
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_POTENTIOMETER               = ITEMS.register("potentiometer",                       id ->  new ItemBlockBCore(POTENTIOMETER.get(),                 new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //Potentiometer
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_CELESTIAL_MANIPULATOR       = ITEMS.register("celestial_manipulator",               id ->  new ItemBlockBCore(CELESTIAL_MANIPULATOR.get(),         new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //CelestialManipulator
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DRACONIUM_CHEST             = ITEMS.register("draconium_chest",                     id ->  new ItemBlockBCore(DRACONIUM_CHEST.get(),               new Item.Properties().rarity(Rarity.RARE).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumChest
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_PARTICLE_GENERATOR          = ITEMS.register("particle_generator",                  id ->  new ItemBlockBCore(PARTICLE_GENERATOR.get(),            new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //ParticleGenerator
//    public static final RegistryObject<ItemBlockBCore> ITEM_PLACED_ITEM                 = ITEMS.register("placed_item",                         () ->  new ItemBlockBCore(PLACED_ITEM.get(),                   new Item.Properties()));  //PlacedItem
//    public static final RegistryObject<ItemBlockBCore> ITEM_PORTAL                      = ITEMS.register("portal",                              () ->  new ItemBlockBCore(PORTAL.get(),                        new Item.Properties()));  //Portal
//    public static final RegistryObject<ItemBlockBCore> ITEM_CHAOS_CRYSTAL               = ITEMS.register("chaos_crystal",                       () ->  new ItemBlockBCore(CHAOS_CRYSTAL.get(),                 new Item.Properties()));  //ChaosCrystal
//    public static final RegistryObject<ItemBlockBCore> ITEM_CHAOS_CRYSTAL_PART          = ITEMS.register("chaos_crystal_part",                  () ->  new ItemBlockBCore(CHAOS_CRYSTAL_PART.get(),            new Item.Properties()));  //ChaosCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_BASIC_CRAFTING_INJECTOR     = ITEMS.register("basic_crafting_injector",             id ->  new ItemBlockBCore(BASIC_CRAFTING_INJECTOR.get(),       new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //CraftingInjector
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_WYVERN_CRAFTING_INJECTOR    = ITEMS.register("wyvern_crafting_injector",            id ->  new ItemBlockBCore(WYVERN_CRAFTING_INJECTOR.get(),      new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //CraftingInjector
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_AWAKENED_CRAFTING_INJECTOR  = ITEMS.register("awakened_crafting_injector",          id ->  new ItemBlockBCore(AWAKENED_CRAFTING_INJECTOR.get(),    new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //CraftingInjector
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_CHAOTIC_CRAFTING_INJECTOR   = ITEMS.register("chaotic_crafting_injector",           id ->  new ItemBlockBCore(CHAOTIC_CRAFTING_INJECTOR.get(),     new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //CraftingInjector
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_CRAFTING_CORE               = ITEMS.register("crafting_core",                       id ->  new ItemBlockBCore(CRAFTING_CORE.get(),                 new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //FusionCraftingCore
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_ENERGY_CORE                 = ITEMS.register("energy_core",                         id ->  new ItemBlockBCore(ENERGY_CORE.get(),                   new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCore
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_ENERGY_CORE_STABILIZER      = ITEMS.register("energy_core_stabilizer",              id ->  new ItemBlockBCore(ENERGY_CORE_STABILIZER.get(),        new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCoreStabilizer
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_ENERGY_PYLON                = ITEMS.register("energy_pylon",                        id ->  new ItemBlockBCore(ENERGY_PYLON.get(),                  new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyPylon
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_STRUCTURE_BLOCK             = ITEMS.register("structure_block",                     id ->  new ItemBlockBCore(STRUCTURE_BLOCK.get(),               new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //StructureBlock
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_REACTOR_CORE                = ITEMS.register("reactor_core",                        id ->  new ItemBlockBCore(REACTOR_CORE.get(),                  new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //ReactorCore
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_REACTOR_STABILIZER          = ITEMS.register("reactor_stabilizer",                  id ->  new ItemBlockBCore(REACTOR_STABILIZER.get(),            new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //ReactorComponent
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_REACTOR_INJECTOR            = ITEMS.register("reactor_injector",                    id ->  new ItemBlockBCore(REACTOR_INJECTOR.get(),              new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //ReactorComponent
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_RAIN_SENSOR                 = ITEMS.register("rain_sensor",                         id ->  new ItemBlockBCore(RAIN_SENSOR.get(),                   new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //RainSensor
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DISLOCATION_INHIBITOR       = ITEMS.register("dislocation_inhibitor",               id ->  new ItemBlockBCore(DISLOCATION_INHIBITOR.get(),         new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DislocationInhibitor
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_OVERWORLD_DRACONIUM_ORE     = ITEMS.register("overworld_draconium_ore",             id ->  new ItemBlockBCore(OVERWORLD_DRACONIUM_ORE.get(),       new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumOre
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DEEPSLATE_DRACONIUM_ORE     = ITEMS.register("deepslate_draconium_ore",             id ->  new ItemBlockBCore(DEEPSLATE_DRACONIUM_ORE.get(),       new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumOre
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_NETHER_DRACONIUM_ORE        = ITEMS.register("nether_draconium_ore",                id ->  new ItemBlockBCore(NETHER_DRACONIUM_ORE.get(),          new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumOre
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_END_DRACONIUM_ORE           = ITEMS.register("end_draconium_ore",                   id ->  new ItemBlockBCore(END_DRACONIUM_ORE.get(),             new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumOre
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DRACONIUM_BLOCK             = ITEMS.register("draconium_block",                     id ->  new ItemBlockBCore(DRACONIUM_BLOCK.get(),               new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumBlock
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_AWAKENED_DRACONIUM_BLOCK    = ITEMS.register("awakened_draconium_block",            id ->  new ItemBlockBCore(AWAKENED_DRACONIUM_BLOCK.get(),      new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //DraconiumBlock
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_INFUSED_OBSIDIAN            = ITEMS.register("infused_obsidian",                    id ->  new ItemBlockBCore(INFUSED_OBSIDIAN.get(),              new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //BlockBCore
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_BASIC_IO_CRYSTAL            = ITEMS.register("basic_io_crystal",                    id ->  new ItemBlockBCore(BASIC_IO_CRYSTAL.get(),              new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_WYVERN_IO_CRYSTAL           = ITEMS.register("wyvern_io_crystal",                   id ->  new ItemBlockBCore(WYVERN_IO_CRYSTAL.get(),             new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DRACONIC_IO_CRYSTAL         = ITEMS.register("draconic_io_crystal",                 id ->  new ItemBlockBCore(DRACONIC_IO_CRYSTAL.get(),           new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_BASIC_RELAY_CRYSTAL         = ITEMS.register("basic_relay_crystal",                 id ->  new ItemBlockBCore(BASIC_RELAY_CRYSTAL.get(),           new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_WYVERN_RELAY_CRYSTAL        = ITEMS.register("wyvern_relay_crystal",                id ->  new ItemBlockBCore(WYVERN_RELAY_CRYSTAL.get(),          new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DRACONIC_RELAY_CRYSTAL      = ITEMS.register("draconic_relay_crystal",              id ->  new ItemBlockBCore(DRACONIC_RELAY_CRYSTAL.get(),        new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_BASIC_WIRELESS_CRYSTAL      = ITEMS.register("basic_wireless_crystal",              id ->  new ItemBlockBCore(BASIC_WIRELESS_CRYSTAL.get(),        new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_WYVERN_WIRELESS_CRYSTAL     = ITEMS.register("wyvern_wireless_crystal",             id ->  new ItemBlockBCore(WYVERN_WIRELESS_CRYSTAL.get(),       new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_DRACONIC_WIRELESS_CRYSTAL   = ITEMS.register("draconic_wireless_crystal",           id ->  new ItemBlockBCore(DRACONIC_WIRELESS_CRYSTAL.get(),     new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //EnergyCrystal
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_FLUX_GATE                   = ITEMS.register("flux_gate",                           id ->  new ItemBlockBCore(FLUX_GATE.get(),                     new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //FlowGate
    public static final DeferredHolder<Item, ItemBlockBCore> ITEM_FLUID_GATE                  = ITEMS.register("fluid_gate",                          id ->  new ItemBlockBCore(FLUID_GATE.get(),                    new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id))));  //FlowGate

    //@formatter:on

//        TierSortingRegistry.registerTier(WYVERN_TIER, new ResourceLocation(MODID, "wyvern_tier"), List.of(Tiers.NETHERITE), Collections.emptyList());
//        if (DEConfig.useToolTierTags) {
//        TierSortingRegistry.registerTier(DRACONIC_TIER, new ResourceLocation(MODID, "draconic_tier"), List.of(WYVERN_TIER), Collections.emptyList());
//        TierSortingRegistry.registerTier(CHAOTIC_TIER, new ResourceLocation(MODID, "chaotic_tier"), List.of(DRACONIC_TIER), Collections.emptyList());
//    }


    //#################################################################
    // Tile Entities
    //#################################################################

    //@formatter:off
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileGenerator>>              TILE_GENERATOR                  = TILES_ENTITIES.register("generator",             () -> new BlockEntityType<>(TileGenerator::new,                         GENERATOR.get()                     ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileGrinder>>                TILE_GRINDER                    = TILES_ENTITIES.register("grinder",               () -> new BlockEntityType<>(TileGrinder::new,                           GRINDER.get()                       ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDisenchanter>>           TILE_DISENCHANTER               = TILES_ENTITIES.register("disenchanter",          () -> new BlockEntityType<>(TileDisenchanter::new,                      DISENCHANTER.get()                  ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEnergyTransfuser>>       TILE_ENERGY_TRANSFUSER          = TILES_ENTITIES.register("energy_transfuser",     () -> new BlockEntityType<>(TileEnergyTransfuser::new,                  ENERGY_TRANSFUSER.get()             ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDislocatorPedestal>>     TILE_DISLOCATOR_PEDESTAL        = TILES_ENTITIES.register("dislocator_pedestal",   () -> new BlockEntityType<>(TileDislocatorPedestal::new,                DISLOCATOR_PEDESTAL.get()           ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDislocatorReceptacle>>   TILE_DISLOCATOR_RECEPTACLE      = TILES_ENTITIES.register("dislocator_receptacle", () -> new BlockEntityType<>(TileDislocatorReceptacle::new,              DISLOCATOR_RECEPTACLE.get()         ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCreativeOPCapacitor>>    TILE_CREATIVE_OP_CAPACITOR      = TILES_ENTITIES.register("creative_op_capacitor", () -> new BlockEntityType<>(TileCreativeOPCapacitor::new,               CREATIVE_OP_CAPACITOR.get()         ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDetector>>         TILE_ENTITY_DETECTOR            = TILES_ENTITIES.register("entity_detector",       () -> new BlockEntityType<>(TileEntityDetector::new,                    ENTITY_DETECTOR.get(), ENTITY_DETECTOR_ADVANCED.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileStabilizedSpawner>>      TILE_STABILIZED_SPAWNER         = TILES_ENTITIES.register("stabilized_spawner",    () -> new BlockEntityType<>(TileStabilizedSpawner::new,                 STABILIZED_SPAWNER.get()            ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRainSensor>>             TILE_RAIN_SENSOR                = TILES_ENTITIES.register("rain_sensor",           () -> new BlockEntityType<>(TileRainSensor::new,                        RAIN_SENSOR.get()                   ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TilePotentiometer>>          TILE_POTENTIOMETER              = TILES_ENTITIES.register("potentiometer",         () -> new BlockEntityType<>(TilePotentiometer::new,                     POTENTIOMETER.get()                 ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCelestialManipulator>>   TILE_CELESTIAL_MANIPULATOR      = TILES_ENTITIES.register("celestial_manipulator", () -> new BlockEntityType<>(TileCelestialManipulator::new,              CELESTIAL_MANIPULATOR.get()         ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDraconiumChest>>         TILE_DRACONIUM_CHEST            = TILES_ENTITIES.register("draconium_chest",       () -> new BlockEntityType<>(TileDraconiumChest::new,                    DRACONIUM_CHEST.get()               ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TilePlacedItem>>             TILE_PLACED_ITEM                = TILES_ENTITIES.register("placed_item",           () -> new BlockEntityType<>(TilePlacedItem::new,                        PLACED_ITEM.get()                   ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TilePortal>>                 TILE_PORTAL                     = TILES_ENTITIES.register("portal",                () -> new BlockEntityType<>(TilePortal::new,                            PORTAL.get()                        ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFusionCraftingInjector>> TILE_CRAFTING_INJECTOR          = TILES_ENTITIES.register("crafting_injector",     () -> new BlockEntityType<>(TileFusionCraftingInjector::new,            BASIC_CRAFTING_INJECTOR.get(), WYVERN_CRAFTING_INJECTOR.get(), AWAKENED_CRAFTING_INJECTOR.get(), CHAOTIC_CRAFTING_INJECTOR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFusionCraftingCore>>     TILE_CRAFTING_CORE              = TILES_ENTITIES.register("crafting_core",         () -> new BlockEntityType<>(TileFusionCraftingCore::new,                CRAFTING_CORE.get()                 ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEnergyCore>>             TILE_STORAGE_CORE               = TILES_ENTITIES.register("storage_core",          () -> new BlockEntityType<>(TileEnergyCore::new,                        ENERGY_CORE.get()                   ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEnergyCoreStabilizer>>   TILE_CORE_STABILIZER            = TILES_ENTITIES.register("core_stabilizer",       () -> new BlockEntityType<>(TileEnergyCoreStabilizer::new,              ENERGY_CORE_STABILIZER.get()        ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEnergyPylon>>            TILE_ENERGY_PYLON               = TILES_ENTITIES.register("energy_pylon",          () -> new BlockEntityType<>(TileEnergyPylon::new,                       ENERGY_PYLON.get()                  ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileStructureBlock>>         TILE_STRUCTURE_BLOCK            = TILES_ENTITIES.register("structure_block",       () -> new BlockEntityType<>(TileStructureBlock::new,                    STRUCTURE_BLOCK.get()               ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileReactorCore>>            TILE_REACTOR_CORE               = TILES_ENTITIES.register("reactor_core",          () -> new BlockEntityType<>(TileReactorCore::new,                       REACTOR_CORE.get()                  ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileReactorStabilizer>>      TILE_REACTOR_STABILIZER         = TILES_ENTITIES.register("reactor_stabilizer",    () -> new BlockEntityType<>(TileReactorStabilizer::new,                 REACTOR_STABILIZER.get()            ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileReactorInjector>>        TILE_REACTOR_INJECTOR           = TILES_ENTITIES.register("reactor_injector",      () -> new BlockEntityType<>(TileReactorInjector::new,                   REACTOR_INJECTOR.get()              ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFluxGate>>               TILE_FLUX_GATE                  = TILES_ENTITIES.register("flux_gate",             () -> new BlockEntityType<>(TileFluxGate::new,                          FLUX_GATE.get()                     ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFluidGate>>              TILE_FLUID_GATE                 = TILES_ENTITIES.register("fluid_gate",            () -> new BlockEntityType<>(TileFluidGate::new,                         FLUID_GATE.get()                    ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChaosCrystal>>           TILE_CHAOS_CRYSTAL              = TILES_ENTITIES.register("chaos_crystal",         () -> new BlockEntityType<>(TileChaosCrystal::new,                      CHAOS_CRYSTAL.get(), CHAOS_CRYSTAL_PART.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCrystalDirectIO>>        TILE_IO_CRYSTAL                 = TILES_ENTITIES.register("io_crystal",            () -> new BlockEntityType<>(TileCrystalDirectIO::new, BASIC_IO_CRYSTAL.get(), WYVERN_IO_CRYSTAL.get(), DRACONIC_IO_CRYSTAL.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCrystalRelay>>           TILE_RELAY_CRYSTAL              = TILES_ENTITIES.register("relay_crystal",         () -> new BlockEntityType<>(TileCrystalRelay::new, BASIC_RELAY_CRYSTAL.get(), WYVERN_RELAY_CRYSTAL.get(), DRACONIC_RELAY_CRYSTAL.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCrystalWirelessIO>>      TILE_WIRELESS_CRYSTAL           = TILES_ENTITIES.register("wireless_crystal",      () -> new BlockEntityType<>(TileCrystalWirelessIO::new, BASIC_WIRELESS_CRYSTAL.get(), WYVERN_WIRELESS_CRYSTAL.get(), DRACONIC_WIRELESS_CRYSTAL.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CometSpawner.TileCometSpawner>>TILE_COMET_SPAWNER            = TILES_ENTITIES.register("comet_spawner",         () -> new BlockEntityType<>(CometSpawner.TileCometSpawner::new,        COMET_SPAWNER.get()                   ));
    //@formatter:on


    //#################################################################
    // MenuTypes
    //#################################################################

    //@formatter:off
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorMenu>> MENU_GENERATOR                              = MENU_TYPES.register("generator",                      () -> IMenuTypeExtension.create(GeneratorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GrinderMenu>> MENU_GRINDER                                  = MENU_TYPES.register("grinder",                        () -> IMenuTypeExtension.create(GrinderMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ConfigurableItemMenu>> MENU_CONFIGURABLE_ITEM               = MENU_TYPES.register("configurable_item",              () -> IMenuTypeExtension.create(ConfigurableItemMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CelestialManipulatorMenu>> MENU_CELESTIAL_MANIPULATOR       = MENU_TYPES.register("celestial_manipulator",          () -> IMenuTypeExtension.create(CelestialManipulatorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DisenchanterMenu>> MENU_DISENCHANTER                        = MENU_TYPES.register("disenchanter",                   () -> IMenuTypeExtension.create(DisenchanterMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<TransfuserMenu>> MENU_ENERGY_TRANSFUSER                     = MENU_TYPES.register("energy_transfuser",              () -> IMenuTypeExtension.create(TransfuserMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<EnergyCoreMenu>> MENU_ENERGY_CORE                           = MENU_TYPES.register("energy_core",                    () -> IMenuTypeExtension.create(EnergyCoreMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FlowGateMenu>> MENU_FLOW_GATE                               = MENU_TYPES.register("flow_gate",                      () -> IMenuTypeExtension.create(FlowGateMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<EntityDetectorMenu>> MENU_ENTITY_DETECTOR                   = MENU_TYPES.register("entity_detector",                () -> IMenuTypeExtension.create(EntityDetectorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<FusionCraftingCoreMenu>> MENU_FUSION_CRAFTING_CORE          = MENU_TYPES.register("fusion_crafting_core",           () -> IMenuTypeExtension.create(FusionCraftingCoreMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ModularItemMenu>> MENU_MODULAR_ITEM                         = MENU_TYPES.register("modular_item",                   () -> IMenuTypeExtension.create(ModularItemMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DraconiumChestMenu>> MENU_DRACONIUM_CHEST                   = MENU_TYPES.register("draconium_chest",                () -> IMenuTypeExtension.create(DraconiumChestMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerEnergyCrystal>> MENU_ENERGY_CRYSTAL                = MENU_TYPES.register("energy_crystal",                 () -> IMenuTypeExtension.create(ContainerEnergyCrystal::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ReactorMenu>> MENU_REACTOR                                  = MENU_TYPES.register("reactor",                        () -> IMenuTypeExtension.create(ReactorMenu::new));
    //@formatter:on


    //#################################################################
    // Entities
    //#################################################################

    public static final DeferredHolder<EntityType<?>, EntityType<DraconicGuardianEntity>>      ENTITY_DRACONIC_GUARDIAN    = ENTITY_TYPES.register("draconic_guardian",            id -> EntityType.Builder.of(DraconicGuardianEntity::new, MobCategory.MONSTER).fireImmune().sized(16.0F, 8.0F).clientTrackingRange(20).build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
    public static final DeferredHolder<EntityType<?>, EntityType<GuardianWither>>              ENTITY_GUARDIAN_WITHER      = ENTITY_TYPES.register("guardian_wither",              id -> EntityType.Builder.of(GuardianWither::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.9F, 3.5F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, id)));

    public static final DeferredHolder<EntityType<?>, EntityType<GuardianProjectileEntity>>    ENTITY_GUARDIAN_PROJECTILE  = ENTITY_TYPES.register("guardian_projectile",          id -> EntityType.Builder.<GuardianProjectileEntity>of(GuardianProjectileEntity::new, MobCategory.MISC).fireImmune().sized(2F, 2F).clientTrackingRange(20)/*.updateInterval(10)*/.build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
    public static final DeferredHolder<EntityType<?>, EntityType<GuardianCrystalEntity>>       ENTITY_GUARDIAN_CRYSTAL     = ENTITY_TYPES.register("guardian_crystal",             id -> EntityType.Builder.<GuardianCrystalEntity>of(GuardianCrystalEntity::new, MobCategory.MISC).fireImmune().sized(2F, 2F).clientTrackingRange(20).updateInterval(100).build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
    public static final DeferredHolder<EntityType<?>, EntityType<DraconicArrowEntity>>         ENTITY_DRACONIC_ARROW       = ENTITY_TYPES.register("draconic_arrow",               id -> EntityType.Builder.<DraconicArrowEntity>of(DraconicArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE, id)));

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ENTITY_DRACONIC_GUARDIAN.get(), DraconicGuardianEntity.registerAttributes().build());
        event.put(ENTITY_GUARDIAN_WITHER.get(), GuardianWither.createAttributes().build());
    }

    //#################################################################
    // Enchantments
    //#################################################################

    public static final ResourceKey<Enchantment> REAPER = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(MODID, "reaper"));


    //#################################################################
    // Features
    //#################################################################

    public static final DeferredHolder<Feature<?>, Feature<?>> CHAOS_ISLAND                     = FEATURES.register("chaos_island", () -> new ChaosIslandFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<?>> END_COMET                        = FEATURES.register("end_comet", () -> new EnderCometFeature(NoneFeatureConfiguration.CODEC));

    //#################################################################
    // Recipe Types
    //#################################################################

    static {
        DraconicAPI.FUSION_RECIPE_SERIALIZER = RECIPE_SERIAL.register("fusion_crafting", FusionRecipe.Serializer::new);
        DraconicAPI.FUSION_RECIPE_TYPE = RECIPE_TYPES.register("fusion_crafting", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(MODID, "fusion_crafting")));
    }

    public static final DeferredHolder<IngredientType<?>, IngredientType<StackIngredient>> STACK_INGREDIENT_TYPE = INGREDIENT_TYPES.register("stack", () -> new IngredientType<>(StackIngredient.CODEC));


    //#################################################################
    // World Entities
    //#################################################################

    public static final DeferredHolder<WorldEntityType<?>, WorldEntityType<?>> WE_GUARDIAN_MANAGER      = WORLD_ENTITY_TYPES.register("guardian_manager",       () -> new WorldEntityType<>(GuardianFightManager::new));
}