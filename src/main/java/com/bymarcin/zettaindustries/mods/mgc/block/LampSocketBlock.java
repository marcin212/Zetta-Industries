package com.bymarcin.zettaindustries.mods.mgc.block;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.item.LampSocketItem;
import com.bymarcin.zettaindustries.mods.mgc.render.LampSocketRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;
import com.bymarcin.zettaindustries.utils.render.RenderUtils.PartInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public class LampSocketBlock extends BlockContainer {
	IIcon[] icon;
	String iconPrefix = ZettaIndustries.MODID + ":mgc/models/";
	String[] iconsName = new String[] {
			iconPrefix + "basementsconce/basementsconce",
			iconPrefix + "chandelier/chandelier",
			iconPrefix + "fluorescentlightsocket/fluorescentlightsocket",
			iconPrefix + "sconce/sconce",

			iconPrefix + "chandelier/candle",
			iconPrefix + "chandelier/candle_on",

			iconPrefix + "fluorescentlightsocket/fluorescentlightbulb",
			iconPrefix + "fluorescentlightsocket/fluorescentlightbulb_on",
	};

	public static final int subblocksCount = 4;
	public static final int basementsconce = 0;
	public static final int chandelier = 1;
	public static final int fluorescentlightsocket = 2;
	public static final int sconce = 3;

	public LampSocketBlock() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName("LampSocket");
		GameRegistry.registerBlock(this, LampSocketItem.class, "LampSocket");
		GameRegistry.registerTileEntity(LampSocketTileEntity.class, "LampSocketTileEntity");
		setBlockTextureName(ZettaIndustries.MODID + ":mgc/LampSocket");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new LampSocketTileEntity();
	}

	@Override
	public void registerBlockIcons(IIconRegister ir) {
		icon = new IIcon[iconsName.length];
		for (int i = 0; i < iconsName.length; i++) {
			icon[i] = ir.registerIcon(iconsName[i]);
		}
	}

	public PartInfo[] getRenderParts(IBlockAccess w, Block b, int x, int y, int z, int meta) {
		boolean isOn = b.getLightValue() > 0;
		boolean hasBulb = false;
		if (w != null) {
			TileEntity te = w.getTileEntity(x, y, z);
			if (te instanceof LampSocketTileEntity) {
				if (((LampSocketTileEntity) te).hasLightBulb()) {
					hasBulb = true;
				}
			}
		}

		switch (meta) {
		case basementsconce:
			return new PartInfo[] { new PartInfo("basemendtsconce", icon[basementsconce]) };
		case chandelier:
			if (hasBulb) {
				if (isOn) {
					return new PartInfo[] { new PartInfo("candle", icon[5]), new PartInfo("chandelier", icon[chandelier]) };
				} else {
					return new PartInfo[] { new PartInfo("candle", icon[4]), new PartInfo("chandelier", icon[chandelier]) };
				}
			} else {
				return new PartInfo[] { new PartInfo("chandelier", icon[chandelier]) };
			}
		case fluorescentlightsocket:
			if (hasBulb) {
				if (isOn) {
					return new PartInfo[] { new PartInfo("fluorescentlightbulb", icon[7]), new PartInfo("fluorescentlightsocket", icon[fluorescentlightsocket]) };
				} else {
					return new PartInfo[] { new PartInfo("fluorescentlightbulb", icon[6]), new PartInfo("fluorescentlightsocket", icon[fluorescentlightsocket]) };
				}
			} else {
				return new PartInfo[] { new PartInfo("fluorescentlightsocket", icon[fluorescentlightsocket]) };
			}
		case sconce:
			return new PartInfo[] { new PartInfo("sconce", icon[sconce]) };
		}
		return null;
	}

	@Override
	public IIcon getIcon(IBlockAccess w, int x, int y, int z, int meta) {
		return icon[0];
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return icon[meta];
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
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < subblocksCount; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public int getRenderType() {
		return LampSocketRenderer.renderid;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case basementsconce:
			this.setBlockBounds(1 / 4f, 0f, 2/8f, 3 / 4f, 1/6f, 6/8f);
			break;
		case chandelier:
			this.setBlockBounds(3 / 8f, 0f, 3/8f, 5 / 8f, 1f, 5/8f);
			break;
		case fluorescentlightsocket:
			this.setBlockBounds(1 / 4f, 25 / 32f, 0f, 3 / 4f, 1f, 1f);
			break;
		case sconce:
			this.setBlockBounds(1 / 4f, 2/8f, 0f, 3 / 4f, 6/8f, 1/8f);
			break;
		default:
			this.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
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

}
