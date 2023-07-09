package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.screen.generic.AbstractHarvesterScreen;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
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
	protected List<? extends FormattedCharSequence> getTooltip() {
		TileBlockPlacer placer = (TileBlockPlacer) menu.getHostFromIntArray();
		List<FormattedCharSequence> tips = new ArrayList<>();

		if (placer != null) {
			tips.add(AssemblyTextUtils.tooltip("cooldown", placer.currentWaitTime.get() - placer.ticksSinceCheck.get()).withStyle(ChatFormatting.GRAY).getVisualOrderText());
		}

		return tips;
	}

}