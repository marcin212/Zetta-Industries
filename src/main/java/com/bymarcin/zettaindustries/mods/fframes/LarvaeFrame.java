package com.bymarcin.zettaindustries.mods.fframes;

import java.util.Random;

import com.bymarcin.zettaindustries.ZettaIndustries;
import com.bymarcin.zettaindustries.basic.BasicItem;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IHiveFrame;
import forestry.api.genetics.IIndividual;
import forestry.core.config.ForestryItem;

public class LarvaeFrame extends BasicItem implements IHiveFrame{
	Random rand = new Random();
	public LarvaeFrame() {
		super("LarvaeFrame");
		setMaxDamage(10);
	}

	@Override
	public float getTerritoryModifier(IBeeGenome genome, float currentModifier) {
		return 1.0F;
	}

	@Override
	public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
		return 0.5F;
	}

	@Override
	public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
		return 1.0F;
	}

	@Override
	public float getProductionModifier(IBeeGenome genome, float currentModifier) {
		return 0.8F;
	}

	@Override
	public float getFloweringModifier(IBeeGenome genome, float currentModifier) {
		return 1.0F;
	}

	@Override
	public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
		return 1.0F;
	}

	@Override
	public boolean isSealed() {
		return false;
	}

	@Override
	public boolean isSelfLighted() {
		return false;
	}

	@Override
	public boolean isSunlightSimulated() {
		return false;
	}

	@Override
	public boolean isHellish() {
		return false;
	}

	@Override
	public ItemStack frameUsed(IBeeHousing housing, ItemStack frame, IBee queen, int wear) {
		boolean shouldDrop = ((int)(rand.nextFloat()*100.0))%20==0;
		if(shouldDrop){
			ItemStack larvae = getMemberStack(queen, EnumBeeType.LARVAE.ordinal());
			housing.addProduct(larvae, true);
		}
		
		frame.setItemDamage(frame.getItemDamage() + 1);
		if(frame.getItemDamage() >= frame.getMaxDamage())
			return null;
		else
			return frame;
	}
	

	public boolean isMember(IIndividual individual) {
		return individual instanceof IBee;
	}
	
	public ItemStack getMemberStack(IIndividual bee, int type) {
		if (!isMember(bee))
			return null;

		Item beeItem = null;
		switch (EnumBeeType.VALUES[type]) {
		case QUEEN:
			beeItem = ForestryItem.beeQueenGE.item();
			break;
		case PRINCESS:
			beeItem = ForestryItem.beePrincessGE.item();
			break;
		case DRONE:
			beeItem = ForestryItem.beeDroneGE.item();
			break;
		case LARVAE:
			beeItem = ForestryItem.beeLarvaeGE.item();
			break;
		default:
			throw new RuntimeException("Cannot instantiate a bee of type " + type);

		}

		NBTTagCompound nbttagcompound = new NBTTagCompound();
		bee.writeToNBT(nbttagcompound);
		ItemStack beeStack = new ItemStack(beeItem);
		beeStack.setTagCompound(nbttagcompound);
		return beeStack;
	}
	
	@Override
	public void registerIcons(IIconRegister registry) {
		itemIcon = registry.registerIcon(ZettaIndustries.MODID + ":larvaeFrame");
	}
}
