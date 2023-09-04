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

import assemblyline.prefab.utils.AssemblyTextUtils;
import electrodynamics.common.blockitem.types.BlockItemDescriptable;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraftforge.eventbus.api.IEventBus;

public class UnifiedAssemblyLineRegister {

	static {

		// MACHINES
		BlockItemDescriptable.addDescription(() -> blockConveyorBelt, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockSorterBelt, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockAutocrafter, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockBlockPlacer, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockBlockBreaker, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockRancher, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockMobGrinder, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> blockFarmer, ElectroTextUtils.voltageTooltip(120));

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
		AssemblyLineCreativeTabs.CREATIVE_TABS.register(bus);
	}
}
