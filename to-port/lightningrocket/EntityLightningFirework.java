package com.bymarcin.zettaindustries.mods.lightningrocket;

import java.lang.reflect.Field;

import com.bymarcin.zettaindustries.ZettaIndustries;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockMetalDecoration;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityLightningRod;

public class EntityLightningFirework extends EntityFireworkRocket{
	double spawnPointX;
	double spawnPointY;
	double spawnPointZ;
	static Field lifetime;
	static{
		 for(Field f : EntityLightningFirework.class.getSuperclass().getDeclaredFields()){
			 if(f.getName().equals("field_92055_b") || f.getName().equals("lifetime")){
				 lifetime=f;
				 f.setAccessible(true);
				 break;
			 }
		 }
	}
	
	public EntityLightningFirework(World world) {
		super(world);
	}
	
	public EntityLightningFirework(World world, double x, double y, double z, ItemStack itemstack){
		 super(world, x, y, z, itemstack);
		 this.spawnPointX = x;
		 this.spawnPointY = y;
		 this.spawnPointZ = z;
		 int i = 5;
		 try {
			lifetime.setInt(this, 10 * i + this.rand.nextInt(4) + this.rand.nextInt(3));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	 }
	
	
	
	@Override
	public void onUpdate() {
        this.motionX /= 1.15D;
        this.motionZ /= 1.15D;
		super.onUpdate();
		if(worldObj.isRemote) return;
		if(isDead && worldObj.canLightningStrikeAt((int)spawnPointX, (int)spawnPointY, (int)spawnPointZ) && this.posY>120 && spawnPointY<100){
			if( (worldObj.isThundering() && ZettaIndustries.RANDOM.nextDouble()<LightningRocket.thunderchance) ||
				(!worldObj.isThundering() && ZettaIndustries.RANDOM.nextDouble()<LightningRocket.rainchance)){
				EntityLightningBolt entitybolt = new EntityLightningBolt(worldObj,spawnPointX,spawnPointY,spawnPointZ);
				worldObj.addWeatherEffect(entitybolt);	
				worldObj.spawnEntityInWorld(entitybolt);
					for(int xx=-1; xx<=1; xx++)
						for(int zz=-1; zz<=1; zz++)
							if(worldObj.getBlock((int)spawnPointX+xx, (int)spawnPointY-1, (int)spawnPointZ+zz).equals(IEContent.blockMetalDecoration) && worldObj.getBlockMetadata((int)spawnPointX+xx, (int)spawnPointY-1, (int)spawnPointZ+zz)==BlockMetalDecoration.META_fence)
								for(int y=(int) spawnPointY-1; y>0; y--)
									if( worldObj.getTileEntity((int)spawnPointX+xx, y, (int)spawnPointZ+zz) instanceof TileEntityLightningRod)
									{
										((TileEntityLightningRod) worldObj.getTileEntity((int)spawnPointX+xx, y, (int)spawnPointZ+zz)).energyStorage.setEnergyStored(Config.getInt("lightning_output"));
										return;
									}
									else if(!(worldObj.getBlock((int)spawnPointX+xx, y, (int)spawnPointZ+zz).equals(IEContent.blockMetalDecoration) && worldObj.getBlockMetadata((int)spawnPointX+xx, y, (int)spawnPointZ+zz)==BlockMetalDecoration.META_fence))
										return;		
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setDouble("spawnPointX", spawnPointX);
		nbt.setDouble("spawnPointY", spawnPointY);
		nbt.setDouble("spawnPointZ", spawnPointZ);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		spawnPointX = nbt.getDouble("spawnPointX");
		spawnPointY = nbt.getDouble("spawnPointY");
		spawnPointZ = nbt.getDouble("spawnPointZ");
	}
}
