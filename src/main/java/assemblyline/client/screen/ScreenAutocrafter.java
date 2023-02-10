package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerAutocrafter;
import assemblyline.common.settings.Constants;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.screen.component.ScreenComponentProgress.ProgressTextures;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenAutocrafter extends GenericScreen<ContainerAutocrafter> {
	public ScreenAutocrafter(ContainerAutocrafter container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		components.add(new ScreenComponentGeneric(ProgressTextures.ARROW_RIGHT_OFF, this, 80, 34));
		components.add(new ScreenComponentElectricInfo(this, -AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.AUTOCRAFTER_USAGE));
	}

	@Override
	protected ScreenComponentSlot createScreenSlot(Slot slot) {
		ScreenComponentSlot component = super.createScreenSlot(slot);
		int index = slot.index;
		switch (index) {
		case 1:
			component.setColor(RenderingUtils.getRGBA(255, 255, 0, 0));
			break;
		case 3:
			component.setColor(RenderingUtils.getRGBA(255, 0, 255, 0));
			break;
		case 5:
			component.setColor(RenderingUtils.getRGBA(255, 0, 0, 255));
			break;
		case 7:
			component.setColor(RenderingUtils.getRGBA(255, 255, 255, 0));
			break;
		case 0, 2, 4, 6, 8:
			component.setColor(RenderingUtils.getRGBA(255, 180, 180, 180));
			break;
		default:
			break;
		}
		return component;
	}

}