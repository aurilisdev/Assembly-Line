package assemblyline.registers;

import assemblyline.References;
import assemblyline.common.block.BlockBlockBreaker;
import assemblyline.common.block.BlockBlockPlacer;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockCrate;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockFarmer;
import assemblyline.common.block.BlockMobGrinder;
import assemblyline.common.block.BlockRancher;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.tile.TileAutocrafter;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
	public static BlockConveyorBelt blockConveyorBelt;
	public static BlockSorterBelt blockSorterBelt;
	public static BlockDetector blockDetector;
	public static BlockCrate blockCrate;
	public static BlockCrate blockCrateMedium;
	public static BlockCrate blockCrateLarge;
	public static GenericMachineBlock blockAutocrafter;
	public static BlockBlockBreaker blockBlockBreaker;
	public static BlockBlockPlacer blockBlockPlacer;
	public static BlockRancher blockRancher;
	public static BlockMobGrinder blockMobGrinder;
	public static BlockFarmer blockFarmer;
	static {
		BLOCKS.register("conveyorbelt", () -> blockConveyorBelt = new BlockConveyorBelt());
		BLOCKS.register("sorterbelt", () -> blockSorterBelt = new BlockSorterBelt());
		BLOCKS.register("detector", () -> blockDetector = new BlockDetector());
		BLOCKS.register("crate", () -> blockCrate = new BlockCrate(64));
		BLOCKS.register("cratemedium", () -> blockCrateMedium = new BlockCrate(128));
		BLOCKS.register("cratelarge", () -> blockCrateLarge = new BlockCrate(256));
		BLOCKS.register("autocrafter", () -> blockAutocrafter = new GenericMachineBlock(TileAutocrafter::new));
		BLOCKS.register("blockbreaker", () -> blockBlockBreaker = new BlockBlockBreaker());
		BLOCKS.register("blockplacer", () -> blockBlockPlacer = new BlockBlockPlacer());
		BLOCKS.register("rancher", () -> blockRancher = new BlockRancher());
		BLOCKS.register("mobgrinder", () -> blockMobGrinder = new BlockMobGrinder());
		BLOCKS.register("farmer", () -> blockFarmer = new BlockFarmer());

	}
}
