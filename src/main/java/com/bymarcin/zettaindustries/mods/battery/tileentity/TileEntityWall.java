package com.bymarcin.zettaindustries.mods.battery.tileentity;

import com.bymarcin.zettaindustries.mods.battery.block.BlockBigBatteryWall;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.common.CoordTriplet;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockValidationException;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class TileEntityWall extends BasicRectangularMultiblockTileEntityBase {

    @Override
    public void onMachineBroken() {
        super.onMachineBroken();
        if (this.worldObj.isRemote) return;
        getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE ,BlockBigBatteryWall.ConnectedTexture.CASING_METADATA_BASE), 2);
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }

    @Override
    public void isGoodForFrame() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForSides() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForTop() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForBottom() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForInterior() throws MultiblockValidationException {
        throw new MultiblockValidationException(
                String.format(
                        "%d, %d, %d - Wall may not be placed in the battery's interior",
                        this.getPos().getX(),
                        this.getPos().getY(),
                        this.getPos().getZ()));
    }

    @Override
    public void onMachineAssembled(MultiblockControllerBase controller) {
        super.onMachineAssembled(controller);
        if (this.worldObj.isRemote) return;
        if (controller == null) {
            throw new IllegalArgumentException(
                    "Being assembled into a null controller. This should never happen. Please report this stacktrace.");
        }

        if (this.getMultiblockController() == null) {
            FMLLog.warning(
                    "Battery part at (%d, %d, %d) is being assembled without being attached to a battery. Attempting to auto-heal. Fully destroying and re-building this reactor is recommended if errors persist.",
                    getPos().getX(), getPos().getY(), getPos().getZ());
            this.onAttached(controller);
        }
        this.setCasingMetadataBasedOnWorldPosition();

    }

    private void setCasingMetadataBasedOnWorldPosition() {
        MultiblockControllerBase controller = this.getMultiblockController();
        assert (controller != null);
        CoordTriplet minCoord = controller.getMinimumCoord();
        CoordTriplet maxCoord = controller.getMaximumCoord();

        int extremes = 0;
        @SuppressWarnings("unused")
        boolean xExtreme, yExtreme, zExtreme;
        xExtreme = yExtreme = zExtreme = false;

        if (getPos().getX() == minCoord.x) {
            extremes++;
            xExtreme = true;
        }
        if (getPos().getY() == minCoord.y) {
            extremes++;
            yExtreme = true;
        }
        if (getPos().getZ() == minCoord.z) {
            extremes++;
            zExtreme = true;
        }

        if (getPos().getX() == maxCoord.x) {
            extremes++;
            xExtreme = true;
        }
        if (getPos().getY() == maxCoord.y) {
            extremes++;
            yExtreme = true;
        }
        if (getPos().getZ() == maxCoord.z) {
            extremes++;
            zExtreme = true;
        }

        if (extremes == 3) {
            // Corner
            getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE , BlockBigBatteryWall.ConnectedTexture.CASING_CORNER), 2);
        } else if (extremes == 2) {
            if (!xExtreme) {
                 //Y/Z - must be east/west
                getWorld().setBlockState(getPos(),
                        getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE ,BlockBigBatteryWall.ConnectedTexture.CASING_EASTWEST), 2);
            } else if (!zExtreme) {
                // X/Y - must be north-south
                getWorld().setBlockState(getPos(),
                        getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE ,BlockBigBatteryWall.ConnectedTexture.CASING_NORTHSOUTH), 2);
            } else {
                // Not a y-extreme, must be vertical
                getWorld().setBlockState(getPos(),
                        getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE ,BlockBigBatteryWall.ConnectedTexture.CASING_VERTICAL), 2);
            }
        } else if (extremes == 1) {
            getWorld().setBlockState(getPos(),
                    getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE ,BlockBigBatteryWall.ConnectedTexture.CASING_CENTER), 2);
        } else {
            // This shouldn't happen.
            getWorld().setBlockState(getPos(),
                    getWorld().getBlockState(getPos()).withProperty(BlockBigBatteryWall.SIDE ,BlockBigBatteryWall.ConnectedTexture.CASING_METADATA_BASE), 2);
        }
    }

}
