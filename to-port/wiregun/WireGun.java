package com.bymarcin.zettaindustries.mods.wiregun;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.INeedLoadCompleteEvent;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import joptsimple.internal.Strings;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.ShapedOreRecipe;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;

public class WireGun implements IMod, IProxy, INeedLoadCompleteEvent{

    ItemHookBullet itemHookBullet;
	ItemHook itemHook;

	@Override
	public void preInit() {

	}

	@Override
	public void init() {
        itemHookBullet = new ItemHookBullet();
		itemHook = new ItemHook();
        GameRegistry.registerItem(itemHookBullet, "hookBullet");
		GameRegistry.registerItem(itemHook, "hook");
		EntityRegistry.registerModEntity(EntityHookBullet.class, "hookBullet", 0, ZettaIndustries.instance, 64, 1, true);
		ZIRegistry.registerProxy(this);
	}

	@Override
	public void postInit() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemHook, 16),
			" B",
			"BI",
			" I",
			'B', new ItemStack(Blocks.iron_bars),
			'I', "ingotIron"));
		for (int i = 1; i <= 3; i++) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemHookBullet, i),
				Strings.repeat('H', i),
				Strings.repeat('C', i),
				Strings.repeat('G', i),
				'H', itemHook,
				'C', GameRegistry.findItemStack("ImmersiveEngineering", "bullet", 1),
				'G', Items.gunpowder
			));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void manualPages(){
		ManualInstance manual = ManualHelper.getManual();
		manual.addEntry("wiregun", ZettaIndustries.MODID, 
				new ManualPages.CraftingMulti(manual, "wiregun1", new ItemStack(itemHookBullet), new ItemStack(itemHook))
		);
	}

	@Override
	public void serverLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientLoadComplete() {
		manualPages();
	}

	@Override
	public void clientSide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub
		
	}
}
