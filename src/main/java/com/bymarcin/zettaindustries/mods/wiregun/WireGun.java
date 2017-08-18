package com.bymarcin.zettaindustries.mods.wiregun;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.INeedLoadCompleteEvent;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import com.bymarcin.zettaindustries.utils.RecipeUtils;
import joptsimple.internal.Strings;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;

public class WireGun implements IMod, IProxy, INeedLoadCompleteEvent{
	ItemHook itemHook;

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof ItemBullet
				&& event.getItemStack().hasTagCompound()) {
			String bulletId = event.getItemStack().getTagCompound().getString("bullet");
			if ("zettaindustries:hook_bullet".equals(bulletId)) {
				event.getToolTip().add(I18n.translateToLocal("item.immersiveengineering.bullet.zettaindustries:hook_bullet.tip"));
			}
		}
	}

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(itemHook);
	}

	@SubscribeEvent
	public void onRegisterModels(ModelRegistryEvent event) {
		ZettaIndustries.proxy.registermodel(itemHook, 0);
	}

	@SubscribeEvent
	public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(itemHook, 16),
				" B",
				"BI",
				" I",
				'B', new ItemStack(Blocks.IRON_BARS),
				'I', "ingotIron").setRegistryName(itemHook.getRegistryName()));
		for (int i = 1; i <= 3; i++) {
			ItemStack hookBullet = BulletHandler.getBulletStack("zettaindustries:hook_bullet").copy();
			hookBullet.setCount(i);
			event.getRegistry().register(RecipeUtils.createShapedRecipe(hookBullet,
					Strings.repeat('H', i),
					Strings.repeat('C', i),
					Strings.repeat('G', i),
					'H', itemHook,
					'C', BulletHandler.emptyCasing,
					'G', Items.GUNPOWDER
			).setRegistryName(new ResourceLocation("zettaindustries:hook_bullet_" + i)));
		}
	}

	@Override
	public void preInit() {
		itemHook = new ItemHook();
		BulletHandler.registerBullet("zettaindustries:hook_bullet", new HookBullet());

		ZIRegistry.registerProxy(this);
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}
	
	@SideOnly(Side.CLIENT)
	public void manualPages(){
		ManualInstance manual = ManualHelper.getManual();
		manual.addEntry("wiregun", ZettaIndustries.MODID, 
				new ManualPages.CraftingMulti(manual, "wiregun1", BulletHandler.getBulletStack("zettaindustries:hook_bullet"), new ItemStack(itemHook))
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
