package assemblyline.registers;

import static assemblyline.registers.AssemblyLineBlocks.blockConveyorBelt;
import static assemblyline.registers.AssemblyLineBlocks.blockCrate;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateLarge;
import static assemblyline.registers.AssemblyLineBlocks.blockCrateMedium;
import static assemblyline.registers.AssemblyLineBlocks.blockDetector;
import static assemblyline.registers.AssemblyLineBlocks.blockSorterBelt;

import assemblyline.prefab.utils.AssemblyTextUtils;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraftforge.eventbus.api.IEventBus;

public class UnifiedAssemblyLineRegister {

	static {

		// MACHINES
		BlockItemDescriptable.addDescription(() -> blockConveyorBelt, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockSorterBelt, ElectroTextUtils.voltageTooltip(120));

		// Misc
		BlockItemDescriptable.addDescription(() -> blockDetector, AssemblyTextUtils.tooltip("detector"));
		BlockItemDescriptable.addDescription(() -> blockCrate, AssemblyTextUtils.tooltip("crate"));
		BlockItemDescriptable.addDescription(() -> blockCrateMedium, AssemblyTextUtils.tooltip("cratemedium"));
		BlockItemDescriptable.addDescription(() -> blockCrateLarge, AssemblyTextUtils.tooltip("cratelarge"));
	}

	public static void register(IEventBus bus) {
		AssemblyLineBlocks.BLOCKS.register(bus);
		AssemblyLineItems.ITEMS.register(bus);
		AssemblyLineBlockTypes.BLOCK_ENTITY_TYPES.register(bus);
		AssemblyLineMenuTypes.MENU_TYPES.register(bus);
	}
}
