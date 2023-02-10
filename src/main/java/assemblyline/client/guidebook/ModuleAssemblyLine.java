package assemblyline.client.guidebook;

import java.util.ArrayList;
import java.util.List;

import assemblyline.References;
import assemblyline.client.guidebook.chapters.ChapterConveyers;
import assemblyline.client.guidebook.chapters.ChapterMachines;
import assemblyline.prefab.utils.TextUtils;
import electrodynamics.client.guidebook.utils.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import net.minecraft.network.chat.MutableComponent;

public class ModuleAssemblyLine extends Module {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(10, 38, 0, 0, 32, 32, 32, 32, References.ID + ":textures/screen/guidebook/assemblylinelogo.png");

	@Override
	protected List<Chapter> genChapters() {
		List<Chapter> chapters = new ArrayList<>();
		chapters.add(new ChapterConveyers());
		chapters.add(new ChapterMachines());
		return chapters;
	}

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return TextUtils.guidebook(References.ID);
	}

	@Override
	public boolean isFirst() {
		return false;
	}

}
