package assemblyline.datagen.server.recipe.vanilla;

import java.util.function.Consumer;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.item.subtype.SubtypeCeramic;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.ElectrodynamicsShapedCraftingRecipe;
import electrodynamics.datagen.utils.recipe.ElectrodynamicsShapelessCraftingRecipe;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class AssemblyLineCraftingTableRecipes extends AbstractRecipeGenerator {

	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer) {

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockCrate.asItem(), 1)
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
				.complete(References.ID, "crate_small", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockCrateMedium.asItem(), 1)
				//
				.addPattern("SCS")
				//
				.addKey('S', AssemblyLineBlocks.blockCrate.asItem())
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.complete(References.ID, "crate_medium", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockCrateLarge.asItem(), 1)
				//
				.addPattern("MCM")
				//
				.addKey('M', AssemblyLineBlocks.blockCrateMedium.asItem())
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.complete(References.ID, "crate_large", consumer);

		addMachines(consumer);

	}

	public void addMachines(Consumer<FinishedRecipe> consumer) {

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockAutocrafter.asItem(), 1)
				//
				.addPattern("GBG")
				//
				.addPattern("CTC")
				//
				.addPattern("PWP")
				//
				.addKey('G', ElectrodynamicsTags.Items.GEAR_STEEL)
				//
				.addKey('B', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
				//
				.addKey('C', Tags.Items.CHESTS)
				//
				.addKey('T', Items.CRAFTING_TABLE)
				//
				.addKey('P', Items.PISTON)
				//
				.addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.copper))
				//
				.complete(References.ID, "autocrafter", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockConveyorBelt.asItem(), 12)
				//
				.addPattern("SSS")
				//
				.addPattern("WMW")
				//
				.addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
				//
				.addKey('W', ItemTags.PLANKS)
				//
				.addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
				//
				.complete(References.ID, "conveyorbelt", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockBlockBreaker.asItem(), 1)
				//
				.addPattern("CPC")
				//
				.addPattern("COC")
				//
				.addPattern("CMC")
				//
				.addKey('C', Tags.Items.COBBLESTONE)
				//
				.addKey('P', Items.IRON_PICKAXE)
				//
				.addKey('O', Items.OBSERVER)
				//
				.addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
				//
				.complete(References.ID, "blockbreaker", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockBlockPlacer.asItem(), 1)
				//
				.addPattern("CPC")
				//
				.addPattern("COC")
				//
				.addPattern("CMC")
				//
				.addKey('C', Tags.Items.COBBLESTONE)
				//
				.addKey('P', Items.PISTON)
				//
				.addKey('O', Items.OBSERVER)
				//
				.addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
				//
				.complete(References.ID, "blockplacer", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockDetector.asItem(), 1)
				//
				.addPattern("IEI")
				//
				.addPattern("ICI")
				//
				.addPattern("I I")
				//
				.addKey('I', ElectrodynamicsTags.Items.INGOT_STEEL)
				//
				.addKey('E', Tags.Items.ENDER_PEARLS)
				//
				.addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
				//
				.complete(References.ID, "detector", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockFarmer.asItem(), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("ACH")
				//
				.addPattern("PWP")
				//
				.addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
				//
				.addKey('S', Items.SHEARS)
				//
				.addKey('A', Items.IRON_AXE)
				//
				.addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
				//
				.addKey('H', Items.IRON_HOE)
				//
				.addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.insulatedcopper))
				//
				.complete(References.ID, "farmer", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockMobGrinder.asItem(), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("SCS")
				//
				.addPattern("PWP")
				//
				.addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
				//
				.addKey('S', Items.IRON_SWORD)
				//
				.addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
				//
				.addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.insulatedcopper))
				//
				.complete(References.ID, "mobgrinder", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockRancher.asItem(), 1)
				//
				.addPattern("PSP")
				//
				.addPattern("SCS")
				//
				.addPattern("PWP")
				//
				.addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
				//
				.addKey('S', Items.SHEARS)
				//
				.addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
				//
				.addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.insulatedcopper))
				//
				.complete(References.ID, "rancher", consumer);

		ElectrodynamicsShapedCraftingRecipe.start(AssemblyLineBlocks.blockSorterBelt.asItem(), 1)
				//
				.addPattern("WWW")
				//
				.addPattern("HCH")
				//
				.addKey('W', ItemTags.PLANKS)
				//
				.addKey('H', Items.HOPPER)
				//
				.addKey('C', AssemblyLineBlocks.blockConveyorBelt.asItem())
				//
				.complete(References.ID, "sorterbelt", consumer);

	}

}
