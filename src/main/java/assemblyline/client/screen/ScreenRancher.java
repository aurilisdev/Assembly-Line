package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.client.screen.generic.GenericOutlineAreaScreen;
import assemblyline.common.inventory.container.ContainerRancher;
import assemblyline.common.settings.AssemblyLineConfig;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import voltaic.api.electricity.formatting.ChatFormatter;
import voltaic.api.electricity.formatting.DisplayUnits;
import voltaic.prefab.screen.component.button.ScreenComponentButton;
import voltaic.prefab.screen.component.types.ScreenComponentCountdown;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;

public class ScreenRancher extends GenericOutlineAreaScreen<ContainerRancher> {

    public ScreenRancher(ContainerRancher container, Inventory inv, Component title) {
	super(container, inv, title);
	addComponent(new ScreenComponentCountdown(this::getTooltip, () -> menu.getSafeHost()
		.map(rancher -> 1.0 - (double) rancher.ticksSinceCheck.getValue() / rancher.currentWaitTime.getValue())
		.orElse(0.0), 10, 50));

	addComponent(new ScreenComponentElectricInfo(this::getElectricInformation,
		-AbstractScreenComponentInfo.SIZE + 1, 2));

	addComponent(new ScreenComponentButton<>(10, 20, 60, 20)
		.setLabel(() -> menu.getSafeHost()
			.map(rancher -> HandlerHarvesterLines.containsLines(rancher.getBlockPos())
				? AssemblyTextUtils.gui("hidearea")
				: AssemblyTextUtils.gui("renderarea"))
			.orElseGet(Component::empty))
		.setOnPress(button -> toggleRendering()));
    }

    private List<? extends FormattedCharSequence> getElectricInformation() {
	ArrayList<FormattedCharSequence> list = new ArrayList<>();
	menu.getSafeHost().ifPresent(harvester -> {
	    ComponentElectrodynamic electro = harvester.requireComponent(IComponentType.Electrodynamic);
	    list.add(
		    AssemblyTextUtils
			    .gui("machine.usage",
				    ChatFormatter.getChatDisplayShort(
					    AssemblyLineConfig.getInstance().RANCHER_USAGE.getAsDouble()
						    * harvester.powerUsageMultiplier.getValue() * 20,
					    DisplayUnits.WATT))
			    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
	    list.add(AssemblyTextUtils
		    .gui("machine.voltage",
			    ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnits.VOLTAGE))
		    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
	});
	return list;
    }

    @Override
    public boolean isFlipped() {
	return true;
    }

    protected List<? extends FormattedCharSequence> getTooltip() {
	List<FormattedCharSequence> tips = new ArrayList<>();
	menu.getSafeHost().ifPresent(harvester -> {
	    tips.add(AssemblyTextUtils
		    .tooltip("cooldown", harvester.currentWaitTime.getValue() - harvester.ticksSinceCheck.getValue())
		    .withStyle(ChatFormatting.GRAY).getVisualOrderText());
	});
	return tips;
    }
}
