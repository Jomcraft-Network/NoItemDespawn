package net.jomcraft.noitemdespawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class EventHooksFML {

    public static HashMap<EntityPlayer, Double> x = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Double> y = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Double> z = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Double> radius = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Long> millis = new HashMap<EntityPlayer, Long>();
    public static List<EntityItem> itemList = new ArrayList<EntityItem>();

    @SubscribeEvent
    public void onConfigChanges(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(NoItemDespawn.MODID)) {

            ConfigFile config = NoItemDespawn.getConfig();
            config.getInstance().save();

            if (!NoItemDespawn.requiresRestart) {

                config.syncConfiguration();
                NoItemDespawn.despawnTime = config.cooldown;
                NoItemDespawn.blacklist = Arrays.asList(config.blacklist);

            }
        }
    }

    public static double distanceSquareToCenterCO(final double x, final double y, final double z, final double x1, final double y1, final double z1) {
        double dx = x + 0.5D - x1;
        double dy = y + 0.5D - y1;
        double dz = z + 0.5D - z1;
        return dx * dx + dy * dy + dz * dz;
    }

    @SubscribeEvent
    public void onConfigChanges(RenderWorldLastEvent event) {
        for (Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
            if (entity instanceof EntityItem) {

                Entity e = (Entity) entity;
                EntityClientPlayerMP ep = Minecraft.getMinecraft().thePlayer;
                if (!millis.containsKey(ep))
                    break;
                if (!itemList.contains((EntityItem) e)) {
                    if (!(millis.get(ep) + 30000 < System.currentTimeMillis())) {

                        double distance = distanceSquareToCenterCO(x.get(ep), y.get(ep), z.get(ep), e.posX, e.posY, e.posZ);
                        if ((radius.get(ep) * radius.get(ep)) < 0 || distance <= (radius.get(ep) * radius.get(ep))) {

                            ((InterfaceEntityItem) e).setGlowing(true);
                            itemList.add((EntityItem) e);

                        } else {
                            ((InterfaceEntityItem) e).setGlowing(false);
                            itemList.remove((EntityItem) e);
                        }
                    }
                } else {
                    if (millis.get(ep) + 30000 < System.currentTimeMillis()) {
                        ((InterfaceEntityItem) e).setGlowing(false);
                        itemList.remove((EntityItem) e);
                    }
                }
            }
    }
}
