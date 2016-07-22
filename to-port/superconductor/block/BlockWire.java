package com.bymarcin.zettaindustries.mods.superconductor.block;

import java.util.ArrayList;
import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.IBlockInfo;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.BlockMultiblockBase;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductorMod;
import com.bymarcin.zettaindustries.mods.superconductor.render.Glowing;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityWire;
import com.bymarcin.zettaindustries.utils.Sides;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockWire extends BlockMultiblockBase implements Glowing, IBlockInfo {
	public static IIcon[] icons = new IIcon[16];
	public static IIcon[] glowIcons = new IIcon[16];
	public static IIcon transparentIcon;
	List<String> info = new ArrayList<String>();
	
	 
	public BlockWire() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName("blockwire");
		setHardness(3.0F);
		info.add("[WIP] Use at own risk!");
	}
	
	public List<String> getInformation() {
		return info;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityWire();
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return icons[0];
	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {

		ForgeDirection[] dirsToCheck = Sides.neighborsBySide[side];
		ForgeDirection dir;
		Block myBlock = blockAccess.getBlock(x, y, z);

		// First check if we have a block in front of us of the same type - if
		// so, just be completely transparent on this side
		ForgeDirection out = ForgeDirection.getOrientation(side);
		if (blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) == myBlock || blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) instanceof BlockControler) {
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
			if (blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == myBlock || blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof BlockControler) {
				// Connected!
				iconIdx |= 1 << i;
			}
		}

		return icons[iconIdx];
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for (int i = 0; i < 16; i++) {
			icons[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_body_" + i);
			glowIcons[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_body_glow_" + i);
		}

		transparentIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/bb_transparent");
	}

	@Override
	public IIcon getGlowIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		
		TileEntityWire tile = (TileEntityWire) blockAccess.getTileEntity(x, y, z);
		if(tile != null && tile.getMultiblockController() !=null){
			if(!((SuperConductor)tile.getMultiblockController()).active){
				return null;
			}
		}else{
			return null;
		}
		ForgeDirection[] dirsToCheck = Sides.neighborsBySide[side];
		ForgeDirection dir;
		Block myBlock = blockAccess.getBlock(x, y, z);

		// First check if we have a block in front of us of the same type -
		// if
		// so, just be completely transparent on this side
		ForgeDirection out = ForgeDirection.getOrientation(side);
		if (blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) == myBlock || blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) instanceof BlockControler) {
			return null;
		}

		// Calculate icon index based on whether the blocks around this
		// block
		// match it
		// Icons use a naming pattern so that the bits correspond to:
		// 1 = Connected on top, 2 = connected on bottom, 4 = connected on
		// left,
		// 8 = connected on right
		int iconIdx = 0;
		for (int i = 0; i < dirsToCheck.length; i++) {
			dir = dirsToCheck[i];
			// Same blockID and metadata on this side?
			if (blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == myBlock || blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof BlockControler) {
				// Connected!
				iconIdx |= 1 << i;
			}
		}
		
		return glowIcons[iconIdx];

	}

	@Override
	public int getRenderType() {
		return SuperConductorMod.glowRenderID;
	}

	@Override
	public boolean canRenderInPass(int pass) {
		SuperConductorMod.pass = pass;
		return true;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return 4;
	}

}
