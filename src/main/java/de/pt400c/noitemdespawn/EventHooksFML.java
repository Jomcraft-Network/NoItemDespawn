/* 
 *		NoItemDespawn - 1.17.x <> Codedesign by Jomcraft Network
 *		© Jomcraft-Network 2022
 */
package de.pt400c.noitemdespawn;

import java.util.HashMap;
import java.util.List;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;

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

			boolean noDespawn = false;
			if (NIDConfig.COMMON.despawnWhitelist.get().get(0).equals("*")) {
				if (!NIDConfig.COMMON.invertToBlacklist.get() && number < NIDConfig.COMMON.maxClumpSize.get()) {
					noDespawn = true;
				}
			} else {
				if (NIDConfig.COMMON.invertToBlacklist.get()) {
					if (number < NIDConfig.COMMON.maxClumpSize.get() && NIDConfig.COMMON.despawnWhitelist.get().contains(event.getEntityItem().getItem().getItem().getRegistryName().toString())) {
						noDespawn = true;
					}
				} else {
					if (number < NIDConfig.COMMON.maxClumpSize.get() || !NIDConfig.COMMON.despawnWhitelist.get().contains(event.getEntityItem().getItem().getItem().getRegistryName().toString())) {
						noDespawn = true;
					}
				}
			}

			if (noDespawn) {
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
	public void serverStarting(FMLServerStartingEvent event) {
		CommandNID.register(event);
	}

}