package com.bymarcin.zettaindustries.mods.nfc.block;

import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCReader;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockNFCReader extends BasicBlockContainer {

    public BlockNFCReader() {
        super(Material.IRON, "nfcreader");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityNFCReader();
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) return false;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityNFCReader) {
            if (!player.getHeldItemMainhand().isEmpty()) {
                if (player.getHeldItemMainhand().getItem() instanceof ItemPrivateCardNFC) {
                    if (player.getName().equals(ItemPrivateCardNFC.getOwner(player.getHeldItemMainhand()))) {
                        ((TileEntityNFCReader) tile).sendEvent(player.getName(), ItemPrivateCardNFC.getNFCData(player.getHeldItemMainhand()));
                        return true;
                    }
                    return false;
                } else if (player.getHeldItemMainhand().getItem() instanceof ItemCardNFC) {
                    ((TileEntityNFCReader) tile).sendEvent(player.getName(), ItemCardNFC.getNFCData(player.getHeldItemMainhand()));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
