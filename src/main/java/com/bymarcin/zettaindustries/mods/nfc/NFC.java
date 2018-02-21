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
import com.bymarcin.zettaindustries.utils.RecipeUtils;
import com.mojang.authlib.GameProfile;
import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;


public class NFC implements IMod, IProxy {
    public static BlockNFCProgrammer blockNFCProgrammer;
    public static BlockNFCReader blockNFCReader;
    public static ItemCardNFC itemCardNFC;
    public static ItemPrivateCardNFC itemPrivateCardNFC;
    public static Item itemBlockNFCProgrammer, itemBlockNFCReader, smartCardTerminalItemBlock;

    public static int dataCardHardLimit = 1048576;
    public static int dataCardSoftLimit = 8192;
    public static int dataCardTimeout = 1;
    public static double dataCardAsymmetric = 10;
    public static double dataCardComplexByte = 0.1;

	public static SmartCardItem smartCardItem;
	public static SmartCardTerminalItem smartCardTerminalItem;
	public static SmartCardTerminalBlock smartCardTerminalBlock;
    public static File saveDirParent = null;

    @Override
    public void preInit() {
        blockNFCProgrammer = new BlockNFCProgrammer();
        blockNFCReader = new BlockNFCReader();
        itemCardNFC = new ItemCardNFC();
        itemPrivateCardNFC = new ItemPrivateCardNFC();
        itemBlockNFCReader = new ItemBlock(blockNFCReader).setRegistryName(blockNFCReader.getRegistryName());
        itemBlockNFCProgrammer = new ItemBlock(blockNFCProgrammer).setRegistryName(blockNFCProgrammer.getRegistryName());

        GameRegistry.registerTileEntity(TileEntityNFCReader.class, "nfcReader");
        GameRegistry.registerTileEntity(TileEntityNFCProgrammer.class, "nfcprogrammer");

        smartCardItem = new SmartCardItem();
        smartCardTerminalItem = new SmartCardTerminalItem();
        smartCardTerminalBlock = new SmartCardTerminalBlock();
        smartCardTerminalItemBlock = new ItemBlock(smartCardTerminalBlock).setRegistryName(smartCardTerminalBlock.getRegistryName());

        GameRegistry.registerTileEntity(SmartCardTerminalTileEntity.class, "SmartCardTerminalTileEntity");

        ZIRegistry.registerProxy(this);
        MinecraftForge.EVENT_BUS.register(new DyeableItemWashHandler());
    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(blockNFCProgrammer, blockNFCReader, smartCardTerminalBlock);
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(itemBlockNFCProgrammer, itemBlockNFCReader, itemCardNFC, itemPrivateCardNFC, smartCardItem, smartCardTerminalItem, smartCardTerminalItemBlock);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRegisterBlockColor(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler(new SmartCardItemColor(), smartCardItem);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRegisterModels(ModelRegistryEvent event) {
        ZettaIndustries.proxy.registermodel(itemBlockNFCProgrammer, 0);
        ZettaIndustries.proxy.registermodel(itemBlockNFCReader, 0);
        ZettaIndustries.proxy.registermodel(itemCardNFC, 0);
        ZettaIndustries.proxy.registermodel(smartCardItem, 0);
        ZettaIndustries.proxy.registermodel(smartCardTerminalItem, 0);
        ZettaIndustries.proxy.registermodel(smartCardTerminalItemBlock, 0);

        ZettaIndustries.proxy.registermodel(itemPrivateCardNFC, 0);
        ZettaIndustries.proxy.registermodel(itemPrivateCardNFC, 1, new ModelResourceLocation(ZettaIndustries.MODID + ":itemprivatecardnfc_owner", "inventory"));
    }

    @SubscribeEvent
    public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        ItemStack microChip1 = Items.get("chip1").createItemStack(1);
        ItemStack microChip2 = Items.get("chip2").createItemStack(1);
        ItemStack microChip3 = Items.get("chip3").createItemStack(1);
        ItemStack cpu1 = Items.get("cpu1").createItemStack(1);
        ItemStack interweb = Items.get("interweb").createItemStack(1);
        ItemStack wifi = Items.get("wlancard").createItemStack(1);
        ItemStack circuitBoard = Items.get("printedcircuitboard").createItemStack(1);
        ItemStack paper = new ItemStack(net.minecraft.init.Items.PAPER, 1);
        ItemStack obsidian = new ItemStack(Blocks.OBSIDIAN, 1);
        ItemStack pressurePlate = new ItemStack(Blocks.STONE_PRESSURE_PLATE);
        ItemStack dataCard2 = Items.get("datacard2").createItemStack(1);

        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockNFCReader, 1),
                "i i", "cnw", "ibi", 'i', "ingotIron", 'c', microChip2, 'n', interweb, 'w', wifi, 'b', circuitBoard).setRegistryName(blockNFCReader.getRegistryName()));
        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockNFCProgrammer, 1),
                "i i", "cnw", "ibi", 'i', "ingotIron", 'c', cpu1, 'n', interweb, 'w', wifi, 'b', circuitBoard).setRegistryName(blockNFCProgrammer.getRegistryName()));
        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(itemCardNFC, 1),
                "ppp", "pcp", "ppp", 'p', paper, 'c', microChip1).setRegistryName(itemCardNFC.getRegistryName()));
        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(itemPrivateCardNFC, 1),
                "ppp", "pcp", "ppp", 'p', paper, 'c', microChip2).setRegistryName(itemPrivateCardNFC.getRegistryName()));

        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(smartCardItem,1),
                "ppp","pcp","ppp",'p',paper,'c',microChip3).setRegistryName(new ResourceLocation("zettaindustries:smartcard_microchip")));

        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(smartCardItem,1),
                "ppp","pcp","ppp",'p',paper,'c',smartCardItem).setRegistryName(new ResourceLocation("zettaindustries:smartcard_smartcard")));

        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(smartCardTerminalBlock,1),
                "ici","dp ","ibi",'p',pressurePlate,'c',microChip2,'i',"ingotIron",'d',dataCard2,'b',circuitBoard).setRegistryName(smartCardTerminalBlock.getRegistryName()));

        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(smartCardTerminalItem,1),
                "ici","dp ","ibi",'p',pressurePlate,'c',microChip2,'i',obsidian,'d',dataCard2,'b',circuitBoard).setRegistryName(smartCardTerminalItem.getRegistryName()));

        event.getRegistry().register(new SmartCardDyeRecipe(Ingredient.fromItem(smartCardItem)).setRegistryName("zettaindustries:smartcard_dye"));
    }

    public static UUID getUUIDForPlayer(String name) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(name);
            if (profile != null) {
                return profile.getId();
            }
        }
        return null;
    }

    @Override
    public void init() {
        Driver.add(smartCardItem);
        Driver.add(smartCardTerminalItem);

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

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        saveDirParent = new File(new File(DimensionManager.getCurrentSaveRootDirectory(), "zettaindustries"),"smartnfc");
        if(!saveDirParent.exists()){
            try {
                Files.createDirectories(saveDirParent.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
