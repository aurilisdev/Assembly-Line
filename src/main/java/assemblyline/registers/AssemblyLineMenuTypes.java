package assemblyline.registers;

import assemblyline.AssemblyLine;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.inventory.container.ContainerMobGrinder;
import assemblyline.common.inventory.container.ContainerRancher;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyLineMenuTypes {
    public static final DeferredRegister<ContainerType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, AssemblyLine.ID);

    public static final RegistryObject<ContainerType<ContainerSorterBelt>> CONTAINER_SORTERBELT = register("sorterbelt", ContainerSorterBelt::new);
    public static final RegistryObject<ContainerType<ContainerAutocrafter>> CONTAINER_AUTOCRAFTER = register("autocrafter", ContainerAutocrafter::new);
    public static final RegistryObject<ContainerType<ContainerBlockPlacer>> CONTAINER_BLOCKPLACER = register("blockplacer", ContainerBlockPlacer::new);
    public static final RegistryObject<ContainerType<ContainerBlockBreaker>> CONTAINER_BLOCKBREAKER = register("blockbreaker", ContainerBlockBreaker::new);
    public static final RegistryObject<ContainerType<ContainerMobGrinder>> CONTAINER_MOBGRINDER = register("mobgrinder", ContainerMobGrinder::new);
    public static final RegistryObject<ContainerType<ContainerRancher>> CONTAINER_RANCHER = register("rancher", ContainerRancher::new);
    public static final RegistryObject<ContainerType<ContainerFarmer>> CONTAINER_FARMER = register("farmer", ContainerFarmer::new);

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> supplier) {
        return MENU_TYPES.register(id, () -> new ContainerType<>(supplier));
    }

}
