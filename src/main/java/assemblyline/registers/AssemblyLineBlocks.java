package assemblyline.registers;

import assemblyline.References;
import assemblyline.common.block.BlockConveyorBelt;
import assemblyline.common.block.BlockDetector;
import assemblyline.common.block.BlockSorterBelt;
import assemblyline.common.tile.TileCrate;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.block.Block;
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
	
	static {
		BLOCKS.register("conveyorbelt", () -> blockConveyorBelt = new BlockConveyorBelt());
		BLOCKS.register("sorterbelt", () -> blockSorterBelt = new BlockSorterBelt());
		BLOCKS.register("detector", () -> blockDetector = new BlockDetector());
		BLOCKS.register("crate", () -> blockCrate = new GenericMachineBlock(world -> new TileCrate()));
		BLOCKS.register("cratemedium", () -> blockCrateMedium = new GenericMachineBlock(world -> new TileCrate()));
		BLOCKS.register("cratelarge", () -> blockCrateLarge = new GenericMachineBlock(world -> new TileCrate()));
	}
}
