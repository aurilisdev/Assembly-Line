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

import assemblyline.prefab.utils.TextUtils;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import net.minecraftforge.eventbus.api.IEventBus;

public class UnifiedAssemblyLineRegister {

	static {

		// MACHINES
		BlockItemDescriptable.addDescription(() -> blockConveyorBelt, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockSorterBelt, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockAutocrafter, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockBlockPlacer, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockBlockBreaker, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockRancher, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockMobGrinder, TextUtils.tooltip("voltage.120"));
		BlockItemDescriptable.addDescription(() -> blockFarmer, TextUtils.tooltip("voltage.120"));

		// Misc
		BlockItemDescriptable.addDescription(() -> blockDetector, TextUtils.tooltip("detector"));
		BlockItemDescriptable.addDescription(() -> blockCrate, TextUtils.tooltip("crate"));
		BlockItemDescriptable.addDescription(() -> blockCrateMedium, TextUtils.tooltip("cratemedium"));
		BlockItemDescriptable.addDescription(() -> blockCrateLarge, TextUtils.tooltip("cratelarge"));
	}

	public static void register(IEventBus bus) {
		AssemblyLineBlocks.BLOCKS.register(bus);
		AssemblyLineItems.ITEMS.register(bus);
		AssemblyLineBlockTypes.BLOCK_ENTITY_TYPES.register(bus);
		AssemblyLineMenuTypes.MENU_TYPES.register(bus);
	}
}
