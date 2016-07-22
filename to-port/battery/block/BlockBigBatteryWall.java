package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.IMultiblockPart;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityWall;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;


public class BlockBigBatteryWall extends BasicBlockMultiblockBase {
	public static IIcon icon;
	public static IIcon corner;
	public static IIcon horizontal;
	public static IIcon vertical;
	public static IIcon center;

	  public static final int CASING_METADATA_BASE = 0;
	  public static final int CASING_CORNER = 1;
	  public static final int CASING_CENTER = 2;
	  public static final int CASING_VERTICAL = 3;
	  public static final int CASING_EASTWEST = 4;
	  public static final int CASING_NORTHSOUTH = 5;
	

	public BlockBigBatteryWall() {
		super("batterywall");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.bottom") + ", " + localize("tooltip.top") +
                        ", " + localize("tooltip.sides") + ", " + localize("tooltip.frame"));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityWall();
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		switch(par2){
		case BlockBigBatteryWall.CASING_METADATA_BASE: return icon;
		case BlockBigBatteryWall.CASING_CORNER: return corner;
		case BlockBigBatteryWall.CASING_CENTER: return center;
		case BlockBigBatteryWall.CASING_EASTWEST: return horizontal;
		case BlockBigBatteryWall.CASING_NORTHSOUTH: return (par1 == 0 || par1 == 1)?vertical:horizontal;
		case BlockBigBatteryWall.CASING_VERTICAL: return vertical;
		}
		
		return icon;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_part");
		corner = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_corner");
		horizontal = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_horizontal");
		vertical = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_vertical");
		center = iconRegister.registerIcon(ZettaIndustries.MODID + ":battery/bb_center");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {	
		if(player.isSneaking()) {
			return false;
		}
		
		if(!world.isRemote) {
			ItemStack currentEquippedItem = player.getCurrentEquippedItem();
			if(currentEquippedItem == null) {
				TileEntity te = world.getTileEntity(x, y, z);
				if(te instanceof IMultiblockPart) {
					MultiblockControllerBase controller = ((IMultiblockPart)te).getMultiblockController();
					if(controller != null) {
						Exception e = controller.getLastValidationException();
						if(e != null) {
							player.addChatMessage(new ChatComponentText(e.getMessage()));
							return true;
						}
					}else {
						player.addChatMessage(new ChatComponentText("Block is not connected to a battery. This could be due to lag, or a bug. If the problem persists, try breaking and re-placing the block."));
						return true;
					}
				}
			 }
		}
		return false;
	}
}
