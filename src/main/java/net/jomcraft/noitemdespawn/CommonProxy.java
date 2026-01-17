package net.jomcraft.noitemdespawn;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {

    public EntityPlayer getPlayerEntitys(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }
}
