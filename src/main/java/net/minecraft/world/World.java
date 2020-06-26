package net.minecraft.world;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class World {

   public boolean isRemote;

   public RegistryKey<World> func_234923_W_() {
	  return null;
   }

   public List<ItemEntity> getEntitiesWithinAABB(Class<ItemEntity> class1, AxisAlignedBB grow) {
	   return null;
   }
}