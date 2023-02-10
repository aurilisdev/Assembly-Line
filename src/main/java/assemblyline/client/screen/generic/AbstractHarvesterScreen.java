package assemblyline.client.screen.generic;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.render.event.levelstage.HandlerHarvesterLines;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.tile.generic.TileFrontHarvester;
import assemblyline.prefab.utils.TextUtils;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentCountdown;
import electrodynamics.prefab.screen.component.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.button.ButtonSwappableLabel;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractHarvesterScreen<T extends AbstractHarvesterContainer> extends GenericScreen<T> {

	protected AbstractHarvesterScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		components.add(new ScreenComponentCountdown(this::getTooltip, () -> {
			TileFrontHarvester harvester = menu.getHostFromIntArray();
			if (harvester != null) {
				return getProgress(harvester);
			}
			return 0.0;
		}, this, 10, 50));
		components.add(new ScreenComponentElectricInfo(this::getElectricInformation, this, -AbstractScreenComponentInfo.SIZE + 1, 2));
	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if (harvester != null) {
			ComponentElectrodynamic electro = harvester.getComponent(ComponentType.Electrodynamic);
			list.add(TextUtils.gui("machine.usage", Component.literal(ChatFormatter.getChatDisplayShort(harvester.getUsage() * harvester.powerUsageMultiplier.get() * 20, DisplayUnit.WATT)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(TextUtils.gui("machine.voltage", Component.literal(ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
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

	@Override
	protected void init() {
		super.init();
		initButtons();
	}

	protected void initButtons() {
		int i = (width - imageWidth) / 2;
		int j = (height - imageHeight) / 2;
		ButtonSwappableLabel renderArea = new ButtonSwappableLabel(i + 10, j + 20, 60, 20, TextUtils.gui("renderarea"), TextUtils.gui("hidearea"), () -> {
			TileFrontHarvester harvester = menu.getHostFromIntArray();
			if (harvester != null) {
				return HandlerHarvesterLines.containsLines(harvester.getBlockPos());
			}
			return false;
		}, button -> toggleRendering());
		addRenderableWidget(renderArea);
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
