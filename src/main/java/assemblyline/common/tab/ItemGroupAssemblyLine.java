package assemblyline.common.tab;

import assemblyline.registers.AssemblyLineBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemGroupAssemblyLine extends CreativeModeTab {

	public ItemGroupAssemblyLine(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(AssemblyLineBlocks.blockConveyorBelt);
	}
}