package com.bymarcin.zettaindustries.mods.ocwires;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;


public class TelecommunicationWireType extends WireType {

	public static TelecommunicationWireType TELECOMMUNICATION = new TelecommunicationWireType();
	
	private TelecommunicationWireType() {}
	
	public static void register(){
		getValues().add(TELECOMMUNICATION);
	}

	@Override
	public double getLossRatio() {
		return 0;
	}

	@Override
	public int getMaxLength() {
		return OCWires.cableLength;
	}

	@Override
	public int getTransferRate() {
		return 0;
	}

	@Override
	public String getUniqueName() {
		return ZettaIndustries.MODID + ":telecommunication";
	}

	@Override
	public ItemStack getWireCoil() {
		return new ItemStack(OCWires.wire);
	}

	@Override
	public double getRenderDiameter() {
		return .0625;
	}

	@Override
	public boolean isEnergyWire() {
		return false;
	}

	@Override
	public int getColour(ImmersiveNetHandler.Connection arg0) {
		return 1318204;
	}

	@Override
	public double getSlack() {
		return 1.005;
	}

	@Override
	public TextureAtlasSprite getIcon(ImmersiveNetHandler.Connection arg0) {
		return iconDefaultWire;
	}

}
