package assemblyline.datagen.server;

import java.util.function.Consumer;

import assemblyline.References;
import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.server.ElectrodynamicsAdvancementProvider;
import electrodynamics.datagen.utils.AdvancementBuilder;
import electrodynamics.datagen.utils.AdvancementBuilder.AdvancementBackgrounds;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;

public class AssemblyLineAdvancementProvider extends ElectrodynamicsAdvancementProvider {

	public AssemblyLineAdvancementProvider(DataGenerator generatorIn) {
		super(generatorIn, References.ID);
	}

	@Override
	public void registerAdvancements(Consumer<AdvancementBuilder> consumer) {

		// Credit to pyro206 for original JSON
		Advancement root = advancement("root")
				//
				.display(AssemblyLineBlocks.blockConveyorBelt.asItem(), AssemblyTextUtils.advancement("root.title").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD, ChatFormatting.ITALIC), AssemblyTextUtils.advancement("root.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.STONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("getaconveyerbelt", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineBlocks.blockConveyorBelt.asItem()))
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("sorter")
				//
				.display(AssemblyLineBlocks.blockSorterBelt.asItem(), AssemblyTextUtils.advancement("sorter.title").withStyle(ChatFormatting.AQUA), AssemblyTextUtils.advancement("sorter.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasSorterBelt", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineBlocks.blockSorterBelt.asItem()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("detector")
				//
				.display(AssemblyLineBlocks.blockDetector.asItem(), AssemblyTextUtils.advancement("detector.title").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD, ChatFormatting.ITALIC, ChatFormatting.UNDERLINE), AssemblyTextUtils.advancement("detector.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.CHALLENGE, true, true, false)
				//
				.addCriterion("HasDetector", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineBlocks.blockDetector.asItem()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("crate")
				//
				.display(AssemblyLineBlocks.blockCrate.asItem(), AssemblyTextUtils.advancement("crate.title").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), AssemblyTextUtils.advancement("crate.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasCrate", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineBlocks.blockCrate.asItem()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

	}

}
