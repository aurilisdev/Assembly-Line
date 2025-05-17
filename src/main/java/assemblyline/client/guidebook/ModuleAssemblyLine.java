package assemblyline.client.guidebook;

import assemblyline.AssemblyLine;
import assemblyline.client.guidebook.chapters.ChapterConveyers;
import assemblyline.client.guidebook.chapters.ChapterMachines;
import assemblyline.prefab.utils.AssemblyTextUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import voltaic.client.guidebook.utils.components.Module;
import voltaic.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;

public class ModuleAssemblyLine extends Module {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, AssemblyLine.rl("textures/screen/guidebook/assemblylinelogo.png"));

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public IFormattableTextComponent getTitle() {
		return AssemblyTextUtils.guidebook(AssemblyLine.ID);
	}

	@Override
	public void addChapters() {
		chapters.add(new ChapterConveyers(this));
		chapters.add(new ChapterMachines(this));
	}

}
