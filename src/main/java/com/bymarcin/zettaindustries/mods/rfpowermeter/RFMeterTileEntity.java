package com.bymarcin.zettaindustries.mods.rfpowermeter;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.utils.WorldUtils;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class RFMeterTileEntity extends TileEntity implements IEnergyHandler, SimpleComponent{
	int transfer=0;//curent flow in RF/t
	int transferLimit=-1;
	long value=0;//current used energy
	long lastValue = 0;	
	
	String name = "";
	String password = "";
	boolean inCounterMode = true;
	boolean isOn = true;
	boolean isProtected = false;
	
	int tick = 0;
	public float r,g=1,b;

	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		data.setFloat("r", r);
		data.setFloat("g", g);
		data.setFloat("b", b);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, data);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		r=pkt.func_148857_g().getFloat("r");
		g=pkt.func_148857_g().getFloat("g");
		b=pkt.func_148857_g().getFloat("b");
	}
	
	
	public int getTransfer() {
		return transfer;
	}
	
	public long getCurrentValue() {
		return value;
	}
	
	@Override
	public String getComponentName() {
		return "rfmeter";
	}
	
	public void setPassword(String pass) {
		password = pass;
		isProtected = true;
	}

	public void removePassword() {
		password = "";
		isProtected = false;
	}
	
	public boolean canEdit(String pass) {
		return !isProtected || (isProtected && pass.equals(password));
	}
	
	
	public boolean canEnergyFlow() {
		return isOn && (inCounterMode || (0 < value));
	}
	
	public boolean checkPassword(int pos, final Arguments args){
		if(args.isString(pos)){
			if(canEdit(args.checkString(pos)))
				return true;
		}else{
			if(canEdit(null)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * OpenComputers methods
	 */
	
	@Callback(doc = "function(password:string [, oldPassword:string]):bool")
	public Object[] setPassword(final Context context, final Arguments args) {
		if(!checkPassword(1,args)) return new Object[]{false};
		
		String password = args.checkString(0);
		setPassword(password);
		return new Object[]{true};
	}
	
	@Callback(doc = "function(password:string):bool")
	public Object[] removePassword(final Context context, final Arguments args){
		if(!checkPassword(0,args)) return new Object[]{false};
		removePassword();
		return new Object[]{true};
	}
	
	@Callback(doc = "function(name:string [, password:string]):bool")
	public Object[] setName(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		name = args.checkString(0)!=null?args.checkString(0):"";
		return new Object[]{true};
	}
	
	@Callback(doc = "function():string")
	public Object[] getName(final Context context, final Arguments args){
		return new Object[]{name};
	}
	
	public Object[] getAvg(Context ctx, Arguments arg){
		return new Object[]{transfer};
	}
	
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOn(final Context context, final Arguments args){
		if(!checkPassword(0,args)) return new Object[]{false};
		isOn = true;
		return new Object[]{true};
	}
	
	@Callback(doc = "function([password:string]):bool")
	public Object[] setOff(final Context context, final Arguments args){
		if(!checkPassword(0,args)) return new Object[]{false};
		isOn = false;
		return new Object[]{true};
	}

	@Callback(doc = "function(value:int [, password:string]):bool")
	public Object[] setEnergyCounter(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		value = lastValue = args.checkInteger(0);
		return new Object[]{true};
	}
	
	@Callback(doc = "function():string")
	public Object[] getCounterMode(final Context context, final Arguments args){
		String type = inCounterMode?"counter":"prepaid";
		return new Object[]{type};
	}
	
	@Callback(doc = "function(type:bool [, password:string]):bool -- true == counter, false == prepaid")
	public Object[] setCounterMode(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		inCounterMode = args.checkBoolean(0);
		return new Object[]{true};
	}
	
	@Callback(doc = "function():double")
	public Object[] getCounterValue(final Context context, final Arguments args){
		return new Object[]{value};
	}
	
	@Callback(doc = "function():bool")
	public Object[] canEnergyFlow(final Context context, final Arguments args){
		return new Object[]{canEnergyFlow()};
	}
	
	@Callback(doc = "function(limit:int [, password:string]):bool")
	public Object[] setLimitPerTick(final Context context, final Arguments args){
		if(!checkPassword(1,args)) return new Object[]{false};
		if(args.checkInteger(0)>=0)
			transferLimit = args.checkInteger(0);
		else
			transferLimit = -1;
		return new Object[]{true};
	}
	
	
	/*
	 * end 
	 * 
	 */
	
	public void onPacket(long value, int transfer, boolean inCounterMode){
		if(WorldUtils.isServerWorld(worldObj)) return;
		this.value=value;
		this.transfer = transfer;
		this.inCounterMode = inCounterMode;
	}
	
	@Override
	public void updateEntity() {
		tick++;
		if(WorldUtils.isServerWorld(worldObj)){
			//tick++;
			if(tick%20==0){
				ZIRegistry.packetHandler.sendToAllAround(new RFMeterUpdatePacket(this, value, transfer, inCounterMode),
						new TargetPoint(getWorldObj().provider.dimensionId, xCoord+0.5, yCoord+0.5, zCoord+0.5, 32));
				tick=0;
			}
			long lastRecive = Math.abs(value - lastValue);
			transfer= (int) ((transfer * 9.0 + (double)lastRecive)/10.0);	
			lastValue = value;
		}else{
			if(inCounterMode)
				value+=transfer;
			else
				value-=transfer;
		}
	}
	
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return from == ForgeDirection.UP || from == ForgeDirection.DOWN;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if(!canEnergyFlow()) return 0;
		int temp = 0;
		if(from == ForgeDirection.UP){
			if(WorldUtils.isEnergyHandlerFromSide(this, from)){
                TileEntity tile= WorldUtils.getAdjacentTileEntity(worldObj,this.xCoord, this.yCoord, this.zCoord, ForgeDirection.DOWN);
                if(tile==null || !WorldUtils.isEnergyHandlerFromSide(tile,ForgeDirection.DOWN)){
                    return 0;
                }
				IEnergyHandler a = (IEnergyHandler) tile;
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
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 10000;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
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
		
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);
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
		
		
		nbt.setFloat("r", r);
		nbt.setFloat("g", g);
		nbt.setFloat("b", b);
	}
	
	public void  setTag(NBTTagCompound nbt){
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
		if(nbt.hasKey("r"))
			r = nbt.getFloat("r");
		if(nbt.hasKey("g"))
			g = nbt.getFloat("g");
		if(nbt.hasKey("b"))
			b = nbt.getFloat("b");	
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
		if(nbt.hasKey("r"))
			r = nbt.getFloat("r");
		if(nbt.hasKey("g"))
			g = nbt.getFloat("g");
		if(nbt.hasKey("b"))
			b = nbt.getFloat("b");	
	}
	
}
