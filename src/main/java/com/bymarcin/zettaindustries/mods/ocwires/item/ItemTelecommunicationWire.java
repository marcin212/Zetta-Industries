package com.bymarcin.zettaindustries.mods.ocwires.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.IEContent;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;
import com.bymarcin.zettaindustries.mods.ocwires.TelecommunicationWireType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTelecommunicationWire extends BasicItem implements IWireCoil {

	public ItemTelecommunicationWire() {
		super("telecommunicationwire");
		setMaxStackSize(64);
		setNoRepair();
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag)
	{
		list.add(I18n.translateToLocal("tooltip.coil.info0"));
		list.add(I18n.translateToLocal("tooltip.coil.info1"));

		if(stack.getTagCompound()!=null && stack.getTagCompound().hasKey("linkingPos"))
		{
			int[] link = stack.getTagCompound().getIntArray("linkingPos");
			if(link!=null&&link.length>3)
				list.add(I18n.translateToLocalFormatted("tooltip.coil.attachedTo", link[1],link[2],link[3],link[0]));
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return IEContent.itemWireCoil.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public WireType getWireType(ItemStack arg) {
		return TelecommunicationWireType.TELECOMMUNICATION;
	}
}
