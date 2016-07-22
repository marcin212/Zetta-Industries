package com.bymarcin.zettaindustries.mods.superconductor.render;


import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public interface Glowing  {
	IIcon getGlowIcon(IBlockAccess blockAccess, int x, int y, int z, int side);
}
