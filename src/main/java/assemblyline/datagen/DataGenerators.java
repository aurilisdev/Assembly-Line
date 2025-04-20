package assemblyline.datagen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import assemblyline.AssemblyLine;
import assemblyline.datagen.client.AssemblyLineBlockStateProvider;
import assemblyline.datagen.client.AssemblyLineItemModelsProvider;
import assemblyline.datagen.client.AssemblyLineLangKeyProvider;
import assemblyline.datagen.client.AssemblyLineSoundProvider;
import assemblyline.datagen.server.AssemblyLineAdvancementProvider;
import assemblyline.datagen.server.AssemblyLineBlockTagsProvider;
import assemblyline.datagen.server.AssemblyLineLootTablesProvider;
import assemblyline.datagen.server.recipe.AssemblyLineRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import voltaic.datagen.utils.client.BaseLangKeyProvider;

@EventBusSubscriber(modid = AssemblyLine.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();

        PackOutput output = generator.getPackOutput();

        ExistingFileHelper helper = event.getExistingFileHelper();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {

            generator.addProvider(true, new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(AssemblyLineLootTablesProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
            generator.addProvider(true, new AssemblyLineRecipeProvider(output, lookupProvider));
            generator.addProvider(true, new AssemblyLineAdvancementProvider(output, lookupProvider));
            generator.addProvider(true, new AssemblyLineBlockTagsProvider(output, lookupProvider, helper));

        }
        if (event.includeClient()) {
            generator.addProvider(true, new AssemblyLineBlockStateProvider(output, helper));
            generator.addProvider(true, new AssemblyLineItemModelsProvider(output, helper));
            generator.addProvider(true, new AssemblyLineLangKeyProvider(output, BaseLangKeyProvider.Locale.EN_US));
            generator.addProvider(true, new AssemblyLineSoundProvider(output, helper));
        }
    }

}
