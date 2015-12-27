package com.bymarcin.zettaindustries.mods.mgc.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.render.LampSocketRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.util.ForgeDirection;

public class LampSocketBlock extends BlockContainer {

	public LampSocketBlock() {
		super(Material.iron);
		GameRegistry.registerBlock(this, "LampSocket");
		GameRegistry.registerTileEntity(LampSocketTileEntity.class, "LampSocketTileEntity");
		setBlockTextureName(ZettaIndustries.MODID + ":mgc/lampsocket");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new LampSocketTileEntity();
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof LampSocketTileEntity) {
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

	@Override
	public int getRenderType() {
		return LampSocketRenderer.renderid;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		this.setBlockBounds(1/4f, 25/32f, 0f, 3/4f, 1f, 1f);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

}
