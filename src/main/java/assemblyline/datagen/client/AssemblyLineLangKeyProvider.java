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
			addGuidebook("chapter.conveyers.p1l1", "    Conveyer belts provide a   ");
			addGuidebook("chapter.conveyers.p1l2", "robust method for moving items ");
			addGuidebook("chapter.conveyers.p1l3", "between inventories. Requiring ");
			addGuidebook("chapter.conveyers.p1l4", "only 8 J/t or 160W at 120 V to ");
			addGuidebook("chapter.conveyers.p1l5", "run, they offer a valuable     ");
			addGuidebook("chapter.conveyers.p1l6", "alternative to moving items    ");
			addGuidebook("chapter.conveyers.p1l7", "manually. Furthermore, only    ");
			addGuidebook("chapter.conveyers.p1l8", "one belt in a segment needs to ");
			addGuidebook("chapter.conveyers.p1l9", "have a cable attached, as it   ");
			addGuidebook("chapter.conveyers.p1l10", "will distribute power to the   ");
			addGuidebook("chapter.conveyers.p1l11", "other belts.                   ");
			addGuidebook("chapter.conveyers.p1l12", "    The yellow arrows on the   ");
			addGuidebook("chapter.conveyers.p1l13", "belt represents the direction  ");
			addGuidebook("chapter.conveyers.p1l14", "items will be transfered. Items");
			addGuidebook("chapter.conveyers.p1l15", "will either be extracted from  ");

			addGuidebook("chapter.conveyers.p2l1", "an inventory or can be         ");
			addGuidebook("chapter.conveyers.p2l2", "dropped onto the belt. If a    ");
			addGuidebook("chapter.conveyers.p2l3", "belt does not end in an        ");
			addGuidebook("chapter.conveyers.p2l4", "inventory, then it will drop its");
			addGuidebook("chapter.conveyers.p2l5", "items onto the ground. In total,");
			addGuidebook("chapter.conveyers.p2l6", "conveyers have 4 modes you     ");
			addGuidebook("chapter.conveyers.p2l7", "are able to swap between       ");
			addGuidebook("chapter.conveyers.p2l8", "using the Wrench:              ");
			addGuidebook("chapter.conveyers.p2l9", "     Horizontal                ");
			addGuidebook("chapter.conveyers.p2l10", "     Diagonal Up               ");
			addGuidebook("chapter.conveyers.p2l11", "     Diagonal Down             ");
			addGuidebook("chapter.conveyers.p2l12", "     Vertical                  ");
			addGuidebook("chapter.conveyers.p2l13", "These are pictured on the      ");
			addGuidebook("chapter.conveyers.p2l14", "following pages. Note where    ");
			addGuidebook("chapter.conveyers.p2l15", "the bounding boxes are:        ");

			addGuidebook("chapter.conveyers.p5l1", "    A specialized form of the  ");
			addGuidebook("chapter.conveyers.p5l2", "Conveyer Belt is the Sorter    ");
			addGuidebook("chapter.conveyers.p5l3", "Belt. As the name suggests, it ");
			addGuidebook("chapter.conveyers.p5l4", "is able to sort items that pass");
			addGuidebook("chapter.conveyers.p5l5", "through it. The belt has an    ");
			addGuidebook("chapter.conveyers.p5l6", "input at the front, with the   ");
			addGuidebook("chapter.conveyers.p5l7", "other 3 sides being outputs.   ");
			addGuidebook("chapter.conveyers.p5l8", "The GUI of the belt has 2 sets ");
			addGuidebook("chapter.conveyers.p5l9", "of 9 slots. Any item placed in ");
			addGuidebook("chapter.conveyers.p5l10", "a left slot will be output to the");
			addGuidebook("chapter.conveyers.p5l11", "left of the input. Any item    ");
			addGuidebook("chapter.conveyers.p5l12", "placed in a right slot will be ");
			addGuidebook("chapter.conveyers.p5l13", "output to the right of the of  ");
			addGuidebook("chapter.conveyers.p5l14", "the input. Any other items not ");
			addGuidebook("chapter.conveyers.p5l15", "specified will be output to the");

			addGuidebook("chapter.conveyers.p6l1", "back of the Sorter Belt. The   ");
			addGuidebook("chapter.conveyers.p6l2", "Sorter also needs power to     ");
			addGuidebook("chapter.conveyers.p6l3", "function. This power can either");
			addGuidebook("chapter.conveyers.p6l4", "be supplied to the bottom or   ");
			addGuidebook("chapter.conveyers.p6l5", "from an adjacent belt with     ");
			addGuidebook("chapter.conveyers.p6l6", "power.                         ");

			addGuidebook("chapter.conveyers.p8l1", "    Another useful block is the");
			addGuidebook("chapter.conveyers.p8l2", "Detector. If an item on a      ");
			addGuidebook("chapter.conveyers.p8l3", "Conveyer Belt passes in front  ");
			addGuidebook("chapter.conveyers.p8l4", "of the green square of the     ");
			addGuidebook("chapter.conveyers.p8l5", "Detector, or an item is laying ");
			addGuidebook("chapter.conveyers.p8l6", "on the ground in front of said ");
			addGuidebook("chapter.conveyers.p8l7", "square, then the Detector will ");
			addGuidebook("chapter.conveyers.p8l8", "output a redstone signal.      ");

			addGuidebook("chapter.machines", "Machines");
			addGuidebook("chapter.machines.blockbreaktitle", "Block Breaker");
			addGuidebook("chapter.machines.p1l1", "    The Block Breaker will     ");
			addGuidebook("chapter.machines.p1l2", "break any block as long as it  ");
			addGuidebook("chapter.machines.p1l3", "is minable with a pickaxe. Use ");
			addGuidebook("chapter.machines.p1l4", "the \"Show Area\" button to view");
			addGuidebook("chapter.machines.p1l5", "the block it is mining. The    ");
			addGuidebook("chapter.machines.p1l6", "progress bar counts down the   ");
			addGuidebook("chapter.machines.p1l7", "break time left.               ");

			addGuidebook("chapter.machines.blockplacetitle", "Block Placer");
			addGuidebook("chapter.machines.p2l1", "    The Block Placer will place");
			addGuidebook("chapter.machines.p2l2", "any block added to its         ");
			addGuidebook("chapter.machines.p2l3", "inventory. Use the \"Show Area\"");
			addGuidebook("chapter.machines.p2l4", "button to view the area it can ");
			addGuidebook("chapter.machines.p2l5", "place a block in. The progress ");
			addGuidebook("chapter.machines.p2l6", "bar counts down the time       ");
			addGuidebook("chapter.machines.p2l7", "between block placements.      ");

			addGuidebook("chapter.machines.ranchertitle", "Energized Rancher");
			addGuidebook("chapter.machines.p3l1", "    The Energized Rancher will ");
			addGuidebook("chapter.machines.p3l2", "shear sheep inside its working ");
			addGuidebook("chapter.machines.p3l3", "area. Use the \"Show Area\" to ");
			addGuidebook("chapter.machines.p3l4", "button to view the area it is  ");
			addGuidebook("chapter.machines.p3l5", "working in. The progress bar   ");
			addGuidebook("chapter.machines.p3l6", "counts down the time between   ");
			addGuidebook("chapter.machines.p3l7", "shearings.                     ");
			addGuidebook("chapter.machines.p3l8", "    The Rancher can use up to  ");
			addGuidebook("chapter.machines.p3l9", "12 Range Upgrades to expand    ");
			addGuidebook("chapter.machines.p3l10", "its working area.              ");

			addGuidebook("chapter.machines.grindertitle", "Mob Grinder");
			addGuidebook("chapter.machines.p4l1", "    The Mob Grinder will kill  ");
			addGuidebook("chapter.machines.p4l2", "any mobs in its working area.  ");
			addGuidebook("chapter.machines.p4l3", "Use the \"Show Area\" button to");
			addGuidebook("chapter.machines.p4l4", "view the area it is working in.");
			addGuidebook("chapter.machines.p4l5", "The progress bar counts down   ");
			addGuidebook("chapter.machines.p4l6", "the time between checks.       ");
			addGuidebook("chapter.machines.p4l7", "    The Grinder can use up to  ");
			addGuidebook("chapter.machines.p4l8", "12 Range Upgrades to expand    ");
			addGuidebook("chapter.machines.p4l9", "its working area.              ");

			addGuidebook("chapter.machines.farmertitle", "Farmer");
			addGuidebook("chapter.machines.p5l1", "    The Farmer will plant and  ");
			addGuidebook("chapter.machines.p5l2", "harvest crops and trees within ");
			addGuidebook("chapter.machines.p5l3", "its working area. The colors of");
			addGuidebook("chapter.machines.p5l4", "the input slots corrospond to  ");
			addGuidebook("chapter.machines.p5l5", "the regions the items will be  ");
			addGuidebook("chapter.machines.p5l6", "planted in. The Farmer must be ");
			addGuidebook("chapter.machines.p5l7", "placed underneath the land it  ");
			addGuidebook("chapter.machines.p5l8", "is working. The top right slot ");
			addGuidebook("chapter.machines.p5l9", "is for Bone Meal.              ");
			addGuidebook("chapter.machines.p5l10", "    The \"Show Area\" button   ");
			addGuidebook("chapter.machines.p5l11", "will display the working area  ");

			addGuidebook("chapter.machines.p6l1", "of the Farmer. The \"Bone Full\"");
			addGuidebook("chapter.machines.p6l2", "button toggles whether or not  ");
			addGuidebook("chapter.machines.p6l3", "the Farmer will use one Bone   ");
			addGuidebook("chapter.machines.p6l4", "Meal per plant, or keep using  ");
			addGuidebook("chapter.machines.p6l5", "Bone Meal until the crop is    ");
			addGuidebook("chapter.machines.p6l6", "fully grown. The \"Refill Empty\"");
			addGuidebook("chapter.machines.p6l7", "button toggles whether or not  ");
			addGuidebook("chapter.machines.p6l8", "the Farmer will add the seeds  ");
			addGuidebook("chapter.machines.p6l9", "from harvested crops back      ");
			addGuidebook("chapter.machines.p6l10", "into the planting slots.       ");
			addGuidebook("chapter.machines.p6l11", "    The Farmer can use up to   ");
			addGuidebook("chapter.machines.p6l12", "4 Range Upgrades to increase   ");
			addGuidebook("chapter.machines.p6l13", "its working area. Note the     ");
			addGuidebook("chapter.machines.p6l14", "upgrades increase the range    ");
			addGuidebook("chapter.machines.p6l15", "by multiples of 3.             ");

			addGuidebook("chapter.otherblocks", "Other Blocks");

		}

	}

}
