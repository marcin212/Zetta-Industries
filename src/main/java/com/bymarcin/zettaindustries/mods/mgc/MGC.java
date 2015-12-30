package com.bymarcin.zettaindustries.mods.mgc;

import com.bymarcin.zettaindustries.modmanager.IMod;
import com.bymarcin.zettaindustries.mods.mgc.block.ElectricalConnectorBlock;
import com.bymarcin.zettaindustries.mods.mgc.block.LampSocketBlock;
import com.bymarcin.zettaindustries.mods.mgc.gui.LampSocketContainer;
import com.bymarcin.zettaindustries.mods.mgc.gui.LampSocketGUI;
import com.bymarcin.zettaindustries.mods.mgc.item.LightBulbItem;
import com.bymarcin.zettaindustries.mods.mgc.render.ElectricalConnectorRenderer;
import com.bymarcin.zettaindustries.mods.mgc.render.LampSocketRenderer;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.ElectricalConnectorTileEntity;
import com.bymarcin.zettaindustries.mods.mgc.tileentities.LampSocketTileEntity;
import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.registry.gui.IGUI;
import com.bymarcin.zettaindustries.registry.proxy.IProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MGC implements IMod, IGUI, IProxy {
	LampSocketBlock lampSocketBlock;
	LightBulbItem lightBulbItem;
	ElectricalConnectorBlock connector;

	@Override
	public void preInit() {
		lampSocketBlock = new LampSocketBlock();
		lightBulbItem = new LightBulbItem();
		connector = new ElectricalConnectorBlock();
		
		ZIRegistry.registerProxy(this);

	}

	@Override
	public void init() {
		ZIRegistry.registerGUI(this);
	}

	@Override
	public void postInit() {
		ItemStack cable = GameRegistry.findItemStack("Magneticraft", "item.cable_low",1);
		ItemStack cableMV = GameRegistry.findItemStack("Magneticraft", "item.cable_medium",1);
		ItemStack cableHV = GameRegistry.findItemStack("Magneticraft", "item.cable_high",1);
		ItemStack machine = GameRegistry.findItemStack("Magneticraft", "machine_hausing",1);
		ItemStack glass = GameRegistry.findItemStack("minecraft", "glass",1);
		ItemStack clay = GameRegistry.findItemStack("minecraft", "clay_ball",1);
		
		
		if(cable!=null && machine !=null && glass!=null){
			GameRegistry.addShapedRecipe(new ItemStack(lampSocketBlock, 1, 0), 
					"CMC",
					"GGG",
					"   ",
					'C', cable, 'M', machine, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lampSocketBlock, 1, 1), 
					" C ",
					"GMG",
					" G ",
					'C', cable, 'M', machine, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lampSocketBlock, 1, 2), 
					"GCG",
					"GMG",
					"   ",
					'C', cable, 'M', machine, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lampSocketBlock, 1, 3), 
					"GCG",
					"GMG",
					"GGG",
					'C', cable, 'M', machine, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lampSocketBlock, 1, 4), 
					"GGG",
					"CMG",
					"GGG",
					'C', cable, 'M', machine, 'G', glass
					);
			
			
			
			
			
			GameRegistry.addShapedRecipe(new ItemStack(lightBulbItem, 1, 0), 
					"GCG",
					" G ",
					"   ",
					'C', cable, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lightBulbItem, 1, 1), 
					"GCG",
					"GCG",
					" G",
					'C', cable, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lightBulbItem, 1, 2), 
					"GCG",
					"GGG",
					"   ",
					'C', cable, 'G', glass
					);
			GameRegistry.addShapedRecipe(new ItemStack(lightBulbItem, 1, 3), 
					"GCG",
					"GCG",
					"GGG",
					'C', cable, 'G', glass
					);
		
			
			
			
			
			GameRegistry.addShapedRecipe(new ItemStack(connector, 1, 0), 
					" C ",
					"GCG",
					"GCG",
					'C', cable, 'G', clay
					);
			
			GameRegistry.addShapedRecipe(new ItemStack(connector, 1, 1), 
					" C ",
					"GCG",
					"GCG",
					'C', cableMV, 'G', clay
					);
			GameRegistry.addShapedRecipe(new ItemStack(connector, 1, 2), 
					" C ",
					"GCG",
					"GCG",
					'C', cableHV, 'G', clay
					);
			GameRegistry.addShapedRecipe(new ItemStack(connector, 1, 3), 
					"GCG",
					"GCG",
					" C ",
					'C', cableHV, 'G', glass
					);
			
			
			
		}
	}

	@Override
	public Object getServerGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof LampSocketTileEntity) {
			return new LampSocketContainer(player.inventory, (LampSocketTileEntity) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, TileEntity blockEntity, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof LampSocketTileEntity) {
			return new LampSocketGUI(player.inventory, (LampSocketTileEntity) tileEntity);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientSide() {
		ElectricalConnectorRenderer electricalConnectorRenderer = new ElectricalConnectorRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(ElectricalConnectorTileEntity.class, electricalConnectorRenderer);
		RenderingRegistry.registerBlockHandler(electricalConnectorRenderer);
		RenderingRegistry.registerBlockHandler(new LampSocketRenderer());
	}

	@Override
	public void serverSide() {
		// TODO Auto-generated method stub

	}

}
