package assemblyline.client.screen.generic;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.render.event.levelstage.HandlerHarvesterLines;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.prefab.utils.AssemblyTextUtils;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.type.ButtonSwappableLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentCountdown;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractHarvesterScreen<T extends AbstractHarvesterContainer> extends GenericScreen<T> {

	public ButtonSwappableLabel renderArea;

	public AbstractHarvesterScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		addComponent(new ScreenComponentCountdown(this::getTooltip, () -> {
			TileFrontHarvester harvester = menu.getHostFromIntArray();
			if (harvester != null) {
				return getProgress(harvester);
			}
			return 0.0;
		}, 10, 50));
		addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(renderArea = new ButtonSwappableLabel(10, 20, 60, 20, AssemblyTextUtils.gui("renderarea"), AssemblyTextUtils.gui("hidearea"), () -> {
			TileFrontHarvester harvester = menu.getHostFromIntArray();
			if (harvester != null) {
				return HandlerHarvesterLines.containsLines(harvester.getBlockPos());
			}
			return false;
		}).setOnPress(button -> toggleRendering()));
	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if (harvester != null) {
			ComponentElectrodynamic electro = harvester.getComponent(IComponentType.Electrodynamic);
			list.add(AssemblyTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(harvester.getUsage() * harvester.powerUsageMultiplier.get() * 20, DisplayUnit.WATT)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(AssemblyTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if (harvester != null && HandlerHarvesterLines.containsLines(harvester.getBlockPos())) {
			HandlerHarvesterLines.removeLines(harvester.getBlockPos());
			updateBox(harvester);
		}
	}

	private void toggleRendering() {
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if (harvester != null) {
			BlockPos pos = harvester.getBlockPos();
			if (HandlerHarvesterLines.containsLines(pos)) {
				HandlerHarvesterLines.removeLines(pos);
			} else {
				updateBox(harvester);
			}
		}
	}

	protected abstract boolean isFlipped();

	protected abstract double getProgress(TileFrontHarvester harvester);

	protected abstract List<? extends FormattedCharSequence> getTooltip();

	private void updateBox(TileFrontHarvester harvester) {
		HandlerHarvesterLines.addLines(harvester.getBlockPos(), harvester.getAABB(harvester.width.get(), harvester.length.get(), harvester.height.get(), isFlipped(), true, harvester));
	}

}
