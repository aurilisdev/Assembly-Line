package assemblyline.registers;

import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraftforge.eventbus.api.IEventBus;
import voltaic.common.blockitem.BlockItemDescriptable;
import voltaic.prefab.utilities.VoltaicTextUtils;

public class UnifiedAssemblyLineRegister {

	static {

		// MACHINES
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCK_CONVEYORBELT.get(), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCK_SORTERBELT.get(), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.autocrafter), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockplacer), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.blockbreaker), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.rancher), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.mobgrinder), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.farmer), VoltaicTextUtils.voltageTooltip(120));

		// Misc
		//BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCK_DETECTOR, AssemblyTextUtils.tooltip("detector").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.crate), AssemblyTextUtils.tooltip("crate").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.cratemedium), AssemblyTextUtils.tooltip("cratemedium").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(() -> AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getValue(SubtypeAssemblyMachine.cratelarge), AssemblyTextUtils.tooltip("cratelarge").withStyle(ChatFormatting.DARK_GRAY));
	}

	public static void register(IEventBus bus) {
		AssemblyLineBlocks.BLOCKS.register(bus);
		AssemblyLineItems.ITEMS.register(bus);
		AssemblyLineTiles.BLOCK_ENTITY_TYPES.register(bus);
		AssemblyLineMenuTypes.MENU_TYPES.register(bus);
		AssemblyLineSounds.SOUNDS.register(bus);
	}
}
