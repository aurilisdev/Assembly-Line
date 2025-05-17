package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.client.event.levelstage.HandlerHarvesterLines;
import assemblyline.client.screen.generic.GenericOutlineAreaScreen;
import assemblyline.common.inventory.container.ContainerRancher;
import assemblyline.common.settings.AssemblyLineConstants;
import assemblyline.common.tile.TileRancher;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import voltaic.api.electricity.formatting.ChatFormatter;
import voltaic.api.electricity.formatting.DisplayUnits;
import voltaic.prefab.screen.component.button.ScreenComponentButton;
import voltaic.prefab.screen.component.types.ScreenComponentCountdown;
import voltaic.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import voltaic.prefab.screen.component.utils.AbstractScreenComponentInfo;
import voltaic.prefab.tile.components.IComponentType;
import voltaic.prefab.tile.components.type.ComponentElectrodynamic;
import voltaic.prefab.utilities.VoltaicTextUtils;

public class ScreenRancher extends GenericOutlineAreaScreen<ContainerRancher> {

    public ScreenRancher(ContainerRancher container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);

        addComponent(new ScreenComponentCountdown(this::getTooltip, () -> {
            TileRancher rancher = menu.getSafeHost();
            if (rancher != null) {
                return 1.0 - (double) rancher.ticksSinceCheck.getValue() / (double) rancher.currentWaitTime.getValue();
            }
            return 0.0;
        }, 10, 50));
        addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));
        addComponent(new ScreenComponentButton<>(10, 20, 60, 20).setLabel(() -> {
            TileRancher harvester = menu.getSafeHost();
            if (harvester != null) {
                return HandlerHarvesterLines.containsLines(harvester.getBlockPos()) ? AssemblyTextUtils.gui("hidearea") : AssemblyTextUtils.gui("renderarea");
            }
            return VoltaicTextUtils.empty();
        }).setOnPress(button -> toggleRendering()));
    }

    private List<? extends IReorderingProcessor> getElectricInformation() {
        ArrayList<IReorderingProcessor> list = new ArrayList<>();
        TileRancher harvester = menu.getSafeHost();
        if (harvester != null) {
            ComponentElectrodynamic electro = harvester.getComponent(IComponentType.Electrodynamic);
            list.add(AssemblyTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(AssemblyLineConstants.RANCHER_USAGE * harvester.powerUsageMultiplier.getValue() * 20, DisplayUnits.WATT)).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.DARK_GRAY).getVisualOrderText());
            list.add(AssemblyTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnits.VOLTAGE)).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.DARK_GRAY).getVisualOrderText());
        }
        return list;
    }

    @Override
    public boolean isFlipped() {
        return true;
    }

    protected List<? extends IReorderingProcessor> getTooltip() {
        List<IReorderingProcessor> tips = new ArrayList<>();
        TileRancher harvester = menu.getSafeHost();
        if (harvester != null) {
            tips.add(AssemblyTextUtils.tooltip("cooldown", harvester.currentWaitTime.getValue() - harvester.ticksSinceCheck.getValue()).withStyle(TextFormatting.GRAY).getVisualOrderText());
        }

        return tips;
    }
}
