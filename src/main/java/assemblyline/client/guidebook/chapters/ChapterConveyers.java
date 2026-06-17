package assemblyline.client.guidebook.chapters;

import assemblyline.AssemblyLine;
import assemblyline.prefab.utils.AssemblyTextUtils;
import assemblyline.registers.AssemblyLineItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import voltaic.client.guidebook.ScreenGuidebook;
import voltaic.client.guidebook.utils.components.Chapter;
import voltaic.client.guidebook.utils.components.Module;
import voltaic.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import voltaic.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import voltaic.client.guidebook.utils.pagedata.text.TextWrapperObject;

public class ChapterConveyers extends Chapter {

    private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F,
	    AssemblyLineItems.ITEM_CONVEYORBELT.get());

    public ChapterConveyers(Module module) {
	super(module);
    }

    @Override
    public ItemWrapperObject getLogo() {
	return LOGO;
    }

    @Override
    public MutableComponent getTitle() {
	return AssemblyTextUtils.guidebook("chapter.conveyers");
    }

    @Override
    public void addData() {

	// Default Conveyor Belts
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.l1")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.l2")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.horizontal")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.diagonalup")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.diagonaldown"))
		.setIndentions(1).setSeparateStart());
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.vertical")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.l3")).setSeparateStart());
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/conveyerhorizontal.png")));
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/conveyerdiagonalup.png")));
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/conveyerdiagonaldown.png")));
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/conveyervertical.png")));

	// Sorter Belt
	pageData.add(new TextWrapperObject(
		AssemblyLineItems.ITEM_SORTERBELT.get().getDescription().copy().withStyle(ChatFormatting.BOLD))
		.setCentered().setNewPage());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F,
		AssemblyLineItems.ITEM_SORTERBELT.get()));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.l4")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/sorterbelt1.png")));
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/sorterbelt2.png")));
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/sorterbelt3.png")));

	// Detector
	pageData.add(new TextWrapperObject(
		AssemblyLineItems.ITEM_DETECTOR.get().getDescription().copy().withStyle(ChatFormatting.BOLD))
		.setCentered().setNewPage());
	pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F,
		AssemblyLineItems.ITEM_DETECTOR.get()));
	pageData.add(new TextWrapperObject(AssemblyTextUtils.guidebook("chapter.conveyers.l5")).setIndentions(1)
		.setSeparateStart());
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/detector1.png")));
	pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75,
		AssemblyLine.rl("textures/screen/guidebook/detector2.png")));

    }

}
