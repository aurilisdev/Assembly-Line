package assemblyline.datagen.client;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider;
import net.minecraft.data.DataGenerator;

public class AssemblyLineLangKeyProvider extends ElectrodynamicsLangKeyProvider {

	public AssemblyLineLangKeyProvider(DataGenerator gen, Locale locale) {
		super(gen, locale, References.ID);
	}

	@Override
	protected void addTranslations() {

		switch (locale) {
		case EN_US:
		default:

			add("itemGroup.itemgroup" + References.ID, "Assembly Line");

			addBlock(AssemblyLineBlocks.blockConveyorBelt, "Conveyor Belt");
			addBlock(AssemblyLineBlocks.blockSorterBelt, "Sorter Belt");
			addBlock(AssemblyLineBlocks.blockDetector, "Detector");
			addBlock(AssemblyLineBlocks.blockCrate, "Crate");
			addBlock(AssemblyLineBlocks.blockCrateMedium, "Medium Crate");
			addBlock(AssemblyLineBlocks.blockCrateLarge, "Large Crate");

			addContainer("sorterbelt", "Sorter Belt");
			addContainer("autocrafter", "Autocrafter");
			addContainer("blockplacer", "Block Placer");
			addContainer("blockbreaker", "Block Breaker");
			addContainer("rancher", "Energized Rancher");
			addContainer("mobgrinder", "Mob Grinder");
			addContainer("farmer", "Farmer");

			addGuiLabel("machine.voltage", "Voltage: %s");
			addGuiLabel("machine.usage", "Usage: %s");
			addGuiLabel("renderarea", "Show Area");
			addGuiLabel("hidearea", "Hide Area");
			addGuiLabel("fullbonemeal", "Bone Full");
			addGuiLabel("regbonemeal", "Bone Once");
			addGuiLabel("refillempty", "Refill Empty");
			addGuiLabel("ignoreempty", "Skip Empty");

			addTooltip("detector", "Emits redstone signal when an item is in front of it");
			addTooltip("crate", "Stores 4096 of one item");
			addTooltip("cratemedium", "Stores 8192 of one item");
			addTooltip("cratelarge", "Stores 16384 of one item");
			addTooltip("breakingprogress", "Mining Progress: %s");
			addTooltip("cooldown", "Cooldown: %s ticks");

			addAdvancement("root.title", "Assembly Line");
			addAdvancement("root.desc", "Wow you made a conveyor belt");
			addAdvancement("sorter.title", "Organizer");
			addAdvancement("sorter.desc", "Make a Sorter Belt");
			addAdvancement("detector.title", "Always Watching");
			addAdvancement("detector.desc", "Make a Detector");
			addAdvancement("crate.title", "No More Chests");
			addAdvancement("crate.desc", "Make a Crate");

			addGuidebook(References.ID, "Assembly Line");

			addGuidebook("chapter.conveyers", "Conveyers");
			addGuidebook("chapter.conveyers.l1", "Conveyor Belts provide a robust method for moving items between inventories. Requiring only 8 J/t or 160W at 120 V to run, they offer a valuable alternative to moving items manually. Furthermore, only one belt in a segment needs to have a cable attached, as it will distribute power to the other belts. " + "Power is fed in from the bottom of the conveyor.");
			addGuidebook("chapter.conveyers.l2", "The yellow arrows on the belt represents the direction items will be transfered. To load items onto a belt, you can either drop the item and the belt will pick it up, or the belt can extract items from an inventory. If a belt does not end in an inventory, then it will drop its items onto the ground. " + "Conveyor Belts have 4 modes they can switch between by Right-Clicking a wrench on them. These are:");
			addGuidebook("chapter.conveyers.horizontal", "Horizontal");
			addGuidebook("chapter.conveyers.diagonalup", "Diagonal Up");
			addGuidebook("chapter.conveyers.diagonaldown", "Diagonal Down");
			addGuidebook("chapter.conveyers.vertical", "Vertical");
			addGuidebook("chapter.conveyers.l3", "These modes are pictured on the following pages. Note where the bounding boxes are:");

			addGuidebook("chapter.conveyers.l4", "A specialized form of the Conveyer Belt is the Sorter Belt. As the name suggests, it is able to sort items that pass through it. The belt has an input at the front, with the other 3 sides being outputs. The GUI of the belt has 2 sets of 9 slots. Any item placed in a left slot will be output to the " + "left of the input. Any item placed in a right slot will be output to the right of the of the input. Any other items not specified will be output to the back of the Sorter Belt. The Sorter also needs power to function, and has the same usage as a standard Conveyor Belt. This power can either " + "be supplied to the bottom or from an adjacent belt with power.");

			addGuidebook("chapter.conveyers.l5", "Another useful block is the Detector. The Detector will output a 15 redstone signal if an item on a Conveyer Belt passes in front of the green square of the Detector, or if an item is laying on the ground in front of said square.");

		}

	}

}
