package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerRancher;
import electrodynamics.prefab.screen.GenericScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenRancher extends GenericScreen<ContainerRancher> {

	public ScreenRancher(ContainerRancher container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
	}

}
