package assemblyline.datagen;

import assemblyline.References;
import assemblyline.datagen.client.AssemblyLineBlockStateProvider;
import assemblyline.datagen.client.AssemblyLineItemModelsProvider;
import assemblyline.datagen.client.AssemblyLineLangKeyProvider;
import assemblyline.datagen.server.AssemblyLineAdvancementProvider;
import assemblyline.datagen.server.AssemblyLineBlockTagsProvider;
import assemblyline.datagen.server.AssemblyLineLootTablesProvider;
import assemblyline.datagen.server.recipe.AssemblyLineRecipeProvider;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider.Locale;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = References.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {

		DataGenerator generator = event.getGenerator();
		if (event.includeServer()) {

			generator.addProvider(new AssemblyLineBlockTagsProvider(generator, event.getExistingFileHelper()));
			generator.addProvider(new AssemblyLineLootTablesProvider(generator));
			generator.addProvider(new AssemblyLineRecipeProvider(generator));
			generator.addProvider(new AssemblyLineAdvancementProvider(generator));

		}
		if (event.includeClient()) {
			generator.addProvider(new AssemblyLineBlockStateProvider(generator, event.getExistingFileHelper()));
			generator.addProvider(new AssemblyLineItemModelsProvider(generator, event.getExistingFileHelper()));
			generator.addProvider(new AssemblyLineLangKeyProvider(generator, Locale.EN_US));
		}
	}

}
