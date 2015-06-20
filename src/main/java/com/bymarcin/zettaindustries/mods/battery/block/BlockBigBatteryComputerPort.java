package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityComputerPort;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBigBatteryComputerPort extends BasicBlockMultiblockBase{
	public static IIcon icon;
	
	public BlockBigBatteryComputerPort() {
		super("batterycomputerport");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.sides"));
	}

	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityComputerPort();
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		return icon;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ZettaIndustries.MODID+":battery/bb_computer");
	}
}
