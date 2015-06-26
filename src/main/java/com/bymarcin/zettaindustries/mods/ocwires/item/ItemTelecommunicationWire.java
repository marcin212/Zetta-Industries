package com.bymarcin.zettaindustries.mods.ocwires.item;

import java.util.List;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;
import com.bymarcin.zettaindustries.mods.ocwires.TelecommunicationWireType;
import com.bymarcin.zettaindustries.mods.ocwires.tileentity.TileEntityTelecomunicationConnector;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import blusunrize.immersiveengineering.api.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.IWireCoil;
import blusunrize.immersiveengineering.api.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.WireType;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;

public class ItemTelecommunicationWire extends BasicItem implements IWireCoil{

	public ItemTelecommunicationWire() {
		super("TelecommunicationWire");
		setMaxStackSize(64);
		setNoRepair();
	}
	
	@Override
	public void registerIcons(IIconRegister ir) {
		itemIcon = ir.registerIcon(ZettaIndustries.MODID + ":coil_telecommunication");
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv)
	{
		list.add(StatCollector.translateToLocal("tooltip.coil.info0"));
		list.add(StatCollector.translateToLocal("tooltip.coil.info1"));

		if(stack.getTagCompound()!=null && stack.getTagCompound().hasKey("linkingPos"))
		{
			int[] link = stack.getTagCompound().getIntArray("linkingPos");
			if(link!=null&&link.length>3)
				list.add(StatCollector.translateToLocalFormatted("tooltip.coil.attachedTo", link[1],link[2],link[3],link[0]));
		}
	}
	
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote && world.getTileEntity(x, y, z) instanceof IImmersiveConnectable && ((IImmersiveConnectable)world.getTileEntity(x, y, z)).canConnect() )
		{
			TargetingInfo target = new TargetingInfo(side, hitX,hitY,hitZ);
			if( !((IImmersiveConnectable)world.getTileEntity(x, y, z)).canConnectCable(getWireType(stack), target) || !(world.getTileEntity(x, y, z) instanceof TileEntityTelecomunicationConnector))
			{
				player.addChatMessage(new ChatComponentTranslation("chat.warning.wrongCable"));
				return false;
			}

			if(!ItemNBTHelper.hasKey(stack, "linkingPos"))
			{
				ItemNBTHelper.setIntArray(stack, "linkingPos", new int[]{world.provider.dimensionId,x,y,z});
				target.writeToNBT(stack.getTagCompound());
			}
			else
			{
				WireType type = getWireType(stack);
				int[] pos = ItemNBTHelper.getIntArray(stack, "linkingPos");
				int distance = (int) Math.ceil(Math.sqrt( (pos[1]-x)*(pos[1]-x) + (pos[2]-y)*(pos[2]-y) + (pos[3]-z)*(pos[3]-z) ));
				if(pos[0]!=world.provider.dimensionId)
					player.addChatMessage(new ChatComponentTranslation("chat.warning.wrongDimension"));
				else if(pos[1]==x&&pos[2]==y&&pos[3]==z)
					player.addChatMessage(new ChatComponentTranslation("chat.warning.sameConnection"));
				else if( distance > type.getMaxLength())
					player.addChatMessage(new ChatComponentTranslation("chat.warning.tooFar"));
				else if(!(world.getTileEntity(x, y, z) instanceof TileEntityTelecomunicationConnector) || !(world.getTileEntity(pos[1], pos[2], pos[3]) instanceof TileEntityTelecomunicationConnector))
					player.addChatMessage(new ChatComponentTranslation("chat.warning.invalidPoint"));
				else
				{
					IImmersiveConnectable nodeHere = (IImmersiveConnectable)world.getTileEntity(x, y, z);
					IImmersiveConnectable nodeLink = (IImmersiveConnectable)world.getTileEntity(pos[1], pos[2], pos[3]);
					boolean connectionExists = false;
					if(nodeHere!=null && nodeLink!=null)
					{
						for(ImmersiveNetHandler.Connection con : ImmersiveNetHandler.INSTANCE.getConnections(world, Utils.toCC(nodeHere)))
						{
							if(con.end.equals(Utils.toCC(nodeLink)))
								connectionExists = true;
						}
					}
					if(connectionExists)
						player.addChatMessage(new ChatComponentTranslation("chat.warning.connectionExists"));
					else
					{
						Vec3 rtOff0 = nodeHere.getRaytraceOffset().addVector(x, y, z);
						Vec3 rtOff1 = nodeLink.getRaytraceOffset().addVector(pos[1], pos[2], pos[3]);
						boolean canSee = Utils.canBlocksSeeOther(world, new ChunkCoordinates(x,y,z), new ChunkCoordinates(pos[1], pos[2], pos[3]), rtOff0,rtOff1);
						if(canSee)
						{
							TargetingInfo targetLink = TargetingInfo.readFromNBT(stack.getTagCompound());
							ImmersiveNetHandler.INSTANCE.addConnection(world, Utils.toCC(nodeHere), Utils.toCC(nodeLink), distance, type);
							nodeHere.connectCable(type, target);
							nodeLink.connectCable(type, targetLink);
							IESaveData.setDirty(world.provider.dimensionId);

							if(!player.capabilities.isCreativeMode)
								stack.stackSize--;
							((TileEntity)nodeHere).markDirty();
							world.markBlockForUpdate(x, y, z);
							((TileEntity)nodeLink).markDirty();
							world.markBlockForUpdate(pos[1], pos[2], pos[3]);
						}
						else
							player.addChatMessage(new ChatComponentTranslation("chat.warning.cantSee"));
					}
				}
				ItemNBTHelper.remove(stack, "linkingPos");
				ItemNBTHelper.remove(stack, "side");
				ItemNBTHelper.remove(stack, "hitX");
				ItemNBTHelper.remove(stack, "hitY");
				ItemNBTHelper.remove(stack, "hitZ");
			}
			return true;
		}
		return false;
	}

	@Override
	public WireType getWireType(ItemStack arg) {
		return TelecommunicationWireType.TELECOMMUNICATION;
	}
}
