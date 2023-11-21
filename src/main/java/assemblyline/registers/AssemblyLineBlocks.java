package assemblyline.registers;

import assemblyline.References;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.tile.TileAutocrafter;
import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.common.tile.TileCrate;
import assemblyline.common.tile.TileFarmer;
import assemblyline.common.tile.TileMobGrinder;
import assemblyline.common.tile.TileRancher;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
	public static BlockConveyorBelt blockConveyorBelt;
	public static BlockSorterBelt blockSorterBelt;
	public static BlockDetector blockDetector;
	public static GenericMachineBlock blockCrate;
	public static GenericMachineBlock blockCrateMedium;
	public static GenericMachineBlock blockCrateLarge;
	public static GenericMachineBlock blockAutocrafter;
	public static GenericMachineBlock blockBlockBreaker;
	public static GenericMachineBlock blockBlockPlacer;
	public static GenericMachineBlock blockRancher;
	public static GenericMachineBlock blockMobGrinder;
	public static GenericMachineBlock blockFarmer;
	static {
		BLOCKS.register("conveyorbelt", () -> blockConveyorBelt = new BlockConveyorBelt());
		BLOCKS.register("sorterbelt", () -> blockSorterBelt = new BlockSorterBelt());
		BLOCKS.register("detector", () -> blockDetector = new BlockDetector());
		BLOCKS.register("crate", () -> blockCrate = new GenericMachineBlock(TileCrate::new));
		BLOCKS.register("cratemedium", () -> blockCrateMedium = new GenericMachineBlock(TileCrate::new));
		BLOCKS.register("cratelarge", () -> blockCrateLarge = new GenericMachineBlock(TileCrate::new));
		BLOCKS.register("autocrafter", () -> blockAutocrafter = new GenericMachineBlock(TileAutocrafter::new));
		BLOCKS.register("blockbreaker", () -> blockBlockBreaker = new GenericMachineBlock(TileBlockBreaker::new));
		BLOCKS.register("blockplacer", () -> blockBlockPlacer = new GenericMachineBlock(TileBlockPlacer::new));
		BLOCKS.register("rancher", () -> blockRancher = new GenericMachineBlock(TileRancher::new));
		BLOCKS.register("mobgrinder", () -> blockMobGrinder = new GenericMachineBlock(TileMobGrinder::new));
		BLOCKS.register("farmer", () -> blockFarmer = new GenericMachineBlock(TileFarmer::new));

	}
}
