package com.brandon3055.draconicevolution.datagen;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.blocks.DislocatorReceptacle;
import com.brandon3055.draconicevolution.blocks.Portal;
import com.brandon3055.draconicevolution.blocks.RainSensor;
import com.brandon3055.draconicevolution.blocks.machines.EnergyPylon;
import com.brandon3055.draconicevolution.blocks.machines.Generator;
import com.brandon3055.draconicevolution.blocks.machines.Grinder;
import com.brandon3055.draconicevolution.init.DEContent;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.client.data.models.BlockModelGenerators.condition;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;

/**
 * Created by brandon3055 on 28/2/20.
 */
public class BlockStateGenerator extends ModelProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<Identifier> createdModels = new HashSet<>();
    private BlockModelGenerators blockModels;

    public BlockStateGenerator(PackOutput output) {
        super(output, DraconicEvolution.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blockModels = blockModels;
        registerStatesAndModels();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.empty();
    }

    protected void registerStatesAndModels() {
        //Simple Blocks
        simpleBlock(DEContent.DRACONIUM_BLOCK);
        simpleBlock(DEContent.AWAKENED_DRACONIUM_BLOCK, cubeBottomTop("awakened_draconium_block", modLoc("block/awakened_draconium_block_side"), modLoc("block/awakened_draconium_block"), modLoc("block/awakened_draconium_block")));
        simpleBlock(DEContent.INFUSED_OBSIDIAN);
        simpleBlock(DEContent.ENERGY_CORE);
        simpleBlock(DEContent.ENERGY_CORE_STABILIZER, modLoc("block/energy_core_stabilizer"));
        simpleBlock(DEContent.CREATIVE_OP_CAPACITOR);
        simpleBlock(DEContent.STABILIZED_SPAWNER, modLoc("block/stabilized_spawner"));
        simpleBlock(DEContent.PARTICLE_GENERATOR, modLoc("block/particle_generator"));
        simpleBlock(DEContent.CRAFTING_CORE, modLoc("block/crafting/fusion_crafting_core"));
        simpleBlock(DEContent.DISLOCATION_INHIBITOR, cubeBottomTop("dislocation_inhibitor", modLoc("block/dislocation_inhibitor"), modLoc("block/parts/machine_top"), modLoc("block/parts/machine_top")));

        //TODO
//        multiLayerBlock(DEContent.OVERWORLD_DRACONIUM_ORE, mcLoc("block/stone"), modLoc("block/draconium_ore_overlay"));
//        multiLayerBlock(DEContent.NETHER_DRACONIUM_ORE, mcLoc("block/netherrack"), modLoc("block/draconium_ore_overlay"));
//        multiLayerBlock(DEContent.END_DRACONIUM_ORE, mcLoc("block/end_stone"), modLoc("block/draconium_ore_overlay"));
//        multiLayerBlock(DEContent.DEEPSLATE_DRACONIUM_ORE, mcLoc("block/deepslate"), modLoc("block/draconium_ore_overlay"));

        simpleBlock(DEContent.OVERWORLD_DRACONIUM_ORE);
        simpleBlock(DEContent.NETHER_DRACONIUM_ORE);
        simpleBlock(DEContent.END_DRACONIUM_ORE);
        simpleBlock(DEContent.DEEPSLATE_DRACONIUM_ORE);

        directionalBlock(DEContent.BASIC_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_draconium"));
        directionalBlock(DEContent.WYVERN_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_wyvern"));
        directionalBlock(DEContent.AWAKENED_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_draconic"));
        directionalBlock(DEContent.CHAOTIC_CRAFTING_INJECTOR, modLoc("block/crafting/crafting_injector_chaotic"));

        directionalFromNorth(DEContent.FLUID_GATE, modLoc("block/fluid_gate"));
        directionalFromNorth(DEContent.FLUX_GATE, modLoc("block/flux_gate"));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DEContent.RAIN_SENSOR.get())
                .with(PropertyDispatch.initial(RainSensor.ACTIVE).generate(active -> plainVariant(active ? modLoc("block/rain_sensor_active") : modLoc("block/rain_sensor")))));

        directionalBlock(DEContent.POTENTIOMETER, modLoc("block/potentiometer"));

        simpleBlock(DEContent.ENERGY_TRANSFUSER, modLoc("block/energy_transfuser"));

        simpleBlock(DEContent.DISENCHANTER, modLoc("block/disenchanter"));
        simpleBlock(DEContent.CELESTIAL_MANIPULATOR, modLoc("block/celestial_manipulator"));
        simpleBlock(DEContent.ENTITY_DETECTOR, modLoc("block/entity_detector"));
        simpleBlock(DEContent.ENTITY_DETECTOR_ADVANCED, modLoc("block/entity_detector_advanced"));

        dummyBlock(DEContent.BASIC_IO_CRYSTAL);
        dummyBlock(DEContent.WYVERN_IO_CRYSTAL);
        dummyBlock(DEContent.DRACONIC_IO_CRYSTAL);
//        dummyBlock(DEContent.  CRYSTAL_IO_CHAOTIC);
        dummyBlock(DEContent.BASIC_RELAY_CRYSTAL);
        dummyBlock(DEContent.WYVERN_RELAY_CRYSTAL);
        dummyBlock(DEContent.DRACONIC_RELAY_CRYSTAL);
//        dummyBlock(DEContent.  CRYSTAL_RELAY_CHAOTIC);
        dummyBlock(DEContent.BASIC_WIRELESS_CRYSTAL);
        dummyBlock(DEContent.WYVERN_WIRELESS_CRYSTAL);
        dummyBlock(DEContent.DRACONIC_WIRELESS_CRYSTAL);
//        dummyBlock(DEContent.  CRYSTAL_WIRELESS_CHAOTIC);
        dummyBlock(DEContent.STRUCTURE_BLOCK);
        dummyBlock(DEContent.CHAOS_CRYSTAL);
        dummyBlock(DEContent.PLACED_ITEM);
        dummyBlock(DEContent.CHAOS_CRYSTAL_PART);
        dummyBlock(DEContent.COMET_SPAWNER);

        dummyBlock(DEContent.DRACONIUM_CHEST);
        dummyBlock(DEContent.REACTOR_CORE);
        dummyBlock(DEContent.REACTOR_STABILIZER);
        dummyBlock(DEContent.REACTOR_INJECTOR);

        Map<String, Identifier> pylonModels = new HashMap<>();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DEContent.ENERGY_PYLON.get())
                .with(PropertyDispatch.initial(EnergyPylon.FACING, EnergyPylon.MODE).generate((dir, mode) -> {
                    String io = mode == EnergyPylon.Mode.OUTPUT ? "output" : "input";
                    Identifier model = pylonModels.computeIfAbsent(io, e -> cubeBottomTop("energy_pylon_" + io, modLoc("block/energy_pylon/energy_pylon_" + io), modLoc("block/energy_pylon/energy_pylon_" + io), modLoc("block/energy_pylon/energy_pylon_active_face")));
                    return plainVariant(model)
                            .with(rotationY(dir.getAxis() == Direction.Axis.Y ? 0 : 180 + (90 * dir.get2DDataValue())))
                            .with(rotationX(dir == UP ? 0 : dir == DOWN ? 180 : 90));
                })));

        simpleBlock(DEContent.DISLOCATOR_PEDESTAL, modLoc("block/dislocator_pedestal"));

        Identifier receptacleCamo = cubeAll("infused_obsidian", modLoc("block/infused_obsidian"));
        Identifier receptacleInactive = cubeAll("dislocator_receptacle_inactive", modLoc("block/dislocator_receptacle_inactive"));
        Identifier receptacleActive = cubeAll("dislocator_receptacle_active", modLoc("block/dislocator_receptacle_active"));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(DEContent.DISLOCATOR_RECEPTACLE.get())
                .with(PropertyDispatch.initial(DislocatorReceptacle.ACTIVE, DislocatorReceptacle.CAMO).generate((active, camo) -> plainVariant(camo ? receptacleCamo : active ? receptacleActive : receptacleInactive))));

        //Generate portal block state
        Identifier portalModel = modLoc("block/portal/portal");
        Identifier portalWallX = modLoc("block/portal/portal_wall_x");
        Identifier portalWallY = modLoc("block/portal/portal_wall_y");
        Identifier portalWallZ = modLoc("block/portal/portal_wall_z");
        MultiPartGenerator portalBuilder = MultiPartGenerator.multiPart(DEContent.PORTAL.get());
        for (Direction.Axis axis : Direction.Axis.values()) {
            Identifier wallWestEast = axis == Direction.Axis.Z ? portalWallX : axis == Direction.Axis.Y ? portalWallY : portalWallZ;
            Identifier wallUpDown = axis == Direction.Axis.Z || axis == Direction.Axis.Y ? portalWallY : portalWallZ;
            portalBuilder
                    .with(condition().term(Portal.VISIBLE, true).term(Portal.AXIS, axis), plainVariant(portalModel)
                            .with(rotationX(axis != Direction.Axis.Y ? 90 : 0))
                            .with(rotationY(axis == Direction.Axis.X ? 90 : 0)))
                    //Up
                    .with(condition().term(Portal.DRAW_UP, true).term(Portal.AXIS, axis), plainVariant(wallUpDown)
                            .with(rotationX(axis == Direction.Axis.X || axis == Direction.Axis.Z ? -90 : 0)))
                    //Down
                    .with(condition().term(Portal.DRAW_DOWN, true).term(Portal.AXIS, axis), plainVariant(wallUpDown)
                            .with(rotationX(axis == Direction.Axis.X || axis == Direction.Axis.Z ? 90 : 0))
                            .with(rotationY(axis == Direction.Axis.Y ? 180 : 0)))
                    //West
                    .with(condition().term(Portal.DRAW_WEST, true).term(Portal.AXIS, axis), plainVariant(wallWestEast)
                            .with(rotationY(axis == Direction.Axis.Z ? 180 : axis == Direction.Axis.Y ? -90 : 0)))
                    //East
                    .with(condition().term(Portal.DRAW_EAST, true).term(Portal.AXIS, axis), plainVariant(wallWestEast)
                            .with(rotationY(axis == Direction.Axis.Y ? 90 : 0))
                            .with(rotationX(axis == Direction.Axis.X ? 180 : 0)));
        }
        blockModels.blockStateOutput.accept(portalBuilder);

        //Generator
        Identifier modelGenerator = modLoc("block/generator/generator");
        Identifier modelGeneratorFlame = modLoc("block/generator/generator_flame");
        MultiPartGenerator generatorBuilder = MultiPartGenerator.multiPart(DEContent.GENERATOR.get());
        for (Direction dir : FenceGateBlock.FACING.getPossibleValues()) {
            int angle = (int) dir.getOpposite().toYRot();
            generatorBuilder
                    .with(condition().term(Generator.FACING, dir), plainVariant(modelGenerator).with(rotationY(angle)))
                    .with(condition().term(Generator.FACING, dir).term(Generator.ACTIVE, true), plainVariant(modelGeneratorFlame).with(rotationY(angle)));
        }
        blockModels.blockStateOutput.accept(generatorBuilder);

        //Grinder
        Identifier modelGrinder = modLoc("block/grinder/grinder");
        Identifier modelGrinderActive = modLoc("block/grinder/grinder_eyes");
        MultiPartGenerator grinderBuilder = MultiPartGenerator.multiPart(DEContent.GRINDER.get());

         Direction[] BY_2D_DATA = Arrays.stream(Direction.values())
                .filter(p_235685_ -> p_235685_.getAxis().isHorizontal())
                .sorted(Comparator.comparingInt(Direction::get2DDataValue))
                .toArray(Direction[]::new);

        for (Direction dir : BY_2D_DATA) {
            int angle = (int) dir.getOpposite().toYRot();
            grinderBuilder.with(condition().term(Grinder.FACING, dir), plainVariant(modelGrinder).with(rotationY(angle)))
                    .with(condition().term(Grinder.FACING, dir).term(Grinder.ACTIVE, true), plainVariant(modelGrinderActive).with(rotationY(angle)));
        }
        blockModels.blockStateOutput.accept(grinderBuilder);
    }


    private void dummyBlock(Supplier<? extends Block> block) {
        Identifier model = modLoc("block/dummy");
        if (createdModels.add(model)) {
            new ModelTemplate(Optional.of(mcLoc("block/block")), Optional.empty(), TextureSlot.PARTICLE).create(model, TextureMapping.particle(new Material(mcLoc("block/glass"))), blockModels.modelOutput);
        }
        simpleBlock(block, model);
    }

    public void directionalFromNorth(Supplier<? extends Block> block, Identifier model) {
        directionalFromNorth(block, model, 180);
    }

    public void directionalFromNorth(Supplier<? extends Block> block, Identifier model, int angleOffset) {
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get())
                .with(PropertyDispatch.initial(BlockStateProperties.FACING).generate(dir -> plainVariant(model)
                        .with(rotationX(dir == DOWN ? 90 : dir == UP ? -90 : 0))
                        .with(rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)))));
    }

    public void simpleBlock(Supplier<? extends Block> block, Identifier model) {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.get(), plainVariant(model)));
    }

    public void directionalBlock(Supplier<? extends Block> block, Identifier model) {
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get())
                .with(PropertyDispatch.initial(BlockStateProperties.FACING).generate(dir -> plainVariant(model)
                        .with(rotationX(dir == DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0))
                        .with(rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)))));
    }

    public void simpleBlock(Supplier<? extends Block> block) {
        Identifier name = BuiltInRegistries.BLOCK.getKey(block.get());
        simpleBlock(block, cubeAll(name.getPath(), name.withPrefix("block/")));
    }

    private Identifier cubeAll(String name, Identifier texture) {
        Identifier model = modLoc("block/" + name);
        if (createdModels.add(model)) {
            ModelTemplates.CUBE_ALL.create(model, TextureMapping.cube(new Material(texture)), blockModels.modelOutput);
        }
        return model;
    }

    private Identifier cubeBottomTop(String name, Identifier side, Identifier bottom, Identifier top) {
        Identifier model = modLoc("block/" + name);
        if (createdModels.add(model)) {
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.SIDE, new Material(side))
                    .put(TextureSlot.BOTTOM, new Material(bottom))
                    .put(TextureSlot.TOP, new Material(top));
            ModelTemplates.CUBE_BOTTOM_TOP.create(model, textures, blockModels.modelOutput);
        }
        return model;
    }

    private static VariantMutator rotationX(int degrees) {
        return VariantMutator.X_ROT.withValue(quadrant(degrees));
    }

    private static VariantMutator rotationY(int degrees) {
        return VariantMutator.Y_ROT.withValue(quadrant(degrees));
    }

    private static Quadrant quadrant(int degrees) {
        return Quadrant.values()[Mth.positiveModulo(degrees, 360) / 90];
    }

    private static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(DraconicEvolution.MODID, path);
    }

    private static Identifier mcLoc(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    @Override
    public String getName() {
        return "Draconic Evolution Blockstates";
    }
}
