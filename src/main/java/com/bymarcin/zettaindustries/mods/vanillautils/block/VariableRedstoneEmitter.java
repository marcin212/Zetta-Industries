package com.bymarcin.zettaindustries.mods.vanillautils.block;


import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlock;

public class VariableRedstoneEmitter extends BasicBlock{
	static IIcon emitterIcon[] = new IIcon[16];
	static IIcon emitterIconBottom;
	static IIcon emitterIconSide;

	public VariableRedstoneEmitter() {
		super(Material.iron, "variableredstoneemitter");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.getHeldItem()==null && player.isSneaking() && world.getBlockMetadata(x, y, z)>0){
			world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) -1, 2);
			world.notifyBlockChange(x, y, z, this);
			return true;
		}
		
		if(player.getHeldItem()==null && player.isSneaking() && world.getBlockMetadata(x, y, z)==0){
			world.setBlockMetadataWithNotify(x, y, z, 15, 2);
			world.notifyBlockChange(x, y, z, this);
			return true;
		}
		
		if(player.getHeldItem()==null && !player.isSneaking() && world.getBlockMetadata(x, y, z)<15){
			world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 2);
			world.notifyBlockChange(x, y, z, this);
			return true;
		}
		
		if(player.getHeldItem()==null && !player.isSneaking() && world.getBlockMetadata(x, y, z)==15){
			world.setBlockMetadataWithNotify(x, y, z,0, 2);
			world.notifyBlockChange(x, y, z, this);
			return true;
		}
		return false;
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return par1iBlockAccess.getBlockMetadata(par2, par3, par4);
	}
	@Override
	
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		for(int i=0;i<16;i++)
			emitterIcon[i] = par1IconRegister.registerIcon(ZettaIndustries.MODID + ":vre/redstone_emitter_"+i);
			emitterIconBottom = par1IconRegister.registerIcon(ZettaIndustries.MODID + ":vre/redstone_emitter_bottom");
			emitterIconSide = par1IconRegister.registerIcon(ZettaIndustries.MODID + ":vre/redstone_emitter_side");
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		switch(par1){
			case 0: return emitterIconBottom;
			case 1: return emitterIcon[par2];
			default: return emitterIconSide;
		}
	}
}

