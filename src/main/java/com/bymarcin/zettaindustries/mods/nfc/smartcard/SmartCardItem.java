package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.mods.nfc.NFC;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class SmartCardItem extends Item implements DriverItem, IDyeableItem {
    public static final String PRIVATE_KEY = "sc:private_key";
    public static final String PUBLIC_KEY = "sc:public_key";
    public static final String UUID_KEY = "sc:uuid_key";
    public static final String OWNER_UUID = "sc:owner_uuid";
    public static final String OWNER_UUID_LEAST = "sc:owner_uuidLeast";
    public static SecureRandom srand;

    public SmartCardItem() {
        try {
            srand = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        setCreativeTab(ZettaIndustries.tabZettaIndustries);
        setUnlocalizedName("smartcard");
        setRegistryName("smartcard");
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World p, List list, ITooltipFlag q) {
        super.addInformation(stack, p, list, q);

        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            if (tag.hasKey(OWNER_UUID_LEAST)) {
                list.add("Protected");
            }
            if (tag.hasKey(UUID_KEY)) {
                list.add(tag.getString(UUID_KEY));
            }
        }
    }

    public static NBTTagCompound generateKeyPair() {
        NBTTagCompound tag = new NBTTagCompound();
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(384, srand);
            KeyPair kp = kpg.generateKeyPair();
            tag.setByteArray(PRIVATE_KEY, kp.getPrivate().getEncoded());
            tag.setByteArray(PUBLIC_KEY, kp.getPublic().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tag;
    }

    private static void saveNewUUID(NBTTagCompound tag) {
        String uuid = UUID.randomUUID().toString();
        NBTTagCompound keys = generateKeyPair();
        Path filePub = NFC.saveDirParent.toPath().resolve(uuid + ".pub");
        Path filePriv = NFC.saveDirParent.toPath().resolve(uuid + ".prv");
        try {
            Files.write(filePub, keys.getByteArray(PUBLIC_KEY), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            Files.write(filePriv, keys.getByteArray(PRIVATE_KEY), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            tag.setString(UUID_KEY, uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static NBTTagCompound getNBT(ItemStack stack) {
        if (stack.hasTagCompound()) {
            if (!stack.getTagCompound().hasKey(UUID_KEY)) {
                saveNewUUID(stack.getTagCompound());
            }
            return stack.getTagCompound();
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            saveNewUUID(tag);
            stack.setTagCompound(tag);
            return tag;
        }
    }

    public static byte[] getPrivateKey(ItemStack stack) {
        String uuid = getNBT(stack).getString(UUID_KEY);
        try {
            return Files.readAllBytes(NFC.saveDirParent.toPath().resolve(uuid + ".prv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] getPublicKey(ItemStack stack) {
        String uuid = getNBT(stack).getString(UUID_KEY);
        try {
            return Files.readAllBytes(NFC.saveDirParent.toPath().resolve(uuid + ".pub"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Nullable
    public static UUID getOwner(ItemStack stack) {
        if (getNBT(stack).hasKey("sc:owner")) {
            UUID uuid = NFC.getUUIDForPlayer(getNBT(stack).getString("sc:owner"));
            if (uuid != null) {
                getNBT(stack).removeTag("sc:owner");
                getNBT(stack).setUniqueId(OWNER_UUID, uuid);
            }
        }

        if (!getNBT(stack).hasKey(OWNER_UUID_LEAST)) {
            return null;
        } else {
            return getNBT(stack).getUniqueId(OWNER_UUID);
        }
    }

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().equals(this);
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        return null;
    }

    @Override
    public String slot(ItemStack stack) {
        return Slot.Any;
    }

    @Override
    public int tier(ItemStack stack) {
        return 0;
    }

    @Override
    public NBTTagCompound dataTag(ItemStack stack) {
        return new NBTTagCompound();
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return getNBT(stack).hasKey("color");
    }

    @Override
    public int getColor(ItemStack stack) {
        return hasColor(stack) ? getNBT(stack).getInteger("color") : -1;
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        getNBT(stack).setInteger("color", color & 0xFFFFFF);
    }

    @Override
    public boolean removeColor(ItemStack stack) {
        if (hasColor(stack)) {
            getNBT(stack).removeTag("color");
            return true;
        } else {
            return false;
        }
    }
}
