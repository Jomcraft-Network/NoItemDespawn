package net.jomcraft.noitemdespawn;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {

    private static ClientProxy clientProxy;

    public static ClientProxy instance() {
        if (clientProxy == null) {
            clientProxy = (ClientProxy) NoItemDespawn.proxy;
        }
        return clientProxy;
    }

    @Override
    public EntityPlayer getPlayerEntitys(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntitys(ctx));
    }
}
