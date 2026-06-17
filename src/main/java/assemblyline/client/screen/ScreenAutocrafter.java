package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.settings.AssemblyLineConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import voltaic.prefab.screen.GenericScreen;
import voltaic.prefab.screen.component.ScreenComponentGeneric;
import voltaic.prefab.screen.component.types.ScreenComponentProgress;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;

public class ScreenAutocrafter extends GenericScreen<ContainerAutocrafter> {

    public ScreenAutocrafter(ContainerAutocrafter container, Inventory playerInventory, Component title) {
	super(container, playerInventory, title);

	addComponent(new ScreenComponentGeneric(ScreenComponentProgress.ProgressTextures.ARROW_RIGHT_OFF, 80, 34));
	addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2)
		.wattage(AssemblyLineConstants.AUTOCRAFTER_USAGE));
	new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75,
		82, 8, 72);
    }

}