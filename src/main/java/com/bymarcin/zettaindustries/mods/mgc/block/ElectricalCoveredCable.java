package com.bymarcin.zettaindustries.mods.mgc.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalCoveredTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

public class ElectricalCoveredCable extends BlockContainer{

	
	public ElectricalCoveredCable() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName("CoveredCable");
		GameRegistry.registerBlock(this, "CoveredCable");
		GameRegistry.registerTileEntity(ElectricalCoveredTileEntity.class, "CoveredCable");
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float hitx, float hity, float hitz) {
		if(p.getHeldItem()!=null && p.getHeldItem().getItem()!=null){
			Block b = Block.getBlockFromItem(p.getHeldItem().getItem());
			int metadata = p.getHeldItem().getItemDamage();
			if(b!=null){
				if(b.isOpaqueCube()){
					String name = GameData.getBlockRegistry().getNameForObject(b);
					TileEntity te = w.getTileEntity(x, y, z);
					if(te instanceof ElectricalCoveredTileEntity){
						((ElectricalCoveredTileEntity)te).blockID = name + ":" + metadata;
						((ElectricalCoveredTileEntity)te).block = b;
						w.setBlockMetadataWithNotify(x, y, z, metadata, 2);
					}
					
					System.out.println("Block:" + name + ":" + metadata);
				}else{
					
				}
			}
		}
		return true;
	}
	
	@Override
	public IIcon getIcon(IBlockAccess w, int x, int y, int z, int s) {
		TileEntity te = w.getTileEntity(x, y, z);
		if(te instanceof ElectricalCoveredTileEntity){
			if(((ElectricalCoveredTileEntity)te).block!=null){
				return ((ElectricalCoveredTileEntity)te).block.getIcon(w, x, y, z, s);
			}
		}
			return super.getIcon(w, x, y, z, s);
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return super.getIcon(side, metadata);
	}

	@Override
	public TileEntity createNewTileEntity(World w, int metadata) {
		return new ElectricalCoveredTileEntity();
	}

}
