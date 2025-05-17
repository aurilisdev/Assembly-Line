package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.client.screen.generic.GenericOutlineAreaScreen;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.common.tile.TileBlockPlacer;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import voltaic.api.electricity.formatting.ChatFormatter;
import voltaic.api.electricity.formatting.DisplayUnits;
import voltaic.prefab.screen.component.button.ScreenComponentButton;
import voltaic.prefab.screen.component.types.ScreenComponentCountdown;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.utilities.VoltaicTextUtils;

public class ScreenBlockPlacer extends GenericOutlineAreaScreen<ContainerBlockPlacer> {
	
	public ScreenBlockPlacer(ContainerBlockPlacer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);

		addComponent(new ScreenComponentCountdown(this::getTooltip, () -> {
			TileBlockPlacer placer = menu.getSafeHost();
			if (placer != null) {
				return 1.0 - (double) placer.ticksSinceCheck.getValue() / (double) placer.currentWaitTime.getValue();
			}
			return 0.0;
		}, 10, 50));

		addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));

		addComponent(new ScreenComponentButton<>(10, 20, 60, 20).setLabel(() -> {
			TileBlockPlacer harvester = menu.getSafeHost();
			if (harvester != null) {
				return HandlerHarvesterLines.containsLines(harvester.getBlockPos()) ? AssemblyTextUtils.gui("hidearea") : AssemblyTextUtils.gui("renderarea");
			}
			return VoltaicTextUtils.empty();
		}).setOnPress(button -> toggleRendering()));

		new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75, 82, 8, 72);
	}

	private List<? extends IReorderingProcessor> getElectricInformation() {
		ArrayList<IReorderingProcessor> list = new ArrayList<>();
		TileBlockPlacer harvester = menu.getSafeHost();
		if (harvester != null) {
			ComponentElectrodynamic electro = harvester.getComponent(IComponentType.Electrodynamic);
			list.add(AssemblyTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(AssemblyLineConstants.BLOCKPLACER_USAGE * 20, DisplayUnits.WATT)).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.DARK_GRAY).getVisualOrderText());
			list.add(AssemblyTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnits.VOLTAGE)).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}

	@Override
	public boolean isFlipped() {
		return true;
	}

	protected List<? extends IReorderingProcessor> getTooltip() {
		TileBlockPlacer placer = menu.getSafeHost();
		List<IReorderingProcessor> tips = new ArrayList<>();

		if (placer != null) {
			tips.add(AssemblyTextUtils.tooltip("cooldown", placer.currentWaitTime.getValue() - placer.ticksSinceCheck.getValue()).withStyle(TextFormatting.GRAY).getVisualOrderText());
		}

		return tips;
	}

}