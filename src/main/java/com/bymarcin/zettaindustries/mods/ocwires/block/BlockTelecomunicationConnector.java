package com.bymarcin.zettaindustries.mods.ocwires.block;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Arrays;

public class BlockTelecomunicationConnector extends BlockIETileProvider<ConnectorTypes> {

	public BlockTelecomunicationConnector() {
		super("telecommunicationconnector", Material.IRON, PropertyEnum.create("type", ConnectorTypes.class), ItemBlockIEBase.class, IEProperties.FACING_ALL);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		setAllNotNormalBlock();
		setBlockLayer(BlockRenderLayer.SOLID, BlockRenderLayer.TRANSLUCENT);
		this.setCreativeTab(ZettaIndustries.tabZettaIndustries);
	}


	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityTelecomunicationConnector) {
			TileEntityTelecomunicationConnector connector = (TileEntityTelecomunicationConnector) te;
			if(world.isAirBlock(pos.offset(connector.f))) {
				this.dropBlockAsItem(connector.getWorld(), pos, world.getBlockState(pos), 0);
				connector.getWorld().setBlockToAir(pos);
			}
		}
	}
	@Override
	protected BlockStateContainer createBlockState() {
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty<?>[] unlisted = (base instanceof ExtendedBlockState) ? ((ExtendedBlockState) base).getUnlistedProperties().toArray(new IUnlistedProperty[0]) : new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState) {
			IExtendedBlockState ext = (IExtendedBlockState) state;
			TileEntity te = world.getTileEntity(pos);
			if (!(te instanceof TileEntityTelecomunicationConnector))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityTelecomunicationConnector)te).genConnBlockstate());
		}
		return state;
	}
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta==0)
			return new TileEntityTelecomunicationConnector();
		return null;
	}
	@Override
	public String createRegistryName() {
		return ZettaIndustries.MODID+":"+name;
	}
}
