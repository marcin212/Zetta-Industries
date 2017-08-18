package com.bymarcin.zettaindustries.mods.battery;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.InformationalItemBlock;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.battery.block.*;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockClientTickHandler;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockEventHandler;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockServerTickHandler;
import com.bymarcin.zettaindustries.mods.battery.fluid.AcidFluid;
import com.bymarcin.zettaindustries.mods.battery.gui.*;
import com.bymarcin.zettaindustries.mods.battery.tileentity.*;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.gui.IGUI;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;
import com.bymarcin.zettaindustries.utils.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashMap;

public class Battery implements IMod, IGUI, IProxy {

    public static BlockBigBatteryElectrode blockBigBatteryElectrode;
    public static BlockBigBatteryGlass blockBigBatteryGlass;
    public static BlockBigBatteryPowerTap blockBigBatteryPowerTap;
    public static BlockBigBatteryWall blockBigBatteryWall;
    public static BlockBigBatteryComputerPort blockBigBatteryComputerPort;
    public static BlockBigBatteryController blockBigBatteryControler;
    public static Block fluidBlock;
    public static Item fluidItem, itemBlockSulfur, itemBlockBigBatteryWall, itemBlockBigBatteryPowerTap, itemBlockBigBatteryControler, itemBlockBigBatteryComputerPort, itemBlockBigBatteryGlass, itemBlockBigBatteryElectrode, itemBlockGraphite;
    public static BlockGraphite blockGraphite;
    public double capacityMultiplier = 1;
    public static int electrodeTransferRate = 2500;
    public static BlockSulfur blockSulfur;
    public static AcidFluid acidFluid;
    static HashMap<Fluid, Integer> electrolyteList = new HashMap<Fluid, Integer>();

    public static Fluid acid = new Fluid("sulfurousacid", AcidFluid.stillIcon, AcidFluid.flowingIcon).setLuminosity(0).setDensity(1200).setViscosity(1500).setTemperature(320).setRarity(EnumRarity.UNCOMMON);
    /*Crafting items*/

    ItemStack obsidian;
    ItemStack sawDust;
    ItemStack redstone;
    ItemStack rfmeter;
    ItemStack enderFrame;
    ItemStack specialGlass;
    String electrum;
    ItemStack electrumFrame;
    String sulfur;
    ItemStack gunpowder;
    @Override
    public void preInit() {
        capacityMultiplier = ZettaIndustries.instance.config.get("BigBattery", "energyMultiplier", 1d).getDouble(1d);
        electrodeTransferRate = ZettaIndustries.instance.config.get("BigBattery", "electrodeTransferRate", 2500).getInt(2500);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());

        FluidRegistry.registerFluid(acid);
        acidFluid = new AcidFluid(acid);
        fluidBlock = acidFluid;
        fluidItem = new ItemBlock(fluidBlock).setRegistryName(acidFluid.getRegistryName());
        acid.setBlock(acidFluid);

        FluidRegistry.addBucketForFluid(acid);

        blockSulfur = new BlockSulfur(acidFluid);
        itemBlockSulfur = (new ItemBlock(blockSulfur).setRegistryName(blockSulfur.getRegistryName()));

