package com.bymarcin.zettaindustries.mods.eawiring.connectors.block;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.description.ConnectorDesc;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.node.ConnectorNode;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityConnectorBase;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorConventer;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorHV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorLV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorMV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorRHV;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAWireConnector;
import com.bymarcin.zettaindustries.registry.ICustomHighlight;

import mods.eln.node.simple.SimpleNode;
import mods.eln.node.simple.SimpleNodeBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;
 
public class EAConnector extends SimpleNodeBlock  implements ICustomHighlight{
	IIcon[] icons;
	String[] iconsName = new String[] { ":eawires/wireconnector", ":eawires/connectorLV", ":eawires/connectorMV", ":eawires/connectorHV", ":eawires/relayHV", ":eawires/connectorHV" };
	
	AxisAlignedBB[] box = new AxisAlignedBB[]{
			AxisAlignedBB.getBoundingBox(4/16f, 3/16f, 4/16f, 12/16f, 16/16f, 12/16f),
			AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 4/16f, 1)
	};
	
	public static final int wireconnector = 0;
	public static final int connectorLV = 1;
	public static final int connectorMV = 2;
	public static final int connectorHV = 3;
	public static final int relayHV = 4;
	public static final int connectorConventer = 5;
	
	public EAConnector() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName("EAConnector");
		setHardness(3.0F);
		setResistance(5);
		setDescriptor(new ConnectorDesc());
		
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i=0; i<iconsName.length; i++)
			list.add(new ItemStack(item, 1, i));
	}
	
	
	@Override
	public SimpleNode newNode() {
		return new ConnectorNode();
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
		if (world.getTileEntity(x, y, z) instanceof TileEntityConnectorBase && !(world.getTileEntity(x, y, z) instanceof TileEntityEAWireConnector))
		{
			TileEntityConnectorBase relay = (TileEntityConnectorBase) world.getTileEntity(x, y, z);
			ForgeDirection fd = ForgeDirection.getOrientation(relay.getFacing());
			if (world.isAirBlock(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ))
			{
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		switch(meta){
			case wireconnector: return new TileEntityEAWireConnector();
			case connectorLV:return new TileEntityEAConnectorLV();
			case connectorMV:return new TileEntityEAConnectorMV();
			case connectorHV:return new TileEntityEAConnectorHV();
			case relayHV:return new TileEntityEAConnectorRHV();
			case connectorConventer:return new TileEntityEAConnectorConventer();
			default: return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegistry) {
		icons = new IIcon[iconsName.length];
		for (int i = 0; i < icons.length; i++) {
			icons[i] = iconRegistry.registerIcon(ZettaIndustries.MODID + iconsName[i]);	
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}


	@Override
	public int getRenderType() {
		return EAConnectorRender.renderid;
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
	
	public void setBlockBounds(IBlockAccess world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(!(te instanceof TileEntityConnectorBase)) return;
			float length = te instanceof TileEntityEAConnectorRHV?.875f: te instanceof TileEntityEAConnectorHV?.75f: te instanceof TileEntityEAConnectorMV?.5625f: .5f;

			switch(((TileEntityConnectorBase)te).facing )
			{
			case 0://UP
				this.setBlockBounds(.3125f,0,.3125f,  .6875f,length,.6875f);
				break;
			case 1://DOWN
				this.setBlockBounds(.3125f,1-length,.3125f,  .6875f,1,.6875f);
				break;
			case 2://SOUTH
				this.setBlockBounds(.3125f,.3125f,0,  .6875f,.6875f,length);
				break;
			case 3://NORTH
				this.setBlockBounds(.3125f,.3125f,1-length,  .6875f,.6875f,1);
				break;
			case 4://EAST
				this.setBlockBounds(0,.3125f,.3125f,  length,.6875f,.6875f);
				break;
			case 5://WEST
				this.setBlockBounds(1-length,.3125f,.3125f,  1,.6875f,.6875f);
				break;
		}
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World wrd, int x, int y, int z, Vec3 origin, Vec3 direction) {
		if(wrd.getBlockMetadata(x, y, z)==wireconnector){
			MovingObjectPosition closest = null;
			for (AxisAlignedBB aabb : box) {
				MovingObjectPosition mop = aabb.getOffsetBoundingBox(x, y, z).calculateIntercept(origin, direction);
				if (mop != null) {
					if (closest != null && mop.hitVec.distanceTo(origin) < closest.hitVec.distanceTo(origin)) {
						closest = mop;
					} else {
						closest = mop;
					}
				}
			}
			if (closest != null) {
				closest.blockX = x;
				closest.blockY = y;
				closest.blockZ = z;
			}
			return closest==null?super.collisionRayTrace(wrd, x, y, z, origin, direction):closest;
		}else{
			return super.collisionRayTrace(wrd, x, y, z, origin, direction);
		} 
	}
	
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB ref, List list, Entity player) {
		int meta = w.getBlockMetadata(x, y, z);
		if(meta == wireconnector){
			setBlockBounds(4/16f, 3/16f, 4/16f, 12/16f, 16/16f, 12/16f);
			super.addCollisionBoxesToList(w, x, y, z, ref, list, player);
			setBlockBounds(0, 0, 0, 1, 4/16f, 1);
			super.addCollisionBoxesToList(w, x, y, z, ref, list, player);
		}else{
			this.setBlockBounds(w,x,y,z);
			super.addCollisionBoxesToList(w, x, y, z, ref, list, player);
		}
	}
	
	@Override
	public AxisAlignedBB[] getSelectedBoundingBoxFromPool(World world, int x, int y, int z, EntityPlayer player) {
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == wireconnector){
			return box;
		}else{
			setBlockBounds(world,x,y,z);
			return new AxisAlignedBB[]{getCollisionBoundingBoxFromPool(world, x, y, z).offset(-x, -y, -z)};
		}
	}
	
	
}
