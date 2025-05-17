package assemblyline.datagen.server;

import java.util.function.Consumer;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.text.TextFormatting;
import voltaic.datagen.utils.server.advancement.AdvancementBuilder;
import voltaic.datagen.utils.server.advancement.AdvancementBuilder.AdvancementBackgrounds;
import voltaic.datagen.utils.server.advancement.BaseAdvancementProvider;

public class AssemblyLineAdvancementProvider extends BaseAdvancementProvider {

	public AssemblyLineAdvancementProvider(DataGenerator gen) {
		super(gen, AssemblyLine.ID);
	}

	@Override
	public void registerAdvancements(Consumer<AdvancementBuilder> consumer) {

		// Credit to pyro206 for original JSON
		Advancement root = advancement("root")
				//
				.display(AssemblyLineItems.ITEM_CONVEYORBELT.get(), AssemblyTextUtils.advancement("root.title").withStyle(TextFormatting.YELLOW, TextFormatting.BOLD, TextFormatting.ITALIC), AssemblyTextUtils.advancement("root.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.STONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("getaconveyerbelt", InventoryChangeTrigger.Instance.hasItems(AssemblyLineItems.ITEM_CONVEYORBELT.get()))
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("sorter")
				//
				.display(AssemblyLineItems.ITEM_SORTERBELT.get(), AssemblyTextUtils.advancement("sorter.title").withStyle(TextFormatting.AQUA), AssemblyTextUtils.advancement("sorter.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasSorterBelt", InventoryChangeTrigger.Instance.hasItems(AssemblyLineItems.ITEM_SORTERBELT.get()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("detector")
				//
				.display(AssemblyLineItems.ITEM_DETECTOR.get(), AssemblyTextUtils.advancement("detector.title").withStyle(TextFormatting.DARK_RED, TextFormatting.BOLD, TextFormatting.ITALIC, TextFormatting.UNDERLINE), AssemblyTextUtils.advancement("detector.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.CHALLENGE, true, true, false)
				//
				.addCriterion("HasDetector", InventoryChangeTrigger.Instance.hasItems(AssemblyLineItems.ITEM_DETECTOR.get()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("crate")
				//
				.display(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.crate), AssemblyTextUtils.advancement("crate.title").withStyle(TextFormatting.GOLD, TextFormatting.BOLD), AssemblyTextUtils.advancement("crate.desc").withStyle(TextFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasCrate", InventoryChangeTrigger.Instance.hasItems(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.crate)))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

	}

}
