package com.bymarcin.zettaindustries.mods.nfc.block;


import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicBlockContainer;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.item.ItemPrivateCardNFC;
import com.bymarcin.zettaindustries.mods.nfc.tileentity.TileEntityNFCProgrammer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockNFCProgrammer extends BasicBlockContainer {
    public static final PropertyBool STATUS = PropertyBool.create("status");

    public BlockNFCProgrammer() {
        super(Material.IRON, "nfcprogrammer");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATUS);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STATUS) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(STATUS, meta == 1 ? true : false);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityNFCProgrammer && ((TileEntityNFCProgrammer) tile).NFCData != null) {
            TileEntityNFCProgrammer tilenfc = (TileEntityNFCProgrammer) tile;
            if (player.getHeldItemMainhand() != null) {
                if (player.getHeldItemMainhand().getItem() instanceof ItemPrivateCardNFC) {
                    if (ItemPrivateCardNFC.getOwner(player.getHeldItemMainhand()) != null && player.getName().equals(ItemPrivateCardNFC.getOwner(player.getHeldItemMainhand()))) {
                        ItemPrivateCardNFC.setNFCData(tilenfc.writeCardNFC(), player.getHeldItemMainhand());

                        player.addChatMessage(new TextComponentString(I18n.format(ZettaIndustries.MODID + ".text.nfc.message1")));
                        world.setBlockState(pos, state.withProperty(STATUS, false), 2);
                        world.notifyNeighborsOfStateChange(pos, state.getBlock());
                        return true;
                    } else {
                        player.addChatMessage(new TextComponentString(I18n.format(ZettaIndustries.MODID + ".text.nfc.message2")));
                        return false;
                    }
                } else if (player.getHeldItemMainhand().getItem() instanceof ItemCardNFC) {
                    ItemCardNFC.setNFCData(tilenfc.writeCardNFC(), player.getHeldItemMainhand());
                    player.addChatMessage(new TextComponentString(I18n.format(ZettaIndustries.MODID + ".text.nfc.message1")));
                    world.setBlockState(pos, state.withProperty(STATUS, false), 2);
                    world.notifyNeighborsOfStateChange(pos, state.getBlock());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityNFCProgrammer();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
