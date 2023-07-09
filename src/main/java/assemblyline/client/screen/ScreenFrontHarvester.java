package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.screen.generic.AbstractHarvesterScreen;
import assemblyline.common.inventory.container.ContainerFrontHarvester;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
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
		return 1 - harvester.getProgress();
	}

	@Override
	protected List<? extends FormattedCharSequence> getTooltip() {
		List<FormattedCharSequence> tips = new ArrayList<>();
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if (harvester != null) {
			tips.add(AssemblyTextUtils.tooltip("cooldown", harvester.currentWaitTime.get() - harvester.ticksSinceCheck.get()).withStyle(ChatFormatting.GRAY).getVisualOrderText());
		}

		return tips;
	}

}
