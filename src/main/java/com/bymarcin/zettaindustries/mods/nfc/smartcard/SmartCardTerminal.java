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
import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.crypto.KeyAgreement;

import com.bymarcin.zettaindustries.mods.nfc.NFC;

import li.cil.oc.api.Network;
import li.cil.oc.api.component.RackBusConnectable;
import li.cil.oc.api.component.RackMountable;
import li.cil.oc.api.internal.Rack;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Analyzable;
import li.cil.oc.api.network.ComponentConnector;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SmartCardTerminal extends AbstractManagedEnvironment implements RackMountable, Analyzable {
	@Nonnull ItemStack card = ItemStack.EMPTY;
	protected ComponentConnector node;
	UUID player;
	Rack host;

	public SmartCardTerminal(Rack host) {
		this.host = host;
		setNode(Network.newNode(this, Visibility.Network).withConnector().withComponent("smartcard_terminal", Visibility.Network).create());
	}

	@Override
	public Node node() {
		return this.node != null ? this.node : super.node();
	}

	@Override
	protected void setNode(Node value) {
		if (value == null) {
			this.node = null;
		} else if (value instanceof ComponentConnector) {
			this.node = ((ComponentConnector) value);
		}
		super.setNode(value);
	}

	@Override
	public void save(NBTTagCompound nbt) {
		super.save(nbt);
		if (player != null) {
			nbt.setUniqueId("PLAYER_UUID", player);
		}
		if (!card.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			card.writeToNBT(tag);
			nbt.setTag("CARD", tag);
		}
	}

	@Override
	public void load(NBTTagCompound nbt) {
		super.load(nbt);
		if (nbt.hasKey("PLAYER_UUID")) {
			player = nbt.getUniqueId("PLAYER_UUID");
		} else if (nbt.hasKey("PLAYER")) {
			player = NFC.getUUIDForPlayer(nbt.getString("PLAYER"));
		}

		if (nbt.hasKey("CARD")) {
			card = new ItemStack(nbt.getCompoundTag("CARD"));
		} else {
			card = ItemStack.EMPTY;
		}
	}

	@Override
	public EnumSet<State> getCurrentState() {
		return EnumSet.noneOf(State.class);
	}

	protected byte[] checkCost(Context context, Arguments args, double baseCost, double byteCost) throws Exception {
		byte[] data = args.checkByteArray(0);
		if (data.length > NFC.dataCardHardLimit)
			throw new IllegalArgumentException("data size limit exceeded");
		double cost = baseCost + data.length * byteCost;
		if (!node.tryChangeBuffer(-cost))
			throw new Exception("not enough energy");
		if (data.length > NFC.dataCardSoftLimit)
			context.pause(NFC.dataCardTimeout);
		return data;
	}

	protected byte[] asymmetricCost(Context context, Arguments args) throws Exception {
		return checkCost(context, args, NFC.dataCardAsymmetric, NFC.dataCardComplexByte);
	}

	protected void checkCost(Double baseCost) throws Exception {
		if (!node.tryChangeBuffer(-baseCost))
			throw new Exception("not enough energy");
	}

	@Callback(direct = true)
	public Object[] hasCard(Context ctx, Arguments args) {
		return card.isEmpty() ? new Object[] { false } : new Object[] { true };
	}

	@Callback(direct = true)
	public Object[] protect(Context ctx, Arguments args) {
		if (!card.isEmpty() && !SmartCardItem.getNBT(card).hasKey(SmartCardItem.OWNER_UUID_LEAST)) {
			SmartCardItem.getNBT(card).setUniqueId(SmartCardItem.OWNER_UUID, player);
			host.markChanged(host.indexOfMountable(this));
			return new Object[] { true, player };
		}
		return new Object[] { false };
	}

	@Callback(direct = true, limit = 1, doc = "function(data:string [, sig:string]):string or bool -- Signs or verifies data.")
	public Object[] ecdsa(Context context, Arguments args) throws Exception {
		if (card.isEmpty())
			return new Object[] { null, "Card expected" };
		if (SmartCardItem.getNBT(card).hasKey(SmartCardItem.OWNER_UUID_LEAST) && !SmartCardItem.getOwner(card).equals(player)) {
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

	@Callback(direct = true, limit = 1, doc = "function(pub:userdata):string -- Generates a shared key. ecdh(a.priv, b.pub) == ecdh(b.priv, a.pub)")
	public Object[] ecdh(Context context, Arguments args) throws Exception {
		if (card.isEmpty())
			return new Object[] { null, "Card expected" };
		if (SmartCardItem.getNBT(card).hasKey(SmartCardItem.OWNER_UUID_LEAST) && !SmartCardItem.getOwner(card).equals(player)) {
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

	@Override
	public NBTTagCompound getData() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("hasCard", !card.isEmpty());
		if ((!card.isEmpty() && SmartCardItem.getOwner(card) == null) || (!card.isEmpty() && player != null && player.equals(SmartCardItem.getOwner(card)))) {
			nbt.setBoolean("validOwner", true);
			nbt.setBoolean("isProtected", SmartCardItem.getOwner(card) != null);
		} else {
			nbt.setBoolean("validOwner", false);
		}
		return nbt;
	}

	@Override
	public int getConnectableCount() {
		return 0;
	}

	@Override
	public RackBusConnectable getConnectableAt(int index) {
		return null;
	}

	@Override
	public boolean onActivate(EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY) {
		if (card.isEmpty() && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof SmartCardItem) {
			card = player.getHeldItemMainhand().copy();
			player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			this.player = player.getGameProfile().getId();
			if (node != null)
				node.sendToReachable("computer.signal", "smartcard_in", player.getName());
			host.markChanged(host.indexOfMountable(this));
			return true;
		}

		if (!card.isEmpty() && player.getHeldItemMainhand().isEmpty()) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, card);
			this.player = null;
			card = ItemStack.EMPTY;
			if (node != null)
				node.sendToReachable("computer.signal", "smartcard_out", player.getName());
			host.markChanged(host.indexOfMountable(this));
			return true;
		}

		return false;
	}


	@Override
	public Node[] onAnalyze(EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		return new Node[] { node };
	}

}
