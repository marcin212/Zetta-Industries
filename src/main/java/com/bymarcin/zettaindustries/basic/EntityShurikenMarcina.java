package com.bymarcin.zettaindustries.basic;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.UUID;

public class EntityShurikenMarcina extends EntityThrowable {
    public static final UUID OWNER_UUID = UUID.fromString("c8535e14-cf70-4204-9d1c-94266326fc59");

    public EntityShurikenMarcina(World worldIn) {
        super(worldIn);
    }

    public EntityShurikenMarcina(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityShurikenMarcina(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            boolean hit = false;

            if (!world.isRemote) {
                if (result.entityHit instanceof EntityPlayerMP) {
                    GameProfile profile = ((EntityPlayerMP) result.entityHit).getGameProfile();
                    if (profile != null && OWNER_UUID.equals(profile.getId())) {
                        hit = true;
                        EntityLightningBolt lightningBolt = new EntityLightningBolt(world, getThrower().posX, getThrower().posY, getThrower().posZ, false);
                        world.addWeatherEffect(lightningBolt);
                        world.spawnEntity(lightningBolt);
                    }
                }
            }

            if (!hit) {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}
