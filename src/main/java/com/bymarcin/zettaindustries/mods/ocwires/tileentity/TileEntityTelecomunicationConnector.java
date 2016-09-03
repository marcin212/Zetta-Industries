package com.bymarcin.zettaindustries.mods.ocwires.tileentity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.immersiveengineering.common.util.Utils;
import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.ocwires.TelecommunicationWireType;
import com.bymarcin.zettaindustries.mods.ocwires.block.BlockTelecomunicationConnector;
import com.google.common.collect.ImmutableSet;
import li.cil.oc.api.Network;
import li.cil.oc.api.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;


public class TileEntityTelecomunicationConnector extends TileEntity implements IImmersiveConnectable, Environment, SidedEnvironment, ITickable{
    protected Node node;
    protected boolean addedToNetwork = false;
    private boolean needUpdate = false;
    
    public TileEntityTelecomunicationConnector() {
    	node = Network.newNode(this, Visibility.None).create();
	}
    
    public int getFacing() {
		return getBlockMetadata();
	}

	@Override
	public boolean canConnectCable(WireType wiretype, TargetingInfo target) {
		return TelecommunicationWireType.TELECOMMUNICATION.getUniqueName() == wiretype.getUniqueName();
	}

	@Override
	public void connectCable(WireType wireType, TargetingInfo targetingInfo, IImmersiveConnectable iImmersiveConnectable) {
		checkConnections();
	}

	@Override
	public boolean receiveClientEvent(int id, int arg)
	{
		if(id==-1)
		{
			//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
			if(PosToTileEntity(con.end)!=null && node!=null){
				node.disconnect(PosToTileEntity(con.end).node());
				//System.out.println("Disconnecting:  " + CCToTileEntity(con.end) + " --FROM-- " + this);
			}
		}
		//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void checkConnections(){
		Set<Connection> a = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
		if(a==null)return;
		for(Connection s : a){
			if(s.start.equals(Utils.toCC(this)) && PosToTileEntity(s.end)!=null){
				Node n = PosToTileEntity(s.end).node();
				if(!node.isNeighborOf(n)){
					node.connect(n);
					//System.out.println( Utils.toCC(this).equals(s.start)+ " +++ "+ CCToTileEntity(s.start) + "-k-" + CCToTileEntity(s.end));
				}
			}
		}
	}
	
	public TileEntityTelecomunicationConnector PosToTileEntity(BlockPos pos){
		TileEntity te  = worldObj.getTileEntity(pos);
		if(te instanceof TileEntityTelecomunicationConnector){
			return (TileEntityTelecomunicationConnector)te;
		}else{
			return null;
		}
	}
	
	@Override
	public Vec3d getRaytraceOffset(IImmersiveConnectable arg0) {
		EnumFacing fd = EnumFacing.values()[getFacing()].getOpposite();
		return new Vec3d(.5+fd.getFrontOffsetX()*.0625, .5+fd.getFrontOffsetY()*.0625, .5+fd.getFrontOffsetZ()*.0625);
	}
	
	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		EnumFacing fd = EnumFacing.values()[getFacing()].getOpposite();
		double conRadius = .03125;
		return new Vec3d(.5-conRadius*fd.getFrontOffsetX(), .5-conRadius*fd.getFrontOffsetY(), .5-conRadius*fd.getFrontOffsetZ());
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
	public BlockPos getConnectionMaster(WireType wireType, TargetingInfo targetingInfo) {
		return getPos();
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


	public Set<Connection> genConnBlockstate()
	{
		Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, pos);
		if (conns == null)
			return ImmutableSet.of();
		Set<Connection> ret = new HashSet<Connection>()
		{
			@Override
			public boolean equals(Object o)
			{
				if (o == this)
					return true;
				if (!(o instanceof HashSet))
					return false;
				HashSet<Connection> other = (HashSet<Connection>) o;
				if (other.size() != this.size())
					return false;
				for (Connection c : this)
					if (!other.contains(c))
						return false;
				return true;
			}
		};
		for (Connection c : conns)
		{
			// generate subvertices
			if (c.end.compareTo(pos) >= 0)
				continue;
			IImmersiveConnectable end = ApiUtils.toIIC(c.end, worldObj, false);
			if (end==null)
				continue;
			c.getSubVertices(worldObj);
			ret.add(c);
		}

		return ret;
	}

    @Override
    public void update() {
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
			ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(this),worldObj,true);
        // Make sure to remove the node from its network when its environment,
        // meaning this tile entity, gets unloaded.
        if (node != null) node.remove();
    }

    // ----------------------------------------------------------------------- //


	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		{
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			this.writeToNBT(nbttagcompound);
			if (worldObj != null && !worldObj.isRemote) {
				NBTTagList connectionList = new NBTTagList();
				Set<Connection> conL = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this));
				if (conL != null)
					for (Connection con : conL)
						connectionList.appendTag(con.writeToNBT());
				nbttagcompound.setTag("connectionList", connectionList);
			}
			return new SPacketUpdateTileEntity(getPos(), 3, nbttagcompound);
		}

	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound nbt = pkt.getNbtCompound();
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
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        // See readFromNBT() regarding host check.
        if (node != null && node.host() == this) {
            final NBTTagCompound nodeNbt = new NBTTagCompound();
            node.save(nodeNbt);
            nbt.setTag("oc:node", nodeNbt);
        }
        return nbt;
    }

	@Override
	public Node sidedNode(EnumFacing side) {
		return node();
	}

	@Override
	public boolean canConnect(EnumFacing side) {
		return side == EnumFacing.values()[getBlockMetadata()];
	}

	@Override
	public boolean allowEnergyToPass(Connection arg0) {
		return false;
	}

	@Override
	public void onEnergyPassthrough(int arg0) {

	}


}
