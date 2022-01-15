package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerMobGrinder;
import electrodynamics.prefab.screen.GenericScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenMobGrinder extends GenericScreen<ContainerMobGrinder> {

	public ScreenMobGrinder(ContainerMobGrinder container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
	}

}
