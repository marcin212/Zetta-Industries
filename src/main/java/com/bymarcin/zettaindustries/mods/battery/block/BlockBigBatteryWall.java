package com.bymarcin.zettaindustries.mods.battery.block;

import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.IMultiblockPart;
import com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock.MultiblockControllerBase;
import com.bymarcin.zettaindustries.mods.battery.tileentity.TileEntityWall;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class BlockBigBatteryWall extends BasicBlockMultiblockBase {
    public static final PropertyEnum<ConnectedTexture> SIDE = PropertyEnum.create("side", ConnectedTexture.class);

    public enum ConnectedTexture implements IStringSerializable{
        CASING_METADATA_BASE,
        CASING_CORNER ,
        CASING_CENTER,
        CASING_VERTICAL,
        CASING_EASTWEST,
        CASING_NORTHSOUTH;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
    }



    public BlockBigBatteryWall() {
        super("batterywall");
        info.add(localize("tooltip.validfor") + " " + localize("tooltip.bottom") + ", " + localize("tooltip.top") +
                ", " + localize("tooltip.sides") + ", " + localize("tooltip.frame"));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SIDE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SIDE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(SIDE, ConnectedTexture.values()[meta]);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityWall();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) return false;
        if (player.isSneaking()) {
            return false;
        }

        if (!world.isRemote) {
            if (player.getHeldItemMainhand().isEmpty()) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof IMultiblockPart) {
                    MultiblockControllerBase controller = ((IMultiblockPart) te).getMultiblockController();
                    if (controller != null) {
                        Exception e = controller.getLastValidationException();
                        if (e != null) {
                            player.sendMessage(new TextComponentString(e.getMessage()));
                            return true;
                        }
                    } else {
                        player.sendMessage(new TextComponentString("Block is not connected to a battery. This could be due to lag, or a bug. If the problem persists, try breaking and re-placing the block."));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