        blockBigBatteryWall = (new BlockBigBatteryWall());
        itemBlockBigBatteryWall = (new InformationalItemBlock(blockBigBatteryWall).setRegistryName(blockBigBatteryWall.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityWall.class, "BatteryTileEntityWall");


        blockBigBatteryPowerTap = (new BlockBigBatteryPowerTap());
        itemBlockBigBatteryPowerTap = (new InformationalItemBlock(blockBigBatteryPowerTap).setRegistryName(blockBigBatteryPowerTap.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityPowerTap.class, "BatteryTileEntityPowerTap");

        blockBigBatteryGlass = (new BlockBigBatteryGlass());
        itemBlockBigBatteryGlass = (new InformationalItemBlock(blockBigBatteryGlass).setRegistryName(blockBigBatteryGlass.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityGlass.class, "BatteryTileEntityGlass");

        blockBigBatteryElectrode = (new BlockBigBatteryElectrode());
        itemBlockBigBatteryElectrode = (new InformationalItemBlock(blockBigBatteryElectrode).setRegistryName(blockBigBatteryElectrode.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityElectrode.class, "BatteryTileEntityElectrode");

        blockBigBatteryControler = (new BlockBigBatteryController());
        itemBlockBigBatteryControler = (new InformationalItemBlock(blockBigBatteryControler).setRegistryName(blockBigBatteryControler.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityControler.class, "BatteryTileEntityControler");

        blockBigBatteryComputerPort = (new BlockBigBatteryComputerPort());
        itemBlockBigBatteryComputerPort = (new InformationalItemBlock(blockBigBatteryComputerPort).setRegistryName(blockBigBatteryComputerPort.getRegistryName()));
        GameRegistry.registerTileEntity(TileEntityComputerPort.class, "BatteryTileEntityComputerPort");

        blockGraphite = (new BlockGraphite());
        itemBlockGraphite = (new InformationalItemBlock(blockGraphite).setRegistryName(blockGraphite.getRegistryName()));


//		if(Loader.isModLoaded("ComputerCraft")){
//			IntegrationComputerCraft.computercraftInit();
//		}
//
        ZIRegistry.registerPacket(1, EnergyUpdatePacket.class, Side.CLIENT);
        ZIRegistry.registerPacket(2, PowerTapUpdatePacket.class, Side.SERVER);
        ZIRegistry.registerPacket(3, PowerTapUpdatePacket.class, Side.CLIENT);
        ZIRegistry.registerGUI(this);
        ZIRegistry.registerProxy(this);


    }

    @Override
    public void init() {
        OreDictionary.registerOre("blockGraphite", blockGraphite);
        OreDictionary.registerOre("blockSulfur", blockSulfur);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(fluidBlock, blockBigBatteryWall, blockSulfur, blockGraphite, blockBigBatteryElectrode, blockBigBatteryComputerPort, blockBigBatteryControler,
                blockBigBatteryGlass, blockBigBatteryPowerTap);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(fluidItem, itemBlockBigBatteryComputerPort, itemBlockBigBatteryControler, itemBlockBigBatteryElectrode, itemBlockBigBatteryGlass,
                itemBlockBigBatteryPowerTap, itemBlockBigBatteryWall, itemBlockGraphite, itemBlockSulfur);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ZettaIndustries.proxy.registermodel(itemBlockSulfur, 0);
        ZettaIndustries.proxy.registermodel(itemBlockBigBatteryWall, 0);
        ZettaIndustries.proxy.registermodel(itemBlockBigBatteryPowerTap, 0);
        ZettaIndustries.proxy.registermodel(itemBlockBigBatteryGlass, 0);
        ZettaIndustries.proxy.registermodel(itemBlockBigBatteryElectrode, 0);
        ZettaIndustries.proxy.registermodel(itemBlockBigBatteryControler, 0);
        ZettaIndustries.proxy.registermodel(itemBlockBigBatteryComputerPort, 0);
        ZettaIndustries.proxy.registermodel(itemBlockGraphite, 0);
        registerFluidModel(acidFluid);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        obsidian = new ItemStack(Blocks.OBSIDIAN, 1);
        gunpowder = new ItemStack(Items.GUNPOWDER, 1);

		electrum = "ingotElectrum";
		sawDust = GameRegistry.makeItemStack("ThermalExpansion:dustWoodCompressed", 0,1, "");
		specialGlass = GameRegistry.makeItemStack("ThermalExpansion:frameIlluminator", 0,1, "");

		rfmeter =GameRegistry.makeItemStack("ThermalExpansion:multimeter",0,1,"");
		enderFrame =GameRegistry.makeItemStack("ThermalExpansion:frameTesseractEmpty",0,1,"");
		electrumFrame =GameRegistry.makeItemStack("ThermalExpansion:frameCellReinforcedEmpty",0,1,"");

		sulfur = "dustSulfur";

		if(!electrum.isEmpty() && !sawDust.isEmpty() && !specialGlass.isEmpty() && !rfmeter.isEmpty() &&
		    !enderFrame.isEmpty() && !electrumFrame.isEmpty()){

			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryWall,16), "ODE","OFE","ODE",
					'O',obsidian, 'D', sawDust, 'E', electrum, 'F', enderFrame).setRegistryName(blockBigBatteryWall.getRegistryName()));

			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryControler), "ODE","MRE","ODE",
					'O', obsidian, 'D', sawDust, 'E', electrum, 'M', rfmeter, 'R', electrumFrame).setRegistryName(blockBigBatteryControler.getRegistryName()));

			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryElectrode,2), "WGW","WGW","WWW",
					'W', "blockGraphite", 'G', "blockGold").setRegistryName(blockBigBatteryElectrode.getRegistryName()));

			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryGlass,4), "GGG","GFG","GGG",
					'G',specialGlass,'F', enderFrame).setRegistryName(blockBigBatteryGlass.getRegistryName()));

			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryPowerTap), "ORO","DFD","EEE",
					'O', obsidian, 'D', sawDust, 'R', "dustRedstone", 'E', electrum, 'F', electrumFrame).setRegistryName(blockBigBatteryPowerTap.getRegistryName()));

			event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryComputerPort),"ODE","RMF","ODE",
					'O', obsidian, 'D', sawDust, 'E', electrum, 'M', rfmeter, 'R', "dustRedstone", 'F', enderFrame).setRegistryName(blockBigBatteryComputerPort.getRegistryName()));

		}else{
            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryWall, 4), "OGB", "ODB", "OGB",
                    'O', Blocks.OBSIDIAN, 'D', Items.DIAMOND, 'G', Blocks.GOLD_BLOCK, 'B', Items.BLAZE_POWDER).setRegistryName(blockBigBatteryWall.getRegistryName()));

            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryControler), "CLR", "XTX", "BBB", 'B', Items.BLAZE_POWDER,
                    'C', Items.COMPARATOR, 'L', Blocks.REDSTONE_LAMP, 'R', Items.REPEATER, 'X', blockBigBatteryWall, 'T', Blocks.REDSTONE_BLOCK).setRegistryName(blockBigBatteryControler.getRegistryName()));

            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryElectrode, 2), "WGW", "WGW", "WWW",
                    'W', "blockGraphite", 'G', "blockGold").setRegistryName(blockBigBatteryElectrode.getRegistryName()));

            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryGlass, 1), "GGG", "GFG", "GGG",
                    'G', Blocks.GLASS, 'F', blockBigBatteryWall).setRegistryName(blockBigBatteryGlass.getRegistryName()));

            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryPowerTap), "OCO", "RHR", "BGB", 'G', blockGraphite,
                    'O', Blocks.OBSIDIAN, 'C', Items.COMPARATOR, 'R', Blocks.REDSTONE_BLOCK, 'H', blockBigBatteryWall, 'B', Items.BLAZE_POWDER)
                   .setRegistryName(blockBigBatteryPowerTap.getRegistryName()));

            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockBigBatteryComputerPort), "RCR", "OHO", "BRB",
                    'R', Blocks.REDSTONE_BLOCK, 'C', Items.COMPARATOR, 'O', Blocks.OBSIDIAN, 'H', blockBigBatteryWall, 'B', Items.BLAZE_ROD)
                    .setRegistryName(blockBigBatteryComputerPort.getRegistryName()));

            event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockSulfur, 1), "SGS", "SSS", "SSS",
                    'S', Items.BLAZE_POWDER, 'G', Items.GUNPOWDER).setRegistryName(new ResourceLocation("zettaindustries:sulfur_blaze")));
		}

        event.getRegistry().register(RecipeUtils.createShapedRecipe(new ItemStack(blockSulfur, 1), "SGS", "SSS", "SSS",
                'S', "dustSulfur", 'G', Items.GUNPOWDER).setRegistryName(blockSulfur.getRegistryName()));
    }

    @Override
    public void postInit() {
//
//		registerElectrolyte("redstone", (int)Math.floor(75000000*capacityMultiplier));
//		registerElectrolyte("ender", (int)Math.floor(100000000*capacityMultiplier));
        registerElectrolyte("sulfurousacid", (int) Math.floor(150000000 * capacityMultiplier));
//
        OreDictionary.getOres("blockCharcoal").forEach( cb -> GameRegistry.addSmelting(cb, new ItemStack(blockGraphite), 0F));
    }

    private void registerElectrolyte(String name, int valuePerBlock) {
        Fluid temp = FluidRegistry.getFluid(name);
        if (temp != null) {
            electrolyteList.put(temp, valuePerBlock);
        } else {
            ZettaIndustries.logger.warn("Try add fluid" + name + "as electrolyte, but fluid not found!");
        }
    }

    @Override
    public Object getServerGuiElement(int id, TileEntity blockEntity,
                                      EntityPlayer player, World world, int x, int y, int z) {
        if (blockEntity instanceof TileEntityControler) {
            return new BigBatteryContainer((TileEntityControler) blockEntity, player);
        }

        if (blockEntity instanceof TileEntityPowerTap) {
            return ((TileEntityPowerTap) blockEntity).getContainer(player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, TileEntity blockEntity,
                                      EntityPlayer player, World world, int x, int y, int z) {
        if (blockEntity instanceof TileEntityControler) {
            return new GuiControler((BatteryController) ((TileEntityControler) blockEntity).getMultiblockController(),
                    ((BatteryController) ((TileEntityControler) blockEntity).getMultiblockController()).getContainer(player));
        }

        if (blockEntity instanceof TileEntityPowerTap) {
            return new GuiPowerTap(((TileEntityPowerTap) blockEntity).getContainer(player), (TileEntityPowerTap) blockEntity);
        }

        return null;
    }

    public static HashMap<Fluid, Integer> getElectrolyteList() {
        return electrolyteList;
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void textureHook(TextureStitchEvent.Post event) {
//        if (event.map.getTextureType() == 0)
//        {
//            acid.setIcons(acidFluid.getBlockTextureFromSide(1), acidFluid.getBlockTextureFromSide(2));
//        }
    }

    @Override
    public void clientSide() {
        MinecraftForge.EVENT_BUS.register(new MultiblockClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new MultiblockServerTickHandler());
    }

    @Override
    public void serverSide() {
        FMLCommonHandler.instance().bus().register(new MultiblockServerTickHandler());
    }

    @SideOnly(Side.CLIENT)
    private void registerFluidModel(IFluidBlock fluidBlock) {
        final Item item = Item.getItemFromBlock((Block) fluidBlock);
        assert item != null;

        ModelBakery.registerItemVariants(item);
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(ZettaIndustries.MODID + ":sulfurousacid");

        ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

        ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return modelResourceLocation;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    interface MeshDefinitionFix extends ItemMeshDefinition {
        ModelResourceLocation getLocation(ItemStack stack);

        // Helper method to easily create lambda instances of this class
        static ItemMeshDefinition create(MeshDefinitionFix lambda) {
            return lambda;
        }

        default ModelResourceLocation getModelLocation(ItemStack stack) {
            return getLocation(stack);
        }
    }
}
