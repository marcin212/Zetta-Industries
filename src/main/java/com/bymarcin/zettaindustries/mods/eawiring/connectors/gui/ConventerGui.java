package com.bymarcin.zettaindustries.mods.eawiring.connectors.gui;

import com.bymarcin.zettaindustries.mods.eawiring.connectors.node.ConnectorNode;
import com.bymarcin.zettaindustries.mods.eawiring.connectors.tileentity.TileEntityEAConnectorConventer;

import mods.eln.gui.GuiHelper;
import mods.eln.gui.GuiScreenEln;
import mods.eln.gui.GuiVerticalTrackBar;
import mods.eln.gui.IGuiObject;

import net.minecraft.entity.player.EntityPlayer;

public class ConventerGui extends GuiScreenEln{
	TileEntityEAConnectorConventer render;
    GuiVerticalTrackBar voltage;

	public ConventerGui(EntityPlayer player, TileEntityEAConnectorConventer render) {
		this.render = render;
	}

	@Override
	public void initGui() {
		super.initGui();

		voltage = newGuiVerticalTrackBar(6, 6 + 2, 20, 50);
		voltage.setStepIdMax((int)100);
		voltage.setEnable(true);
    	voltage.setRange(0f, 1f);

    	syncVoltage();
	}
	
    public void syncVoltage() {
    	voltage.setValue(render.inPowerFactor);
    	render.hasChanges = false;
    }

    @Override
    public void guiObjectEvent(IGuiObject object) {
    	super.guiObjectEvent(object);
    	if (object == voltage) {
    		render.sender.clientSendFloat(ConnectorNode.setInPowerFactor, voltage.getValue());
    	}
    }

    @Override
    protected void preDraw(float f, int x, int y) {
    	super.preDraw(f, x, y);
    	if (render.hasChanges) syncVoltage();
    	voltage.setComment(0, "Input power is limited to  " + (int)(voltage.getValue() * ConnectorNode.inPowerMax) + " W");
    }

	@Override
	protected GuiHelper newHelper() {
		return new GuiHelper(this, 12 + 20, 12 + 50 + 4);
	}
}
