package assemblyline.common.tab;

import assemblyline.DeferredRegisters;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupAssemblyLine extends ItemGroup {

    public ItemGroupAssemblyLine(String label) {
	super(label);
    }

    @Override
    public ItemStack createIcon() {
	return new ItemStack(DeferredRegisters.blockConveyorBelt);
    }
}