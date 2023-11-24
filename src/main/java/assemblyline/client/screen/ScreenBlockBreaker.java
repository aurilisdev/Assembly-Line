package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.screen.generic.AbstractHarvesterScreen;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.tile.TileBlockBreaker;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.prefab.utils.AssemblyTextUtils;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
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
	protected List<? extends FormattedCharSequence> getTooltip() {
		List<FormattedCharSequence> tips = new ArrayList<>();
		TileBlockBreaker harvester = (TileBlockBreaker) menu.getHostFromIntArray();

		if (harvester != null) {
			tips.add(AssemblyTextUtils.tooltip("breakingprogress", ChatFormatter.getChatDisplayShort(100 * getProgress(harvester), DisplayUnit.PERCENTAGE)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
		}

		return tips;

	}

}
