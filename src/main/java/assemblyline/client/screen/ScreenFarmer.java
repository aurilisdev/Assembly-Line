package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.ClientEvents;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.TileFarmer;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentCountdown;
import electrodynamics.prefab.screen.component.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.ScreenComponentInfo;
import electrodynamics.prefab.screen.component.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.button.ButtonSwappableLabel;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ScreenFarmer extends GenericScreen<ContainerFarmer> {

	private ButtonSwappableLabel renderArea;
	private ButtonSwappableLabel fullBonemeal;
	private ButtonSwappableLabel refillEmpty;

	public ScreenFarmer(ContainerFarmer container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		imageHeight += 58;
		inventoryLabelY += 58;
		components.add(new ScreenComponentCountdown(() -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return 1 - farmer.clientProgress;
			}
			return 0.0;
		}, this, 10, 50 + 58, "tooltip.countdown.cooldown"));
		components.add(new ScreenComponentElectricInfo(this::getElectricInformation, this, -ScreenComponentInfo.SIZE + 1, 2));
	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileFarmer farmer = menu.getHostFromIntArray();
		if (farmer != null) {
			ComponentElectrodynamic electro = farmer.getComponent(ComponentType.Electrodynamic);
			list.add(new TranslatableComponent("gui.machine.usage", new TextComponent(ChatFormatter.getChatDisplayShort(Constants.FARMER_USAGE * farmer.clientUsageMultiplier * 20, DisplayUnit.WATT)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(new TranslatableComponent("gui.machine.voltage", new TextComponent(ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}

	@Override
	protected ScreenComponentSlot createScreenSlot(Slot slot) {
		ScreenComponentSlot component = super.createScreenSlot(slot);
		int index = slot.index;
		if (index < 9) {
			List<Integer> rgba = TileFarmer.COLORS.get(index);
			component.color(RenderingUtils.getRGBA(rgba.get(0), rgba.get(1), rgba.get(2), rgba.get(3)));
		}
		return component;
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		TileFarmer farmer = menu.getHostFromIntArray();
		if (farmer != null && ClientEvents.farmerLines.containsKey(farmer.getBlockPos())) {
			ClientEvents.farmerLines.remove(farmer.getBlockPos());
			updateBox(farmer);
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
		fullBonemeal = new ButtonSwappableLabel(i + 10, j + 20, 60, 20, new TranslatableComponent("label.fullbonemeal"), new TranslatableComponent("label.regbonemeal"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return farmer.clientGrowBonemeal;
			}
			return false;
		}, button -> menu.toggleBoolean(0));
		refillEmpty = new ButtonSwappableLabel(i + 10, j + 20 + 30, 60, 20, new TranslatableComponent("label.refillempty"), new TranslatableComponent("label.ignoreempty"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return farmer.clientRefillEmpty;
			}
			return false;
		}, button -> menu.toggleBoolean(1));
		renderArea = new ButtonSwappableLabel(i + 10, j + 20 + 60, 60, 20, new TranslatableComponent("label.renderarea"), new TranslatableComponent("label.hidearea"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return ClientEvents.farmerLines.containsKey(farmer.getBlockPos());
			}
			return false;
		}, button -> toggleRendering());
		addRenderableWidget(fullBonemeal);
		addRenderableWidget(refillEmpty);
		addRenderableWidget(renderArea);
	}

	private void toggleRendering() {
		TileFarmer harvester = menu.getHostFromIntArray();
		if (harvester != null) {
			BlockPos pos = harvester.getBlockPos();
			if (ClientEvents.farmerLines.containsKey(pos)) {
				ClientEvents.farmerLines.remove(pos);
			} else {
				updateBox(harvester);
			}
		}
	}

	private static void updateBox(TileFarmer farmer) {
		ClientEvents.farmerLines.put(farmer.getBlockPos(), farmer.getLines(farmer));
	}

}
