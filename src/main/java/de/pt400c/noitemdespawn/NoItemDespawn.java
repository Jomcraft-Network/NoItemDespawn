/* 
 *      NoItemDespawn - 1.15.2 <> Idea and codedesign by PT400C - Mod's main class
 *      © Jomcraft Network 2020
 */
package de.pt400c.noitemdespawn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.config.ModConfig;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(value = NoItemDespawn.MODID)
public class NoItemDespawn {
 
	public static final String MODID = "noitemdespawn";
	public static final Logger log = LogManager.getLogger(NoItemDespawn.MODID);
	public static final String VERSION = "2.0.1";
	public static NoItemDespawn instance;

	public NoItemDespawn() {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NIDConfig.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.register(new EventHooksFML());
	}
	
	public static NoItemDespawn getInstance() {
		return instance;
	}

}