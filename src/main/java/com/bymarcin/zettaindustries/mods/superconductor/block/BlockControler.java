package com.bymarcin.zettaindustries.mods.superconductor.block;

import java.util.ArrayList;
import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.IBlockInfo;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.BlockMultiblockBase;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductor;
import com.bymarcin.zettaindustries.mods.superconductor.SuperConductorMod;
import com.bymarcin.zettaindustries.mods.superconductor.render.Glowing;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;
import com.bymarcin.zettaindustries.utils.Sides;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyHandler;

public class BlockControler extends BlockMultiblockBase implements Glowing, IBlockInfo{
	public static IIcon icons[][] = new IIcon[2][16];
	public static IIcon iconsWire[] = new IIcon[16];
	public static IIcon glowIconsWire[] = new IIcon[16];
	public static IIcon glowIcons[][] = new IIcon[2][16];
	public static IIcon transparentIcon;
	 List<String> info = new ArrayList<String>();
	 
	public BlockControler() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName("blockcontroller");
		setHardness(3.0F);
		info.add("[WIP] Use at own risk!");
	}
    
	@Override
	public List<String> getInformation() {
		return info;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for(int i=0; i<16; i++){
			icons[0][i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_output_"+i);
			icons[1][i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_input_"+i);
			glowIcons[0][i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_output_glow_"+i);
			glowIcons[1][i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_input_glow_"+i);
			iconsWire[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_body_" + i);
			glowIconsWire[i] = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/sc_conductor_body_glow_" + i);
			
		}
			transparentIcon = iconRegister.registerIcon(ZettaIndustries.MODID + ":superconductor/bb_transparent");
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return icons[getBlocktype(metadata)][0];
	}
	
	public static int getBlocktype(int metadata){
		return ((metadata&8)==8)?1:0;
	}
	
	public static int getBlockType(IBlockAccess blockAccess, int x, int y, int z){
		return getBlocktype(blockAccess.getBlockMetadata(x, y, z));
	}
	
	@Override
	public boolean onBlockActivated(World world, int par2, int par3,
			int par4, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		if(player.getCurrentEquippedItem()==null && player.isSneaking()){
			world.setBlockMetadataWithNotify(par2, par3, par4, world.getBlockMetadata(par2, par3, par4)^8, 2);
			return true;
		}
		
		if(!player.isSneaking() && world.getTileEntity(par2, par3, par4) instanceof TileEntityControler){
			if(((TileEntityControler)world.getTileEntity(par2, par3, par4)).getMultiblockController().isAssembled()){ 
				player.openGui(ZettaIndustries.instance, 4, world, par2, par3, par4);
				return true;
			}
		}
		
		return false;
	}

	
	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		ForgeDirection[] dirsToCheck = Sides.neighborsBySide[side];
		ForgeDirection dir;
		Block myBlock = blockAccess.getBlock(x, y, z);
		World world = blockAccess.getTileEntity(x, y, z)!=null?blockAccess.getTileEntity(x, y, z).getWorldObj():null;

		// First check if we have a block in front of us of the same type - if
		// so, just be completely transparent on this side
		ForgeDirection out = ForgeDirection.getOrientation(side);
		if (blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) instanceof BlockWire || blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) == myBlock) {
			return transparentIcon;
		}
		if(isEnergyHandler(world, x + out.offsetX, y + out.offsetY, z + out.offsetZ))
			return (blockAccess.getBlockMetadata(x, y, z)&7)==side?icons[getBlockType(blockAccess,x, y, z)][15]:iconsWire[15];
		

		// Calculate icon index based on whether the blocks around this block
		// match it
		// Icons use a naming pattern so that the bits correspond to:
		// 1 = Connected on top, 2 = connected on bottom, 4 = connected on left,
		// 8 = connected on right
		int iconIdx = 0;
		for (int i = 0; i < dirsToCheck.length; i++) {
			dir = dirsToCheck[i];
			// Same blockID and metadata on this side?
			if (blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof BlockWire || blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == myBlock || (isEnergyHandler(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)&& dir.ordinal()==(blockAccess.getBlockMetadata(x, y, z)&7)) ) {
				// Connected!
				iconIdx |= 1 << i;
			}
		}
		if((blockAccess.getBlockMetadata(x, y, z)&7)==side)
			return icons[getBlockType(blockAccess,x, y, z)][iconIdx];
		else{
			return iconsWire[iconIdx];
		}
	}
	
	
	public boolean isEnergyHandler(World world, int x, int y, int z){
        return world != null && world.getTileEntity(x, y, z) instanceof IEnergyHandler;
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityControler();
	}

	@Override
	public IIcon getGlowIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		
		TileEntityControler tile = (TileEntityControler) blockAccess.getTileEntity(x, y, z);
		if(tile != null && tile.getMultiblockController() !=null){
			if(!((SuperConductor)tile.getMultiblockController()).active)
				return null;
		}else
			return null;
		
		ForgeDirection[] dirsToCheck = Sides.neighborsBySide[side];
		ForgeDirection dir;
		Block myBlock = blockAccess.getBlock(x, y, z);
		World world = blockAccess.getTileEntity(x, y, z)!=null?blockAccess.getTileEntity(x, y, z).getWorldObj():null;

		// First check if we have a block in front of us of the same type - if
		// so, just be completely transparent on this side
		ForgeDirection out = ForgeDirection.getOrientation(side);
		if (blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) instanceof BlockWire || blockAccess.getBlock(x + out.offsetX, y + out.offsetY, z + out.offsetZ) == myBlock) {
			return null;
		}
		if(isEnergyHandler(world, x + out.offsetX, y + out.offsetY, z + out.offsetZ))
			return (blockAccess.getBlockMetadata(x, y, z)&7)==side?glowIcons[getBlockType(blockAccess,x, y, z)][15]:glowIconsWire[15];

		// Calculate icon index based on whether the blocks around this block
		// match it
		// Icons use a naming pattern so that the bits correspond to:
		// 1 = Connected on top, 2 = connected on bottom, 4 = connected on left,
		// 8 = connected on right
		int iconIdx = 0;
		for (int i = 0; i < dirsToCheck.length; i++) {
			dir = dirsToCheck[i];
			// Same blockID and metadata on this side?
			if (blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof BlockWire || blockAccess.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == myBlock || (isEnergyHandler(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)&& dir.ordinal()==(blockAccess.getBlockMetadata(x, y, z)&7)) ) {
				// Connected!
				iconIdx |= 1 << i;
			}
		}
		
		if((blockAccess.getBlockMetadata(x, y, z)&7)==side)
			return glowIcons[getBlockType(blockAccess,x, y, z)][iconIdx];
		else{
			return glowIconsWire[iconIdx];
		}
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
	
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		int whichDirectionFacing=0;
		if(par5EntityLivingBase.rotationPitch>=70){
			whichDirectionFacing = 0;//down
		}else if(par5EntityLivingBase.rotationPitch<=-70){
			whichDirectionFacing = 1;//up
		}else{
			whichDirectionFacing = (MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
			switch(whichDirectionFacing){
				case 0: whichDirectionFacing = ForgeDirection.SOUTH.ordinal(); break;
				case 1: whichDirectionFacing = ForgeDirection.WEST.ordinal(); break;
				case 2: whichDirectionFacing = ForgeDirection.NORTH.ordinal(); break;
				case 3: whichDirectionFacing = ForgeDirection.EAST.ordinal(); break;
			}
		}
		par1World.setBlockMetadataWithNotify(x, y, z,par5EntityLivingBase.isSneaking()?whichDirectionFacing:ForgeDirection.OPPOSITES[whichDirectionFacing], 2);
	}
}
