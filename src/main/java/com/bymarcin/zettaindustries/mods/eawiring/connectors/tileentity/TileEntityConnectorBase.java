package com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.bymarcin.zettaindustries.mods.eawiring.connection.AbstractConnection;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.VirtualConnectionRegistry;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.node.ConnectorNode;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;

import mods.eln.misc.Coordonate;
import mods.eln.node.simple.SimpleNodeEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.immersiveengineering.common.util.Utils;

public abstract class TileEntityConnectorBase extends SimpleNodeEntity implements IImmersiveConnectable{
	LinkedList<AbstractConnection> connections = new LinkedList<AbstractConnection>();
	public int facing;
	public int qFacing;
	ForgeDirection[] Q_VALID_DIRECTIONS = {ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST};
	@Override
	public String getNodeUuid() {
		return ConnectorNode.getNodeID();
	}

	public abstract ForgeDirection[] getValidEAConnectionSide();
	
	@Override
	public boolean allowEnergyToPass(Connection arg0) {
		return false;
	}

	public void setQFacing(int qFacing) {
		this.qFacing = qFacing;
	}
	
    public void setFacing(int facing) {
		this.facing = facing;
	}
    
    public int getFacing() {
		return facing;
	}
    
    public int getQFacing() {
		return qFacing;
	}

	@Override
	public boolean canConnect() {
		return true;
	}
	
	@Override
	public boolean receiveClientEvent(int id, int arg)
	{
		if(id==-1)
		{
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	public boolean connectionExists(ConnectorNode n1, ConnectorNode n2) {
		Iterator<AbstractConnection> it = connections.iterator();
		while(it.hasNext()) {
			AbstractConnection acon = it.next();
			if( (acon.getCoords1().equals(n1.coordonate) && acon.getCoords2().equals(n2.coordonate)) ||  
					(acon.getCoords2().equals(n1.coordonate) && acon.getCoords1().equals(n2.coordonate))){
				return true;
			}
		}	
		
		return false;
	}

	public boolean removeConnection(AbstractConnection con) {
		return connections.remove(con);
	}
	
	@Override
	public void connectCable(WireType wt, TargetingInfo arg1) {
		if(worldObj.isRemote) return;
		TileEntity te = worldObj.getTileEntity((int) arg1.hitX, (int) arg1.hitY, (int) arg1.hitZ);
		if (te instanceof TileEntityConnectorBase) {
			ConnectorNode n = (ConnectorNode) ((TileEntityConnectorBase) te).getNode();
			if (!connectionExists((ConnectorNode) this.getNode(), n) && !((TileEntityConnectorBase) te).connectionExists((ConnectorNode) this.getNode(), n)) {
				AbstractConnection connection = new AbstractConnection((ConnectorNode) this.getNode(), n, ((WireBase)wt).getWireProperties());
				connections.add(connection);
				connection.init();
			}
		}
	}
	
	public List<AbstractConnection> getConnectionsForPoint(Coordonate con){
		LinkedList<AbstractConnection> result = new LinkedList<AbstractConnection>();
		for(AbstractConnection c: connections){
			if(c.getCoords1().equals(con) || c.getCoords2().equals(con)){
				result.add(c);
			}
		}
		return result;
	}

	public void removeConnection(ChunkCoordinates con){
		Coordonate coords = new Coordonate(con.posX, con.posY, con.posZ, worldObj);
		Iterator<AbstractConnection> it = connections.iterator();
		boolean removed = false;
		while(it.hasNext()){
			AbstractConnection acon = it.next();
			if( acon.getCoords1().equals(coords) || acon.getCoords2().equals(coords) ){
				it.remove();
				acon.onRemove();
				removed = true;
			}
		}
		if(removed)
			this.markDirty();
	}
	

	@Override
	public void removeCable(Connection con) {
		if(worldObj.isRemote) return;
		ChunkCoordinates coords = Utils.toCC(this);
		if(!con.start.equals(coords)){
			removeConnection(con.start);
		}else{
			removeConnection(con.end);
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean isEnergyOutput() {
		return false;
	}

	@Override
	public int outputEnergy(int arg0, boolean arg1, int arg2) {
		return 0;
	}

	@Override
	public void invalidate() {
		super.invalidate();

		if (worldObj != null && !worldObj.isRemote){
			ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(this), worldObj);
			removeAllConnections();
		}

	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		if (worldObj != null && !worldObj.isRemote)
		{
			NBTTagList connectionList = new NBTTagList();
			Set<Connection> conL = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
			if (conL != null)
				for (Connection con : conL)
					connectionList.appendTag(con.writeToNBT());
			nbttagcompound.setTag("connectionList", connectionList);
		}
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound nbt = pkt.func_148857_g();
		this.readFromNBT(nbt);
		if (worldObj != null && worldObj.isRemote && !Minecraft.getMinecraft().isSingleplayer())
		{
			NBTTagList connectionList = nbt.getTagList("connectionList", 10);
			ImmersiveNetHandler.INSTANCE.clearConnectionsOriginatingFrom(Utils.toCC(this), worldObj);
			for (int i = 0; i < connectionList.tagCount(); i++)
			{
				NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
				Connection con = Connection.readFromNBT(conTag);
				if (con != null)
				{
					ImmersiveNetHandler.INSTANCE.addConnection(worldObj, Utils.toCC(this), con);
				}
				else
					IELogger.error("CLIENT read connection as null");
			}

		}
		
		if(FMLCommonHandler.instance().getSide().isClient()){
			receiveClientEvent(-1, 0);
		}
	}
	
	private void removeAllConnections(){
		for(AbstractConnection con: connections){
			con.onRemove();
		}
		connections.clear();
		
	}
	
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		nbt.setInteger("qFacing", qFacing);
		
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
		NBTTagCompound cons = new NBTTagCompound();
		cons.setInteger("size", connections.size());
		for(int i=0;i<connections.size();i++){
			connections.get(i).writeToNBT(cons, "["+i+"]");
		}
		nbt.setTag("conList", cons);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		facing = nbt.getInteger("facing");
		qFacing = nbt.getInteger("qFacing");
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
		NBTTagCompound cons = nbt.getCompoundTag("conList");
		int size = cons.getInteger("size");
		removeAllConnections();
		for(int i=0;i<size;i++){
			AbstractConnection ac = new AbstractConnection();
			ac.readFromNBT(cons,"["+i+"]");
			VirtualConnectionRegistry.instance().addToQueue(ac);
			connections.add(ac);
		}
	}


}
