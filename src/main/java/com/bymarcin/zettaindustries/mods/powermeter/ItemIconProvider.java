package com.bymarcin.zettaindustries.mods.powermeter;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import buildcraft.api.core.IIconProvider;

public class ItemIconProvider implements IIconProvider {
	public static final int PowerMeter = 0;

	public static final int MAX = 1;
	@SideOnly(Side.CLIENT)
	private IIcon[] _icons;

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int iconIndex) {
		return _icons[iconIndex];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		_icons = new IIcon[MAX];
		_icons[PowerMeter] = iconRegister.registerIcon(ZettaIndustries.MODID + ":powermeter");

	}
}