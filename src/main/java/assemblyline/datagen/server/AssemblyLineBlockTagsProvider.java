package assemblyline.datagen.server;

import assemblyline.AssemblyLine;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AssemblyLineBlockTagsProvider extends BlockTagsProvider {

	public AssemblyLineBlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, AssemblyLine.ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

	}

}
