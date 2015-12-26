package com.bymarcin.zettaindustries.mods.mgc.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public class LampSocketBlock extends BlockContainer{

	public LampSocketBlock() {
		super(Material.iron);
		GameRegistry.registerBlock(this, "LampSocket");
		GameRegistry.registerTileEntity(LampSocketTileEntity.class, "LampSocketTileEntity");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new LampSocketTileEntity();
	}

	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof LampSocketTileEntity){
			return ((LampSocketTileEntity) te).getLightValue();
		}
		return 0;
	}
	
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float what, float these, float are) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity == null || player.isSneaking()) {
                    return false;
            }
            player.openGui(ZettaIndustries.instance, 0, world, x, y, z);
            return true;
    }


}
