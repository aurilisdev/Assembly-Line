package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.settings.AssemblyLineConstants;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import voltaic.prefab.screen.GenericScreen;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;

public class ScreenSorterBelt extends GenericScreen<ContainerSorterBelt> {
	public ScreenSorterBelt(ContainerSorterBelt container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);

		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(AssemblyLineConstants.SORTERBELT_USAGE * 20));
	}
}