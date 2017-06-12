package com.bymarcin.zettaindustries.mods.battery.test;

import forestry.api.multiblock.IMultiblockComponent;
import forestry.api.multiblock.IMultiblockController;
import forestry.api.multiblock.IMultiblockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Marcin on 24.06.2016.
 */
public class TestLogic implements IMultiblockLogic{
    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public IMultiblockController getController() {
        return null;
    }

    @Override
    public void validate(World world, IMultiblockComponent part) {

    }

    @Override
    public void invalidate(World world, IMultiblockComponent part) {

    }

    @Override
    public void onChunkUnload(World world, IMultiblockComponent part) {

    }

    @Override
    public void encodeDescriptionPacket(NBTTagCompound packetData) {

    }

    @Override
    public void decodeDescriptionPacket(NBTTagCompound packetData) {

    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        return null;
    }
}
