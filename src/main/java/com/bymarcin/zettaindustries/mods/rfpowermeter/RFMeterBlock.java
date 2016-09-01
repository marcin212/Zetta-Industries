package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.basic.BasicBlockContainer;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RFMeterBlock extends BasicBlockContainer {
	public static final PropertyBool inverted = PropertyBool.create("inverted");
	public static final PropertyDirection front = PropertyDirection.create("front", EnumFacing.Plane.HORIZONTAL);
	public static final AxisAlignedBB[] boundingBox = new AxisAlignedBB[]{
			new AxisAlignedBB(2/16F-0.001F, 0, 0, 14/16F+0.001F, 1, 14/16F+0.001F),
			new AxisAlignedBB(0, 0, 2/16F-0.001F, 14/16F+0.001F, 1, 14/16F+0.001F),
			new AxisAlignedBB(2/16F-0.001F, 0, 2/16F-0.001F, 14/16F+0.001F, 1, 1),
			new AxisAlignedBB(2/16F-0.001F, 0, 2/16F-0.001F, 1, 1, 14/16F+0.001F),
			new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
	};

	public RFMeterBlock() {
		super(Material.IRON, "rfmeterblock");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return RFMeter.isOCAllowed ? new RFMeterTileEntityOC() : new RFMeterTileEntity();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, inverted, front);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(front, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
    public int getMetaFromState(IBlockState state)
    {
    	return (state.getValue(inverted)?1:0) | (state.getValue(front).ordinal() << 1);
    }

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getDefaultState().withProperty(inverted, (1&meta)==1?true:false).withProperty(front, EnumFacing.Plane.HORIZONTAL.facings()[((meta>>1)&3)] );
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		 if(player.getHeldItemMainhand()!=null && player.getHeldItemMainhand().getItem() instanceof ItemDye){
		 	RFMeterTileEntity te = (RFMeterTileEntity) world.getTileEntity(pos);
		 	if(te!=null){
		 		int color = EnumDyeColor.byDyeDamage(player.getHeldItemMainhand().getMetadata()).getMapColor().colorValue;
		 		te.r = (float)(color >> 16 & 255) / 255.0F;
		 		te.g = (float)(color >> 8 & 255) / 255.0F;
		 		te.b = (float)(color & 255) / 255.0F;
		 		return true;
		 	}
		 }

		 if(player.getHeldItemMainhand()==null && player.isSneaking()){
		 RFMeterTileEntity te = (RFMeterTileEntity) world.getTileEntity(pos);
		 	if(te!=null){
		 		te.invert();
				world.setBlockState(pos, world.getBlockState(pos).withProperty(RFMeterBlock.inverted, te.isInverted),2);
		 	}
		 	return true;
		 }
		 return false;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		 List<ItemStack> items = getDrops(world, pos, state, 0);
		 if (world.setBlockToAir(pos))
		 {
		 	if (!world.isRemote) {
		 		for (ItemStack item : items) {
		 			if ((player == null) || (!player.capabilities.isCreativeMode) || (item.hasTagCompound())) {
						spawnAsEntity(world, pos, item);
		 			}
		 		}
		 	}
		 	return true;
		 }
		 return false;
	}


	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack(this);
		RFMeterTileEntity tile = (RFMeterTileEntity) world.getTileEntity(pos);
		if (tile != null)
		{
			stack.setTagCompound( new NBTTagCompound());
			tile.getTag(stack.getTagCompound());
		}
		ret.add(stack);
		return ret;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {

	}


	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == base_state.getValue(front).getOpposite();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
	 EnumFacing meta = state.getValue(RFMeterBlock.front);

		switch (meta){
			case SOUTH:
				return boundingBox[0];
			case EAST:
				return boundingBox[1];
			case NORTH:
				return boundingBox[2];
			case WEST:
				return boundingBox[3];
			default:
				return boundingBox[4];
		}
	}

}
