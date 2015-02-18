package com.bymarcin.zettaindustries.mods.ecatalogue;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import forestry.api.mail.PostManager;
import forestry.mail.Letter;
import forestry.mail.items.ItemLetter;

public class ECatalogueBlock extends BasicBlockContainer {
	IIcon top;
	IIcon bottom;
	IIcon side;

	public ECatalogueBlock() {
		super(Material.iron, "ecatalogue");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int w, float px, float py, float pz) {
		if (world.isRemote)
			return false;
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof ECatalogueTileEntity && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemLetter) {
			ECatalogueTileEntity eCalogueTileEntity = (ECatalogueTileEntity) te;
			if (eCalogueTileEntity.isAddressSet()) {
				ItemStack l = (ItemStack) player.getHeldItem();
				if (l.stackSize == 1) {
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
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new ECatalogueTileEntity();
	}

	@Override
	public void registerBlockIcons(IIconRegister r) {
		top = r.registerIcon(ZettaIndustries.MODID + ":ecatalogue/ecatalogue_top");
		bottom = r.registerIcon(ZettaIndustries.MODID + ":ecatalogue/ecatalogue_bottom");
		side = r.registerIcon(ZettaIndustries.MODID + ":ecatalogue/ecatalogue_side");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		switch (side) {
		case 0:
			return bottom;
		case 1:
			return top;
		default:
			return this.side;
		}
	}
}
