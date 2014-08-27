package com.bymarcin.zettaindustries;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bymarcin.zettaindustries.modmanager.ModManager;
import com.bymarcin.zettaindustries.registry.Proxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ZettaIndustries.MODID, version = ZettaIndustries.VERSION)
public class ZettaIndustries
{
    public static final String MODID = "zettaindustries";
    public static final String VERSION = "0.1";
    public static final String MODNAME = "Zetta Industries";
    
    /* Creative tab for Zetta Industries */
    public CreativeTabs tabZettaIndustries;
	
    public static Item itemLogo;

    
    /*ZettaIndustries config*/
    public Configuration config;
    
    /*ZettaIndustries logger*/
    public static Logger logger = LogManager.getLogger(ZettaIndustries.MODID);
    
    @SidedProxy(clientSide="com.bymarcin.zettaindustries.registry.ClientProxy", serverSide="com.bymarcin.zettaindustries.registry.Proxy")
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
    	modManager.preInit();
    	logger.info("End preInit!");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	tabZettaIndustries = new ZettaIndustriesCreativeTab();
    	modManager.init(); 
    	proxy.init();
    	config.save();

    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	modManager.postInit();
    }
}
