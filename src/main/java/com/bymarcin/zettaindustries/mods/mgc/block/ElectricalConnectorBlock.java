package com.bymarcin.zettaindustries.mods.mgc.block;

import java.util.List;
import java.util.Random;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.MGC;
import com.bymarcin.zettaindustries.mods.mgc.item.ElectricalConnectorItem;
import com.bymarcin.zettaindustries.mods.mgc.render.ElectricalConnectorRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;
import com.bymarcin.zettaindustries.utils.WorldUtils;
import com.cout970.magneticraft.api.MgAPI;
import com.cout970.magneticraft.api.electricity.IInterPoleWire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;

public class ElectricalConnectorBlock extends BlockContainer {
	public static int renderid = RenderingRegistry.getNextAvailableRenderId();
	public static final int connectorLV = 0;
	public static final int connectorMV = 1;
	public static final int connectorHV = 2;
	public static final int relayHV = 3;
	IIcon[] icons;
	String[] iconsName = new String[] { ":mgc/connectorLV", ":mgc/connectorMV", ":mgc/connectorHV", ":mgc/relayHV" };

	public ElectricalConnectorBlock() {
		super(Material.iron);
		setHardness(3.0F);
		setResistance(5);
		setBlockName("ElectricalConnector");
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		GameRegistry.registerBlock(this, ElectricalConnectorItem.class, "ElectricalConnector");
		GameRegistry.registerTileEntity(ElectricalConnectorTileEntity.class, "ElectricalConnectorTileEntity");
	}

	@Override
	public void breakBlock(World w, int x, int y, int z, Block b, int meta) {
		TileEntity t = w.getTileEntity(x, y, z);
		if (t instanceof ElectricalConnectorTileEntity) {
			((ElectricalConnectorTileEntity) t).getPoleConnection().disconnectAll();
		}
		super.breakBlock(w, x, y, z, b, meta);
	}
	
    public void onBlockPreDestroy(World w, int x, int y, int z, int meta) {
        super.onBlockPreDestroy(w, x, y, z, meta);
        if (w.isRemote || MGC.coil==null) return;
        TileEntity tileEntity = w.getTileEntity(x, y, z);
        if ((tileEntity instanceof ElectricalConnectorTileEntity)) {
        	ElectricalConnectorTileEntity inventory = (ElectricalConnectorTileEntity) tileEntity;
            Random rand = w.rand;
            
            for(IInterPoleWire a: inventory.getPoleConnection().getConnectedConductors()){
                WorldUtils.dropItem(ItemStack.copyItemStack(MGC.coil), rand, x, y, z, w);
            }
        }
    }

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new ElectricalConnectorTileEntity(metadata);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < iconsName.length; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public int getRenderType() {
		return renderid;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid)
	{
		super.onNeighborBlockChange(world, x, y, z, nbid);
		if (world.getTileEntity(x, y, z) instanceof ElectricalConnectorTileEntity)
		{
			ElectricalConnectorTileEntity relay = (ElectricalConnectorTileEntity) world.getTileEntity(x, y, z);
			ForgeDirection fd = ForgeDirection.getOrientation(relay.getFacing());
			if (world.isAirBlock(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ))
			{
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if (!(te instanceof ElectricalConnectorTileEntity))
			return;
		float length = meta == relayHV ? .875f : meta == connectorHV ? .75f : meta == connectorMV ? .5625f : .5f;

		switch (((ElectricalConnectorTileEntity) te).facing)
		{
		case 0:// UP
			this.setBlockBounds(.3125f, 0, .3125f, .6875f, length, .6875f);
			break;
		case 1:// DOWN
			this.setBlockBounds(.3125f, 1 - length, .3125f, .6875f, 1, .6875f);
			break;
		case 2:// SOUTH
			this.setBlockBounds(.3125f, .3125f, 0, .6875f, .6875f, length);
			break;
		case 3:// NORTH
			this.setBlockBounds(.3125f, .3125f, 1 - length, .6875f, .6875f, 1);
			break;
		case 4:// EAST
			this.setBlockBounds(0, .3125f, .3125f, length, .6875f, .6875f);
			break;
		case 5:// WEST
			this.setBlockBounds(1 - length, .3125f, .3125f, 1, .6875f, .6875f);
			break;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegistry) {
		icons = new IIcon[iconsName.length];
		for (int i = 0; i < icons.length; i++) {
			icons[i] = iconRegistry.registerIcon(ZettaIndustries.MODID + iconsName[i]);
		}
	}
}
