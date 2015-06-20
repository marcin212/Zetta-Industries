package com.bymarcin.zettaindustries.mods.superconductor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.bymarcin.zettaindustries.registry.network.Packet;
import com.bymarcin.zettaindustries.utils.Pair;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.IMultiblockPart;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import com.bymarcin.zettaindustries.mods.superconductor.gui.PacketUpdateFluidAmount;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityBase;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityWire;
import com.bymarcin.zettaindustries.utils.WorldUtils;



public class SuperConductor extends MultiblockControllerBase{

	ArrayList<TileEntityControler> controlersInput;
	HashMap<TileEntityControler,Pair<Integer,Integer>> controlersOutput;
	
	public FluidTank tank = new FluidTank(0);
	int ticksFromLastDrain = 0;
	public boolean active = false;
	private boolean lock = false;
	int ntick = 0;
	
	public SuperConductor(World world) {
		super(world);
		controlersInput= new ArrayList<TileEntityControler>();
		controlersOutput= new HashMap<TileEntityControler,Pair<Integer,Integer>>();
	}

	
	public void validateAllControlers(){
		lock=false;
		tank.setCapacity(0);
		tank.setFluid(null);
		controlersInput.clear();
		controlersOutput.clear();
		for(IMultiblockPart c : connectedParts){
			tank.setCapacity(tank.getCapacity() + ((TileEntityBase)c).getCoolantCapacity());
			tank.setFluid(new FluidStack(SuperConductorMod.coolantFluid,((TileEntityBase)c).getCoolantAmount() + tank.getFluidAmount()));
			if(c instanceof TileEntityWire) continue;
			if(((TileEntityControler) c).isOutput()){
				controlersOutput.put((TileEntityControler) c, new Pair<Integer,Integer>(0,0));
			}else{
				controlersInput.add((TileEntityControler) c);
			}
		}
	}

	public int onReceiveEnergy(int maxRecive, boolean simulate){
		if(lock || !active) return 0;
		lock = true;
		
		ArrayList<Pair<Integer,TileEntityControler>> order = new ArrayList<Pair<Integer,TileEntityControler>>();
		long requiredEnergy = 0;
		double ratio = 0;
		int energyConsumed=0;
		
		for(Entry<TileEntityControler, Pair<Integer, Integer>> c:controlersOutput.entrySet()){
			if(c.getValue().getKey()!=ntick){
				c.getValue().setValue(c.getKey().onReceiveEnergy(Integer.MAX_VALUE, true));
				c.getValue().setKey(ntick);
			}
			order.add(new Pair<Integer,TileEntityControler>(c.getValue().getValue(),c.getKey()));
			requiredEnergy += c.getValue().getValue();	
		}
		
		ratio = (double)maxRecive/(double)requiredEnergy;
		
		for(Pair<Integer, TileEntityControler> con: order){
			int get = con.getValue().onReceiveEnergy((int)Math.floor(con.getKey()*ratio),simulate);
			energyConsumed += get;
			if(!simulate)
				controlersOutput.get(con.getValue()).setValue(controlersOutput.get(con.getValue()).getValue()-get);
		}
		lock = false;
		return energyConsumed;
	}
	
