package com.bymarcin.zettaindustries.mods.quarryfixer;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.core.BuildCraftAPI;
import buildcraft.builders.TileQuarry;
import buildcraft.core.Box;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class QuarryFixerBlock extends BasicBlock {
	private static IIcon[] topIconOn = new IIcon[4];
	private static IIcon[] topIconOff = new IIcon[4];
	private static IIcon bottomIcon;
	private static IIcon sideIcon;
	private static IIcon frontIcon;

	protected QuarryFixerBlock() {
		super(Material.iron, "quarryfixer");
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister){
		for (int i = 0; i < 4; i++) {
			topIconOff[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":quarry_fixer/quarry_fixer_top_off_" + i);
			topIconOn[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":quarry_fixer/quarry_fixer_top_on_" + i);
		}
		bottomIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":quarry_fixer/quarry_fixer_bottom");
		sideIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":quarry_fixer/quarry_fixer_side");
		frontIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":quarry_fixer/quarry_fixer_front");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2){
		switch (par1) {
		case 0:
			return bottomIcon;
		case 1:
			switch (par2 & 3) {
			case 0:
				return ((par2 >> 3) & 1) == 0 ? topIconOff[(par2 & 3)] : topIconOn[(par2 & 3)];
			case 1:
				return ((par2 >> 3) & 1) == 0 ? topIconOff[(par2 & 3)] : topIconOn[(par2 & 3)];
			case 2:
				return ((par2 >> 3) & 1) == 0 ? topIconOff[(par2 & 3)] : topIconOn[(par2 & 3)];
			case 3:
				return ((par2 >> 3) & 1) == 0 ? topIconOff[(par2 & 3)] : topIconOn[(par2 & 3)];
			}
		case 2:
			return ((par2 & 3) == 2) ? frontIcon : sideIcon;
		case 3:
			return (par2 & 3) == 0 ? frontIcon : sideIcon;
		case 4:
			return (par2 & 3) == 1 ? frontIcon : sideIcon;
		case 5:
			return (par2 & 3) == 3 ? frontIcon : sideIcon;
		default:
			return null;
		}
	}

	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		int whichDirectionFacing = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	}

	@Override
	public int tickRate(World par1World) {
		return 20;
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4,Random par5Random) {
		par1World.setBlockMetadataWithNotify(par2, par3, par4, (par1World.getBlockMetadata(par2, par3, par4) & 3), 2);
		super.updateTick(par1World, par2, par3, par4, par5Random);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if ((par1World.getBlockMetadata(par2, par3, par4) & 8) != 0)
			return true;
		TileQuarry tq = null;
		int sx, sy, sz;
		sx = par2;
		sy = par3;
		sz = par4;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (par1World.getTileEntity(sx + dir.offsetX, sy + dir.offsetY, sz + dir.offsetZ) instanceof TileQuarry) {
				tq = (TileQuarry) par1World.getTileEntity(sx + dir.offsetX, sy + dir.offsetY, sz + dir.offsetZ);
				break;
			}
		}
		if (tq == null)
			return false;
		par1World.setBlockMetadataWithNotify(par2, par3, par4, (par1World.getBlockMetadata(par2, par3, par4) | 8), 2);
		par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate(par1World));
		Box box = tq.getBox();
		for (int x = box.xMin; x <= box.xMax; x++) {
			for (int y = box.yMin - 1; y >= 1; y--) {
				for (int z = box.zMin; z <= box.zMax; z++) {
					if (x == box.xMin || x == box.xMax || z == box.zMin
							|| z == box.zMax) {
						if (BuildCraftAPI.softBlocks.contains(par1World.getBlock(x, y, z)))
							par1World.setBlock(x, y, z, Blocks.stone);
						continue;
					}
					if (BuildCraftAPI.softBlocks.contains(par1World.getBlock(x, y, z))) {
						par1World.setBlockToAir(x, y, z);
					}
				}
			}
		}
		return true;
	}
}
