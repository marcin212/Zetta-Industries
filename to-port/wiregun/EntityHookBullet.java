package com.bymarcin.zettaindustries.mods.wiregun;

import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.common.entities.EntityRevolvershot;

import blusunrize.immersiveengineering.common.util.IEDamageSources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHookBullet extends EntityRevolvershot {

	private boolean alredyHit = false;

	public EntityHookBullet(World world) {
		super(world);
		setTickLimit(40*4);
	}

	public EntityHookBullet(World world, double x, double y, double z, double ax, double ay, double az) {
		super(world, x, y, z, ax, ay, az, 0);
		setTickLimit(40*4);
	}

	public EntityHookBullet(World world, EntityLivingBase living, double ax, double ay, double az, ItemStack stack) {
		super(world, living, ax, ay, az, 0, stack);
		setTickLimit(40*4);
	}



	@Override
	protected void onImpact(RayTraceResult mop) {
		if (!this.worldObj.isRemote && this.shootingEntity != null && this.shootingEntity instanceof EntityPlayer) {
			if (alredyHit) {
				return;
			} else {
				alredyHit = true;
			}
			EntityPlayer ep = (EntityPlayer) this.shootingEntity;
			int next = (ep.inventory.currentItem + 1) % 10;
			ItemStack toUse = ep.inventory.getStackInSlot(next);
			
			if (toUse != null && toUse.getItem() instanceof IWireCoil) {
				if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
					if (toUse.getItem().onItemUseFirst(toUse, ep, this.worldObj, mop.getBlockPos(), mop.sideHit, (float) mop.hitVec.xCoord % 1, (float) mop.hitVec.yCoord % 1, (float) mop.hitVec.zCoord % 1)) {
						this.worldObj.playSoundAtEntity(ep, "random.successful_hit", .8F, 1.2F / (this.rand.nextFloat() * .2F + .9F));
					}
				}
			}

			if (mop.typeOfHit == RayTraceResult.Type.ENTITY) {
				mop.entityHit.attackEntityFrom(IEDamageSources.causeCasullDamage(this, shootingEntity), .5F);
			}
		}
	}
}
