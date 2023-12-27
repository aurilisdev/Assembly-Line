package assemblyline.registers;

import assemblyline.References;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineMenuTypes {
	public static final DeferredRegister<ContainerType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);

	public static final RegistryObject<ContainerType<ContainerSorterBelt>> CONTAINER_SORTERBELT = MENU_TYPES.register("sorterbelt", () -> new ContainerType<>(ContainerSorterBelt::new));

}
