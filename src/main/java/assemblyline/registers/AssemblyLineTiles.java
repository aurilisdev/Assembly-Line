package assemblyline.registers;

import com.google.common.collect.Sets;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.common.tile.TileAutocrafter;
import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileFarmer;
import assemblyline.common.tile.TileMobGrinder;
import assemblyline.common.tile.TileRancher;
import assemblyline.common.tile.belt.TileConveyorBelt;
import assemblyline.common.tile.belt.TileDetector;
import assemblyline.common.tile.belt.TileSorterBelt;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import voltaic.common.block.BlockMachine;

public class AssemblyLineTiles {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AssemblyLine.ID);
	
	public static final RegistryObject<BlockEntityType<TileConveyorBelt>> TILE_BELT = BLOCK_ENTITY_TYPES.register("belt", () -> new BlockEntityType<>(TileConveyorBelt::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get()), null));
	public static final RegistryObject<BlockEntityType<TileDetector>> TILE_DETECTOR = BLOCK_ENTITY_TYPES.register("detector", () -> new BlockEntityType<>(TileDetector::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_DETECTOR.get()), null));
	public static final RegistryObject<BlockEntityType<TileSorterBelt>> TILE_SORTERBELT = BLOCK_ENTITY_TYPES.register("sorterbelt", () -> new BlockEntityType<>(TileSorterBelt::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_SORTERBELT.get()), null));
	public static final RegistryObject<BlockEntityType<TileCrate>> TILE_CRATE = BLOCK_ENTITY_TYPES.register("crate", () -> new BlockEntityType<>(TileCrate::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getSpecificValuesArray(new BlockMachine[0], SubtypeAssemblyMachine.crate, SubtypeAssemblyMachine.cratemedium, SubtypeAssemblyMachine.cratelarge)), null));
	public static final RegistryObject<BlockEntityType<TileAutocrafter>> TILE_AUTOCRAFTER = BLOCK_ENTITY_TYPES.register("autocrafter", () -> new BlockEntityType<>(TileAutocrafter::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.autocrafter)), null));
	public static final RegistryObject<BlockEntityType<TileBlockBreaker>> TILE_BLOCKBREAKER = BLOCK_ENTITY_TYPES.register("blockbreaker", () -> new BlockEntityType<>(TileBlockBreaker::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker)), null));
	public static final RegistryObject<BlockEntityType<TileBlockPlacer>> TILE_BLOCKPLACER = BLOCK_ENTITY_TYPES.register("blockplacer", () -> new BlockEntityType<>(TileBlockPlacer::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockplacer)), null));
	public static final RegistryObject<BlockEntityType<TileRancher>> TILE_RANCHER = BLOCK_ENTITY_TYPES.register("rancher", () -> new BlockEntityType<>(TileRancher::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.rancher)), null));
	public static final RegistryObject<BlockEntityType<TileMobGrinder>> TILE_MOBGRINDER = BLOCK_ENTITY_TYPES.register("mobgrinder", () -> new BlockEntityType<>(TileMobGrinder::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder)), null));
	public static final RegistryObject<BlockEntityType<TileFarmer>> TILE_FARMER = BLOCK_ENTITY_TYPES.register("farmer", () -> new BlockEntityType<>(TileFarmer::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.farmer)), null));
}
