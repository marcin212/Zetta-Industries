package com.bymarcin.zettaindustries.mods.nfc.tileentity;

import com.bymarcin.zettaindustries.mods.nfc.block.BlockNFCProgrammer;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityNFCProgrammer extends TileEntity implements SimpleComponent {
    public byte[] NFCData = null;

    @Override
    public String getComponentName() {
        return "NFCProgrammer";
    }

    @Callback
    public Object[] writeNFCData(Context contex, Arguments args) {
        if (args.count() == 1 && args.checkByteArray(0).length <= 2048) {
            NFCData = args.checkByteArray(0);
            worldObj.setBlockState(pos, worldObj.getBlockState(getPos()).withProperty(BlockNFCProgrammer.STATUS, true), 2);
            worldObj.notifyNeighborsOfStateChange(pos, worldObj.getBlockState(getPos()).getBlock());
        } else {
            return new Object[]{false, "No arguments or data is bigger than 2048 characters."};
        }
        return new Object[]{true};
    }

    @Callback
    public Object[] clearNFCData(Context contex, Arguments args) {
        NFCData = null;
        worldObj.setBlockState(pos, worldObj.getBlockState(getPos()).withProperty(BlockNFCProgrammer.STATUS, false), 2);
        worldObj.notifyNeighborsOfStateChange(pos, worldObj.getBlockState(getPos()).getBlock());
        return null;
    }

    @Callback
    public Object[] isDataWaiting(Context contex, Arguments args) {
        return new Object[]{(NFCData != null)};
    }

    public byte[] writeCardNFC() {
        byte[] temp = NFCData;
        NFCData = null;
        return temp;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        if (NFCData != null) {
            nbt.setByteArray("NFCDataByte", NFCData);
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        //compat with old version
        if (nbt.hasKey("NFCData")) {
            NFCData = nbt.getString("NFCData").getBytes();
        }

        if (nbt.hasKey("NFCDataByte")) {
            NFCData = nbt.getByteArray("NFCDataByte");
        }
    }

}
