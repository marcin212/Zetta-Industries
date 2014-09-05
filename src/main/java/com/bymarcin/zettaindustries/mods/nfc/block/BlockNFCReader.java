package com.bymarcin.zettaindustries.mods.nfc.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCReader;

public class BlockNFCReader extends BasicBlockContainer{
	IIcon iconTop;
	IIcon iconSides;
	
	public BlockNFCReader() {
		super(Material.iron, "nfcreader");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityNFCReader();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, 
									EntityPlayer player, int l, float px, float py, float pz) {
		TileEntity tile =  world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityNFCReader){
			if(player.getHeldItem()!=null){
				if(player.getHeldItem().getItem() instanceof ItemPrivateCardNFC){
					if(player.getDisplayName().equals(ItemPrivateCardNFC.getOwner(player.getHeldItem()))){
						((TileEntityNFCReader)tile).sendEvent(player.getDisplayName(),ItemPrivateCardNFC.getNFCData(player.getHeldItem()));
						return true;
					}
					return false;
				}else if(player.getHeldItem().getItem() instanceof ItemCardNFC){
					((TileEntityNFCReader)tile).sendEvent(player.getDisplayName(),ItemCardNFC.getNFCData(player.getHeldItem()));
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		switch(par1){
		case 0:
		case 1:
			return iconTop;
		}
		return iconSides;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		iconTop = iconRegister.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_block_top");
		iconSides = iconRegister.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_reader_sides");
	}
}
