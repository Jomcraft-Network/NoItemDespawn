/* 
 *		ServerPassword - 1.18.x <> Codedesign by Jomcraft Network
 *		© Jomcraft-Network 2022
 */
package net.jomcraft.noitemdespawn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.jomcraft.noitemdespawn.config.NIDConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(value = NoItemDespawn.MODID)
public class NoItemDespawn {

	public static final String MODID = "noitemdespawn";
	public static final Logger log = LogManager.getLogger(NoItemDespawn.MODID);
	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	public static final String VERSION = "2.0.7";

	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final String NETWORK_PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);
	public static NoItemDespawn instance;

	public NoItemDespawn() throws Exception {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NIDConfig.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.register(new EventHooksFML());

		int networkId = 0;
		CHANNEL.registerMessage(networkId++, MarkPacket.class, MarkPacket::encode, MarkPacket::decode, MarkPacket::handle);
	}

	public static NoItemDespawn getInstance() {
		return instance;
	}
}