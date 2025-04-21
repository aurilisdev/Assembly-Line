package assemblyline.registers;

import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.ChatFormatting;
import net.neoforged.bus.api.IEventBus;
import voltaic.common.blockitem.BlockItemDescriptable;
import voltaic.prefab.utilities.VoltaicTextUtils;

public class UnifiedAssemblyLineRegister {

	static {

		// MACHINES
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCK_CONVEYORBELT, VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCK_SORTERBELT, VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.autocrafter), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.blockplacer), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.blockbreaker), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.rancher), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.mobgrinder), VoltaicTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.farmer), VoltaicTextUtils.voltageTooltip(120));

		// Misc
		//BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCK_DETECTOR, AssemblyTextUtils.tooltip("detector").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.crate), AssemblyTextUtils.tooltip("crate").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.cratemedium), AssemblyTextUtils.tooltip("cratemedium").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(AssemblyLineBlocks.BLOCKS_ASSEMBLYMACHINES.getHolder(SubtypeAssemblyMachine.cratelarge), AssemblyTextUtils.tooltip("cratelarge").withStyle(ChatFormatting.DARK_GRAY));
	}

	public static void register(IEventBus bus) {
		AssemblyLineAttachmentTypes.ATTACHMENT_TYPES.register(bus);
		AssemblyLineBlocks.BLOCKS.register(bus);
		AssemblyLineItems.ITEMS.register(bus);
		AssemblyLineTiles.BLOCK_ENTITY_TYPES.register(bus);
		AssemblyLineMenuTypes.MENU_TYPES.register(bus);
		AssemblyLineCreativeTabs.CREATIVE_TABS.register(bus);
		AssemblyLineSounds.SOUNDS.register(bus);
	}
}
