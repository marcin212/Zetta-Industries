package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.utils.WorldUtils;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class SmartCardTerminalBlock extends BlockContainer{
	IIcon[] icons = new IIcon[3];
	public static final short FRONT = 0;
	public static final short SIDE = 1;
	public static final short TOP_BOTTOM = 2;
	
	public SmartCardTerminalBlock() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setHardness(3f);
		setBlockName("smartcardterminalblock");
		GameRegistry.registerBlock(this, "smartcardterminalblock");
		GameRegistry.registerTileEntity(SmartCardTerminalTileEntity.class, "SmartCardTerminalTileEntity");
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int hitx, float hity, float hitz, float c) {
		TileEntity te = w.getTileEntity(x, y, z);
		if(te instanceof SmartCardTerminalTileEntity){
			return ((SmartCardTerminalTileEntity) te).onBlockActivated(p);
		}
		return false;
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int p_149725_5_) {
		TileEntity te = world.getTileEntity(x, y, z);
		 if (te instanceof SmartCardTerminalTileEntity) {
			ItemStack card = ((SmartCardTerminalTileEntity) te).card;
			if(card!=null){
				WorldUtils.dropItem(ItemStack.copyItemStack(card), world.rand, x, y, z, world);
				((SmartCardTerminalTileEntity) te).card = null;
			}
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister ir) {
		icons[TOP_BOTTOM] = ir.registerIcon(ZettaIndustries.MODID + ":nfc/terminal_top");
		icons[FRONT] = ir.registerIcon(ZettaIndustries.MODID + ":nfc/terminal_front");
		icons[SIDE] = ir.registerIcon(ZettaIndustries.MODID + ":nfc/terminal_side");
	}
	
	public static final int[] sides = {3,4,2,5};
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		if(metadata<4){
		switch(side){
		case 0:
		case 1:
			return icons[TOP_BOTTOM];
		default:
			return sides[metadata]==side?icons[FRONT]:icons[SIDE];
		}
		}
		return icons[TOP_BOTTOM];
		//south == 0
		//east == 3
		//north == 2
		//west == 1
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		int whichDirectionFacing = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new SmartCardTerminalTileEntity();
	}

}
