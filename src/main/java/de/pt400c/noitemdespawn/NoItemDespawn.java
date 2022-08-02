/* 
 *		NoItemDespawn - 1.17.x <> Codedesign by Jomcraft Network
 *		© Jomcraft-Network 2022
 */
package de.pt400c.noitemdespawn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

@Mod(value = NoItemDespawn.MODID)
public class NoItemDespawn {

	public static final String MODID = "noitemdespawn";
	public static final Logger log = LogManager.getLogger(NoItemDespawn.MODID);
	public static final String VERSION = "2.0.9";

	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final String NETWORK_PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> NETWORK_PROTOCOL_VERSION, (serverVersion) -> true, (clientVersion) -> true);
	public static NoItemDespawn instance;

	public NoItemDespawn() throws Exception {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NIDConfig.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.register(new EventHooksFML());

		int networkId = 0;
		CHANNEL.registerMessage(networkId++, MarkPacket.class, MarkPacket::encode, MarkPacket::decode, MarkPacket::handle);
		
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
	}

	public static NoItemDespawn getInstance() {
		return instance;
	}
}