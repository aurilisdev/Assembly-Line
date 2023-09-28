package assemblyline.registers;

import assemblyline.References;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AssemblyLineMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, References.ID);

	public static final RegistryObject<MenuType<ContainerSorterBelt>> CONTAINER_SORTERBELT = register("sorterbelt", ContainerSorterBelt::new);
	public static final RegistryObject<MenuType<ContainerAutocrafter>> CONTAINER_AUTOCRAFTER = register("autocrafter", ContainerAutocrafter::new);
	public static final RegistryObject<MenuType<ContainerBlockPlacer>> CONTAINER_BLOCKPLACER = register("blockplacer", ContainerBlockPlacer::new);
	public static final RegistryObject<MenuType<ContainerBlockBreaker>> CONTAINER_BLOCKBREAKER = register("blockbreaker", ContainerBlockBreaker::new);
	public static final RegistryObject<MenuType<ContainerFrontHarvester>> CONTAINER_HARVESTER = register("harvester", ContainerFrontHarvester::new);
	public static final RegistryObject<MenuType<ContainerFarmer>> CONTAINER_FARMER = register("farmer", ContainerFarmer::new);

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, MenuSupplier<T> supplier) {
		return MENU_TYPES.register(id, () -> new MenuType<>(supplier, FeatureFlags.VANILLA_SET));
	}

}
