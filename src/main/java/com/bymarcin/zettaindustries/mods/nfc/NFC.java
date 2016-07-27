package com.bymarcin.zettaindustries.mods.nfc;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.nfc.block.BlockNFCProgrammer;
import com.bymarcin.zettaindustries.mods.nfc.block.BlockNFCReader;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.smartcard.*;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCProgrammer;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCReader;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;
import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class NFC implements IMod, IProxy {
    public static BlockNFCProgrammer blockNFCProgrammer;
    public static BlockNFCReader blockNFCReader;
    public static ItemCardNFC itemCardNFC;
    public static ItemPrivateCardNFC itemPrivateCardNFC;


    public static int dataCardHardLimit = 1048576;
    public static int dataCardSoftLimit = 8192;
    public static int dataCardTimeout = 1;
    public static double dataCardAsymmetric = 10;
    public static double dataCardComplexByte = 0.1;

	public static SmartCardItem smartCardItem;
	public static SmartCardTerminalItem smartCardTerminalItem;
	public static SmartCardTerminalBlock smartCardTerminalBlock;

    @Override
    public void preInit() {

        blockNFCProgrammer = new BlockNFCProgrammer();
        blockNFCReader = new BlockNFCReader();
        itemCardNFC = new ItemCardNFC();
        itemPrivateCardNFC = new ItemPrivateCardNFC();

        GameRegistry.register(blockNFCReader);
        ZettaIndustries.proxy.registermodel(GameRegistry.register(new ItemBlock(blockNFCReader).setRegistryName(blockNFCReader.getRegistryName())), 0);

        GameRegistry.register(blockNFCProgrammer);
        ZettaIndustries.proxy.registermodel(GameRegistry.register(new ItemBlock(blockNFCProgrammer).setRegistryName(blockNFCProgrammer.getRegistryName())), 0);

        GameRegistry.registerTileEntity(TileEntityNFCReader.class, "nfcReader");
        GameRegistry.registerTileEntity(TileEntityNFCProgrammer.class, "nfcprogrammer");

        Item tempItem =  GameRegistry.register(itemPrivateCardNFC);

        ZettaIndustries.proxy.registermodel(tempItem, 0);
        ZettaIndustries.proxy.registermodel(tempItem, 1, new ModelResourceLocation(ZettaIndustries.MODID + ":itemprivatecardnfc_owner","inventory"));

        ZettaIndustries.proxy.registermodel(GameRegistry.register(itemCardNFC), 0);

		smartCardItem = new SmartCardItem();
		smartCardTerminalItem = new SmartCardTerminalItem();
		smartCardTerminalBlock = new SmartCardTerminalBlock();

        ZettaIndustries.proxy.registermodel(GameRegistry.register(smartCardTerminalItem),0);
        ZettaIndustries.proxy.registermodel(GameRegistry.register(smartCardItem), 0);

        Block temp = GameRegistry.register(smartCardTerminalBlock);
        ZettaIndustries.proxy.registermodel(GameRegistry.register(new ItemBlock(temp).setRegistryName(temp.getRegistryName())),0);
        GameRegistry.registerTileEntity(SmartCardTerminalTileEntity.class, "SmartCardTerminalTileEntity");





        ZIRegistry.registerProxy(this);
    }

    @Override
    public void init() {

        ItemStack microChip1 = Items.get("chip1").createItemStack(1);
        ItemStack microChip2 = Items.get("chip2").createItemStack(1);
        ItemStack microChip3 = Items.get("chip3").createItemStack(1);
        ItemStack cpu1 = Items.get("cpu1").createItemStack(1);
        ItemStack interweb = Items.get("interweb").createItemStack(1);
        ItemStack wifi = Items.get("wlanCard").createItemStack(1);
        ItemStack circuitBoard = Items.get("printedCircuitBoard").createItemStack(1);
        ItemStack paper = new ItemStack(net.minecraft.init.Items.PAPER, 1);
        ItemStack iron = new ItemStack(net.minecraft.init.Items.IRON_INGOT, 1);
        ItemStack obsidian = new ItemStack(Blocks.OBSIDIAN, 1);
        ItemStack pressurePlate = new ItemStack(Blocks.STONE_PRESSURE_PLATE);
        ItemStack dataCard2 = Items.get("dataCard2").createItemStack(1);

        Driver.add((li.cil.oc.api.driver.Item)smartCardItem);
        Driver.add((li.cil.oc.api.driver.Item)smartCardTerminalItem);
        Driver.add((li.cil.oc.api.driver.EnvironmentProvider)smartCardTerminalItem);

        GameRegistry.addRecipe(new ItemStack(blockNFCReader, 1),
                "i i", "cnw", "ibi", 'i', iron, 'c', microChip2, 'n', interweb, 'w', wifi, 'b', circuitBoard);
        GameRegistry.addRecipe(new ItemStack(blockNFCProgrammer, 1),
                "i i", "cnw", "ibi", 'i', iron, 'c', cpu1, 'n', interweb, 'w', wifi, 'b', circuitBoard);
        GameRegistry.addRecipe(new ItemStack(itemCardNFC, 1),
                "ppp", "pcp", "ppp", 'p', paper, 'c', microChip1);
        GameRegistry.addRecipe(new ItemStack(itemPrivateCardNFC, 1),
                "ppp", "pcp", "ppp", 'p', paper, 'c', microChip2);

        GameRegistry.addRecipe(new ItemStack(smartCardItem,1),
	        		"ppp","pcp","ppp",'p',paper,'c',microChip3);

        GameRegistry.addRecipe(new ItemStack(smartCardItem,1),
	        		"ppp","pcp","ppp",'p',paper,'c',smartCardItem);

        GameRegistry.addRecipe(new ItemStack(smartCardTerminalBlock,1),
	        		"ici","dp ","ibi",'p',pressurePlate,'c',microChip2,'i',iron,'d',dataCard2,'b',circuitBoard );

        GameRegistry.addRecipe(new ItemStack(smartCardTerminalItem,1),
	    		 "ici","dp ","ibi",'p',pressurePlate,'c',microChip2,'i',obsidian,'d',dataCard2,'b',circuitBoard );


//	     SmartCardTerminalExtension i = new SmartCardTerminalExtension();
//	     GameRegistry.registerItem(i, "SmartCardTerminalExtension");
//	     Driver.add((li.cil.oc.api.driver.Item)i);


    }

    @Override
    public void postInit() {


    }

    @SideOnly(Side.CLIENT)
    @Override
    public void clientSide() {
        ClientRegistry.bindTileEntitySpecialRenderer(SmartCardTerminalTileEntity.class, new SmartCardBlockTerminalRenderer());
        MinecraftForge.EVENT_BUS.register(new SmartCardRackRenderer());
    }

    @Override
    public void serverSide() {

    }
}
