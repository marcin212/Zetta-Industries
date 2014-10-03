package com.bymarcin.zettaindustries.mods.superconductor;

import java.util.ArrayList;

import com.bymarcin.zettaindustries.utils.Pair;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.IMultiblockPart;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityControler;
import com.bymarcin.zettaindustries.mods.superconductor.tileentity.TileEntityWire;
import com.bymarcin.zettaindustries.utils.WorldUtils;


public class SuperConductor extends MultiblockControllerBase{
	
	ArrayList<TileEntityControler> controlersInput;
	ArrayList<TileEntityControler> controlersOutput;
	
	FluidTank tank = new FluidTank(0);
	int ticksFromLastDrain = 0;
	public boolean active = false;
	private boolean lock = false;
	
	
	
	public SuperConductor(World world) {
		super(world);
		controlersInput= new ArrayList<TileEntityControler>();
		controlersOutput= new ArrayList<TileEntityControler>();
	}

	public FluidTank getTank() {
		return tank;
	}
	
	public void validateAllControlers(){
		lock=false;
		controlersInput.clear();
		controlersOutput.clear();
		for(IMultiblockPart c : connectedParts){
			if(c instanceof TileEntityWire) continue;
			if(((TileEntityControler) c).isOutput()){
				controlersOutput.add((TileEntityControler) c);
			}else{
				controlersInput.add((TileEntityControler) c);
			}
		}
	}

	public int onReciveEnergy(int maxRecive, boolean simulate){
		if(lock) return 0;
		lock = true;
		
		if(!active) return 0;
		ArrayList<Pair<Integer,TileEntityControler>> order = new ArrayList<Pair<Integer,TileEntityControler>>();
		int tempOrder;
		long requiredEnergy = 0;
		double ratio = 0;
		int energyConsumed=0;
		
		for(TileEntityControler c:controlersOutput){
			tempOrder = c.onReciveEnergy(Integer.MAX_VALUE, true);
			order.add(new Pair<Integer,TileEntityControler>(tempOrder,c));
			requiredEnergy +=tempOrder;
		}
		
		ratio = (double)maxRecive/(double)requiredEnergy;
		
		for(Pair<Integer, TileEntityControler> con: order){
			energyConsumed += con.getValue().onReciveEnergy((int)Math.floor(con.getKey()*ratio),simulate);
		}
		lock = false;
		return energyConsumed;
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
	
	public void renderUpdate(){
		if(WorldUtils.isServerWorld(worldObj)) return;
		for(IMultiblockPart p: connectedParts)
			p.getWorldObj().func_147479_m(p.xCoord,p.yCoord,p.zCoord);
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
		return 3;
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
		((SuperConductor) arg0).getTank().fill(tank.getFluid(), true);
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
				controlersOutput.add((TileEntityControler) arg0);
			else
				controlersInput.add((TileEntityControler) arg0);
		}
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart arg0) {
		if(arg0 instanceof TileEntityControler){
			controlersInput.remove(arg0);
			controlersOutput.remove(arg0);
		}
	}

	@Override
	protected void onMachineAssembled() {
		tank.setCapacity(connectedParts.size()*4000);
		if(tank.getFluidAmount()>tank.getCapacity()){
			tank.getFluid().amount = tank.getCapacity();
		}
		validateAllControlers();
	}

	@Override
	protected void onMachineDisassembled() {
		active = false;
		renderUpdate();
	}

	@Override
	protected void onMachinePaused() {
		validateAllControlers();
	}

	@Override
	protected void onMachineRestored() {
		worldObj.markBlockForUpdate(getReferenceCoord().x, getReferenceCoord().y,getReferenceCoord().z);
		tank.setCapacity(connectedParts.size()*4000);
		if(tank.getFluidAmount()>tank.getCapacity()){
			tank.getFluid().amount = tank.getCapacity();
		}
		validateAllControlers();
	}
	
	@Override
	protected void updateClient() {

			
	}

	@Override
	protected boolean updateServer() {
		if(ticksFromLastDrain%10==0){
			for(TileEntityControler c : controlersInput)
				c.updateTick();
			for(TileEntityControler c : controlersOutput)
				c.updateTick();
		}
		
		if(ticksFromLastDrain>= 12*60*60*20/1000){
			tank.drain(1, true);
			ticksFromLastDrain =0;
		}
		ticksFromLastDrain++;
		
		boolean newactive = (float)tank.getFluidAmount()/(float)tank.getCapacity()>=0.5;
		if(newactive != active){
			active = newactive;
			worldObj.markBlockForUpdate(getReferenceCoord().x, getReferenceCoord().y,getReferenceCoord().z);
			renderUpdate();
			validateAllControlers();
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

}
