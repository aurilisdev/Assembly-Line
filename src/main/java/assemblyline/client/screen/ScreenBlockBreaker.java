package assemblyline.client.screen;

import assemblyline.client.screen.generic.AbstractHarvesterScreen;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.tile.generic.TileFrontHarvester;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenBlockBreaker extends AbstractHarvesterScreen<ContainerBlockBreaker> {

	public ScreenBlockBreaker(ContainerBlockBreaker screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected boolean isFlipped() {
		return true;
	}

	@Override
	protected double getProgress(TileFrontHarvester harvester) {
		return harvester.getProgress();
	}

	@Override
	protected String getLangKey() {
		return "tooltip.countdown.progress";
	}

}
