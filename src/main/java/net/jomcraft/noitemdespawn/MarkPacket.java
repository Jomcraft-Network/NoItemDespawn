/* 
 *		NoItemDespawn - 1.18.x <> Codedesign by Jomcraft Network
 *		© Jomcraft-Network 2022
 */
package net.jomcraft.noitemdespawn;

import net.jomcraft.noitemdespawn.config.NIDConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

	public static void encode(final MarkPacket msg, final FriendlyByteBuf packetBuffer) {
		packetBuffer.writeDouble(msg.x);
		packetBuffer.writeDouble(msg.y);
		packetBuffer.writeDouble(msg.z);
		packetBuffer.writeInt(msg.radius);
	}

	public static MarkPacket decode(final FriendlyByteBuf packetBuffer) {
		return new MarkPacket(packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readInt());
	}

	@SuppressWarnings("resource")
	public static void handle(final MarkPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
		final NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Iterable<Entity> iterator = Minecraft.getInstance().level.entitiesForRendering();
			EventHooksFML.markedEntities.clear();

			for (Entity e : iterator) {
				if (e != null && e instanceof ItemEntity) {
					if (CommandNID.distanceBetweenTwoPoints(e.getX(), e.getY(), e.getZ(), msg.x, msg.y, msg.z) <= msg.radius) {
						EventHooksFML.markedEntities.put(e, NIDConfig.COMMON.markTicks.get());
					}

				}
			}
			if (EventHooksFML.markedEntities.size() > 0)
				EventHooksFML.rendering = true;
		});
		context.setPacketHandled(true);
	}

}