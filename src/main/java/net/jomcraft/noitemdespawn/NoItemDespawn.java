package net.jomcraft.noitemdespawn;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer.Disableable;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = NoItemDespawn.MODID, version = NoItemDespawn.VERSION, acceptableRemoteVersions = "*", guiFactory = NoItemDespawn.modGuiFactory, canBeDeactivated = false)
public class NoItemDespawn {

    public static final String MODID = "noitemdespawn";
    public static final String VERSION = "1.7.10-2.0.0";
    public static final String modGuiFactory = "net.jomcraft.noitemdespawn.GuiConfigFactory";
    public static int despawnTime = 6000;
    public static List<String> blacklist;
    private static ConfigFile config;
    public static PacketPipeline packetPipeline;
    public static boolean requiresRestart;
    public static NoItemDespawnAPI instanceAPI;

    @SidedProxy(clientSide = "net.jomcraft.noitemdespawn.ClientProxy", serverSide = "net.jomcraft.noitemdespawn.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = "noitemdespawn")
    public static NoItemDespawn instance;

    public static NoItemDespawnAPI instanceAPI() {
        if (instanceAPI == null) {
            instanceAPI = new NoItemDespawnAPI();
        }

        return instanceAPI;
    }

    public static NoItemDespawn instance() {
        if (instance == null) {
            instance = new NoItemDespawn();
        }
        return instance;
    }

    @EventHandler
    public void test(FMLPreInitializationEvent e) {
        packetPipeline = new PacketPipeline();
        FMLModContainer mc = (FMLModContainer) cpw.mods.fml.common.Loader.instance().activeModContainer();
        try {
            Field f = mc.getClass().getDeclaredField("disableability");
            f.setAccessible(true);
            f.set(mc, Disableable.NEVER);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        if (e.getSide().equals(Side.CLIENT)) {
            FMLCommonHandler.instance().bus().register(new EventHooksFML());
            MinecraftForge.EVENT_BUS.register(new EventHooksFML());
        }
        config = new ConfigFile(e.getSuggestedConfigurationFile());
        packetPipeline.registerPackets();

        NoItemDespawn.getConfig().getInstance().load();

        if (!NoItemDespawn.requiresRestart) {
            config.syncConfiguration();
            NoItemDespawn.despawnTime = config.cooldown;
            NoItemDespawn.blacklist = Arrays.asList(config.blacklist);
        }
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent e) {
        e.registerServerCommand((ICommand) new CommandChange());
    }

    public static void log(Level level, String msg) {
        LogManager.getLogger("" + NoItemDespawn.MODID).log(level, msg);
    }

    public static ConfigFile getConfig() {
        return config;
    }

}