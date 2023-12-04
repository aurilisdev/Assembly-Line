package assemblyline.client.guidebook;

import assemblyline.References;
import assemblyline.client.guidebook.chapters.ChapterConveyers;
import assemblyline.client.guidebook.chapters.ChapterMachines;
import assemblyline.prefab.utils.AssemblyTextUtils;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ModuleAssemblyLine extends Module {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, new ResourceLocation(References.ID, "textures/screen/guidebook/assemblylinelogo.png"));

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return AssemblyTextUtils.guidebook(References.ID);
	}

	@Override
	public void addChapters() {
		chapters.add(new ChapterConveyers(this));
		chapters.add(new ChapterMachines(this));
	}

}