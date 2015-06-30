package com.bymarcin.zettaindustries.mods.simpledhd;

import com.bymarcin.zettaindustries.basic.InformationalItemBlock;
import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.simpledhd.block.BlockSimpleDHD;
import com.bymarcin.zettaindustries.mods.simpledhd.gui.ContainerSimpleDHD;
import com.bymarcin.zettaindustries.mods.simpledhd.gui.GuiSimpleDHD;
import com.bymarcin.zettaindustries.mods.simpledhd.network.GuiActionPacket;
import com.bymarcin.zettaindustries.mods.simpledhd.render.BlockRenderSimpleDHD;
import com.bymarcin.zettaindustries.mods.simpledhd.tileentity.TileEntitySimpleDHD;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.gui.IGUI;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import lordfokas.stargatetech2.ModuleAutomation;
import lordfokas.stargatetech2.ModuleCore;
import lordfokas.stargatetech2.ModuleEnemy;
import lordfokas.stargatetech2.ModuleTransport;
import lordfokas.stargatetech2.ModuleWorld;
import lordfokas.stargatetech2.StargateTech2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class SimpleDHD implements IMod, IGUI, IProxy{
	public static final BlockSimpleDHD simpledhd = new BlockSimpleDHD();
	
	@Override
	public void init() {
		ZIRegistry.registerGUI(this);
		ZIRegistry.registerPacket(10, GuiActionPacket.class, Side.SERVER);
		ZIRegistry.registerProxy(this);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

	}

	@Override
	public void preInit() {
		GameRegistry.registerBlock(simpledhd, InformationalItemBlock.class, "SimpleDHD");
		GameRegistry.registerTileEntity(TileEntitySimpleDHD.class, "SimpleDHDTileEntity");
	}
	
	@Override
	public void postInit() {
		ItemStack circuit = new ItemStack(ModuleCore.naquadahItem,1,3);
		ItemStack plate = new ItemStack(ModuleCore.naquadahItem,1,2);
		ItemStack wall = new ItemStack(ModuleWorld.lanteanWall,1,7);
		ItemStack busCable = new ItemStack(ModuleAutomation.busCable);
		GameRegistry.addRecipe(new ShapedOreRecipe(simpledhd, "BOB", "CLC", "PWP", 
				'B', Blocks.stone_button,
				'O', new ItemStack(Items.dye,1,14),
				'C', circuit,
				'L', wall,
				'P', plate,
				'W', busCable));
		
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		RenderingRegistry.registerBlockHandler(new BlockRenderSimpleDHD());
		
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub
		
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void textureStich(TextureStitchEvent.Post event)
	{
			BlockRenderSimpleDHD.realoadModels();
			rebindUVsToIcon(BlockRenderSimpleDHD.model1, simpledhd.getIcon(0, 0));
			rebindUVsToIcon(BlockRenderSimpleDHD.model2, simpledhd.getIcon(0, 0));
			rebindUVsToIcon(BlockRenderSimpleDHD.model3, simpledhd.getIcon(0, 0));
			rebindUVsToIcon(BlockRenderSimpleDHD.model4, simpledhd.getIcon(0, 0));
	}

	void rebindUVsToIcon(WavefrontObject model, IIcon icon)
	{
		float minU = icon.getInterpolatedU(0);
		float sizeU = icon.getInterpolatedU(16) - minU;
		float minV = icon.getInterpolatedV(0);
		float sizeV = icon.getInterpolatedV(16) - minV;

		for(GroupObject groupObject : model.groupObjects)
			for(Face face : groupObject.faces)
				for (int i = 0; i < face.vertices.length; ++i)
				{
					TextureCoordinate textureCoordinate = face.textureCoordinates[i];
					face.textureCoordinates[i] = new TextureCoordinate(
							minU + sizeU * textureCoordinate.u,
							minV + sizeV * textureCoordinate.v
							);
				}
	}
	
	@Override
	public Object getServerGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z) {
   	 	if(blockEntity instanceof TileEntitySimpleDHD){
   	 		return new ContainerSimpleDHD((TileEntitySimpleDHD) blockEntity);
   	 	}	
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z) {
        if(blockEntity instanceof TileEntitySimpleDHD){
        	return new GuiSimpleDHD((TileEntitySimpleDHD) blockEntity);
        }
		return null;
	}

}
