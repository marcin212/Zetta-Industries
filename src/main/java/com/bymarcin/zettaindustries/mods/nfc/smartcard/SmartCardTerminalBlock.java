package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.utils.WorldUtils;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class SmartCardTerminalBlock extends BlockContainer{
	public static final PropertyInteger FACING = PropertyInteger.create("facing",0,3);

	public SmartCardTerminalBlock() {
		super(Material.IRON);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setHardness(3f);
		setRegistryName("smartcardterminalblock");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof SmartCardTerminalTileEntity){
			return ((SmartCardTerminalTileEntity) te).onBlockActivated(playerIn);
		}
		return false;
	}
	

	public void onBlockDestroy(World worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		 if (te instanceof SmartCardTerminalTileEntity) {
			ItemStack card = ((SmartCardTerminalTileEntity) te).card;
			if(card!=null){
				WorldUtils.dropItem(ItemStack.copyItemStack(card), worldIn.rand, pos.getX(), pos.getY(), pos.getZ(), worldIn);
				((SmartCardTerminalTileEntity) te).card = null;
			}
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		onBlockDestroy(worldIn, pos);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		onBlockDestroy(worldIn, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING,meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		int whichDirectionFacing = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		worldIn.setBlockState(pos, state.withProperty(FACING, whichDirectionFacing), 2);
		worldIn.notifyNeighborsOfStateChange(pos, state.getBlock());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new SmartCardTerminalTileEntity();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}


}
