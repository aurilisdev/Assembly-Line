package assemblyline.client;

import assemblyline.DeferredRegisters;
import assemblyline.client.screen.ScreenSorterBelt;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientRegister {

	public static void setup() {
		ScreenManager.registerFactory(DeferredRegisters.CONTAINER_SORTERBELT.get(), ScreenSorterBelt::new);
	}
}
