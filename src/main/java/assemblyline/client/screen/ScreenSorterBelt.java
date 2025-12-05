package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.settings.AssemblyLineConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import voltaic.prefab.screen.GenericScreen;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;

public class ScreenSorterBelt extends GenericScreen<ContainerSorterBelt> {
	public ScreenSorterBelt(ContainerSorterBelt container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);

		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(AssemblyLineConfig.INSTANCE.SORTERBELT_USAGE.getAsDouble() * 20));
	}
}