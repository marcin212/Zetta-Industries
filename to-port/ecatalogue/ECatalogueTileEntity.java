package com.bymarcin.zettaindustries.mods.ecatalogue;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

import net.minecraft.tileentity.TileEntity;

import forestry.api.mail.IMailAddress;
import forestry.api.mail.IPostOffice;
import forestry.api.mail.PostManager;

import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class ECatalogueTileEntity extends TileEntity implements SimpleComponent {
	private boolean isAddressSet;
	private IMailAddress address;

	@Override
	public String getComponentName() {
		return "ecatalogue";
	}

	public boolean isAddressSet() {
		return isAddressSet;
	}

	public void setAddressSet(boolean isAddressSet) {
		this.isAddressSet = isAddressSet;
	}

	public IMailAddress getAddress() {
		return address;
	}

	@Callback
	public Object[] getAllTrades(Context context, Arguments args) {
		IPostOffice postOffice = PostManager.postRegistry.getPostOffice(worldObj);
		return new Object[] { postOffice.getActiveTradeStations(worldObj).values() };
	}

	@Callback
	public Object[] setMailAddress(Context context, Arguments args) {
		String address = args.checkString(0);
		IMailAddress addr = PostManager.postRegistry.getMailAddress(address);
	
		if (PostManager.postRegistry.isValidTradeAddress(worldObj, addr)) {
				this.address = addr;
				setAddressSet(true);
				return null;
		} else {
			return new Object[] { null, "Address is Invalid." };
		}
	}

}
