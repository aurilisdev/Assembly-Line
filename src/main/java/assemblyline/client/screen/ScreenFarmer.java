package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import assemblyline.client.render.event.levelstage.HandlerFarmerLines;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.TileFarmer;
import assemblyline.prefab.utils.AssemblyTextUtils;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.type.ButtonSwappableLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentCountdown;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ScreenFarmer extends GenericScreen<ContainerFarmer> {

	public static final Color[] COLORS = {

			new Color(0, 0, 0, 255), new Color(255, 0, 0, 255), new Color(120, 0, 255, 255), new Color(0, 255, 0, 255), new Color(220, 0, 255, 255), new Color(255, 120, 0, 255), new Color(0, 0, 255, 255), new Color(240, 255, 0, 255), new Color(0, 240, 255, 255)

	};

	public ButtonSwappableLabel renderArea;
	public ButtonSwappableLabel fullBonemeal;
	public ButtonSwappableLabel refillEmpty;

	public ScreenFarmer(ContainerFarmer container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		imageHeight += 58;
		inventoryLabelY += 58;
		addComponent(new ScreenComponentCountdown(() -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return 1 - (float) farmer.ticksSinceCheck.get() / Math.max(farmer.currentWaitTime.get(), 1.0F);
			}
			return 0.0;
		}, 10, 50 + 58));
		addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));

		addComponent(fullBonemeal = new ButtonSwappableLabel(10, 20, 60, 20, AssemblyTextUtils.gui("fullbonemeal"), AssemblyTextUtils.gui("regbonemeal"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return farmer.fullGrowBonemeal.get();
			}
			return false;
		}).setOnPress(button -> menu.toggleBoolean(0)));

		addComponent(refillEmpty = new ButtonSwappableLabel(10, 50, 60, 20, AssemblyTextUtils.gui("refillempty"), AssemblyTextUtils.gui("ignoreempty"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return farmer.refillEmpty.get();
			}
			return false;
		}).setOnPress(button -> menu.toggleBoolean(1)));

		addComponent(renderArea = new ButtonSwappableLabel(10, 80, 60, 20, AssemblyTextUtils.gui("renderarea"), AssemblyTextUtils.gui("hidearea"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return HandlerFarmerLines.isBeingRendered(farmer.getBlockPos());
			}
			return false;
		}).setOnPress(button -> toggleRendering()));

	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileFarmer farmer = menu.getHostFromIntArray();
		if (farmer != null) {
			ComponentElectrodynamic electro = farmer.getComponent(ComponentType.Electrodynamic);
			list.add(AssemblyTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(Constants.FARMER_USAGE * farmer.powerUsageMultiplier.get() * 20, DisplayUnit.WATT).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(AssemblyTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		TileFarmer farmer = menu.getHostFromIntArray();
		if (farmer != null && HandlerFarmerLines.isBeingRendered(farmer.getBlockPos())) {
			HandlerFarmerLines.remove(farmer.getBlockPos());
			updateBox(farmer);
		}
	}

	@Override
	protected ScreenComponentSlot createScreenSlot(Slot slot) {
		ScreenComponentSlot component = super.createScreenSlot(slot);
		int index = slot.index;
		if (index < 9) {
			component.setColor(COLORS[index]);
		}
		return component;
	}

	private void toggleRendering() {
		TileFarmer farmer = menu.getHostFromIntArray();
		if (farmer != null) {
			BlockPos pos = farmer.getBlockPos();
			if (HandlerFarmerLines.isBeingRendered(pos)) {
				HandlerFarmerLines.remove(pos);
			} else {
				updateBox(farmer);
			}
		}
	}

	private void updateBox(TileFarmer farmer) {
		HandlerFarmerLines.addRenderData(farmer.getBlockPos(), Pair.of(COLORS, farmer.getLines(farmer)));
	}

}
