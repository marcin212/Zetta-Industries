package com.bymarcin.zettaindustries;

import java.util.Random;

import com.bymarcin.zettaindustries.basic.FakeItemIcon;
import com.bymarcin.zettaindustries.modmanager.ModManager;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.proxy.Proxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ZettaIndustries.MODID, version = ZettaIndustries.VERSION, dependencies = "after:ImmersiveEngineering;after:forestry;")
public class ZettaIndustries
{
    public static final String MODID = "zettaindustries";
    public static final String VERSION = "1.1";
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
		GameRegistry.register(itemLogo);
		
    	modManager = new ModManager();
    	ZIRegistry.preInitialize();
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
