package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.client.screen.generic.GenericOutlineAreaScreen;
import assemblyline.common.inventory.container.ContainerBlockBreaker;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.common.tile.TileBlockBreaker;
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

public class ScreenBlockBreaker extends GenericOutlineAreaScreen<ContainerBlockBreaker> {

    public ScreenBlockBreaker(ContainerBlockBreaker screenContainer, Inventory inv, Component titleIn) {
	super(screenContainer, inv, titleIn);

	addComponent(new ScreenComponentCountdown(this::getTooltip, () -> {
	    TileBlockBreaker breaker = menu.getSafeHost();
	    if (breaker != null) {
		return (double) breaker.ticksSinceCheck.getValue() / (double) breaker.currentWaitTime.getValue();
	    }
	    return 0.0;
	}, 10, 50));

	addComponent(new ScreenComponentElectricInfo(this::getElectricInformation,
		-AbstractScreenComponentInfo.SIZE + 1, 2));

	addComponent(new ScreenComponentButton<>(10, 20, 60, 20).setLabel(() -> {
	    TileBlockBreaker harvester = menu.getSafeHost();
	    if (harvester != null) {
		return HandlerHarvesterLines.containsLines(harvester.getBlockPos()) ? AssemblyTextUtils.gui("hidearea")
			: AssemblyTextUtils.gui("renderarea");
	    }
	    return Component.empty();
	}).setOnPress(button -> toggleRendering()));

    }

    private List<? extends FormattedCharSequence> getElectricInformation() {
	ArrayList<FormattedCharSequence> list = new ArrayList<>();
	TileBlockBreaker harvester = menu.getSafeHost();
	if (harvester != null) {
	    ComponentElectrodynamic electro = harvester.getComponent(IComponentType.Electrodynamic);
	    list.add(AssemblyTextUtils
		    .gui("machine.usage",
			    ChatFormatter.getChatDisplayShort(AssemblyLineConstants.BLOCKBREAKER_USAGE * 20,
				    DisplayUnits.WATT))
		    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
	    list.add(AssemblyTextUtils
		    .gui("machine.voltage",
			    ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnits.VOLTAGE))
		    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
	}
	return list;
    }

    @Override
    public boolean isFlipped() {
	return true;
    }

    protected List<? extends FormattedCharSequence> getTooltip() {
	List<FormattedCharSequence> tips = new ArrayList<>();
	TileBlockBreaker breaker = menu.getSafeHost();

	if (breaker != null) {
	    tips.add(AssemblyTextUtils
		    .tooltip("breakingprogress",
			    ChatFormatter.getChatDisplayShort(100.0 * (double) breaker.ticksSinceCheck.getValue()
				    / (double) breaker.currentWaitTime.getValue(), DisplayUnits.PERCENTAGE))
		    .withStyle(ChatFormatting.GRAY).getVisualOrderText());
	}

	return tips;

    }

}
