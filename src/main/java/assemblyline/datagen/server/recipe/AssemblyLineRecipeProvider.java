package assemblyline.datagen.server.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import assemblyline.datagen.server.recipe.vanilla.AssemblyLineCraftingTableRecipes;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

public class AssemblyLineRecipeProvider extends RecipeProvider {

	public final List<AbstractRecipeGenerator> GENERATORS = new ArrayList<>();

	public AssemblyLineRecipeProvider(DataGenerator gen) {
		super(gen);
		addRecipes();
	}

	public void addRecipes() {
		GENERATORS.add(new AssemblyLineCraftingTableRecipes());
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		for (AbstractRecipeGenerator generator : GENERATORS) {
			generator.addRecipes(consumer);
		}
	}

}
