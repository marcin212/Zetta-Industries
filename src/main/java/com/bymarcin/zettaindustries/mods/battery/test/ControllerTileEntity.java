package com.bymarcin.zettaindustries.mods.battery.test;

import forestry.api.multiblock.IMultiblockComponent;
import forestry.api.multiblock.IMultiblockController;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by Marcin on 24.06.2016.
 */
public class ControllerTileEntity implements IMultiblockController {
    @Override
    public boolean isAssembled() {
        return false;
    }

    @Override
    public void reassemble() {

    }

    @Override
    public String getLastValidationError() {
        return null;
    }

    @Nullable
    @Override
    public BlockPos getLastValidationErrorPosition() {
        return null;
    }

    @Nonnull
    @Override
    public Collection<IMultiblockComponent> getComponents() {
        return null;
    }
}
