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
			addBlock(AssemblyLineBlocks.blockAutocrafter, "Autocrafter");
			addBlock(AssemblyLineBlocks.blockBlockBreaker, "Block Breaker");
			addBlock(AssemblyLineBlocks.blockBlockPlacer, "Block Placer");
			addBlock(AssemblyLineBlocks.blockRancher, "Energized Rancher");
			addBlock(AssemblyLineBlocks.blockMobGrinder, "Mob Grinder");
			addBlock(AssemblyLineBlocks.blockFarmer, "Farmer");

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

			addTooltip("voltage.120", "Voltage: 120 Volts");
			addTooltip("detector", "Emits redstone signal when an item is in front of it");
			addTooltip("crate", "Stores 4096 of one item");
			addTooltip("cratemedium", "Stores 8192 of one item");
			addTooltip("cratelarge", "Stores 16384 of one item");
			addTooltip("breakingprogress", "Mining Progress: %s");
			addTooltip("cooldown", "Cooldown: %s ticks");

			addGuidebook(References.ID, "Assembly Line");

			addGuidebook("chapter.conveyers", "Conveyers");
			addGuidebook("chapter.conveyers.l1", 
					"Conveyor Belts provide a robust method for moving items between inventories. Requiring only 8 J/t or 160W at 120 V to run, they offer a valuable alternative to moving items manually. Furthermore, only one belt in a segment needs to have a cable attached, as it will distribute power to the other belts. "
					+ 
					"Power is fed in from the bottom of the conveyor.");
			addGuidebook("chapter.conveyers.l2", 
					"The yellow arrows on the belt represents the direction items will be transfered. To load items onto a belt, you can either drop the item and the belt will pick it up, or the belt can extract items from an inventory. If a belt does not end in an inventory, then it will drop its items onto the ground. "
					+ 
					"Conveyor Belts have 4 modes they can switch between by Right-Clicking a wrench on them. These are:");
			addGuidebook("chapter.conveyers.horizontal", "Horizontal");
			addGuidebook("chapter.conveyers.diagonalup", "Diagonal Up");
			addGuidebook("chapter.conveyers.diagonaldown", "Diagonal Down");
			addGuidebook("chapter.conveyers.vertical", "Vertical");
			addGuidebook("chapter.conveyers.l3", "These modes are pictured on the following pages. Note where the bounding boxes are:");

			addGuidebook("chapter.conveyers.l4", 
					"A specialized form of the Conveyer Belt is the Sorter Belt. As the name suggests, it is able to sort items that pass through it. The belt has an input at the front, with the other 3 sides being outputs. The GUI of the belt has 2 sets of 9 slots. Any item placed in a left slot will be output to the "
					+ 
					"left of the input. Any item placed in a right slot will be output to the right of the of the input. Any other items not specified will be output to the back of the Sorter Belt. The Sorter also needs power to function, and has the same usage as a standard Conveyor Belt. This power can either "
					+ 
					"be supplied to the bottom or from an adjacent belt with power.");

			addGuidebook("chapter.conveyers.l5", "Another useful block is the Detector. The Detector will output a 15 redstone signal if an item on a Conveyer Belt passes in front of the green square of the Detector, or if an item is laying on the ground in front of said square.");

			addGuidebook("chapter.machines", "Machines");
			addGuidebook("chapter.machines.blockbreaker", "The Block Breaker will break any block placed in front of it as long as it is minable with a pickaxe. Use the \"Show Area\" button to view the block it is mining. The progress bar counts down the break time left on the block that is being mined.");

			addGuidebook("chapter.machines.blockplacer", "The Block Placer will place any block added to its inventory in front of it. Use the \"Show Area\" button to view the area it can place a block in. The progress bar counts down the time between block placements.");

			addGuidebook("chapter.machines.energizedrancher", 
					"The Energized Rancher will shear sheep inside its working area. Use the \"Show Area\" to button to view the area it is currently covering. The progress bar counts down the time between shearings. The Rancher can use up to 12 Range Upgrades to expand its working area.");

			addGuidebook("chapter.machines.mobgrinder", "The Mob Grinder will kill any mobs in its working area. Use the \"Show Area\" button to view the area it is currently covering. The progress bar counts down the time between checks. The Grinder can use up to 12 Range Upgrades to expand its working area.");

			addGuidebook("chapter.machines.farmer1", "The Farmer will plant and harvest crops and trees within its working area. The colors of the input slots corrospond to the regions the items will be planted in. The Farmer must be placed underneath the land it is working. The top right slot is for Bone Meal.");
			addGuidebook("chapter.machines.farmer2", 
					"The \"Show Area\" button will display the working area of the Farmer. The \"Bone Full\" button toggles whether or not the Farmer will use one Bone Meal per crop, or keep using Bone Meal until the crop is fully grown. The \"Refill Empty\" button toggles whether or not the Farmer will add the seeds "
					+ 
					"from harvested crops back into the planting slots. The Farmer can use up to 4 Range Upgrades to increase its working area. Note the upgrades increase the range by multiples of 3.");

		}

	}

}
