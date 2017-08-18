package com.bymarcin.zettaindustries.mods.ecatalogue;

import com.bymarcin.zettaindustries.basic.BasicBlockContainer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.api.mail.PostManager;
import forestry.mail.Letter;
import forestry.mail.items.ItemLetter;

public class ECatalogueBlock extends BasicBlockContainer {

	public ECatalogueBlock() {
		super(Material.IRON, "ecatalogue");
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return false;
		ItemStack heldItem = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ECatalogueTileEntity && !heldItem.isEmpty() && heldItem.getItem() instanceof ItemLetter) {
			ECatalogueTileEntity eCalogueTileEntity = (ECatalogueTileEntity) te;
			if (eCalogueTileEntity.isAddressSet()) {
				ItemStack l = (ItemStack) heldItem;
				if (l.getCount() == 1) {
					NBTTagCompound nbt;
					Letter letter;
					if (l.getTagCompound() != null) {
						nbt = l.getTagCompound();
						letter = new Letter(nbt);
						letter.setRecipient(eCalogueTileEntity.getAddress());
					} else {
						nbt = new NBTTagCompound();
						letter = new Letter(PostManager.postRegistry.getMailAddress(player.getGameProfile()), eCalogueTileEntity.getAddress());
						letter.setText("");
					}
					letter.writeToNBT(nbt);
					l.setTagCompound(nbt);
					eCalogueTileEntity.setAddressSet(false);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int m) {
		return new ECatalogueTileEntity();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
}
