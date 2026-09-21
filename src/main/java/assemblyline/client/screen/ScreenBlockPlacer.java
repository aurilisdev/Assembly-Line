package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.client.screen.generic.GenericOutlineAreaScreen;
import assemblyline.common.inventory.container.ContainerBlockPlacer;
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
import voltaic.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;

public class ScreenBlockPlacer extends GenericOutlineAreaScreen<ContainerBlockPlacer> {
    public ScreenBlockPlacer(ContainerBlockPlacer container, Inventory playerInventory, Component title) {
	super(container, playerInventory, title);

	addComponent(new ScreenComponentCountdown(this::getTooltip,
		() -> menu.getSafeHost().map(
			placer -> 1.0 - (double) placer.ticksSinceCheck.getValue() / placer.currentWaitTime.getValue())
			.orElse(0.0),
		10, 50));

	addComponent(new ScreenComponentElectricInfo(this::getElectricInformation,
		-AbstractScreenComponentInfo.SIZE + 1, 2));

	addComponent(new ScreenComponentButton<>(10, 20, 60, 20)
		.setLabel(() -> menu.getSafeHost()
			.map(placer -> HandlerHarvesterLines.containsLines(placer.getBlockPos())
				? AssemblyTextUtils.gui("hidearea")
				: AssemblyTextUtils.gui("renderarea"))
			.orElseGet(Component::empty))
		.setOnPress(button -> toggleRendering()));

	new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75,
		82, 8, 72);
    }

    private List<? extends FormattedCharSequence> getElectricInformation() {
	ArrayList<FormattedCharSequence> list = new ArrayList<>();
	menu.getSafeHost().ifPresent(placer -> {
	    ComponentElectrodynamic electro = placer.requireComponent(IComponentType.Electrodynamic);
	    list.add(AssemblyTextUtils
		    .gui("machine.usage", ChatFormatter.getChatDisplayShort(
			    AssemblyLineConfig.getInstance().BLOCKPLACER_USAGE.getAsDouble() * 20, DisplayUnits.WATT))
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

	menu.getSafeHost().ifPresent(placer -> {
	    tips.add(AssemblyTextUtils
		    .tooltip("cooldown", placer.currentWaitTime.getValue() - placer.ticksSinceCheck.getValue())
		    .withStyle(ChatFormatting.GRAY).getVisualOrderText());
	});

	return tips;
    }

}