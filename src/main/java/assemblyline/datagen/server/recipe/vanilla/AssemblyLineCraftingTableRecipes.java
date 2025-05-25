package assemblyline.datagen.server.recipe.vanilla;

import assemblyline.AssemblyLine;
import assemblyline.common.block.subtype.SubtypeAssemblyMachine;
import assemblyline.registers.AssemblyLineItems;
import electrodynamics.Electrodynamics;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import voltaic.common.item.subtype.SubtypeItemUpgrade;
import voltaic.common.tags.VoltaicTags;
import voltaic.datagen.utils.server.recipe.AbstractRecipeGenerator;
import voltaic.datagen.utils.server.recipe.ShapedCraftingRecipeBuilder;

public class AssemblyLineCraftingTableRecipes extends AbstractRecipeGenerator {

	private static final ModLoadedCondition ELECTRO_LOADED = new ModLoadedCondition("electrodynamics");
	private static final NotCondition ELECTRO_NOT_LOADED = new NotCondition(ELECTRO_LOADED);

	@Override
	public void addRecipes(RecipeOutput output) {

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.crate), 1)
				//
				.addPattern("IBI")
				//
				.addPattern("ICI")
				//
				.addPattern("IBI")
				//
				.addKey('I', Tags.Items.INGOTS_IRON)
				//
				.addKey('B', Items.IRON_BARS)
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.complete(AssemblyLine.ID, "crate_small", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.cratemedium), 1)
				//
				.addPattern("SCS")
				//
				.addKey('S', AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.crate))
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.complete(AssemblyLine.ID, "crate_medium", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.cratelarge), 1)
				//
				.addPattern("MCM")
				//
				.addKey('M', AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.cratemedium))
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.complete(AssemblyLine.ID, "crate_large", output);

		/*
		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_SPEEDUPGRADE_ADVANCED.get(), 1)
				//
				.addPattern("PGP")
				//
				.addPattern("BWB")
				//
				.addPattern("CGC")
				//
				.addKey('P', Tags.Items.INGOTS_IRON)
				//
				.addKey('G', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				//
				.addKey('B', ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.basicspeed))
				//
				.addKey('W', Tags.Items.INGOTS_COPPER)
				//
				.addKey('C', Tags.Items.INGOTS_GOLD)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(Electrodynamics.ID, "upgrade_advanced_speed_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_SPEEDUPGRADE_BASIC.get(), 1)
				//
				.addPattern("PGP")
				//
				.addPattern("WWW")
				//
				.addPattern("CGC")
				//
				.addKey('P', Tags.Items.INGOTS_IRON)
				//
				.addKey('G', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				//
				.addKey('W', Tags.Items.INGOTS_GOLD)
				//
				.addKey('C', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(Electrodynamics.ID, "upgrade_basic_speed_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_UPGRADEITEMINPUT.get(), 1)
				//
				.addPattern("C")
				//
				.addPattern("P")
				//
				.addPattern("A")
				//
				.addKey('A', Tags.Items.INGOTS_GOLD)
				//
				.addKey('C', Tags.Items.DUSTS_REDSTONE)
				//
				.addKey('P', Items.STICKY_PISTON)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(Electrodynamics.ID, "upgrade_item_input_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_UPGRADEITEMOUTPUT.get(), 1)
				//
				.addPattern("C")
				//
				.addPattern("P")
				//
				.addPattern("A")
				//
				.addKey('A', Tags.Items.INGOTS_GOLD)
				//
				.addKey('C', Tags.Items.DUSTS_REDSTONE)
				//
				.addKey('P', Items.PISTON)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(Electrodynamics.ID, "upgrade_item_output_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_UPGRADERANGE.get(), 1)
				//
				.addPattern("PWP")
				//
				.addPattern("WBW")
				//
				.addPattern("PWP")
				//
				.addKey('P', Tags.Items.INGOTS_IRON)
				//
				.addKey('W', Tags.Items.INGOTS_COPPER)
				//
				.addKey('B', Tags.Items.DUSTS_REDSTONE)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(Electrodynamics.ID, "upgrade_range_noelectro", output);
		*/
		addMachines(output);

	}

	public void addMachines(RecipeOutput output) {

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.autocrafter), 1)
				//
				.addPattern("GBG")
				//
				.addPattern("CTC")
				//
				.addPattern("PWP")
				//
				.addKey('G', VoltaicTags.Items.GEAR_STEEL)
				//
				.addKey('B', VoltaicTags.Items.CIRCUITS_BASIC)
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.addKey('T', Items.CRAFTING_TABLE)
				//
				.addKey('P', Items.PISTON)
				//
				.addKey('W', ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.copper))
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "autocrafter_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.autocrafter), 1)
				//
				.addPattern("GBG")
				//
				.addPattern("CTC")
				//
				.addPattern("PWP")
				//
				.addKey('G', Tags.Items.INGOTS_IRON)
				//
				.addKey('B', Tags.Items.DUSTS_REDSTONE)
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.addKey('T', Items.CRAFTING_TABLE)
				//
				.addKey('P', Items.PISTON)
				//
				.addKey('W', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "autocrafter_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_CONVEYORBELT.get(), 12)
				//
				.addPattern("SSS")
				//
				.addPattern("WMW")
				//
				.addKey('S', VoltaicTags.Items.INGOT_STEEL)
				//
				.addKey('W', ItemTags.PLANKS)
				//
				.addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "conveyorbelt_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_CONVEYORBELT.get(), 12)
				//
				.addPattern("SSS")
				//
				.addPattern("WMW")
				//
				.addKey('S', Tags.Items.INGOTS_IRON)
				//
				.addKey('W', ItemTags.PLANKS)
				//
				.addKey('M', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "conveyorbelt_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.blockbreaker), 1)
				//
				.addPattern("CPC")
				//
				.addPattern("COC")
				//
				.addPattern("CMC")
				//
				.addKey('C', Tags.Items.COBBLESTONES)
				//
				.addKey('P', Items.IRON_PICKAXE)
				//
				.addKey('O', Items.OBSERVER)
				//
				.addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "blockbreaker_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.blockbreaker), 1)
				//
				.addPattern("CPC")
				//
				.addPattern("COC")
				//
				.addPattern("CMC")
				//
				.addKey('C', Tags.Items.COBBLESTONES)
				//
				.addKey('P', Items.IRON_PICKAXE)
				//
				.addKey('O', Items.OBSERVER)
				//
				.addKey('M', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "blockbreaker_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.blockplacer), 1)
				//
				.addPattern("CPC")
				//
				.addPattern("COC")
				//
				.addPattern("CMC")
				//
				.addKey('C', Tags.Items.COBBLESTONES)
				//
				.addKey('P', Items.PISTON)
				//
				.addKey('O', Items.OBSERVER)
				//
				.addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "blockplacer_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.blockplacer), 1)
				//
				.addPattern("CPC")
				//
				.addPattern("COC")
				//
				.addPattern("CMC")
				//
				.addKey('C', Tags.Items.COBBLESTONES)
				//
				.addKey('P', Items.PISTON)
				//
				.addKey('O', Items.OBSERVER)
				//
				.addKey('M', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "blockplacer_noelectro", output);


		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_DETECTOR.get(), 1)
				//
				.addPattern("IEI")
				//
				.addPattern("ICI")
				//
				.addPattern("I I")
				//
				.addKey('I', VoltaicTags.Items.INGOT_STEEL)
				//
				.addKey('E', Tags.Items.ENDER_PEARLS)
				//
				.addKey('C', VoltaicTags.Items.CIRCUITS_BASIC)
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "detector_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_DETECTOR.get(), 1)
				//
				.addPattern("IEI")
				//
				.addPattern("ICI")
				//
				.addPattern("I I")
				//
				.addKey('I', Tags.Items.INGOTS_IRON)
				//
				.addKey('E', Tags.Items.ENDER_PEARLS)
				//
				.addKey('C', Tags.Items.DUSTS_REDSTONE)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "detector_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.farmer), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("ACH")
				//
				.addPattern("PWP")
				//
				.addKey('P', VoltaicTags.Items.PLATE_STEEL)
				//
				.addKey('S', Items.SHEARS)
				//
				.addKey('A', Items.IRON_AXE)
				//
				.addKey('C', VoltaicTags.Items.CIRCUITS_BASIC)
				//
				.addKey('H', Items.IRON_HOE)
				//
				.addKey('W', VoltaicTags.Items.INSULATED_COPPER_WIRES)
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "farmer_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.farmer), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("ACH")
				//
				.addPattern("PWP")
				//
				.addKey('P', Tags.Items.INGOTS_IRON)
				//
				.addKey('S', Items.SHEARS)
				//
				.addKey('A', Items.IRON_AXE)
				//
				.addKey('C', Tags.Items.DUSTS_REDSTONE)
				//
				.addKey('H', Items.IRON_HOE)
				//
				.addKey('W', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "farmer_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.mobgrinder), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("SCS")
				//
				.addPattern("PWP")
				//
				.addKey('P', VoltaicTags.Items.PLATE_STEEL)
				//
				.addKey('S', Items.IRON_SWORD)
				//
				.addKey('C', VoltaicTags.Items.CIRCUITS_BASIC)
				//
				.addKey('W', VoltaicTags.Items.INSULATED_COPPER_WIRES)
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "mobgrinder_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.mobgrinder), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("SCS")
				//
				.addPattern("PWP")
				//
				.addKey('P', Tags.Items.INGOTS_IRON)
				//
				.addKey('S', Items.IRON_SWORD)
				//
				.addKey('C', Tags.Items.DUSTS_REDSTONE)
				//
				.addKey('W', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "mobgrinder_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.rancher), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("SCS")
				//
				.addPattern("PWP")
				//
				.addKey('P', VoltaicTags.Items.PLATE_STEEL)
				//
				.addKey('S', Items.SHEARS)
				//
				.addKey('C', VoltaicTags.Items.CIRCUITS_BASIC)
				//
				.addKey('W', VoltaicTags.Items.INSULATED_COPPER_WIRES)
				//
				.addConditions(ELECTRO_LOADED)
				//
				.complete(AssemblyLine.ID, "rancher_electro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEMS_ASSEMBLYMACHINE.getValue(SubtypeAssemblyMachine.rancher), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("SCS")
				//
				.addPattern("PWP")
				//
				.addKey('P', Tags.Items.INGOTS_IRON)
				//
				.addKey('S', Items.SHEARS)
				//
				.addKey('C', Tags.Items.DUSTS_REDSTONE)
				//
				.addKey('W', Tags.Items.INGOTS_COPPER)
				//
				.addConditions(ELECTRO_NOT_LOADED)
				//
				.complete(AssemblyLine.ID, "rancher_noelectro", output);

		ShapedCraftingRecipeBuilder.start(AssemblyLineItems.ITEM_SORTERBELT.get(), 1)
				//
				.addPattern("WWW")
				//
				.addPattern("HCH")
				//
				.addKey('W', ItemTags.PLANKS)
				//
				.addKey('H', Items.HOPPER)
				//
				.addKey('C', AssemblyLineItems.ITEM_CONVEYORBELT.get())
				//
				.complete(AssemblyLine.ID, "sorterbelt", output);

	}

}
