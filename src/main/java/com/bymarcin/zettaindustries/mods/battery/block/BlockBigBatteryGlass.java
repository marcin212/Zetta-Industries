package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityGlass;
import com.bymarcin.zettaindustries.utils.Sides;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBigBatteryGlass extends BasicBlockMultiblockBase {
	public static IIcon icons[] = new IIcon[16];
	public static IIcon transparentIcon;
	
	public BlockBigBatteryGlass() {
		super("batteryglass");
        info.add("Valid for: Sides");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityGlass();
	}


	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for(int i=0; i<16; i++){
			icons[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_glass_"+i);
		}
		
			transparentIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_transparent");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	
	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		ForgeDirection[] dirsToCheck = Sides.neighborsBySide[side];
		ForgeDirection dir;
		Block myBlock = blockAccess.getBlock(x, y, z);

		// First check if we have a block in front of us of the same type - if
		// so, just be completely transparent on this side
		ForgeDirection out = ForgeDirection.getOrientation(side);
		if (blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) == myBlock) {
			return transparentIcon;
		}

		// Calculate icon index based on whether the blocks around this block
		// match it
		// Icons use a naming pattern so that the bits correspond to:
		// 1 = Connected on top, 2 = connected on bottom, 4 = connected on left,
		// 8 = connected on right
		int iconIdx = 0;
		for (int i = 0; i < dirsToCheck.length; i++) {
			dir = dirsToCheck[i];
			// Same blockID and metadata on this side?
			if (blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == myBlock) {
				// Connected!
				iconIdx |= 1 << i;
			}
		}

		return icons[iconIdx];
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return icons[0];
	}
}
