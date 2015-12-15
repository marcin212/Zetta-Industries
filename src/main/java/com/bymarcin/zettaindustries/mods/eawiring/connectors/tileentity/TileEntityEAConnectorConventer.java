package com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity;

import java.io.DataInputStream;
import java.io.IOException;

import com.bymarcin.zettaindustries.mods.eawiring.connectors.gui.ConventerGui;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.node.ConnectorNode;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.wires.WireBase;

import mods.eln.Other;
import mods.eln.misc.Direction;
import mods.eln.misc.LRDU;
import mods.eln.node.simple.SimpleNode;
import mods.eln.simplenode.energyconverter.EnergyConverterElnToOtherGui;
import mods.eln.simplenode.energyconverter.EnergyConverterElnToOtherNode;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;

public class TileEntityEAConnectorConventer extends TileEntityConnectorBase implements IEnergyHandler, IEnergyProvider{
	WireType limit = null;
    public float inPowerFactor;
    public boolean hasChanges = false;

    
    @Override
    public boolean onBlockActivated(EntityPlayer entityPlayer, Direction side, float vx, float vy, float vz) {
    	if(!worldObj.isRemote && getNode() != null)
    		getNode().setNeedPublish(true);
    	return super.onBlockActivated(entityPlayer, side, vx, vy, vz);
    }
    
	// *************** RF **************
	@Override
	@Optional.Method(modid = Other.modIdTe)
	public boolean canConnectEnergy(ForgeDirection from) {
		if (worldObj.isRemote)
			return false;
		if (getNode() == null)
			return false;
		return getFacing() == Direction.from(from).getInt();
	}

	@Override
	@Optional.Method(modid = Other.modIdTe)
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	@Optional.Method(modid = Other.modIdTe)
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (worldObj.isRemote)
			return 0;
		if (getNode() == null)
			return 0;
		EnergyConverterElnToOtherNode node = (EnergyConverterElnToOtherNode) getNode();
        int extract = (int) Math.min(maxExtract, node.getOtherModEnergyBuffer(Other.getElnToTeConversionRatio()));
		if (!simulate)
			node.drawEnergy(extract, Other.getElnToTeConversionRatio());

		return extract;
	}

	@Override
	@Optional.Method(modid = Other.modIdTe)
	public int getEnergyStored(ForgeDirection from) {
		return 0;
	}

	@Override
	@Optional.Method(modid = Other.modIdTe)
	public int getMaxEnergyStored(ForgeDirection from) {
		return 0;
	}

	// ***************** Bridges ****************

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (getWorldObj().isRemote) return;
		if (getNode() == null) return;

		ConnectorNode node = (ConnectorNode) getNode();
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[getFacing()];
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);

        if(tileEntity == null || !(tileEntity instanceof IEnergyHandler)) return;
		IEnergyHandler energyHandler = (IEnergyHandler)tileEntity;
		
		double pMax = node.getOtherModEnergyBuffer(Other.getElnToTeConversionRatio());
		node.drawEnergy(energyHandler.receiveEnergy(dir.getOpposite(), (int) pMax, false), Other.getElnToTeConversionRatio());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen newGuiDraw(Direction side, EntityPlayer player) {
		return new ConventerGui(player, this);
	}

	public LRDU lrdu = LRDU.Left;
	@Override
	public void serverPublishUnserialize(DataInputStream stream) {
		super.serverPublishUnserialize(stream);
		try {
			inPowerFactor = stream.readFloat();
			hasChanges = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean canConnectCable(WireType wt, TargetingInfo ti) {
		if(wt instanceof WireBase)
			return limit == null;
		return false;
	}

	@Override
	public WireType getCableLimiter(TargetingInfo arg0) {
		return limit;
	}

	@Override
	public Vec3 getRaytraceOffset(IImmersiveConnectable link)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		return Vec3.createVectorHelper(.5+fd.offsetX*.3125, .5+fd.offsetY*.3125, .5+fd.offsetZ*.3125);
	}
	@Override
	public Vec3 getConnectionOffset(Connection con)
	{
		ForgeDirection fd = ForgeDirection.getOrientation(facing).getOpposite();
		double conRadius = con.cableType.getRenderDiameter()/2;
		return Vec3.createVectorHelper(.5+fd.offsetX*(.25-conRadius), .5+fd.offsetY*(.25-conRadius), .5+fd.offsetZ*(.25-conRadius));
	}

	@Override
	public ForgeDirection[] getValidEAConnectionSide() {
		return new ForgeDirection[0];
	}

}
