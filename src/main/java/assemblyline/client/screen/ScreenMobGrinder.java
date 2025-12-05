package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.client.screen.generic.GenericOutlineAreaScreen;
import assemblyline.common.inventory.container.ContainerMobGrinder;
import assemblyline.common.settings.AssemblyLineConfig;
import assemblyline.common.tile.TileMobGrinder;
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

public class ScreenMobGrinder extends GenericOutlineAreaScreen<ContainerMobGrinder> {

    public ScreenMobGrinder(ContainerMobGrinder container, Inventory inv, Component title) {
        super(container, inv, title);
        addComponent(new ScreenComponentCountdown(this::getTooltip, () -> {
            TileMobGrinder grinder = menu.getSafeHost();
            if (grinder != null) {
                return 1.0 - (double) grinder.ticksSinceCheck.getValue() / (double) grinder.currentWaitTime.getValue();
            }
            return 0.0;
        }, 10, 50));

        addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));

        addComponent(new ScreenComponentButton<>(10, 20, 60, 20).setLabel(() -> {
            TileMobGrinder harvester = menu.getSafeHost();
            if (harvester != null) {
                return HandlerHarvesterLines.containsLines(harvester.getBlockPos()) ? AssemblyTextUtils.gui("hidearea") : AssemblyTextUtils.gui("renderarea");
            }
            return Component.empty();
        }).setOnPress(button -> toggleRendering()));

        new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75, 82, 8, 72);
    }

    private List<? extends FormattedCharSequence> getElectricInformation() {
        ArrayList<FormattedCharSequence> list = new ArrayList<>();
        TileMobGrinder harvester = menu.getSafeHost();
        if (harvester != null) {
            ComponentElectrodynamic electro = harvester.getComponent(IComponentType.Electrodynamic);
            list.add(AssemblyTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(AssemblyLineConfig.INSTANCE.MOBGRINDER_USAGE.getAsDouble()* harvester.powerUsageMultiplier.getValue() * 20, DisplayUnits.WATT)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            list.add(AssemblyTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnits.VOLTAGE)).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
        }
        return list;
    }

    @Override
    public boolean isFlipped() {
        return true;
    }

    protected List<? extends FormattedCharSequence> getTooltip() {
        List<FormattedCharSequence> tips = new ArrayList<>();
        TileMobGrinder harvester = menu.getSafeHost();
        if (harvester != null) {
            tips.add(AssemblyTextUtils.tooltip("cooldown", harvester.currentWaitTime.getValue() - harvester.ticksSinceCheck.getValue()).withStyle(ChatFormatting.GRAY).getVisualOrderText());
        }

        return tips;
    }
}
