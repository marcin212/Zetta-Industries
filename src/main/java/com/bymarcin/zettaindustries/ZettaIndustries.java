package com.bymarcin.zettaindustries;

import java.util.Random;

import com.bymarcin.zettaindustries.modmanager.ModManager;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.Proxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ZettaIndustries.MODID, version = ZettaIndustries.VERSION, dependencies = "after:ImmersiveEngineering")
public class ZettaIndustries
{
    public static final String MODID = "zettaindustries";
    public static final String VERSION = "1.1";
    public static final String MODNAME = "Zetta Industries";
    
    public static final Random RANDOM = new Random();
    
    /* Creative tab for Zetta Industries */
    public CreativeTabs tabZettaIndustries;
	
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
    	modManager = new ModManager();
    	ZIRegistry.preInitialize();
    	tabZettaIndustries = new ZettaIndustriesCreativeTab();
    	modManager.preInit();
    	logger.info("End preInit!");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	modManager.init(); 
    	proxy.init();
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
