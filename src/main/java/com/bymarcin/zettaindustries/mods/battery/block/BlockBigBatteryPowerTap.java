package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityPowerTap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class BlockBigBatteryPowerTap extends BasicBlockMultiblockBase{
	public static IIcon icon;
	public static IIcon iconTopIn;
	public static IIcon iconTopOut;
	
	public BlockBigBatteryPowerTap() {
		super("batterypowertap");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.top"));
        info.add(localize("tooltip.powertap1"));
        info.add(localize("tooltip.powertap2"));
        info.add(localize("tooltip.powertap3"));
	}

	@Override
	public boolean onBlockActivated(World world, int par2, int par3,
			int par4, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		if(!player.isSneaking() && !world.isRemote){
			player.openGui(ZettaIndustries.instance, 0, world, par2, par3, par4);
			return true;
		}
		
		if(player.getCurrentEquippedItem()==null && player.isSneaking()){
			if(world.getBlockMetadata(par2, par3, par4)==0){
				world.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
			}else{
				world.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
			}
			return true;
		}
		return false;
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityPowerTap();
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		if(par1 != ForgeDirection.UP.ordinal()) return icon;
		if(par2==0)
			return iconTopIn;
		else
			return iconTopOut;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_part");
		iconTopOut = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_output");
		iconTopIn = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_input");
	}
}
