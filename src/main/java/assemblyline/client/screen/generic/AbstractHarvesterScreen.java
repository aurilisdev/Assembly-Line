package assemblyline.client.screen.generic;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.ClientEvents;
import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import assemblyline.common.tile.generic.TileFrontHarvester;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.ElectricUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ButtonSwappableLabel;
import electrodynamics.prefab.screen.component.gui.ScreenComponentInfo;
import electrodynamics.prefab.screen.component.gui.type.ScreenComponentCountdown;
import electrodynamics.prefab.screen.component.gui.type.ScreenComponentElectricInfo;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractHarvesterScreen<T extends AbstractHarvesterContainer> extends GenericScreen<T>  {

	private ButtonSwappableLabel renderArea;
	
	public AbstractHarvesterScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		components.add(new ScreenComponentCountdown(() -> { 
			TileFrontHarvester harvester = menu.getHostFromIntArray();
			if(harvester != null) {
				return getProgress(harvester);
			}
			return 0.0;
		}, this, 10, 50, getLangKey()));
		components.add(new ScreenComponentElectricInfo(this::getElectricInformation, this, -ScreenComponentInfo.SIZE + 1, 2));
	}
	
	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if(harvester != null) {
			ComponentElectrodynamic electro = harvester.getComponent(ComponentType.Electrodynamic);
			list.add(new TranslatableComponent("gui.machine.usage",
					new TextComponent(ChatFormatter.getElectricDisplayShort(harvester.getUsage() * harvester.clientUsageMultiplier * 20, ElectricUnit.WATT))
							.withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(new TranslatableComponent("gui.machine.voltage",
					new TextComponent(ChatFormatter.getElectricDisplayShort(electro.getVoltage(), ElectricUnit.VOLTAGE))
							.withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}
	
	@Override
	protected void containerTick() {
		super.containerTick();
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if(harvester != null && ClientEvents.outlines.containsKey(harvester.getBlockPos())) {
			ClientEvents.outlines.remove(harvester.getBlockPos());
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
		renderArea = new ButtonSwappableLabel(i + 10, j + 20, 60, 20, new TranslatableComponent("label.renderarea"), 
			new TranslatableComponent("label.hidearea"), () -> {
				TileFrontHarvester harvester = menu.getHostFromIntArray();
				if(harvester != null) {
					return ClientEvents.outlines.containsKey(harvester.getBlockPos());
				}
				return false;
			}, (button) -> {
			    toggleRendering();
			}
			);
		addRenderableWidget(renderArea);
	}
	
	private void toggleRendering() {
		TileFrontHarvester harvester = menu.getHostFromIntArray();
		if(harvester != null) {
			BlockPos pos = harvester.getBlockPos();
			if(ClientEvents.outlines.containsKey(pos)) {
				ClientEvents.outlines.remove(pos);
			} else {
				updateBox(harvester);
			}
		}
	}
	
	
	private void updateBox(TileFrontHarvester harvester) {
		ClientEvents.outlines.put(harvester.getBlockPos(), 
			harvester.getAABB(harvester.clientWidth, harvester.clientLength, harvester.clientHeight, isFlipped(), true, harvester));
	}

	protected abstract boolean isFlipped();
	
	protected abstract double getProgress(TileFrontHarvester harvester);
	
	protected abstract String getLangKey(); 

}
