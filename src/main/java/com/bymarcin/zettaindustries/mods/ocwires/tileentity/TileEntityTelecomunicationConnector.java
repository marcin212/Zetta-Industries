package com.bymarcin.zettaindustries.mods.ocwires.tileentity;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.ocwires.TelecommunicationWireType;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.SidedEnvironment;
import li.cil.oc.api.network.Visibility;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.immersiveengineering.common.util.Utils;

public class TileEntityTelecomunicationConnector extends TileEntity implements IImmersiveConnectable, Environment, SidedEnvironment{
    protected Node node;
    protected boolean addedToNetwork = false;
    private int facing;
    private boolean needUpdate = false;
    
    public TileEntityTelecomunicationConnector() {
    	node = Network.newNode(this, Visibility.None).create();
	}
    
    public int getFacing() {
		return facing;
	}
    
    public void setFacing(int facing) {
		this.facing = facing;
	}

	@Override
	public boolean canConnectCable(WireType wiretype, TargetingInfo target) {
		return TelecommunicationWireType.TELECOMMUNICATION.getUniqueName() == wiretype.getUniqueName();
	}

	@Override
	public void connectCable(WireType wiretype, TargetingInfo target) {
		checkConnections();	
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
	
	
	@Override
	public void removeCable(Connection con) {
		if(con==null){
			ZettaIndustries.logger.warn("Try to removed empty connection.");
			return;
		}
		if(con.start.equals(Utils.toCC(this))){
			if(CCToTileEntity(con.end)!=null && node!=null){
				node.disconnect(CCToTileEntity(con.end).node());
				//System.out.println("Disconnecting:  " + CCToTileEntity(con.end) + " --FROM-- " + this);
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void checkConnections(){
		Set<Connection> a = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
		if(a==null)return;
		for(Connection s : a){
			if(s.start.equals(Utils.toCC(this)) && CCToTileEntity(s.end)!=null){
				Node n = CCToTileEntity(s.end).node();
				if(!node.isNeighborOf(n)){
					node.connect(n);
					//System.out.println( Utils.toCC(this).equals(s.start)+ " +++ "+ CCToTileEntity(s.start) + "-k-" + CCToTileEntity(s.end));
				}
			}
		}
	}
	
	public TileEntityTelecomunicationConnector CCToTileEntity(ChunkCoordinates cc){
		TileEntity te  = worldObj.getTileEntity(cc.posX, cc.posY, cc.posZ);
		if(te instanceof TileEntityTelecomunicationConnector){
			return (TileEntityTelecomunicationConnector)te;
		}else{
			return null;
		}
	}
	
	@Override
	public Vec3 getRaytraceOffset(IImmersiveConnectable arg0) {
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		return Vec3.createVectorHelper(.5+fd.offsetX*.0625, .5+fd.offsetY*.0625, .5+fd.offsetZ*.0625);
	}
	
	@Override
	public Vec3 getConnectionOffset(Connection con)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		double conRadius = .03125;
		return Vec3.createVectorHelper(.5-conRadius*fd.offsetX, .5-conRadius*fd.offsetY, .5-conRadius*fd.offsetZ);	
	}
	
	@Override
	public WireType getCableLimiter(TargetingInfo arg0) {
		return TelecommunicationWireType.TELECOMMUNICATION;
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
	public boolean canConnect() {
		return true;
	}
	
	
	// OC
	@Override
	public Node node() {
		return node;
	}

	@Override
	public void onConnect(Node node) {
		if(node == node())
			needUpdate = true;
	}

	@Override
	public void onDisconnect(Node node) {
			
	}

	@Override
	public void onMessage(Message message) {
		
	}
	
    @Override
    public void updateEntity() {
    	if(worldObj.isRemote) return;
        if (!addedToNetwork) {
            addedToNetwork = true;
            Network.joinOrCreateNetwork(this);
        }
        
        if(needUpdate){
        	checkConnections();
        	needUpdate=false;
        }
        
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        // Make sure to remove the node from its network when its environment,
        // meaning this tile entity, gets unloaded.
        if (node != null) node.remove();
    }

    @Override
    public void invalidate() {
        super.invalidate();
       
        
		if(worldObj!=null && !worldObj.isRemote)
			ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(this),worldObj);
        // Make sure to remove the node from its network when its environment,
        // meaning this tile entity, gets unloaded.
        if (node != null) node.remove();
    }

    // ----------------------------------------------------------------------- //

    
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		if(worldObj!=null && !worldObj.isRemote)
		{
			NBTTagList connectionList = new NBTTagList();
			Set<Connection> conL = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
			if(conL!=null)
				for(Connection con : conL)
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
		if(worldObj!=null && worldObj.isRemote && !Minecraft.getMinecraft().isSingleplayer())
		{
			NBTTagList connectionList = nbt.getTagList("connectionList", 10);
			ImmersiveNetHandler.INSTANCE.clearConnectionsOriginatingFrom(Utils.toCC(this), worldObj);
			for(int i=0; i<connectionList.tagCount(); i++)
			{
				NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
				Connection con = Connection.readFromNBT(conTag);
				if(con!=null)
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
	
	

  
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if(nbt.hasKey("facing")){
        	facing = nbt.getInteger("facing");
        }
        
        // The host check may be superfluous for you. It's just there to allow
        // some special cases, where getNode() returns some node managed by
        // some other instance (for example when you have multiple internal
        // nodes in this tile entity).
        if (node != null && node.host() == this) {
            // This restores the node's address, which is required for networks
            // to continue working without interruption across loads. If the
            // node is a power connector this is also required to restore the
            // internal energy buffer of the node.
            node.load(nbt.getCompoundTag("oc:node"));
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("facing", facing);
        // See readFromNBT() regarding host check.
        if (node != null && node.host() == this) {
            final NBTTagCompound nodeNbt = new NBTTagCompound();
            node.save(nodeNbt);
            nbt.setTag("oc:node", nodeNbt);
        }
    }

	@Override
	public Node sidedNode(ForgeDirection side) {
		return node();
	}

	@Override
	public boolean canConnect(ForgeDirection side) {
		return side==ForgeDirection.getOrientation(facing);
	}

	@Override
	public boolean allowEnergyToPass(Connection arg0) {
		return false;
	}

	@Override
	public void onEnergyPassthrough(int arg0) {
		
	}

}
