package assemblyline.datagen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import assemblyline.References;
import assemblyline.datagen.client.AssemblyLineBlockStateProvider;
import assemblyline.datagen.client.AssemblyLineItemModelsProvider;
import assemblyline.datagen.client.AssemblyLineLangKeyProvider;
import assemblyline.datagen.server.AssemblyLineAdvancementProvider;
import assemblyline.datagen.server.AssemblyLineBlockTagsProvider;
import assemblyline.datagen.server.AssemblyLineLootTablesProvider;
import assemblyline.datagen.server.recipe.AssemblyLineRecipeProvider;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider.Locale;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {

		DataGenerator generator = event.getGenerator();

		PackOutput output = generator.getPackOutput();

		ExistingFileHelper helper = event.getExistingFileHelper();

		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {

			generator.addProvider(true, new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(AssemblyLineLootTablesProvider::new, LootContextParamSets.BLOCK))));
			generator.addProvider(true, new AssemblyLineRecipeProvider(output));
			generator.addProvider(true, new ForgeAdvancementProvider(output, event.getLookupProvider(), helper, List.of(new AssemblyLineAdvancementProvider())));
			generator.addProvider(true, new AssemblyLineBlockTagsProvider(output, lookupProvider, helper));

		}
		if (event.includeClient()) {
			generator.addProvider(true, new AssemblyLineBlockStateProvider(output, helper));
			generator.addProvider(true, new AssemblyLineItemModelsProvider(output, helper));
			generator.addProvider(true, new AssemblyLineLangKeyProvider(output, Locale.EN_US));
		}
	}

}
