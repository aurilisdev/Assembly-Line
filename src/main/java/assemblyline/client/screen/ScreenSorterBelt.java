package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerSorterBelt;
import electrodynamics.prefab.screen.GenericScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenSorterBelt extends GenericScreen<ContainerSorterBelt> {
    public ScreenSorterBelt(ContainerSorterBelt container, Inventory playerInventory, Component title) {
	super(container, playerInventory, title);
    }

}