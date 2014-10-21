package com.bymarcin.zettaindustries.mods.superconductor;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.superconductor.block.BlockControler;
import com.bymarcin.zettaindustries.mods.superconductor.block.BlockWire;
import com.bymarcin.zettaindustries.mods.superconductor.gui.ContainerControler;
import com.bymarcin.zettaindustries.mods.superconductor.gui.GuiControler;
import com.bymarcin.zettaindustries.mods.superconductor.gui.PacketUpdateFluidAmount;
import com.bymarcin.zettaindustries.mods.superconductor.render.GlowingRender;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityWire;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.gui.IGUI;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class SuperConductorMod implements IMod, IGUI, IProxy{
	public static Fluid coolantFluid = null;
	BlockWire blockWire;
	BlockControler blockControler;
	public static int glowRenderID;
	public static int pass;
	
	@Override
	public void init() {
		blockWire = new BlockWire();
		blockControler = new BlockControler();
		
		GameRegistry.registerBlock(blockWire,"blockwire");
		GameRegistry.registerBlock(blockControler, "blockcontroler");
		
		GameRegistry.registerTileEntity(TileEntityControler.class, "blockcontroler");
		GameRegistry.registerTileEntity(TileEntityWire.class, "blockwire");
		
		ZIRegistry.registerGUI(this);
		ZIRegistry.registerPacket(5, PacketUpdateFluidAmount.class, Side.CLIENT);
		ZIRegistry.registerProxy(this);
	}

	@Override
	public void postInit() {
		ItemStack frameBasic =GameRegistry.findItemStack("ThermalExpansion","Frame",1);
		ItemStack frameHardened =GameRegistry.findItemStack("ThermalExpansion","Frame",1);
		ItemStack tank =GameRegistry.findItemStack("ThermalExpansion","Tank",1);
		ItemStack cell =GameRegistry.findItemStack("ThermalExpansion","Cell",1);
		
		if(frameBasic!=null && frameHardened!=null && tank!=null && cell!=null){
			frameHardened.setItemDamage(1);
			tank.setItemDamage(1);
			cell.setItemDamage(4);
			ItemStack wire = new ItemStack(blockWire,4);
			GameRegistry.addRecipe(new ShapedOreRecipe(wire, "FTF", "TCT", "FTF", 'F', frameBasic, 'T', tank, 'C', cell));
			GameRegistry.addRecipe(new ShapedOreRecipe(blockControler, "FTF", "TCT", "FTF", 'F', frameHardened, 'T', tank, 'C', cell));
			
		}
		
		coolantFluid = FluidRegistry.getFluid("cryotheum");	
	}

	@Override
	public Object getServerGuiElement(int id, TileEntity blockEntity,
			EntityPlayer player, World world, int x, int y, int z) {
			if(blockEntity instanceof TileEntityControler){
				return new ContainerControler(player,(TileEntityControler) blockEntity);
			}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, TileEntity blockEntity,
			EntityPlayer player, World world, int x, int y, int z) {
		if(blockEntity instanceof TileEntityControler){
			return new GuiControler(player,(TileEntityControler) blockEntity);
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void clientSide() {
		glowRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GlowingRender());
	}

	@Override
	@SideOnly(Side.SERVER)
	public void serverSide() {

	}

}
