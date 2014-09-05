package com.bymarcin.zettaindustries.mods.nfc;

import li.cil.oc.api.Items;
import net.minecraft.item.ItemStack;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.nfc.block.BlockNFCProgrammer;
import com.bymarcin.zettaindustries.mods.nfc.block.BlockNFCReader;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCProgrammer;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCReader;

import cpw.mods.fml.common.registry.GameRegistry;


public class NFC implements IMod{
	public static BlockNFCProgrammer blockNFCProgrammer;
	public static BlockNFCReader blockNFCReader;
	public static ItemCardNFC itemCardNFC;
	public static ItemPrivateCardNFC itemPrivateCardNFC;
	
    ItemStack microChip1= Items.get("chip1").createItemStack(1);
    ItemStack microChip2= Items.get("chip2").createItemStack(1);
    ItemStack cpu1= Items.get("cpu1").createItemStack(1);
    ItemStack interweb= Items.get("interweb").createItemStack(1);
    ItemStack wifi= Items.get("wlanCard").createItemStack(1);
    ItemStack circuitBoard= Items.get("printedCircuitBoard").createItemStack(1);
    ItemStack paper= new ItemStack(net.minecraft.init.Items.paper,1);
    ItemStack iron= new ItemStack(net.minecraft.init.Items.iron_ingot,1);
    
	@Override
	public void init() {
		blockNFCProgrammer = new BlockNFCProgrammer();
		blockNFCReader = new BlockNFCReader();
		itemCardNFC = new ItemCardNFC();
		itemPrivateCardNFC = new ItemPrivateCardNFC();
		
        GameRegistry.registerBlock(blockNFCReader, "nfcReader");
        GameRegistry.addRecipe(new ItemStack(blockNFCReader,1),
        		"i i","cnw","ibi",'i',iron,'c',microChip2,'n',interweb,'w',wifi,'b',circuitBoard);

         GameRegistry.registerBlock(blockNFCProgrammer, "nfcprogrammer");
         GameRegistry.addRecipe(new ItemStack(blockNFCProgrammer,1),
         		"i i","cnw","ibi",'i',iron,'c',cpu1,'n',interweb,'w',wifi,'b',circuitBoard);
        
        GameRegistry.registerItem(itemCardNFC, "nfccard");
        GameRegistry.addRecipe(new ItemStack(itemCardNFC,1),
        		"ppp","pcp","ppp",'p',paper,'c',microChip1);

        GameRegistry.registerItem(itemPrivateCardNFC, "nfcprivatecard");
        GameRegistry.addRecipe(new ItemStack(itemPrivateCardNFC,1),
        		"ppp","pcp","ppp",'p',paper,'c',microChip2);
        
        GameRegistry.registerTileEntity(TileEntityNFCReader.class, "nfcReader");
        GameRegistry.registerTileEntity(TileEntityNFCProgrammer.class, "nfcprogrammer");
	}

	@Override
	public void postInit() {
		
	}
}
