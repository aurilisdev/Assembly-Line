package assemblyline.registers;

import assemblyline.AssemblyLine;
import com.google.common.collect.Sets;

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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import voltaic.common.block.BlockMachine;

public class AssemblyLineTiles {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AssemblyLine.ID);
	
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileConveyorBelt>> TILE_BELT = BLOCK_ENTITY_TYPES.register("belt", () -> new BlockEntityType<>(TileConveyorBelt::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileDetector>> TILE_DETECTOR = BLOCK_ENTITY_TYPES.register("detector", () -> new BlockEntityType<>(TileDetector::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_DETECTOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSorterBelt>> TILE_SORTERBELT = BLOCK_ENTITY_TYPES.register("sorterbelt", () -> new BlockEntityType<>(TileSorterBelt::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_SORTERBELT.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCrate>> TILE_CRATE = BLOCK_ENTITY_TYPES.register("crate", () -> new BlockEntityType<>(TileCrate::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getSpecificValuesArray(new BlockMachine[0], SubtypeAssemblyMachine.crate, SubtypeAssemblyMachine.cratemedium, SubtypeAssemblyMachine.cratelarge)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAutocrafter>> TILE_AUTOCRAFTER = BLOCK_ENTITY_TYPES.register("autocrafter", () -> new BlockEntityType<>(TileAutocrafter::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.autocrafter)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileBlockBreaker>> TILE_BLOCKBREAKER = BLOCK_ENTITY_TYPES.register("blockbreaker", () -> new BlockEntityType<>(TileBlockBreaker::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileBlockPlacer>> TILE_BLOCKPLACER = BLOCK_ENTITY_TYPES.register("blockplacer", () -> new BlockEntityType<>(TileBlockPlacer::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockplacer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileRancher>> TILE_RANCHER = BLOCK_ENTITY_TYPES.register("rancher", () -> new BlockEntityType<>(TileRancher::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.rancher)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMobGrinder>> TILE_MOBGRINDER = BLOCK_ENTITY_TYPES.register("mobgrinder", () -> new BlockEntityType<>(TileMobGrinder::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFarmer>> TILE_FARMER = BLOCK_ENTITY_TYPES.register("farmer", () -> new BlockEntityType<>(TileFarmer::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.farmer)), null));
}
