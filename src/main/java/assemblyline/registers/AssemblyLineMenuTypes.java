package assemblyline.registers;

import assemblyline.References;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AssemblyLineMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);

	public static final RegistryObject<MenuType<ContainerSorterBelt>> CONTAINER_SORTERBELT = MENU_TYPES.register("sorterbelt", () -> new MenuType<>(ContainerSorterBelt::new));
	public static final RegistryObject<MenuType<ContainerAutocrafter>> CONTAINER_AUTOCRAFTER = MENU_TYPES.register("autocrafter", () -> new MenuType<>(ContainerAutocrafter::new));
	public static final RegistryObject<MenuType<ContainerBlockPlacer>> CONTAINER_BLOCKPLACER = MENU_TYPES.register("blockplacer", () -> new MenuType<>(ContainerBlockPlacer::new));
	public static final RegistryObject<MenuType<ContainerBlockBreaker>> CONTAINER_BLOCKBREAKER = MENU_TYPES.register("blockbreaker", () -> new MenuType<>(ContainerBlockBreaker::new));
	public static final RegistryObject<MenuType<ContainerFrontHarvester>> CONTAINER_HARVESTER = MENU_TYPES.register("harvester", () -> new MenuType<>(ContainerFrontHarvester::new));
	public static final RegistryObject<MenuType<ContainerFarmer>> CONTAINER_FARMER = MENU_TYPES.register("farmer", () -> new MenuType<>(ContainerFarmer::new));

}
