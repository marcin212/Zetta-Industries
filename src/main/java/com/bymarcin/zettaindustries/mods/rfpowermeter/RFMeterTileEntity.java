package com.bymarcin.zettaindustries.mods.rfpowermeter;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import cofh.api.energy.IEnergyHandler;

import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.utils.Avg;
import com.bymarcin.zettaindustries.utils.MathUtils;
import com.bymarcin.zettaindustries.utils.WorldUtils;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class RFMeterTileEntity extends TileEntity implements IEnergyHandler, ITickable, IEnergyProvider, IEnergyReceiver{
	int transfer=0;//curent flow in RF/t
	int transferLimit=-1;
	long value=0;//current used energy
	long lastValue = 0;	
	Avg avg = new Avg();
	String name = "";
	String password = "";
	boolean inCounterMode = true;
	boolean isOn = true;
	boolean isProtected = false;
	boolean isInverted = false;
	
	int tick = 0;
	public int color = EnumDyeColor.LIME.ordinal();
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return from == EnumFacing.UP || from == EnumFacing.DOWN;
	}
	@Override
	public int getEnergyStored(EnumFacing from) {
		return 0;
	}
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 10000;
	}

	public NBTTagCompound writeNBTData(NBTTagCompound data) {
		data.setFloat("color", color);
		return data;
	}

	public void readNBTData(NBTTagCompound compound) {
		color = compound.getInteger("color");
	}

	@Override
	public final NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.writeToNBT(new NBTTagCompound());
		writeNBTData(compound);
		return compound;
	}

	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if (pkt != null && pkt.getNbtCompound() != null) {
			readNBTData(pkt.getNbtCompound());
		}
	}

    public void invert(){
		isInverted = !isInverted;
		getWorld().setBlockState(getPos(),getWorld().getBlockState(getPos()).withProperty(RFMeterBlock.inverted, isInverted),2);
	}
	
	public boolean isInverted() {
		return isInverted;
	}
	
	public int getTransfer() {
		return transfer;
	}
	
	public long getCurrentValue() {
		return value;
	}
	
	public void setPassword(String pass) {
		password = MathUtils.encryptPassword(pass);
		isProtected = true;
	}

	public void removePassword() {
		password = "";
		isProtected = false;
	}
	
	public boolean canEdit(String pass) {
		return !isProtected || (isProtected && pass!=null && MathUtils.encryptPassword(pass).equals(password));
	}
	
	
	public boolean canEnergyFlow() {
		return isOn && (inCounterMode || (0 < value));
	}


	public void onPacket(long value, int transfer, boolean inCounterMode){
		if(WorldUtils.isServerWorld(worldObj)) return;
		this.value=value;
		this.transfer = transfer;
		this.inCounterMode = inCounterMode;
	}

	@Override
	public void update() {
		tick++;
		if(WorldUtils.isServerWorld(worldObj)){
			//tick++;
			if(tick%20==0){
				ZIRegistry.packetHandler.sendToAllAround(new RFMeterUpdatePacket(this, value, transfer, inCounterMode),
						new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5, 32));
				tick=0;
			}
			long lastRecive = Math.abs(value - lastValue);
			avg.putValue(lastRecive);
			transfer= (int)avg.getAvg();
			lastValue = value;
		}else{
			if(inCounterMode)
				value+=transfer;
			else
				value-=transfer;
		}
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if(!canEnergyFlow()) return 0;
		int temp = 0;
		if(from == (isInverted?EnumFacing.DOWN:EnumFacing.UP) ){
			if(WorldUtils.isEnergyReciverFromSide(this, from)){
                TileEntity tile= WorldUtils.getAdjacentTileEntity(worldObj,this.getPos(), isInverted?EnumFacing.UP:EnumFacing.DOWN);
                if(tile==null || !WorldUtils.isEnergyReciverFromSide(tile,isInverted?EnumFacing.DOWN:EnumFacing.UP)){
                    return 0;
                }
                IEnergyReceiver a = (IEnergyReceiver) tile;
				temp= a.receiveEnergy(from, transferLimit==-1?
												(inCounterMode ? maxReceive : Math.min((int)value, maxReceive))
												:Math.min(transferLimit,(inCounterMode ? maxReceive : Math.min((int)value, maxReceive)))
												, simulate);

				if(!simulate) if(inCounterMode) value+=temp; else value-=temp;
				return temp;
			}
		}
		return 0;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt2) {
        NBTTagCompound nbt = super.writeToNBT(nbt2);
		nbt.setInteger("transfer", transfer);
		nbt.setInteger("transferLimit", transferLimit);
		
		nbt.setLong("value", value);
		nbt.setLong("lastValue", lastValue);
		
		nbt.setString("name", name);
		nbt.setString("password", password);
		
		nbt.setBoolean("inCounterMode", inCounterMode);
		nbt.setBoolean("isOn", isOn);
		nbt.setBoolean("isProtected", isProtected);
		
		nbt.setInteger("tick", tick);

		nbt.setInteger("color", color);

		
		nbt.setBoolean("isInverted", isInverted);
        return nbt;
	}
	
	public void getTag(NBTTagCompound nbt){
		nbt.setInteger("transferLimit", transferLimit);
		
		nbt.setLong("value", value);
		nbt.setLong("lastValue", lastValue);
		
		nbt.setString("name", name);
		nbt.setString("password", password);
		
		nbt.setBoolean("inCounterMode", inCounterMode);
		nbt.setBoolean("isOn", isOn);
		nbt.setBoolean("isProtected", isProtected);

		nbt.setInteger("color", color);
		
		nbt.setBoolean("isInverted", isInverted);
	}
	
	public void setTag(NBTTagCompound nbt){
		if(nbt.hasKey("transferLimit"))
			transferLimit= nbt.getInteger("transferLimit");
		if(nbt.hasKey("value"))
			value= nbt.getLong("value");
		if(nbt.hasKey("lastValue"))
			lastValue = nbt.getLong("lastValue");
		if(nbt.hasKey("name"))
			name = nbt.getString("name");
		if(nbt.hasKey("password"))
			password = nbt.getString("password");
		if(nbt.hasKey("inCounterMode"))
			inCounterMode = nbt.getBoolean("inCounterMode");
		if(nbt.hasKey("isOn"))
			isOn = nbt.getBoolean("isOn");
		if(nbt.hasKey("isProtected"))
			isProtected = nbt.getBoolean("isProtected");
		if(nbt.hasKey("color"))
			color = nbt.getInteger("color");
		if(nbt.hasKey("isInverted")){
			isInverted = nbt.getBoolean("isInverted");
		}
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("transfer"))
			transfer= nbt.getInteger("transfer");
		if(nbt.hasKey("transferLimit"))
			transferLimit= nbt.getInteger("transferLimit");
		if(nbt.hasKey("value"))
			value= nbt.getLong("value");
		if(nbt.hasKey("lastValue"))
			lastValue = nbt.getLong("lastValue");
		if(nbt.hasKey("name"))
			name = nbt.getString("name");
		if(nbt.hasKey("password"))
			password = nbt.getString("password");
		if(nbt.hasKey("inCounterMode"))
			inCounterMode = nbt.getBoolean("inCounterMode");
		if(nbt.hasKey("isOn"))
			isOn = nbt.getBoolean("isOn");
		if(nbt.hasKey("isProtected"))
			isProtected = nbt.getBoolean("isProtected");
		if(nbt.hasKey("tick"))
			tick = nbt.getInteger("tick");
		if(nbt.hasKey("color"))
			color = nbt.getInteger("color");
		if(nbt.hasKey("isInverted"))
			isInverted = nbt.getBoolean("isInverted");
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return newSate.getBlock()!=oldState.getBlock();
	}
}
