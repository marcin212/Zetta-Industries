package com.bymarcin.zettaindustries.mods.simpledhd.block;

import java.util.ArrayList;
import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.basic.IBlockInfo;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.simpledhd.tileentity.TileEntitySimpleDHD;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockSimpleDHD extends BasicBlockContainer implements IBlockInfo{
	private static int renderId = RenderingRegistry.getNextAvailableRenderId();
	List<String> info = new ArrayList<String>();
	
	public BlockSimpleDHD() {
		super(Material.iron,"simpledhd");
		textureName = ZettaIndustries.MODID+":simpledhd/DHD";
		info.add("[WIP] Use at own risk!");
	}
	
	@Override
	public List<String> getInformation() {
		return info;
	}
	
	@Override
	public int getRenderType() {
		return renderId;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
		int whichDirectionFacing = MathHelper.floor_double((double) (par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return false;
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if(player.getHeldItem()!=null && player.getHeldItem().getItem() instanceof ItemCardNFC){
			String address;
			if(player.getHeldItem().getItem() instanceof ItemPrivateCardNFC){
				if(!player.getDisplayName().equals(ItemPrivateCardNFC.getOwner(player.getHeldItem()))){
					return false;
				}
			}
				String data = ItemCardNFC.getNFCData(player.getHeldItem());
				if(data!=null && data.startsWith("sg-address:")){
					String[] parts = data.split(":");
					if(parts.length>1){
						address = parts[1];
						call(address, world, x, y, z);
						return true;
					}
					return false;
				}else{
					return false;
				}
				
		}else{
			player.openGui(ZettaIndustries.instance, 10, world, x, y, z);
			return true;
		}
	}
	
	public static void call(String address, World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntitySimpleDHD){
			call(address, (TileEntitySimpleDHD)te);
		}else{
			ZettaIndustries.logger.error(String.format("Wrong tileentity at %d, %d, %d", x, y, x));
		}
	}
	
	public static void call(String address, TileEntitySimpleDHD tileEntity){
		tileEntity.setAddress(address);
		tileEntity.dial();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntitySimpleDHD();
	}

}
