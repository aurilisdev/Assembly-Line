package assemblyline.registers;

import static assemblyline.registers.AssemblyLineBlocks.blockAutocrafter;
import static assemblyline.registers.AssemblyLineBlocks.blockBlockBreaker;
import static assemblyline.registers.AssemblyLineBlocks.blockBlockPlacer;
import static assemblyline.registers.AssemblyLineBlocks.blockConveyorBelt;
import static assemblyline.registers.AssemblyLineBlocks.blockCrate;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateLarge;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateMedium;
import static assemblyline.registers.AssemblyLineBlocks.blockDetector;
import static assemblyline.registers.AssemblyLineBlocks.blockFarmer;
import static assemblyline.registers.AssemblyLineBlocks.blockMobGrinder;
import static assemblyline.registers.AssemblyLineBlocks.blockRancher;
import static assemblyline.registers.AssemblyLineBlocks.blockSorterBelt;

import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraftforge.eventbus.api.IEventBus;

public class UnifiedAssemblyLineRegister {

	static {

		// MACHINES
		BlockItemDescriptable.addDescription(() -> blockConveyorBelt, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockSorterBelt, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockAutocrafter, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockBlockPlacer, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockBlockBreaker, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockRancher, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockMobGrinder, "|translate|tooltip.voltage.120");
		BlockItemDescriptable.addDescription(() -> blockFarmer, "|translate|tooltip.voltage.120");

		// Misc
		BlockItemDescriptable.addDescription(() -> blockDetector, "|translate|tooltip.detector");
		BlockItemDescriptable.addDescription(() -> blockCrate, "|translate|tooltip.crate");
		BlockItemDescriptable.addDescription(() -> blockCrateMedium, "|translate|tooltip.cratemedium");
		BlockItemDescriptable.addDescription(() -> blockCrateLarge, "|translate|tooltip.cratelarge");
	}

	public static void register(IEventBus bus) {
		AssemblyLineBlocks.BLOCKS.register(bus);
		AssemblyLineItems.ITEMS.register(bus);
		AssemblyLineBlockTypes.BLOCK_ENTITY_TYPES.register(bus);
		AssemblyLineMenuTypes.MENU_TYPES.register(bus);
	}
}
