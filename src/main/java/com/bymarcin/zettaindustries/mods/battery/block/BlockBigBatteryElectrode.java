package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityElectrode;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBigBatteryElectrode extends BasicBlockMultiblockBase{
	public static IIcon icon ;
	public BlockBigBatteryElectrode() {
		super("batteryelectrode");
        info.add("Valid for: Inside");
        info.add("Must be placed in a column below a power tap");
        info.add("Column must span the interior height of the battery");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityElectrode();
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		return icon;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_electrode");
	}
}
