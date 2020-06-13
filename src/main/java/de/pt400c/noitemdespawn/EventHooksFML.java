/* 
 *      NoItemDespawn - 1.15.2 <> Idea and codedesign by PT400C - Eventhandling class
 *      © Jomcraft Network 2020
 */
package de.pt400c.noitemdespawn;

import java.util.HashMap;
import java.util.List;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class EventHooksFML {
	
	public static HashMap<Entity, Integer> markedEntities = new HashMap<Entity, Integer>();
	public static boolean rendering = false;
	
	@SubscribeEvent
	public void despawnEvent(ItemExpireEvent event) {
		if (!event.getEntity().world.isRemote) {

			int number = 0;
			ItemEntity e = (ItemEntity) event.getEntity();

			List<ItemEntity> neighbours = e.world.getEntitiesWithinAABB(ItemEntity.class, e.getBoundingBox().grow(NIDConfig.COMMON.clumpRadius.get()));

			for (ItemEntity entities : neighbours) {
				if (!entities.equals(e) && entities.getItem().getItem().equals(e.getItem().getItem()))
					number++;
			}

			if (NIDConfig.COMMON.despawnWhitelist.get().get(0).equals("*") ? (number < NIDConfig.COMMON.maxClumpSize.get()) :(number < NIDConfig.COMMON.maxClumpSize.get() || !NIDConfig.COMMON.despawnWhitelist.get().contains(event.getEntityItem().getItem().getItem().getRegistryName().toString()))) {

				event.getEntityItem().lifespan = 2000000000;
				if (event.getEntityItem().age > 1999999997)
					event.getEntityItem().age = 0;
				event.setCanceled(true);
			}
		}
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public void tickEvent2(RenderTickEvent event) {
		
		if(rendering) {
		
		if(Minecraft.getInstance().world == null || !Minecraft.getInstance().world.isRemote)
			return;

		int rend = 0;
		for(Entity e : markedEntities.keySet()) {
			int value = markedEntities.get(e);
			if (value > 1)
				markedEntities.put(e, value - 1);
			else {
				continue;
			}
			e.setFire(1);
			rend++;
		}
		if(rend == 0) {
			markedEntities.clear();
			rendering = false;
		
		}

		}
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		CommandNID.register(event);
	}

}