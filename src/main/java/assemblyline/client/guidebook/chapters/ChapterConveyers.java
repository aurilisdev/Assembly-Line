package assemblyline.client.guidebook.chapters;

import assemblyline.References;
import assemblyline.prefab.utils.TextUtils;
import assemblyline.registers.AssemblyLineBlocks;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.TextWrapperObject;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ChapterConveyers extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 2.0F, 32, 32, AssemblyLineBlocks.blockConveyorBelt.asItem());
	
	public ChapterConveyers(Module module) {
		super(module);
	}
	
	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return TextUtils.guidebook("chapter.conveyers");
	}

	@Override
	public void addData() {
		
		//Default Conveyor Belts
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.l1")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.l2")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.horizontal")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.diagonalup")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.diagonaldown")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.vertical")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.l3")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/conveyerhorizontal.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/conveyerdiagonalup.png")));	
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/conveyerdiagonaldown.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/conveyervertical.png")));	
		
		//Sorter Belt
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockSorterBelt.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 2.0F, 32, 32, AssemblyLineBlocks.blockSorterBelt.asItem()));
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.l4")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/sorterbelt1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/sorterbelt2.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/sorterbelt3.png")));
		
		//Detector
		pageData.add(new TextWrapperObject(AssemblyLineBlocks.blockDetector.asItem().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 2.0F, 32, 32, AssemblyLineBlocks.blockDetector.asItem()));
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.conveyers.l5")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/detector1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, new ResourceLocation(References.ID, "textures/screen/guidebook/detector2.png")));
		
	}

}
