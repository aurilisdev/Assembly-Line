package assemblyline.registers;

import assemblyline.References;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AssemblyLineCreativeTabs {

	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, References.ID);

	public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder().title(AssemblyTextUtils.creativeTab("main")).icon(() -> new ItemStack(AssemblyLineBlocks.blockConveyorBelt)).build());

}
