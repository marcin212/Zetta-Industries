package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityControler;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBigBatteryController extends BasicBlockMultiblockBase{
	public static IIcon iconSideOn;
	public static IIcon iconSideOff;
	public static IIcon icon;
	
	public BlockBigBatteryController() {
		super("batterycontroller");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.sides"));
        info.add(localize("tooltip.controller1"));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityControler();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.isSneaking()) {
			return false;
		}
				
		if(world.getTileEntity(x, y, z) !=null &&
			((TileEntityControler) world.getTileEntity(x, y, z)).getMultiblockController()!=null && 
					((TileEntityControler) world.getTileEntity(x, y, z)).getMultiblockController().isAssembled()){
			
			player.openGui(ZettaIndustries.instance, 3, world, x, y, z);
			return true;
		}
		return false;
	}
	
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		if(par1 ==0 || par1 == 1) return icon;
		if(par2==0)
			return iconSideOff;
		else
			return iconSideOn;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_part");
		iconSideOff = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_controler_off");
		iconSideOn = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_controler_on");
	}
}
