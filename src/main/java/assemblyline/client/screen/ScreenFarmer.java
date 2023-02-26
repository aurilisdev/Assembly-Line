package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import assemblyline.client.render.event.levelstage.HandlerFarmerLines;
import assemblyline.common.inventory.container.ContainerFarmer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.TileFarmer;
import assemblyline.prefab.utils.TextUtils;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentCountdown;
import electrodynamics.prefab.screen.component.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.button.ButtonSwappableLabel;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ScreenFarmer extends GenericScreen<ContainerFarmer> {

	public static int[] COLORS = {

			RenderingUtils.getRGBA(255, 0, 0, 0), RenderingUtils.getRGBA(255, 255, 0, 0), RenderingUtils.getRGBA(255, 120, 0, 255), RenderingUtils.getRGBA(255, 0, 255, 0), RenderingUtils.getRGBA(255, 220, 0, 255), RenderingUtils.getRGBA(255, 255, 120, 0), RenderingUtils.getRGBA(255, 0, 0, 255), RenderingUtils.getRGBA(255, 240, 255, 0), RenderingUtils.getRGBA(255, 0, 240, 255)

	};

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
				return 1 - (float) farmer.ticksSinceCheck.get() / Math.max(farmer.currentWaitTime.get(), 1.0F);
			}
			return 0.0;
		}, this, 10, 50 + 58));
		components.add(new ScreenComponentElectricInfo(this::getElectricInformation, this, -AbstractScreenComponentInfo.SIZE + 1, 2));
	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileFarmer farmer = menu.getHostFromIntArray();
		if (farmer != null) {
			ComponentElectrodynamic electro = farmer.getComponent(ComponentType.Electrodynamic);
			list.add(TextUtils.gui("machine.usage", Component.literal(ChatFormatter.getChatDisplayShort(Constants.FARMER_USAGE * farmer.powerUsageMultiplier.get() * 20, DisplayUnit.WATT)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(TextUtils.gui("machine.voltage", Component.literal(ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
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

	@Override
	protected void init() {
		super.init();
		initButtons();
	}

	protected void initButtons() {
		int i = (width - imageWidth) / 2;
		int j = (height - imageHeight) / 2;
		fullBonemeal = new ButtonSwappableLabel(i + 10, j + 20, 60, 20, TextUtils.gui("fullbonemeal"), TextUtils.gui("regbonemeal"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return farmer.fullGrowBonemeal.get();
			}
			return false;
		}, button -> menu.toggleBoolean(0));
		refillEmpty = new ButtonSwappableLabel(i + 10, j + 20 + 30, 60, 20, TextUtils.gui("refillempty"), TextUtils.gui("ignoreempty"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return farmer.refillEmpty.get();
			}
			return false;
		}, button -> menu.toggleBoolean(1));
		renderArea = new ButtonSwappableLabel(i + 10, j + 20 + 60, 60, 20, TextUtils.gui("renderarea"), TextUtils.gui("hidearea"), () -> {
			TileFarmer farmer = menu.getHostFromIntArray();
			if (farmer != null) {
				return HandlerFarmerLines.isBeingRendered(farmer.getBlockPos());
			}
			return false;
		}, button -> toggleRendering());
		addRenderableWidget(fullBonemeal);
		addRenderableWidget(refillEmpty);
		addRenderableWidget(renderArea);
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
