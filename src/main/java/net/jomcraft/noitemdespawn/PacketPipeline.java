package net.jomcraft.noitemdespawn;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayerMP;

@ChannelHandler.Sharable
public class PacketPipeline {

    protected int packetID;
    public SimpleNetworkWrapper dispatcher;

    public PacketPipeline() {
        dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("_nid");
        packetID = 0;
    }

    public void registerPackets() {
        registerPacket(PacketGlowing.ClientHandler.class, PacketGlowing.class);

    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType) {
        try {
            Side side = AbstractClientPacketHandler.class.isAssignableFrom(messageHandler) ? Side.CLIENT : Side.SERVER;
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, side);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerBiPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType) {
        if (AbstractBiPacketHandler.class.isAssignableFrom(messageHandler)) {
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID, Side.CLIENT);
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, Side.SERVER);
        } else {
            throw new IllegalArgumentException("Cannot register " + messageHandler.getName() + " on both sides - error while registration!");
        }
    }


    public void sendTo(IMessage message, EntityPlayerMP player) {
        dispatcher.sendTo(message, player);
    }


}
