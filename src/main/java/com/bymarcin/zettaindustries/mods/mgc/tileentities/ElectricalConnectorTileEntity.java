package com.bymarcin.zettaindustries.mods.mgc.tileentities;

import com.bymarcin.zettaindustries.mods.mgc.ElectricPoleTiered;
import com.bymarcin.zettaindustries.mods.mgc.block.ElectricalConnectorBlock;
import com.cout970.magneticraft.api.electricity.IElectricConductor;
import com.cout970.magneticraft.api.electricity.IElectricPole;
import com.cout970.magneticraft.api.electricity.IElectricTile;
import com.cout970.magneticraft.api.electricity.IInterPoleWire;
import com.cout970.magneticraft.api.electricity.ITileElectricPole;
import com.cout970.magneticraft.api.electricity.prefab.ElectricConductor;
import com.cout970.magneticraft.api.util.VecDouble;
import com.cout970.magneticraft.api.util.VecInt;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;

public class ElectricalConnectorTileEntity extends TileEntity implements ITileElectricPole, IElectricTile {
	VecDouble vec = new VecDouble(0, 0, 0);
	int tier = 0;
	IElectricConductor input;
	IElectricPole component;
	public int facing;
	boolean  updateClient = false;
	
	public ElectricalConnectorTileEntity() {
		input = new ElectricConductor(this, tier, 0.001);
		component = new ElectricPoleTiered(this, input) {
			public VecDouble[] getWireConnectors() {
				return new VecDouble[] { ElectricalConnectorTileEntity.this.vec.copy() };
			};
			
			@Override
			public void onDisconnect(IInterPoleWire conn) {
				updateClient = true;
				super.onDisconnect(conn);
			}
		};
	}
	
	public ElectricalConnectorTileEntity(int metadata) {
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();

		switch (metadata) {
		case ElectricalConnectorBlock.connectorLV:
			vec = new VecDouble(.5, .5, .5);
			tier = 0;
			break;
		case ElectricalConnectorBlock.connectorMV:
			vec = new VecDouble(.5 + fd.offsetX * (.0625), .5 + fd.offsetY * (.0625), .5 + fd.offsetZ * (.0625));
			tier = 2;
			break;
		case ElectricalConnectorBlock.connectorHV:
			vec = new VecDouble(.5 + fd.offsetX * (.25), .5 + fd.offsetY * (.25), .5 + fd.offsetZ * (.25));
			tier = 4;
			break;
		case ElectricalConnectorBlock.relayHV:
			vec = new VecDouble(.5, .125, .5);
			tier = 4;
			break;
		}

		input = new ElectricConductor(this, tier, 0.001);
		component = new ElectricPoleTiered(this, input) {
			public VecDouble[] getWireConnectors() {
				return new VecDouble[] { ElectricalConnectorTileEntity.this.vec.copy() };
			};
			
			@Override
			public void onDisconnect(IInterPoleWire conn) {
				updateClient = true;
				super.onDisconnect(conn);
			}
		};
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}
	
	public void setFacing(int facing) {
		this.facing = facing;
	}

	public int getFacing() {
		return facing;
	}


    public void sendUpdateToClient() {
        if (worldObj.isRemote) return;
        Packet nbt = getDescriptionPacket();
        
        for(Object p:worldObj.playerEntities){
        	if(p instanceof EntityPlayerMP){
        		((EntityPlayerMP) p).playerNetServerHandler.sendPacket(nbt);
        	}
        }
    }
	
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		input.recache();
		input.iterate();
		component.iterate();
		if(((ElectricPoleTiered)component).update || updateClient){
			sendUpdateToClient();
			updateClient = false;
		}
	}

	@Override
	public ITileElectricPole getMainTile() {
		return this;
	}

	@Override
	public IElectricPole getPoleConnection() {
		return component;
	}

	@Override
	public IElectricConductor[] getConds(VecInt dir, int tier) {
		if (blockMetadata != ElectricalConnectorBlock.relayHV && dir.toMgDirection()!=null
				&& dir.toMgDirection().ordinal() == facing
				&& this.tier == tier) {
			return new IElectricConductor[] { input };
		} else if(dir.toMgDirection()==null && this.tier == tier){
			return new IElectricConductor[] { input };
		}else{
			return null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		input.save(nbt);
		component.save(nbt);
		nbt.setInteger("TIER", tier);
		vec.save(nbt, "conOffset");
		nbt.setInteger("Facing", facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		facing = nbt.getInteger("Facing");
		tier = nbt.getInteger("TIER");
		vec = new VecDouble(nbt, "conOffset");
		input.load(nbt);
		component.load(nbt);

	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

}
