package assemblyline.client.screen;

import assemblyline.client.screen.generic.AbstractHarvesterScreen;
import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.tile.generic.TileFrontHarvester;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFrontHarvester extends AbstractHarvesterScreen<ContainerFrontHarvester> {

	public ScreenFrontHarvester(ContainerFrontHarvester container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
	}

	@Override
	protected boolean isFlipped() {
		return true;
	}

	@Override
	protected double getProgress(TileFrontHarvester harvester) {
		return 1 - harvester.clientProgress;
	}

	@Override
	protected String getLangKey() {
		return "tooltip.countdown.cooldown";
	}

}
