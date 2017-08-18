package com.bymarcin.zettaindustries.basic;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class FakeItemIcon extends Item{
	public FakeItemIcon() {
		setMaxStackSize(1);
		setRegistryName("logo");
		setUnlocalizedName(ZettaIndustries.MODID + ".logo");
		setCreativeTab(ZettaIndustries.tabZettaIndustries);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			EntityShurikenMarcina shurikenMarcina = new EntityShurikenMarcina(worldIn, playerIn);
			shurikenMarcina.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntity(shurikenMarcina);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
	}
}
