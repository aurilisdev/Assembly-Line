package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressTextures;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenAutocrafter extends GenericScreen<ContainerAutocrafter> {

	public static final Color RED = new Color(255, 0, 0, 255);
	public static final Color GREEN = new Color(0, 255, 0, 255);
	public static final Color BLUE = new Color(0, 0, 255, 255);
	public static final Color YELLOW = new Color( 255, 255, 0, 255);

	public ScreenAutocrafter(ContainerAutocrafter container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		addComponent(new ScreenComponentGeneric(ProgressTextures.ARROW_RIGHT_OFF, 80, 34));
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.AUTOCRAFTER_USAGE));
	}

	@Override
	protected ScreenComponentSlot createScreenSlot(Slot slot) {
		ScreenComponentSlot component = super.createScreenSlot(slot);
		int index = slot.index;
		switch (index) {
		case 1:
			component.setColor(RED);
			break;
		case 3:
			component.setColor(GREEN);
			break;
		case 5:
			component.setColor(BLUE);
			break;
		case 7:
			component.setColor(YELLOW);
			break;
		default:
			break;
		}
		return component;
	}

}