package net.jomcraft.noitemdespawn;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketGlowing implements IMessage {

    double x, y, z, radius;
    long millis;

    public PacketGlowing() {

    }

    public PacketGlowing(double x, double y, double z, double radius, long millis) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.millis = millis;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        radius = buf.readDouble();
        millis = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(radius);
        buf.writeLong(millis);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketGlowing> {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketGlowing message, MessageContext ctx) {
            EventHooksFML.x.put(player, message.x);
            EventHooksFML.y.put(player, message.y);
            EventHooksFML.z.put(player, message.z);
            EventHooksFML.radius.put(player, message.radius);
            EventHooksFML.millis.put(player, message.millis);
            return null;
        }

    }
}
