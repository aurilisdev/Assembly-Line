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
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import voltaic.common.block.BlockMachine;

public class AssemblyLineTiles {

	public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, AssemblyLine.ID);
	
	public static final RegistryObject<TileEntityType<TileConveyorBelt>> TILE_BELT = BLOCK_ENTITY_TYPES.register("belt", () -> new TileEntityType<>(TileConveyorBelt::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get()), null));
	public static final RegistryObject<TileEntityType<TileDetector>> TILE_DETECTOR = BLOCK_ENTITY_TYPES.register("detector", () -> new TileEntityType<>(TileDetector::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_DETECTOR.get()), null));
	public static final RegistryObject<TileEntityType<TileSorterBelt>> TILE_SORTERBELT = BLOCK_ENTITY_TYPES.register("sorterbelt", () -> new TileEntityType<>(TileSorterBelt::new, Sets.newHashSet(AssemblyLineBlocks.BLOCK_SORTERBELT.get()), null));
	public static final RegistryObject<TileEntityType<TileCrate>> TILE_CRATE = BLOCK_ENTITY_TYPES.register("crate", () -> new TileEntityType<>(TileCrate::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getSpecificValuesArray(new BlockMachine[0], SubtypeAssemblyMachine.crate, SubtypeAssemblyMachine.cratemedium, SubtypeAssemblyMachine.cratelarge)), null));
	public static final RegistryObject<TileEntityType<TileAutocrafter>> TILE_AUTOCRAFTER = BLOCK_ENTITY_TYPES.register("autocrafter", () -> new TileEntityType<>(TileAutocrafter::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.autocrafter)), null));
	public static final RegistryObject<TileEntityType<TileBlockBreaker>> TILE_BLOCKBREAKER = BLOCK_ENTITY_TYPES.register("blockbreaker", () -> new TileEntityType<>(TileBlockBreaker::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker)), null));
	public static final RegistryObject<TileEntityType<TileBlockPlacer>> TILE_BLOCKPLACER = BLOCK_ENTITY_TYPES.register("blockplacer", () -> new TileEntityType<>(TileBlockPlacer::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockplacer)), null));
	public static final RegistryObject<TileEntityType<TileRancher>> TILE_RANCHER = BLOCK_ENTITY_TYPES.register("rancher", () -> new TileEntityType<>(TileRancher::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.rancher)), null));
	public static final RegistryObject<TileEntityType<TileMobGrinder>> TILE_MOBGRINDER = BLOCK_ENTITY_TYPES.register("mobgrinder", () -> new TileEntityType<>(TileMobGrinder::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder)), null));
	public static final RegistryObject<TileEntityType<TileFarmer>> TILE_FARMER = BLOCK_ENTITY_TYPES.register("farmer", () -> new TileEntityType<>(TileFarmer::new, Sets.newHashSet(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.farmer)), null));
}
