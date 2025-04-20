package assemblyline.registers;

import assemblyline.AssemblyLine;
import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.inventory.container.ContainerMobGrinder;
import assemblyline.common.inventory.container.ContainerRancher;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AssemblyLineMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, AssemblyLine.ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerSorterBelt>> CONTAINER_SORTERBELT = register("sorterbelt", ContainerSorterBelt::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerAutocrafter>> CONTAINER_AUTOCRAFTER = register("autocrafter", ContainerAutocrafter::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerBlockPlacer>> CONTAINER_BLOCKPLACER = register("blockplacer", ContainerBlockPlacer::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerBlockBreaker>> CONTAINER_BLOCKBREAKER = register("blockbreaker", ContainerBlockBreaker::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerMobGrinder>> CONTAINER_MOBGRINDER = register("mobgrinder", ContainerMobGrinder::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerRancher>> CONTAINER_RANCHER = register("rancher", ContainerRancher::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerFarmer>> CONTAINER_FARMER = register("farmer", ContainerFarmer::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, MenuSupplier<T> supplier) {
        return MENU_TYPES.register(id, () -> new MenuType<>(supplier, FeatureFlags.VANILLA_SET));
    }

}
