package assemblyline.datagen.server.recipe.vanilla;

import java.util.function.Consumer;

import assemblyline.References;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.ElectrodynamicsShapedCraftingRecipe;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class AssemblyLineCraftingTableRecipes extends AbstractRecipeGenerator {

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer) {

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

	public void addMachines(Consumer<IFinishedRecipe> consumer) {

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
