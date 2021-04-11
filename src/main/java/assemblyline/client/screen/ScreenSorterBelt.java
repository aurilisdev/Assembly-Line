package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerSorterBelt;
import electrodynamics.prefab.screen.GenericScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenSorterBelt extends GenericScreen<ContainerSorterBelt> {
    public ScreenSorterBelt(ContainerSorterBelt container, PlayerInventory playerInventory, ITextComponent title) {
	super(container, playerInventory, title);
    }

}