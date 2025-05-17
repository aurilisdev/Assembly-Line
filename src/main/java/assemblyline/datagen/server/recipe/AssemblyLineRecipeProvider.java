package assemblyline.datagen.server.recipe;

import assemblyline.datagen.server.recipe.vanilla.AssemblyLineCraftingTableRecipes;
import net.minecraft.data.DataGenerator;
import voltaic.datagen.utils.server.recipe.BaseRecipeProvider;

public class AssemblyLineRecipeProvider extends BaseRecipeProvider {

	public AssemblyLineRecipeProvider(DataGenerator gen) {
		super(gen);
	}

	public void addRecipes() {
		generators.add(new AssemblyLineCraftingTableRecipes());
	}


}
