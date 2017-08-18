package com.bymarcin.zettaindustries.mods.nfc.smartcard;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.KeyAgreement;

import com.bymarcin.zettaindustries.mods.nfc.NFC;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Analyzable;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;


public class SmartCardTerminalTileEntity extends TileEntityEnvironment implements Analyzable, ITickable {
	@Nonnull ItemStack card = ItemStack.EMPTY;
	String player;
	String playerUUID;
	boolean needsUpdate = true;

	NBTTagCompound renderInfo = null;
	
	public SmartCardTerminalTileEntity() {
		node = Network.newNode(this, Visibility.Network).withConnector().withComponent("smartcard_terminal", Visibility.Network).create();
	}

	public boolean onBlockActivated(EntityPlayer player) {
		if (card.isEmpty() && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof SmartCardItem) {
			card = player.getHeldItemMainhand().copy();
			player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			this.player = player.getName();
			this.playerUUID = player.getUniqueID().toString();
			if(node!=null)
				node.sendToReachable("computer.signal","smartcard_in",player.getName());
			needsUpdate = true;
		}

		if (!card.isEmpty() && player.getHeldItemMainhand().isEmpty()) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, card);
			this.player = null;
			this.playerUUID = null;
			card = ItemStack.EMPTY;
			if(node!=null)
				node.sendToReachable("computer.signal","smartcard_out",player.getName());
			needsUpdate = true;
		}
		return true;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 666, getData());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return getData();
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		renderInfo = tag;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		renderInfo = pkt.getNbtCompound();
	}

	@Override
	public void update() {
		if(!getWorld().isRemote && needsUpdate){
			needsUpdate = false;
			markDirty();
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 2);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		if (player != null) {
			nbt.setString("PLAYER", player);
		}
		if (playerUUID != null) {
			nbt.setString("PLAYERUUID", playerUUID);
		}
		if (!card.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			card.writeToNBT(tag);
			nbt.setTag("CARD", tag);
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("PLAYER")) {
			player = nbt.getString("PLAYER");
		}
		if (nbt.hasKey("PLAYERUUID")) {
			playerUUID = nbt.getString("PLAYERUUID");
		}
		if (nbt.hasKey("CARD")) {
			card = new ItemStack(nbt.getCompoundTag("CARD"));
		} else {
			card = ItemStack.EMPTY;
		}
	}

	protected void checkCost(Double baseCost) throws Exception {
		if (!((Connector) node).tryChangeBuffer(-baseCost))
			throw new Exception("not enough energy");
	}
	
	protected byte[] checkCost(Context context, Arguments args, double baseCost, double byteCost) throws Exception {
		byte[] data = args.checkByteArray(0);
		if (data.length > NFC.dataCardHardLimit)
			throw new IllegalArgumentException("data size limit exceeded");
		double cost = baseCost + data.length * byteCost;
		if (!((Connector) node).tryChangeBuffer(-cost))
			throw new Exception("not enough energy");
		if (data.length > NFC.dataCardSoftLimit)
			context.pause(NFC.dataCardTimeout);
		return data;
	}

	protected byte[] asymmetricCost(Context context, Arguments args) throws Exception {
		return checkCost(context, args, NFC.dataCardAsymmetric, NFC.dataCardComplexByte);
	}

	@Callback(direct = true)
	public Object[] hasCard(Context ctx, Arguments args) {
		return card.isEmpty() ? new Object[] { false } : new Object[] { true };
	}
	
	@Callback(direct = true, limit = 1, doc = "function(pub:userdata):string -- Generates a shared key. ecdh(a.priv, b.pub) == ecdh(b.priv, a.pub)")
	public Object[] ecdh(Context context, Arguments args) throws Exception {
		if (card.isEmpty())
			return new Object[] { null, "Card expected" };
		if (SmartCardItem.getNBT(card).hasKey(SmartCardItem.OWNER) && !SmartCardItem.getOwner(card).equals(playerUUID)) {
			return new Object[] { null, "You are not owner" };
		}
		
		checkCost(NFC.dataCardAsymmetric);
		Key pubKey = deserialize(KeyType.PUBLIC, args.checkByteArray(0));
		Key privKey = deserialize(KeyType.PRIVATE, SmartCardItem.getPrivateKey(card));
		KeyAgreement ka = KeyAgreement.getInstance("ECDH");
		ka.init(privKey);
		ka.doPhase(pubKey, true);
		return new Object[] { ka.generateSecret() };
	}

	@Callback(direct = true)
	public Object[] protect(Context ctx, Arguments args) {
		if (!card.isEmpty() && !SmartCardItem.getNBT(card).hasKey(SmartCardItem.OWNER)) {
			SmartCardItem.getNBT(card).setString(SmartCardItem.OWNER, playerUUID);
			needsUpdate = true;
			return new Object[] { true, player};
		}
		return new Object[] { false };
	}

	@Callback(direct = true, limit = 1, doc = "function(data:string [, sig:string]):string or bool -- Signs or verifies data.")
	public Object[] ecdsa(Context context, Arguments args) throws Exception {
		if (card.isEmpty())
			return new Object[] { null, "Card expected" };
		if (SmartCardItem.getNBT(card).hasKey(SmartCardItem.OWNER) && !SmartCardItem.getOwner(card).equals(playerUUID)) {
			return new Object[] { null, "You are not owner" };
		}

		byte[] data = asymmetricCost(context, args);
		byte[] sig = args.optByteArray(1, null);
		Signature sign = Signature.getInstance("SHA256withECDSA");
		if (sig != null) {
			// Verify mode
			byte[] key = SmartCardItem.getPublicKey(card);
			sign.initVerify((PublicKey) deserialize(KeyType.PUBLIC, key));
			sign.update(data);
			return new Object[] { sign.verify(sig) };
		} else {
			// Sign mode
			byte[] key = SmartCardItem.getPrivateKey(card);
			sign.initSign((PrivateKey) deserialize(KeyType.PRIVATE, key));
			sign.update(data);
			return new Object[] { sign.sign() };
		}
	}

	@Callback(direct = true, doc = "function():string")
	public Object[] getPublicKey(Context context, Arguments args) throws Exception {
		if (card.isEmpty())
			return new Object[] { null, "Card expected" };
		return new Object[] { SmartCardItem.getPublicKey(card) };
	}

	private Key deserialize(KeyType type, byte[] data) throws InvalidKeySpecException, NoSuchAlgorithmException {
		switch (type) {
		case PRIVATE:
			return KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(data));
		case PUBLIC:
			return KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(data));
		default:
			throw new IllegalArgumentException("invalid key type, must be public or private");
		}
	}

	enum KeyType {
		PRIVATE, PUBLIC;
	}

	public NBTTagCompound getData() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("hasCard", !card.isEmpty());
		if ((!card.isEmpty() && SmartCardItem.getOwner(card).isEmpty()) || (!card.isEmpty() && playerUUID != null && playerUUID.equals(SmartCardItem.getOwner(card)))) {
			nbt.setBoolean("validOwner", true);
			nbt.setBoolean("isProtected", !SmartCardItem.getOwner(card).isEmpty());
		} else {
			nbt.setBoolean("validOwner", false);
		}
		return nbt;
	}

	@Override
	public Node[] onAnalyze(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		return new Node[] { node };
	}

}
