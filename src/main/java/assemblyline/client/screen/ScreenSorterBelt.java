package assemblyline.client.screen;

import assemblyline.References;
import assemblyline.common.inventory.container.ContainerSorterBelt;
import electrodynamics.client.screen.generic.GenericContainerScreenUpgradeable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenSorterBelt extends GenericContainerScreenUpgradeable<ContainerSorterBelt> {
    public static final ResourceLocation SCREEN_BACKGROUND = new ResourceLocation(References.ID + ":textures/gui/sorterbelt.png");

    public ScreenSorterBelt(ContainerSorterBelt container, PlayerInventory playerInventory, ITextComponent title) {
	super(container, playerInventory, title);
    }

    @Override
    public ResourceLocation getScreenBackground() {
	return SCREEN_BACKGROUND;
    }
}