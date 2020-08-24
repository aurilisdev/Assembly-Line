package assemblyline.common.tab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemGroupAssemblyLine extends ItemGroup {

	public ItemGroupAssemblyLine(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(Items.APPLE);
	}
}
