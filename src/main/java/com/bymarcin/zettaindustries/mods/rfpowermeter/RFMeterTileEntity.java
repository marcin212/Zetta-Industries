package com.bymarcin.zettaindustries.mods.rfpowermeter;

import com.bymarcin.zettaindustries.registry.ZIRegistry;
import com.bymarcin.zettaindustries.utils.Avg;
import com.bymarcin.zettaindustries.utils.MathUtils;
import com.bymarcin.zettaindustries.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class RFMeterTileEntity extends TileEntity implements ITickable {
    int transfer = 0;//curent flow in RF/t
    int transferLimit = -1;
    long value = 0;//current used energy
    long lastValue = 0;
    Avg avg = new Avg();
    String name = "";
    String password = "";
    boolean inCounterMode = true;
    boolean isOn = true;
    boolean isProtected = false;
    boolean redstone = false;

    int tick = 0;
    public int color = EnumDyeColor.LIME.ordinal();
    ForgeEnergy up;
    ForgeEnergy down;
    
    public RFMeterTileEntity() {
        this.up = new ForgeEnergy(this, EnumFacing.UP);
        this.down = new ForgeEnergy(this, EnumFacing.DOWN);
    }

    class ForgeEnergy implements IEnergyStorage {
        RFMeterTileEntity tileEntity;
        EnumFacing face;

        public ForgeEnergy(RFMeterTileEntity tileEntity, EnumFacing face) {
            this.tileEntity = tileEntity;
            this.face = face;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return tileEntity.receiveEnergy(face, maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return tileEntity.extractEnergy(face, maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return tileEntity.getEnergyStored(face);
        }

        @Override
        public int getMaxEnergyStored() {
            return tileEntity.getMaxEnergyStored(face);
        }

        @Override
        public boolean canExtract() {
            return tileEntity.canConnectEnergy(face, false);
        }

        @Override
        public boolean canReceive() {
            return tileEntity.canConnectEnergy(face, true);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN)) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            if (facing == EnumFacing.UP) {
                return (T) up;
            } else if (facing == EnumFacing.DOWN) {
                return (T) down;
            }
        }

        return super.getCapability(capability, facing);
    }

    protected boolean canConnectEnergy(EnumFacing from, boolean receive) {
        return (receive ^ isInverted()) ? (from == EnumFacing.UP) : (from == EnumFacing.DOWN);
    }

    protected int getEnergyStored(EnumFacing from) {
        return 0;
    }

    protected int getMaxEnergyStored(EnumFacing from) {
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

    public void invert() {
        IBlockState state = getWorld().getBlockState(getPos());
        if (state.getBlock() instanceof RFMeterBlock) {
            getWorld().setBlockState(getPos(), state.withProperty(RFMeterBlock.inverted, !state.getValue(RFMeterBlock.inverted)), 3);
        }
    }

    public boolean isInverted() {
        return (getBlockMetadata() & 1) != 0;
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
        return !isProtected || (isProtected && pass != null && MathUtils.encryptPassword(pass).equals(password));
    }


    public boolean canEnergyFlow() {
        return isOn && (inCounterMode || (0 < value)) && !redstone;
    }
    
    private int checkRedstone(){
        EnumFacing front = world.getBlockState(getPos()).getValue(RFMeterBlock.front).getOpposite();
        return world.getRedstonePower(getPos().offset(front), front);
    }
    
    void updateRedstone(){
        redstone = 0!=checkRedstone();
    }


    public void onPacket(long value, int transfer, boolean inCounterMode) {
        if (WorldUtils.isServerWorld(getWorld())) return;
        this.value = value;
        this.transfer = transfer;
        this.inCounterMode = inCounterMode;
    }

    @Override
    public void update() {
        tick++;
        if (WorldUtils.isServerWorld(getWorld())) {
            //tick++;
            if (tick % 20 == 0) {
                ZIRegistry.packetHandler.sendToAllAround(new RFMeterUpdatePacket(this, value, transfer, inCounterMode),
                        new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, 32));
                tick = 0;
            }
            long lastRecive = Math.abs(value - lastValue);
            avg.putValue(lastRecive);
            transfer = (int) avg.getAvg();
            lastValue = value;
        } else {
            if (inCounterMode)
                value += transfer;
            else
                value -= transfer;
        }
    }

    protected int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (!canEnergyFlow()) return 0;
        int temp = 0;
        if (from == (isInverted() ? EnumFacing.DOWN : EnumFacing.UP)) {
            IEnergyStorage storage = WorldUtils.getEnergyStorage(getWorld(), this.getPos(), isInverted() ? EnumFacing.UP : EnumFacing.DOWN);
            if(storage!=null){
                temp = storage.receiveEnergy(transferLimit == -1 ?
                                (inCounterMode ? maxReceive : Math.min((int) value, maxReceive))
                                : Math.min(transferLimit, (inCounterMode ? maxReceive : Math.min((int) value, maxReceive)))
                        , simulate);

                if (!simulate) if (inCounterMode) value += temp;
                else value -= temp;
                return temp;
            }
        }
        return 0;
    }

    protected int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
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

        nbt.setBoolean("redstone", redstone);
        return nbt;
    }

    public void getTag(NBTTagCompound nbt) {
        nbt.setInteger("transferLimit", transferLimit);

        nbt.setLong("value", value);
        nbt.setLong("lastValue", lastValue);

        nbt.setString("name", name);
        nbt.setString("password", password);

        nbt.setBoolean("inCounterMode", inCounterMode);
        nbt.setBoolean("isOn", isOn);
        nbt.setBoolean("isProtected", isProtected);

        nbt.setInteger("color", color);

        nbt.setBoolean("redstone", redstone);
    }

    public void setTag(NBTTagCompound nbt) {
        if (nbt.hasKey("transferLimit"))
            transferLimit = nbt.getInteger("transferLimit");
        if (nbt.hasKey("value"))
            value = nbt.getLong("value");
        if (nbt.hasKey("lastValue"))
            lastValue = nbt.getLong("lastValue");
        if (nbt.hasKey("name"))
            name = nbt.getString("name");
        if (nbt.hasKey("password"))
            password = nbt.getString("password");
        if (nbt.hasKey("inCounterMode"))
            inCounterMode = nbt.getBoolean("inCounterMode");
        if (nbt.hasKey("isOn"))
            isOn = nbt.getBoolean("isOn");
        if (nbt.hasKey("isProtected"))
            isProtected = nbt.getBoolean("isProtected");
        if (nbt.hasKey("color"))
            color = nbt.getInteger("color");
        if(nbt.hasKey("redstone")){
            redstone = nbt.getBoolean("redstone");
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("transfer"))
            transfer = nbt.getInteger("transfer");
        if (nbt.hasKey("transferLimit"))
            transferLimit = nbt.getInteger("transferLimit");
        if (nbt.hasKey("value"))
            value = nbt.getLong("value");
        if (nbt.hasKey("lastValue"))
            lastValue = nbt.getLong("lastValue");
        if (nbt.hasKey("name"))
            name = nbt.getString("name");
        if (nbt.hasKey("password"))
            password = nbt.getString("password");
        if (nbt.hasKey("inCounterMode"))
            inCounterMode = nbt.getBoolean("inCounterMode");
        if (nbt.hasKey("isOn"))
            isOn = nbt.getBoolean("isOn");
        if (nbt.hasKey("isProtected"))
            isProtected = nbt.getBoolean("isProtected");
        if (nbt.hasKey("tick"))
            tick = nbt.getInteger("tick");
        if (nbt.hasKey("color"))
            color = nbt.getInteger("color");
        if(nbt.hasKey("redstone"))
            redstone = nbt.getBoolean("redstone");
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return newSate.getBlock() != oldState.getBlock();
    }
}
