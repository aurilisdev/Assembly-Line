package assemblyline.client.screen;

import assemblyline.client.screen.generic.AbstractHarvesterScreen;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.tile.generic.TileFrontHarvester;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBlockPlacer extends AbstractHarvesterScreen<ContainerBlockPlacer> {
	public ScreenBlockPlacer(ContainerBlockPlacer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
	}

	@Override
	protected boolean isFlipped() {
		return true;
	}

	@Override
	protected double getProgress(TileFrontHarvester harvester) {
		return 1 - harvester.getProgress();
	}

	@Override
	protected String getLangKey() {
		return "tooltip.countdown.cooldown";
	}

}