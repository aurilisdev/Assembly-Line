package assemblyline.registers;

import assemblyline.AssemblyLine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AssemblyLineCreativeTabs {

	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AssemblyLine.ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder().title(AssemblyTextUtils.creativeTab("main")).icon(() -> new ItemStack(AssemblyLineBlocks.BLOCK_CONVEYORBELT.get())).build());

}
