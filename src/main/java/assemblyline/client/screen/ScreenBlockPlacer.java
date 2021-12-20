package assemblyline.client.screen;

import java.util.ArrayList;
import java.util.List;

import assemblyline.common.inventory.container.ContainerBlockPlacer;
import assemblyline.common.settings.Constants;
import assemblyline.common.tile.TileBlockPlacer;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.ElectricUnit;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.ScreenComponentInfo;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBlockPlacer extends GenericScreen<ContainerBlockPlacer> {
	public ScreenBlockPlacer(ContainerBlockPlacer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		components.add(new ScreenComponentElectricInfo(this::getEnergyInformation, this, -ScreenComponentInfo.SIZE + 1, 2));
	}

	private List<? extends FormattedCharSequence> getEnergyInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileBlockPlacer box = menu.getHostFromIntArray();
		if (box != null) {
			ComponentElectrodynamic electro = box.getComponent(ComponentType.Electrodynamic);
			list.add(new TranslatableComponent("gui.blockplacer.usage",
					new TextComponent(ChatFormatter.getElectricDisplayShort(Constants.SORTERBELT_USAGE * 20, ElectricUnit.WATT))
							.withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(new TranslatableComponent("gui.blockplacer.voltage",
					new TextComponent(ChatFormatter.getElectricDisplayShort(electro.getVoltage(), ElectricUnit.VOLTAGE))
							.withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}

}