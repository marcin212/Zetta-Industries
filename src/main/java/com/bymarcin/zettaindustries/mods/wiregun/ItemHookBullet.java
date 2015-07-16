package com.bymarcin.zettaindustries.mods.wiregun;

import blusunrize.immersiveengineering.api.tool.IBullet;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;

import java.util.List;

public class ItemHookBullet extends BasicItem implements IBullet{
	ItemStack empty;
	private static final int maxWidth = 220*2/3;

	public ItemHookBullet() {
		super("hook_bullet");
		empty = GameRegistry.findItemStack("ImmersiveEngineering", "bullet", 1);
		iconString = ZettaIndustries.MODID+":hook_bullet";
	}

	@Override
	public boolean canSpawnBullet(ItemStack bulletStack) {
		return bulletStack != null;
	}

	@Override
	public void spawnBullet(EntityPlayer player, ItemStack bulletStack, boolean electro) {
		Vec3 vec = player.getLookVec();

		doSpawnBullet(player, vec, vec, bulletStack, electro);
	}

	EntityHookBullet doSpawnBullet(EntityPlayer player, Vec3 vecSpawn, Vec3 vecDir, ItemStack stack, boolean electro)
	{
		EntityHookBullet bullet = new EntityHookBullet(player.worldObj, player, vecDir.xCoord*1.5,vecDir.yCoord*1.5,vecDir.zCoord*1.5, stack);
		bullet.motionX = vecDir.xCoord;
		bullet.motionY = vecDir.yCoord;
		bullet.motionZ = vecDir.zCoord;
		bullet.bulletElectro = electro;
		player.worldObj.spawnEntityInWorld(bullet);
		return bullet;
	}

	@Override
	public ItemStack getCasing(ItemStack itemStack) {
		return empty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean par4) {
		{
			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			String tip =  StatCollector.translateToLocal("item.hook_bullet.tip").replace("\\n", "\n");
			if(!tip.equals("item.hook_bullet.tip")) {
				String[] lines = tip.split("\n");

				for(String line : lines) {
					List list = font.listFormattedStringToWidth(line, maxWidth);
					tooltip.addAll(list);
				}
			}
		}


		super.addInformation(stack, player, tooltip, par4);
	}
}
