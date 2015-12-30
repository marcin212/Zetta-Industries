package com.bymarcin.zettaindustries.mods.mgc.block;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.mgc.item.LampSocketItem;
import com.bymarcin.zettaindustries.mods.mgc.render.LampSocketRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;
import com.bymarcin.zettaindustries.utils.LocalSides;
import com.bymarcin.zettaindustries.utils.render.RenderUtils;
import com.bymarcin.zettaindustries.utils.render.RenderUtils.PartInfo;
import com.sun.javafx.geom.Vec3f;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

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

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.util.ForgeDirection;

public class LampSocketBlock extends BlockContainer {
	IIcon[] icon;
	String iconPrefix = ZettaIndustries.MODID + ":mgc/models/";
	String[] iconsName = new String[] {
			iconPrefix + "basementsconce/basementsconce",
			iconPrefix + "chandelier/chandelier",
			iconPrefix + "fluorescentlightsocket/fluorescentlightsocket",
			iconPrefix + "sconce/sconce",
			iconPrefix + "lantern/lantern",

			iconPrefix + "chandelier/candle",
			iconPrefix + "chandelier/candle_on",

			iconPrefix + "fluorescentlightsocket/fluorescentlightbulb",
			iconPrefix + "fluorescentlightsocket/fluorescentlightbulb_on",
	};

	public static final int subblocksCount = 5;
	public static final int basementsconce = 0;
	public static final int chandelier = 1;
	public static final int fluorescentlightsocket = 2;
	public static final int sconce = 3;
	public static final int lantern = 4;

	public static Vector3f[][][][] blockBounds;

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	public LampSocketBlock() {
		super(Material.iron);
		setCreativeTab(ZettaIndustries.instance.tabZettaIndustries);
		setBlockName("LampSocket");
		GameRegistry.registerBlock(this, LampSocketItem.class, "LampSocket");
		GameRegistry.registerTileEntity(LampSocketTileEntity.class, "LampSocketTileEntity");
		setBlockTextureName(ZettaIndustries.MODID + ":mgc/LampSocket");

		blockBounds = new Vector3f[subblocksCount][ForgeDirection.VALID_DIRECTIONS.length][LocalSides.values().length][2];
		for (int metadata = 0; metadata < subblocksCount; metadata++) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				for (LocalSides lside : LocalSides.values()) {
					Matrix4f rm = RenderUtils.rotationMatrix[dir.ordinal()][lside.ordinal()];

					Vector4f vecmin;
					Vector4f vecmax;
					switch (metadata) {
					case basementsconce:
						vecmin = Matrix4f.transform(rm, new Vector4f(1 / 4f, 7 / 10f, 1 / 10f, 1), null);
						vecmax = Matrix4f.transform(rm, new Vector4f(3 / 4f, 1f, 9 / 10f, 1), null);
						break;
					case chandelier:
						vecmin = Matrix4f.transform(rm, new Vector4f(3 / 8f, 0f, 3 / 8f, 1), null);
						vecmax = Matrix4f.transform(rm, new Vector4f(5 / 8f, 1f, 5 / 8f, 1), null);
						break;
					case fluorescentlightsocket:
						vecmin = Matrix4f.transform(rm, new Vector4f(1 / 4f, 25 / 32f, 0f, 1), null);
						vecmax = Matrix4f.transform(rm, new Vector4f(3 / 4f, 1f, 1f, 1), null);
						break;
					case sconce:
						vecmin = Matrix4f.transform(rm, new Vector4f(1 / 8f, 6 / 8f, 1 / 8f, 1), null);
						vecmax = Matrix4f.transform(rm, new Vector4f(7 / 8f, 1f, 7 / 8f, 1), null);
						break;
					case lantern:
						vecmin = Matrix4f.transform(rm, new Vector4f(1 / 4f, 0f, 4 / 16f, 1), null);
						vecmax = Matrix4f.transform(rm, new Vector4f(3 / 4f, 1f, 11 / 16f, 1), null);
						break;
					default:
						vecmin = Matrix4f.transform(rm, new Vector4f(0, 0, 0, 1), null);
						vecmax = Matrix4f.transform(rm, new Vector4f(1, 1, 1, 1), null);
					}
					blockBounds[metadata][dir.ordinal()][lside.ordinal()][0] = new Vector3f(Math.min(vecmin.x, vecmax.x), Math.min(vecmin.y, vecmax.y), Math.min(vecmin.z, vecmax.z));
					blockBounds[metadata][dir.ordinal()][lside.ordinal()][1] = new Vector3f(Math.max(vecmin.x, vecmax.x), Math.max(vecmin.y, vecmax.y), Math.max(vecmin.z, vecmax.z));

				}
			}
		}

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
					return new PartInfo[] { new PartInfo("candle", icon[6]), new PartInfo("chandelier", icon[chandelier]) };
				} else {
					return new PartInfo[] { new PartInfo("candle", icon[5]), new PartInfo("chandelier", icon[chandelier]) };
				}
			} else {
				return new PartInfo[] { new PartInfo("chandelier", icon[chandelier]) };
			}
		case fluorescentlightsocket:
			if (hasBulb) {
				if (isOn) {
					return new PartInfo[] { new PartInfo("fluorescentlightbulb", icon[8]), new PartInfo("fluorescentlightsocket", icon[fluorescentlightsocket]) };
				} else {
					return new PartInfo[] { new PartInfo("fluorescentlightbulb", icon[7]), new PartInfo("fluorescentlightsocket", icon[fluorescentlightsocket]) };
				}
			} else {
				return new PartInfo[] { new PartInfo("fluorescentlightsocket", icon[fluorescentlightsocket]) };
			}
		case sconce:
			return new PartInfo[] { new PartInfo("sconce", icon[sconce]) };
		case lantern:
			return new PartInfo[] { new PartInfo("Lantern_lantern", icon[lantern]) };
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
		} else if (player.getHeldItem() != null && tileEntity instanceof LampSocketTileEntity) {
			String find = GameData.getItemRegistry().getNameForObject(player.getHeldItem().getItem());
			if (find != null) {
				String[] find_parts = find.split(":");
				if (find_parts.length > 1 && find_parts[1].toLowerCase().contains("wrench")) {
					((LampSocketTileEntity) tileEntity).rotate();
					return true;
				}
			}

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
		TileEntity te = world.getTileEntity(x, y, z);
		LocalSides sides = LocalSides.NORTH;
		int dir = 0;

		if (te instanceof LampSocketTileEntity) {
			dir = ((LampSocketTileEntity) te).getFacing();
			sides = ((LampSocketTileEntity) te).getIfacing();
		}
		Vector3f min = blockBounds[metadata][dir][sides.ordinal()][0];
		Vector3f max = blockBounds[metadata][dir][sides.ordinal()][1];

		this.setBlockBounds(min.x, min.y, min.z, max.x, max.y, max.z);
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
