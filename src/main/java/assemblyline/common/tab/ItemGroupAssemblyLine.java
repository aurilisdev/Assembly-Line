package assemblyline.common.tab;

import assemblyline.registers.AssemblyLineItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupAssemblyLine extends ItemGroup {

	public ItemGroupAssemblyLine(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(AssemblyLineItems.ITEM_CONVEYORBELT.get());
	}
}