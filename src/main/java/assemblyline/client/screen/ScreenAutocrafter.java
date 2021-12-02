package assemblyline.client.screen;

import assemblyline.common.inventory.container.ContainerAutocrafter;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenAutocrafter extends GenericScreen<ContainerAutocrafter> {
    public ScreenAutocrafter(ContainerAutocrafter container, Inventory playerInventory, Component title) {
	super(container, playerInventory, title);
	components.add(new ScreenComponentProgress(() -> 0, this, 80, 34));
    }

}