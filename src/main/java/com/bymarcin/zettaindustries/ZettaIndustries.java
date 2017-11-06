package com.bymarcin.zettaindustries;

import java.util.Random;

import com.bymarcin.zettaindustries.basic.EntityShurikenMarcina;
import com.bymarcin.zettaindustries.basic.FakeItemIcon;
import com.bymarcin.zettaindustries.basic.RenderShurikenMarcina;
import com.bymarcin.zettaindustries.modmanager.ModManager;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.Proxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ZettaIndustries.MODID, version = ZettaIndustries.VERSION, dependencies = "required-after:forge@[14.23.0.2500,);after:immersiveengineering;after:forestry;")
public class ZettaIndustries
{
    public static final String MODID = "zettaindustries";
    public static final String VERSION = "1.3";
    public static final String MODNAME = "Zetta Industries";
    
    public static final Random RANDOM = new Random();
    
    /* Creative tab for Zetta Industries */
    public static final CreativeTabs tabZettaIndustries = new ZettaIndustriesCreativeTab();
    public static Item itemLogo;

    
    /*ZettaIndustries config*/
    public Configuration config;
    
    /*ZettaIndustries logger*/
    public static Logger logger = LogManager.getLogger(ZettaIndustries.MODID);
    
    @SidedProxy(clientSide="com.bymarcin.zettaindustries.registry.proxy.ClientProxy", serverSide="com.bymarcin.zettaindustries.registry.proxy.Proxy")
    public static Proxy proxy;

    @Instance(value = ZettaIndustries.MODID)
    public static ZettaIndustries instance;
    
    private ModManager modManager;
    
    
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger.info("Start preInit!");
    	config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();	

    	itemLogo = new FakeItemIcon();
		
    	modManager = new ModManager();
    	ZIRegistry.preInitialize();
    	modManager.preInit();

        MinecraftForge.EVENT_BUS.register(this);
        proxy.init();
    	logger.info("End preInit!");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(itemLogo);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ZettaIndustries.proxy.registermodel(itemLogo, 0);
        RenderingRegistry.registerEntityRenderingHandler(EntityShurikenMarcina.class, RenderShurikenMarcina::new);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ResourceLocation nameLoc = new ResourceLocation("zettaindustries:marcinshuriken");
        EntityRegistry.registerModEntity(nameLoc, EntityShurikenMarcina.class, nameLoc.toString(), 1, ZettaIndustries.instance,
                64, 1, true);

    	modManager.init();

    	ZIRegistry.initialize();
    	config.save();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	modManager.postInit();
    }
    
    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.loadComplete();
    }

}
