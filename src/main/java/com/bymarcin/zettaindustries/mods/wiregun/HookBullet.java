package com.bymarcin.zettaindustries.mods.wiregun;

import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.entities.EntityRevolvershot;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import com.bymarcin.zettaindustries.ZettaIndustries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HookBullet implements BulletHandler.IBullet {
    @Override
    public void onHitTarget(World world, RayTraceResult target, @Nullable EntityLivingBase shooter, Entity projectile, boolean headshot) {
        if (!world.isRemote && shooter != null && shooter instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) shooter;
            ItemStack toUse = ep.getHeldItemOffhand();

            if (!toUse.isEmpty() && toUse.getItem() instanceof IWireCoil) {
                if (target.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (toUse.getItem().onItemUse(ep, world, target.getBlockPos(), EnumHand.OFF_HAND, target.sideHit, (float) target.hitVec.x % 1, (float) target.hitVec.y % 1, (float) target.hitVec.z % 1) == EnumActionResult.SUCCESS) {
                        world.playSound(ep, target.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT, SoundCategory.PLAYERS, .8F, 1.2F / (ZettaIndustries.RANDOM.nextFloat() * .2F + .9F));
                    }
                }
            }

            if (target.typeOfHit == RayTraceResult.Type.ENTITY) {
                target.entityHit.attackEntityFrom(IEDamageSources.causeCasullDamage((EntityRevolvershot) projectile, shooter), .5F);
            }
        }
    }

    @Override
    public ItemStack getCasing(ItemStack stack) {
        return BulletHandler.emptyCasing;
    }

    @Override
    public ResourceLocation[] getTextures() {
        return new ResourceLocation[] { new ResourceLocation("zettaindustries:items/hook_bullet") };
    }

    @Override
    public int getColour(ItemStack stack, int layer) {
        return -1;
    }
}
