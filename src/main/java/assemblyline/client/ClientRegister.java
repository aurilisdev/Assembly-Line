package assemblyline.client;

import assemblyline.DeferredRegisters;
import assemblyline.client.render.tile.RenderCrate;
import assemblyline.client.screen.ScreenSorterBelt;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientRegister {

    public static void setup() {
	ScreenManager.registerFactory(DeferredRegisters.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
	ClientRegistry.bindTileEntityRenderer(DeferredRegisters.TILE_CRATE.get(), RenderCrate::new);

    }
}
