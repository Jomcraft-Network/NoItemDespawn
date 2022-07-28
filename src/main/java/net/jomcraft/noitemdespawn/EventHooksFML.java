/* 
 *		ServerPassword - 1.18.x <> Codedesign by Jomcraft Network
 *		© Jomcraft-Network 2022
 */
package net.jomcraft.noitemdespawn;

import java.util.HashMap;
import java.util.List;

import net.jomcraft.noitemdespawn.config.NIDConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHooksFML {

	public static HashMap<Entity, Integer> markedEntities = new HashMap<Entity, Integer>();
	public static boolean rendering = false;

	@SubscribeEvent
	public void despawnEvent(ItemExpireEvent event) {
		if (!event.getEntity().level.isClientSide()) {

			int number = 0;
			ItemEntity e = (ItemEntity) event.getEntity();

			List<ItemEntity> neighbours = e.level.getEntitiesOfClass(ItemEntity.class, e.getBoundingBox().inflate(NIDConfig.COMMON.clumpRadius.get()));

			for (ItemEntity entities : neighbours) {
				if (!entities.equals(e) && entities.getItem().getItem().equals(e.getItem().getItem()))
					number++;
			}

			if (NIDConfig.COMMON.despawnWhitelist.get().get(0).equals("*") ? (number < NIDConfig.COMMON.maxClumpSize.get()) : (number < NIDConfig.COMMON.maxClumpSize.get() || !NIDConfig.COMMON.despawnWhitelist.get().contains(event.getEntityItem().getItem().getItem().getRegistryName().toString()))) {

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

		if (rendering) {

			if (Minecraft.getInstance().level == null || !Minecraft.getInstance().level.isClientSide())
				return;

			int rend = 0;
			for (Entity e : markedEntities.keySet()) {
				int value = markedEntities.get(e);
				if (value > 1)
					markedEntities.put(e, value - 1);
				else {
					continue;
				}
				e.setSecondsOnFire(1);
				rend++;
			}
			if (rend == 0) {
				markedEntities.clear();
				rendering = false;

			}

		}
	}

	@SubscribeEvent
	public void serverStarting(ServerStartingEvent event) {
		CommandNID.register(event);
	}

}