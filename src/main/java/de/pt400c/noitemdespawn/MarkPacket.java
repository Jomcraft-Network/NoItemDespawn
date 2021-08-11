/* 
 *      NoItemDespawn - 1.16.5 <> Idea and codedesign by PT400C - Packet class
 *      © Jomcraft Network 2021
 */
package de.pt400c.noitemdespawn;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

import de.pt400c.noitemdespawn.config.NIDConfig;

public final class MarkPacket {

	private final double x;
	private final double y;
	private final double z;
	private final int radius;

	public MarkPacket(double x, double y, double z, int radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
	}

	public static void encode(final MarkPacket msg, final PacketBuffer packetBuffer) {
		packetBuffer.writeDouble(msg.x);
		packetBuffer.writeDouble(msg.y);
		packetBuffer.writeDouble(msg.z);
		packetBuffer.writeInt(msg.radius);
	}

	public static MarkPacket decode(final PacketBuffer packetBuffer) {
		return new MarkPacket(packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readInt());
	}

	@SuppressWarnings("resource")
	public static void handle(final MarkPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Iterable<Entity> iterator = Minecraft.getInstance().level.entitiesForRendering();
			EventHooksFML.markedEntities.clear();
			
			for(Entity e : iterator) {
				if (e != null && e instanceof ItemEntity) {
					if(CommandNID.distanceBetweenTwoPoints(e.getX(), e.getY(), e.getZ(), msg.x, msg.y, msg.z) <= msg.radius) {
						EventHooksFML.markedEntities.put(e, NIDConfig.COMMON.markTicks.get());
					}

				}
			}
			if(EventHooksFML.markedEntities.size() > 0)
				EventHooksFML.rendering = true;
		});
		context.setPacketHandled(true);
	}

}