/* 
 *      NoItemDespawn - 1.16.5 <> Idea and codedesign by PT400C - Mod's main class
 *      © Jomcraft Network 2021
 */
package de.pt400c.noitemdespawn;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(value = NoItemDespawn.MODID)
public class NoItemDespawn {

	public static final String MODID = "noitemdespawn";
	public static final Logger log = LogManager.getLogger(NoItemDespawn.MODID);
	public static final String VERSION = "2.0.8";

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
		
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> "ANY", (remote, isServer) -> true));
	}

	public static NoItemDespawn getInstance() {
		return instance;
	}
}