package assemblyline.datagen.server.recipe;

import assemblyline.datagen.server.recipe.vanilla.AssemblyLineCraftingTableRecipes;
import net.minecraft.data.PackOutput;
import voltaic.datagen.utils.server.recipe.BaseRecipeProvider;

public class AssemblyLineRecipeProvider extends BaseRecipeProvider {

	public AssemblyLineRecipeProvider(PackOutput output) {
		super(output);
	}

	public void addRecipes() {
		generators.add(new AssemblyLineCraftingTableRecipes());
	}


}
