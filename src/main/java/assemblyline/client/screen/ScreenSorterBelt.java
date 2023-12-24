package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerSorterBelt;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenSorterBelt extends GenericScreen<ContainerSorterBelt> {
	public ScreenSorterBelt(ContainerSorterBelt container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.SORTERBELT_USAGE * 20));
	}

}