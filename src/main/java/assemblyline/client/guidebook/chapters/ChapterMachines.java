package assemblyline.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import assemblyline.DeferredRegisters;
import electrodynamics.api.item.ItemUtils;
import electrodynamics.client.guidebook.utils.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.TextWrapperObject;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Page;
import net.minecraft.ChatFormatting;

public class ChapterMachines extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(17, 60, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockFarmer));

	@Override
	protected List<Page> genPages() {
		List<Page> pages = new ArrayList<>();

		pages.add(new Page(new TextWrapperObject[] {
				//
				new TextWrapperObject(45, 53, 4210752, "guidebook.assemblyline.chapter.machines.blockbreaktitle").setTextStyles(ChatFormatting.UNDERLINE),
				//
				new TextWrapperObject(10, 80, 4210752, "guidebook.assemblyline.chapter.machines.p1l1"),
				//
				new TextWrapperObject(10, 90, 4210752, "guidebook.assemblyline.chapter.machines.p1l2"),
				//
				new TextWrapperObject(10, 100, 4210752, "guidebook.assemblyline.chapter.machines.p1l3"),
				//
				new TextWrapperObject(10, 110, 4210752, "guidebook.assemblyline.chapter.machines.p1l4"),
				//
				new TextWrapperObject(10, 120, 4210752, "guidebook.assemblyline.chapter.machines.p1l5"),
				//
				new TextWrapperObject(10, 130, 4210752, "guidebook.assemblyline.chapter.machines.p1l6"),
				//
				new TextWrapperObject(10, 140, 4210752, "guidebook.assemblyline.chapter.machines.p1l7") },
				new ItemWrapperObject[] {
						//
						new ItemWrapperObject(17, 50, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockBlockBreaker)) }));

		pages.add(new Page(new TextWrapperObject[] {
				//
				new TextWrapperObject(45, 53, 4210752, "guidebook.assemblyline.chapter.machines.blockplacetitle").setTextStyles(ChatFormatting.UNDERLINE),
				//
				new TextWrapperObject(10, 80, 4210752, "guidebook.assemblyline.chapter.machines.p2l1"),
				//
				new TextWrapperObject(10, 90, 4210752, "guidebook.assemblyline.chapter.machines.p2l2"),
				//
				new TextWrapperObject(10, 100, 4210752, "guidebook.assemblyline.chapter.machines.p2l3"),
				//
				new TextWrapperObject(10, 110, 4210752, "guidebook.assemblyline.chapter.machines.p2l4"),
				//
				new TextWrapperObject(10, 120, 4210752, "guidebook.assemblyline.chapter.machines.p2l5"),
				//
				new TextWrapperObject(10, 130, 4210752, "guidebook.assemblyline.chapter.machines.p2l6"),
				//
				new TextWrapperObject(10, 140, 4210752, "guidebook.assemblyline.chapter.machines.p2l7") },
				new ItemWrapperObject[] {
						//
						new ItemWrapperObject(17, 50, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockBlockPlacer)) }));

		pages.add(new Page(new TextWrapperObject[] {
				//
				new TextWrapperObject(45, 53, 4210752, "guidebook.assemblyline.chapter.machines.ranchertitle").setTextStyles(ChatFormatting.UNDERLINE),
				//
				new TextWrapperObject(10, 80, 4210752, "guidebook.assemblyline.chapter.machines.p3l1"),
				//
				new TextWrapperObject(10, 90, 4210752, "guidebook.assemblyline.chapter.machines.p3l2"),
				//
				new TextWrapperObject(10, 100, 4210752, "guidebook.assemblyline.chapter.machines.p3l3"),
				//
				new TextWrapperObject(10, 110, 4210752, "guidebook.assemblyline.chapter.machines.p3l4"),
				//
				new TextWrapperObject(10, 120, 4210752, "guidebook.assemblyline.chapter.machines.p3l5"),
				//
				new TextWrapperObject(10, 130, 4210752, "guidebook.assemblyline.chapter.machines.p3l6"),
				//
				new TextWrapperObject(10, 140, 4210752, "guidebook.assemblyline.chapter.machines.p3l7"),
				//
				new TextWrapperObject(10, 150, 4210752, "guidebook.assemblyline.chapter.machines.p3l8"),
				//
				new TextWrapperObject(10, 160, 4210752, "guidebook.assemblyline.chapter.machines.p3l9") },
				new ItemWrapperObject[] {
						//
						new ItemWrapperObject(17, 50, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockRancher)) }));

		pages.add(new Page(new TextWrapperObject[] {
				//
				new TextWrapperObject(45, 53, 4210752, "guidebook.assemblyline.chapter.machines.grindertitle").setTextStyles(ChatFormatting.UNDERLINE),
				//
				new TextWrapperObject(10, 80, 4210752, "guidebook.assemblyline.chapter.machines.p4l1"),
				//
				new TextWrapperObject(10, 90, 4210752, "guidebook.assemblyline.chapter.machines.p4l2"),
				//
				new TextWrapperObject(10, 100, 4210752, "guidebook.assemblyline.chapter.machines.p4l3"),
				//
				new TextWrapperObject(10, 110, 4210752, "guidebook.assemblyline.chapter.machines.p4l4"),
				//
				new TextWrapperObject(10, 120, 4210752, "guidebook.assemblyline.chapter.machines.p4l5"),
				//
				new TextWrapperObject(10, 130, 4210752, "guidebook.assemblyline.chapter.machines.p4l6"),
				//
				new TextWrapperObject(10, 140, 4210752, "guidebook.assemblyline.chapter.machines.p4l7"),
				//
				new TextWrapperObject(10, 150, 4210752, "guidebook.assemblyline.chapter.machines.p4l8"),
				//
				new TextWrapperObject(10, 160, 4210752, "guidebook.assemblyline.chapter.machines.p4l9") },
				new ItemWrapperObject[] {
						//
						new ItemWrapperObject(17, 50, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockMobGrinder)) }));

		pages.add(new Page(new TextWrapperObject[] {
				//
				new TextWrapperObject(45, 53, 4210752, "guidebook.assemblyline.chapter.machines.farmertitle").setTextStyles(ChatFormatting.UNDERLINE),
				//
				new TextWrapperObject(10, 80, 4210752, "guidebook.assemblyline.chapter.machines.p5l1"),
				//
				new TextWrapperObject(10, 90, 4210752, "guidebook.assemblyline.chapter.machines.p5l2"),
				//
				new TextWrapperObject(10, 100, 4210752, "guidebook.assemblyline.chapter.machines.p5l3"),
				//
				new TextWrapperObject(10, 110, 4210752, "guidebook.assemblyline.chapter.machines.p5l4"),
				//
				new TextWrapperObject(10, 120, 4210752, "guidebook.assemblyline.chapter.machines.p5l5"),
				//
				new TextWrapperObject(10, 130, 4210752, "guidebook.assemblyline.chapter.machines.p5l6"),
				//
				new TextWrapperObject(10, 140, 4210752, "guidebook.assemblyline.chapter.machines.p5l7"),
				//
				new TextWrapperObject(10, 150, 4210752, "guidebook.assemblyline.chapter.machines.p5l8"),
				//
				new TextWrapperObject(10, 160, 4210752, "guidebook.assemblyline.chapter.machines.p5l9"),
				//
				new TextWrapperObject(10, 170, 4210752, "guidebook.assemblyline.chapter.machines.p5l10"),
				//
				new TextWrapperObject(10, 180, 4210752, "guidebook.assemblyline.chapter.machines.p5l11") },
				new ItemWrapperObject[] {
						//
						new ItemWrapperObject(17, 50, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockFarmer)) }));

		pages.add(new Page(new TextWrapperObject[] {
				//
				new TextWrapperObject(10, 40, 4210752, "guidebook.assemblyline.chapter.machines.p6l1"),
				//
				new TextWrapperObject(10, 50, 4210752, "guidebook.assemblyline.chapter.machines.p6l2"),
				//
				new TextWrapperObject(10, 60, 4210752, "guidebook.assemblyline.chapter.machines.p6l3"),
				//
				new TextWrapperObject(10, 70, 4210752, "guidebook.assemblyline.chapter.machines.p6l4"),
				//
				new TextWrapperObject(10, 80, 4210752, "guidebook.assemblyline.chapter.machines.p6l5"),
				//
				new TextWrapperObject(10, 90, 4210752, "guidebook.assemblyline.chapter.machines.p6l6"),
				//
				new TextWrapperObject(10, 100, 4210752, "guidebook.assemblyline.chapter.machines.p6l7"),
				//
				new TextWrapperObject(10, 110, 4210752, "guidebook.assemblyline.chapter.machines.p6l8"),
				//
				new TextWrapperObject(10, 120, 4210752, "guidebook.assemblyline.chapter.machines.p6l9"),
				//
				new TextWrapperObject(10, 130, 4210752, "guidebook.assemblyline.chapter.machines.p6l10"),
				//
				new TextWrapperObject(10, 140, 4210752, "guidebook.assemblyline.chapter.machines.p6l11"),
				//
				new TextWrapperObject(10, 150, 4210752, "guidebook.assemblyline.chapter.machines.p6l12"),
				//
				new TextWrapperObject(10, 160, 4210752, "guidebook.assemblyline.chapter.machines.p6l13"),
				//
				new TextWrapperObject(10, 170, 4210752, "guidebook.assemblyline.chapter.machines.p6l14"),
				//
				new TextWrapperObject(10, 180, 4210752, "guidebook.assemblyline.chapter.machines.p6l15") }));

		return pages;
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public String getTitleKey() {
		return "guidebook.assemblyline.chapter.machines";
	}

}