	public FluidTankInfo[] getCoolantTankInfo(){
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	public int onFillCoolant(FluidStack fluid){
		int ratio = (int)((float)fluid.amount/(float)connectedParts.size());
		int get = 0;
		for(IMultiblockPart c:connectedParts){
			get += ((TileEntityBase)c).addCoolant(ratio);
		}
		tank.setFluid(new FluidStack(SuperConductorMod.coolantFluid, tank.getFluidAmount() + get));
		return get;
	}

	public void renderUpdate(){
		if(WorldUtils.isServerWorld(worldObj)) return;
		for(IMultiblockPart p: connectedParts)
			p.getWorldObj().func_147479_m(p.xCoord,p.yCoord,p.zCoord);
	}
	
	@Override
	protected void isMachineWhole() throws MultiblockValidationException {
		if(controlersInput.size() + controlersOutput.size() <2){
			active=false;
			renderUpdate();
			throw new MultiblockValidationException("Wire must have minimum 2 controlers");
		}
		
	}

	@Override
	protected void onAssimilate(MultiblockControllerBase arg0) {
		validateAllControlers();
	}

	@Override
	protected void onAssimilated(MultiblockControllerBase arg0) {
		validateAllControlers();
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart arg0,NBTTagCompound arg1) {
		readFromNBT(arg1);
		validateAllControlers();
	}

	@Override
	protected void onBlockAdded(IMultiblockPart arg0) {
		if(arg0 instanceof TileEntityControler){
			if(((TileEntityControler) arg0).isOutput())
				controlersOutput.put((TileEntityControler) arg0,new Pair<Integer, Integer>(0,0));
			else
				controlersInput.add((TileEntityControler) arg0);
		}
		tank.setCapacity(tank.getCapacity() + ((TileEntityBase)arg0).getCoolantCapacity());
		tank.setFluid(new FluidStack(SuperConductorMod.coolantFluid,((TileEntityBase)arg0).getCoolantAmount() + tank.getFluidAmount()));
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart arg0) {
		if(arg0 instanceof TileEntityControler){
			controlersInput.remove(arg0);
			controlersOutput.remove(arg0);
		}
		tank.setCapacity(tank.getCapacity() - ((TileEntityBase)arg0).getCoolantCapacity());
		tank.setFluid(new FluidStack(SuperConductorMod.coolantFluid, tank.getFluidAmount()-((TileEntityBase)arg0).getCoolantAmount()));
	}

	@Override
	protected void onMachineAssembled() {
		validateAllControlers();
	}

	@Override
	protected void onMachineDisassembled() {
		active = false;
		renderUpdate();
	}

	@Override
	protected void onMachineRestored() {
		worldObj.markBlockForUpdate(getReferenceCoord().x, getReferenceCoord().y,getReferenceCoord().z);
		validateAllControlers();
	}
	
	@Override
	protected void updateClient() {
	
	}

	private void taskDrain(){
		int drain =0;
		for(IMultiblockPart c: connectedParts){
			drain +=((TileEntityBase)c).drainCoolant(1);
		}
		tank.drain(drain, true);
	}
	
	@SuppressWarnings("rawtypes")
	public Packet getUpdatePacket(TileEntityControler c){
	     return new PacketUpdateFluidAmount(c, tank.getFluidAmount(), ((tank.getFluid()!=null)?tank.getFluid().getFluidID():0));
	}
	
	@Override
	protected boolean updateServer() {
		if(ticksFromLastDrain%10==0){
			for(TileEntityControler c : controlersInput)
				c.updateGUI();
			for(Entry<TileEntityControler, Pair<Integer, Integer>> c : controlersOutput.entrySet())
				c.getKey().updateGUI();
		}
		
		if(ticksFromLastDrain >= 2*12*60*60*20/1000){
			taskDrain();
			ticksFromLastDrain =0;
		}
		ticksFromLastDrain++;
		ntick++;
		boolean newactive = (float)tank.getFluidAmount()/(float)tank.getCapacity()>=0.5;
		if(newactive != active){
			active = newactive;
			worldObj.markBlockForUpdate(getReferenceCoord().x, getReferenceCoord().y,getReferenceCoord().z);
			validateAllControlers();
			renderUpdate();
		}
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound arg0) {
		tank.writeToNBT(arg0);
		arg0.setInteger("ticksFromLastDrain", ticksFromLastDrain);
		arg0.setBoolean("activMachine", active);
	}

	@Override
	public void readFromNBT(NBTTagCompound arg0) {
		tank = tank.readFromNBT(arg0);	
		if(arg0.hasKey("ticksFromLastDrain"))
			ticksFromLastDrain = arg0.getInteger("ticksFromLastDrain");
		if(arg0.hasKey("activMachine"))
			active = arg0.getBoolean("activMachine");
	}
	
	@Override
	public void decodeDescriptionPacket(NBTTagCompound arg0) {
		tank.readFromNBT(arg0);	
		boolean lactive = arg0.getBoolean("activMachine");
		if(lactive != active){
			active = lactive;
			renderUpdate();
			validateAllControlers();
		}
	}	

	@Override
	public void formatDescriptionPacket(NBTTagCompound arg0) {
		tank.writeToNBT(arg0);
		arg0.setBoolean("activMachine", active);
	}
	
	@Override
	protected int getMaximumXSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected int getMaximumYSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected int getMaximumZSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 2;
	}
	
	@Override
	protected void onMachinePaused() {

	}
}
