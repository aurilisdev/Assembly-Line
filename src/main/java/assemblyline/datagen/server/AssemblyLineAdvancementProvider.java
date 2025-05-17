package assemblyline.datagen.server;

import java.util.function.Consumer;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraftforge.common.data.ExistingFileHelper;
import voltaic.datagen.utils.server.advancement.AdvancementBuilder.AdvancementBackgrounds;
import voltaic.datagen.utils.server.advancement.BaseAdvancementProvider;

public class AssemblyLineAdvancementProvider extends BaseAdvancementProvider {

	public AssemblyLineAdvancementProvider() {
		super(AssemblyLine.ID);
	}

	@Override
	public void generate(Provider registries, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {

		// Credit to pyro206 for original JSON
		Advancement root = advancement("root")
				//
				.display(AssemblyLineItems.ITEM_CONVEYORBELT.get(), AssemblyTextUtils.advancement("root.title").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD, ChatFormatting.ITALIC), AssemblyTextUtils.advancement("root.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.STONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("getaconveyerbelt", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineItems.ITEM_CONVEYORBELT.get()))
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("sorter")
				//
				.display(AssemblyLineItems.ITEM_SORTERBELT.get(), AssemblyTextUtils.advancement("sorter.title").withStyle(ChatFormatting.AQUA), AssemblyTextUtils.advancement("sorter.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasSorterBelt", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineItems.ITEM_SORTERBELT.get()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("detector")
				//
				.display(AssemblyLineItems.ITEM_DETECTOR.get(), AssemblyTextUtils.advancement("detector.title").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD, ChatFormatting.ITALIC, ChatFormatting.UNDERLINE), AssemblyTextUtils.advancement("detector.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.CHALLENGE, true, true, false)
				//
				.addCriterion("HasDetector", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineItems.ITEM_DETECTOR.get()))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

		// Credit to pyro206 for original JSON
		advancement("crate")
				//
				.display(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.crate), AssemblyTextUtils.advancement("crate.title").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), AssemblyTextUtils.advancement("crate.desc").withStyle(ChatFormatting.GRAY), AdvancementBackgrounds.NONE, FrameType.TASK, true, true, false)
				//
				.addCriterion("HasCrate", InventoryChangeTrigger.TriggerInstance.hasItems(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.crate)))
				//
				.parent(root)
				//
				.author("pyro206")
				//
				.save(consumer);

	}

}
