package com.bymarcin.zettaindustries.mods.nfc.block;


import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCProgrammer;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockNFCProgrammer extends BasicBlockContainer{
	IIcon iconTop;
	IIcon iconSidesOn;
	IIcon iconSidesOff;
	
	public BlockNFCProgrammer() {
		super(Material.iron,"nfcprogrammer");
	}
	
	@Override
	public boolean onBlockActivated(World world, int par2, int par3,
			int par4, EntityPlayer player, int par6, float par7,float par8, float par9) {
		TileEntity tile = world.getTileEntity(par2, par3, par4);
		if(tile instanceof TileEntityNFCProgrammer && ((TileEntityNFCProgrammer)tile).NFCData!=null){
			TileEntityNFCProgrammer tilenfc = (TileEntityNFCProgrammer)tile;
			if(player.getHeldItem()!=null){
				if(player.getHeldItem().getItem() instanceof ItemPrivateCardNFC){
					if(ItemPrivateCardNFC.getOwner(player.getHeldItem())!=null && player.getCommandSenderName().equals(ItemPrivateCardNFC.getOwner(player.getHeldItem()))){
						ItemPrivateCardNFC.setNFCData(tilenfc.writeCardNFC(),player.getHeldItem());
						
						player.addChatMessage(new ChatComponentText(LanguageRegistry.instance().getStringLocalization(ZettaIndustries.MODID + ".text.nfc.message1")));
						world.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
						return true;
					}else{
						player.addChatMessage(new ChatComponentText(LanguageRegistry.instance().getStringLocalization(ZettaIndustries.MODID + ".text.nfc.message2")));
						return false;
					}
				}else if(player.getHeldItem().getItem() instanceof ItemCardNFC){
					ItemCardNFC.setNFCData(tilenfc.writeCardNFC(),player.getHeldItem());
					player.addChatMessage(new ChatComponentText(LanguageRegistry.instance().getStringLocalization(ZettaIndustries.MODID + ".text.nfc.message1")));
					world.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
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
		if(par2==0){
			return iconSidesOff;
		}else{
			return iconSidesOn;
		}
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		iconTop = iconRegister.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_block_top");
		iconSidesOn = iconRegister.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_programmer_on");
		iconSidesOff = iconRegister.registerIcon(ZettaIndustries.MODID + ":" + "nfc/nfc_programmer_off");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityNFCProgrammer();
	}
}
