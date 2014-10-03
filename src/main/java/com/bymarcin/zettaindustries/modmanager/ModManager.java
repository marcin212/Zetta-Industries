package com.bymarcin.zettaindustries.modmanager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bymarcin.zettaindustries.ZettaIndustries;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModManager {
	HashSet<ModDescription> mods = new HashSet<ModManager.ModDescription>();
	private static final String modsClassPath = "com.bymarcin.zettaindustries.mods.";

	private void addMods() {
		addMod("quarryfixer.QuarryFixer", "$('BuildCraft|Energy')", "QuarryFixer");
//		addMod("energysiphon.EnergySiphonMod", "$('ThermalExpansion')", "EnergySiphonMod");
//		addMod("scanner.ScannerMod", "$('OpenComponents')", "ScannerMod");
		addMod("nfc.NFC", "$('OpenComponents')", "NFC");
	//	addMod("rfpowermeter.RFMeter", "$('OpenComponents') && $('ThermalExpansion')", "RFPowerMeter");
		addMod("rfpowermeter.RFMeter", "", "RFPowerMeter");
//		addMod("additionalconverters.ModAdditionalConverters", "$('OpenComponents')", "AdditionalConverters");
		addMod("vanillautils.VanillaUtils", "", "VanillaUtils");
//		addMod("battery.Battery", "", "BigBattery");
		addMod("battery.Battery", "$('ThermalExpansion')", "BigBattery");
		addMod("fframes.Frames", "$('Forestry')", "ForestryFrames");
//		addMod("additionalsounds.SoundsMod", "", "SoundsMod");
//		addMod("superconductor.SuperConductorMod", "", "SuperConductor");
		addMod("superconductor.SuperConductorMod", "$('BigReactors') && $('ThermalExpansion')", "SuperConductor");
	}

	private void addMod(String path, String dependencies, String name) {
		mods.add(new ModDescription(modsClassPath + path, dependencies, ZettaIndustries.instance.config.get("Mods", name, true).getBoolean(true)));
	}

	public void preInit() {
		HashSet<String> mods = new HashSet<String>();
		for (ModContainer mod : Loader.instance().getModList()) {
			mods.add(mod.getModId());
		}
		binding.put("mods", mods);
		addMods();
	}

	public void init() {
		for (ModDescription mod : mods) {
			if (mod.toLoad) {
				loadMod(mod);
			}
			if (mod.isLoaded) {
				mod.mod.init();
				ZettaIndustries.logger.info("Modification has been loaded [" + mod.classPath + "]");
			}
		}
	}

	public void postInit() {
		for (ModDescription mod : mods) {
			if (mod.isLoaded)
				mod.mod.postInit();
		}
	}

	private void loadMod(ModDescription mod) {
		try {
			Class<?> modClass = Class.forName(mod.classPath);
			Constructor<?> c = modClass.getConstructor();
			mod.mod = (IMod) c.newInstance();
			mod.isLoaded = mod.mod != null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("js");
	private Bindings binding = engine.createBindings();

	private class ModDescription {
		Boolean toLoad = false;
		Boolean isLoaded = false;
		IMod mod = null;
		String classPath;

		public ModDescription(String classPath, String dependencies, Boolean toLoad) {
			this.classPath = classPath;
			this.toLoad = toLoad && eval(dependencies);
		}

		private boolean eval(String dependencies) {
			String script = "function $(modId){"
					+ "return mods.contains(modId)}; true; ";
			try {
				return (Boolean) engine.eval(script + dependencies, binding);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}