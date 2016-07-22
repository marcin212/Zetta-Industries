package com.bymarcin.zettaindustries.mods.battery.test;

import forestry.api.multiblock.IMultiblockComponent;
import forestry.api.multiblock.IMultiblockController;

import javax.annotation.Nonnull;
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

    @Nonnull
    @Override
    public Collection<IMultiblockComponent> getComponents() {
        return null;
    }
}
