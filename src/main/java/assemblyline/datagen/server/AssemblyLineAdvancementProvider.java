package assemblyline.datagen.server;

import java.util.function.Consumer;

import assemblyline.References;
import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.server.ElectrodynamicsAdvancementProvider;
import electrodynamics.datagen.utils.AdvancementBuilder;
import electrodynamics.datagen.utils.AdvancementBuilder.AdvancementBackgrounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.text.TextFormatting;

public class AssemblyLineAdvancementProvider extends ElectrodynamicsAdvancementProvider {

	public AssemblyLineAdvancementProvider(DataGenerator generatorIn) {
		super(generatorIn, References.ID);
	}

	@Override
	public void registerAdvancements(Consumer<AdvancementBuilder> consumer) {

		// Credit to pyro206 for original JSON
		Advancement root = advancement("root")
				//
				.display(AssemblyLineBlocks.blockConveyorBelt.asItem(), AssemblyTextUtils.advancement("root.title").withStyle(TextFormatting.YELLOW, TextFormatting.BOLD, TextFormatting.ITALIC), AssemblyTextUtils.advancement("root.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.STONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("getaconveyerbelt", InventoryChangeTrigger.Instance.hasItems(AssemblyLineBlocks.blockConveyorBelt.asItem()))
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("sorter")
				//
				.display(AssemblyLineBlocks.blockSorterBelt.asItem(), AssemblyTextUtils.advancement("sorter.title").withStyle(TextFormatting.AQUA), AssemblyTextUtils.advancement("sorter.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasSorterBelt", InventoryChangeTrigger.Instance.hasItems(AssemblyLineBlocks.blockSorterBelt.asItem()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("detector")
				//
				.display(AssemblyLineBlocks.blockDetector.asItem(), AssemblyTextUtils.advancement("detector.title").withStyle(TextFormatting.DARK_RED, TextFormatting.BOLD, TextFormatting.ITALIC, TextFormatting.UNDERLINE), AssemblyTextUtils.advancement("detector.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.CHALLENGE, true, true, false)
				//
				.addCriterion("HasDetector", InventoryChangeTrigger.Instance.hasItems(AssemblyLineBlocks.blockDetector.asItem()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("crate")
				//
				.display(AssemblyLineBlocks.blockCrate.asItem(), AssemblyTextUtils.advancement("crate.title").withStyle(TextFormatting.GOLD, TextFormatting.BOLD), AssemblyTextUtils.advancement("crate.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasCrate", InventoryChangeTrigger.Instance.hasItems(AssemblyLineBlocks.blockCrate.asItem()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

	}

}
