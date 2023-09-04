package assemblyline.datagen.server.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import assemblyline.datagen.server.recipe.vanilla.AssemblyLineCraftingTableRecipes;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public class AssemblyLineRecipeProvider extends RecipeProvider {

	public final List<AbstractRecipeGenerator> GENERATORS = new ArrayList<>();

	public AssemblyLineRecipeProvider(PackOutput output) {
		super(output);
		addRecipes();
	}

	public void addRecipes() {
		GENERATORS.add(new AssemblyLineCraftingTableRecipes());
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		for (AbstractRecipeGenerator generator : GENERATORS) {
			generator.addRecipes(consumer);
		}
	}

}
