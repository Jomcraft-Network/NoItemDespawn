package net.jomcraft.noitemdespawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;

public class CommandChange implements ICommand {
    private final List aliases;
    public static HashMap<EntityPlayer, Double> x = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Double> y = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Double> z = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Double> radius = new HashMap<EntityPlayer, Double>();
    public static HashMap<EntityPlayer, Long> cooldown = new HashMap<EntityPlayer, Long>();
    public static HashMap<EntityPlayer, Byte> type = new HashMap<EntityPlayer, Byte>();

    public CommandChange() {
        aliases = new ArrayList();

        aliases.add("noitemdespawn");

        aliases.add("nid");

    }

    @Override
    public int compareTo(Object o) {
        return 0;

    }

    @Override
    public String getCommandName() {
        return "noitemdespawn";

    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/nid <argument> [value]";

    }

    @Override
    public List getCommandAliases() {
        return this.aliases;

    }

    @Override
    public void processCommand(ICommandSender sender, String[] argString) {
        if (sender instanceof EntityPlayerMP && !(((EntityPlayerMP) sender).mcServer.getConfigurationManager().func_152596_g(((EntityPlayerMP) sender).getGameProfile()))) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You are not permitted to execute this command!"));
            return;
        }

        if (argString.length == 0) {

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]"
                    + EnumChatFormatting.YELLOW + " Please specify an argument!"));

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]"
                    + EnumChatFormatting.AQUA + " Choose from: " + EnumChatFormatting.GOLD + "dstime" + EnumChatFormatting.DARK_GREEN + "," + EnumChatFormatting.GOLD + " dsconfig" + EnumChatFormatting.DARK_GREEN + ","));
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]"
                    + EnumChatFormatting.GOLD + " updateTime" + EnumChatFormatting.DARK_GREEN + "," + EnumChatFormatting.GOLD + " despawn " + EnumChatFormatting.DARK_GREEN + "or" + EnumChatFormatting.GOLD + " count"));
            return;

        }
        if (argString.length == 1 || argString.length == 2) {
            if (argString[0].equals("updateTime")) {
                int edited = 0;
                for (WorldServer wS : MinecraftServer.getServer().worldServers) {
                    synchronized (wS) {
                        Iterator iterator = wS.loadedEntityList.iterator();
                        while (iterator.hasNext()) {
                            Entity e = (Entity) iterator.next();
                            if (e != null && e instanceof EntityItem) {
                                EntityItem eI = (EntityItem) e;
                                if (NoItemDespawn.despawnTime > -1) {

                                    if (argString.length == 2) {

                                        try {

                                            Double.parseDouble(argString[1]);

                                        } catch (Exception exc) {

                                            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Please enter a valid value!"));
                                            break;
                                        }

                                        if (sender instanceof EntityPlayer) {

                                            try {

                                                EntityPlayer ep = (EntityPlayer) sender;
                                                if (ep.dimension == eI.dimension) {
                                                    final double radius = Double.parseDouble(argString[1]);
                                                    double distance = distanceSquareToCenterCO(ep.posX, ep.posY, ep.posZ, e.posX, e.posY, e.posZ);
                                                    if ((radius * radius) < 0 || distance <= (radius * radius)) {
                                                        eI.age = 0;
                                                        eI.lifespan = NoItemDespawn.despawnTime;
                                                        edited++;
                                                    }
                                                }

                                            } catch (Exception exc) {

                                                NoItemDespawn.log(Level.ERROR, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(exc));
                                                break;
                                            }


                                        } else {
                                            eI.age = 0;
                                            eI.lifespan = NoItemDespawn.despawnTime;
                                            edited++;

                                        }

                                    } else {
                                        eI.age = 0;
                                        eI.lifespan = NoItemDespawn.despawnTime;
                                        edited++;

                                    }

                                } else {
                                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " You cannot use this command:"));
                                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.GOLD + " The despawn-cooldown is infinite!"));
                                    return;
                                }

                            }

                        }
                    }
                }
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.GOLD + edited + EnumChatFormatting.AQUA + " items have been modified!"));
                return;
            } else if (argString[0].equals("dstime") && argString.length == 2) {
                try {
                    Integer value = Integer.parseInt(argString[1]);
                    Integer dTime = NoItemDespawn.instance().despawnTime;

                    if ((dTime == Integer.MAX_VALUE && value > 0) || (dTime > 0 && value < 0)) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Enabling/Disabling item-despawn is not"));
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " possible while game is running!"));
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.GREEN + " Please use /nid dsconfig <time> to change"));
                        return;
                    }

                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.AQUA + " Changed despawn-time from "
                            + EnumChatFormatting.GOLD + dTime + EnumChatFormatting.AQUA + " to: "
                            + EnumChatFormatting.GOLD + value + EnumChatFormatting.AQUA + "!"));

                    NoItemDespawn.instance().despawnTime = value;
                    NoItemDespawn.getConfig().getInstance().get("Main", "Despawn-cooldown", 6000, "The custom despawn-cooldown of dropped items.").set(value);
                    NoItemDespawn.getConfig().getInstance().save();
                    NoItemDespawn.getConfig().syncConfiguration();

                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Something definitly went wrong. Logged!"));
                    NoItemDespawn.log(Level.ERROR, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
                }
                return;
            } else if (argString[0].equals("dsconfig") && argString.length == 2) {

                try {
                    Integer value = Integer.parseInt(argString[1]);
                    Integer dTime = NoItemDespawn.getConfig().cooldown;


                    sender.addChatMessage(
                            new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.AQUA + " Changed despawn-time in config from "
                                    + EnumChatFormatting.GOLD + (dTime < 0 ? "infinite" : dTime) + EnumChatFormatting.AQUA + " to: "
                                    + EnumChatFormatting.GOLD + (value < 0 ? "infinite" : value) + EnumChatFormatting.AQUA + "!"));
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " This will only apply after a restart!"));


                    NoItemDespawn.getConfig().getInstance().get("Main", "Despawn-cooldown", 6000, "The custom despawn-cooldown of dropped items.").set(value);
                    NoItemDespawn.getConfig().getInstance().save();

                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Something definitly went wrong. Logged!"));
                    NoItemDespawn.log(Level.ERROR, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
                }
                return;
            } else if (argString[0].equals("despawn") && argString.length == 2) {
                int deleted = 0;
                for (WorldServer wS : MinecraftServer.getServer().worldServers) {
                    Iterator iterator = wS.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        Entity e = (Entity) iterator.next();
                        if (e != null && e instanceof EntityItem) {

                            try {

                                Double.parseDouble(argString[1]);

                            } catch (Exception exc) {

                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Please enter a valid value!"));
                                break;
                            }

                            if (sender instanceof EntityPlayer) {
										
										/*try {
											
											EntityPlayer ep = (EntityPlayer) sender;
											if(ep.dimension == e.dimension) {
									        final double radius = Double.parseDouble(argString[1]);
											double distance = distanceSquareToCenterCO(ep.posX, ep.posY, ep.posZ, e.posX, e.posY, e.posZ);
											if((radius * radius) < 0 || distance <= (radius * radius)) {
												deleted++;
												e.setDead();
											}
											}
										}catch(Exception exc) {
											
											NoItemDespawn.log(Level.ERROR,  org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(exc));
											break;
										}
										
										*/

                                try {

                                    EntityPlayer ep = (EntityPlayer) sender;
                                    if (ep.dimension == e.dimension) {
                                        final double radiusF = Double.parseDouble(argString[1]);
                                        if (cooldown.containsKey(ep)) {
                                            if (type.get(ep) != 0) {
                                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Wrong type!"));
                                                return;
                                            } else {

                                                if (cooldown.get(ep) + 10000 > System.currentTimeMillis()) {

                                                    double distance = distanceSquareToCenterCO(x.get(ep), y.get(ep), z.get(ep), e.posX, e.posY, e.posZ);

                                                    if ((radius.get(ep) * radius.get(ep)) < 0 || distance <= (radius.get(ep) * radius.get(ep))) {
                                                        deleted++;
                                                        e.setDead();
                                                    }

                                                } else {
                                                    cooldown.remove(ep);
                                                    type.remove(ep);
                                                    x.remove(ep);
                                                    y.remove(ep);
                                                    z.remove(ep);
                                                    radius.remove(ep);
                                                    NoItemDespawn.packetPipeline.sendTo(new PacketGlowing(0, 0, 0, 0, -1), ((EntityPlayerMP) sender));
                                                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Your verification took longer than 10 seconds"));
                                                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Process has been interrupted."));
                                                    return;
                                                }
                                            }
                                        } else {
                                            NoItemDespawn.packetPipeline.sendTo(new PacketGlowing(ep.posX, ep.posY, ep.posZ, radiusF, System.currentTimeMillis()), ((EntityPlayerMP) sender));
                                            x.put(ep, ep.posX);
                                            y.put(ep, ep.posY);
                                            z.put(ep, ep.posZ);
                                            radius.put(ep, radiusF);
                                            type.put(ep, (byte) 0);
                                            cooldown.put(ep, System.currentTimeMillis());
                                            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.YELLOW + " Please verify your command by"));
                                            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.YELLOW + " sending it again (10 seconds remaining)"));
                                            return;
                                        }


                                    }
                                } catch (Exception exc) {

                                    NoItemDespawn.log(Level.ERROR, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(exc));
                                    break;
                                }


                            } else {
                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Cannot be executed as console!"));
                            }


                        }

                    }

                }
                cooldown.remove((EntityPlayer) sender);
                type.remove((EntityPlayer) sender);
                x.remove((EntityPlayer) sender);
                y.remove((EntityPlayer) sender);
                z.remove((EntityPlayer) sender);
                radius.remove((EntityPlayer) sender);
                NoItemDespawn.packetPipeline.sendTo(new PacketGlowing(0, 0, 0, 0, -1), ((EntityPlayerMP) sender));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.AQUA + " " + EnumChatFormatting.GOLD + deleted + EnumChatFormatting.AQUA + " dropped items have been destroyed"));
                if (deleted > 0) {
                    for (Object s : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {

                        EntityPlayerMP player = (EntityPlayerMP) s;

                        if (!player.equals(sender) && ((EntityPlayerMP) player).mcServer.getConfigurationManager().func_152596_g(((EntityPlayerMP) player).getGameProfile())) {
                            player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.GREEN + sender.getCommandSenderName() + EnumChatFormatting.AQUA + " removed " + EnumChatFormatting.GOLD + deleted + EnumChatFormatting.AQUA + " dropped items"));
                        }
                    }

                }
                return;

            } else if (argString[0].equals("count")) {
                int number = 0;
                for (WorldServer wS : MinecraftServer.getServer().worldServers) {
                    Iterator iterator = wS.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        Entity e = (Entity) iterator.next();
                        if (e != null && e instanceof EntityItem) {
                            if (argString.length == 2) {

                                try {

                                    Double.parseDouble(argString[1]);

                                } catch (Exception exc) {

                                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Please enter a valid value!"));
                                    break;
                                }

                                if (sender instanceof EntityPlayer) {


                                    try {

                                        EntityPlayer ep = (EntityPlayer) sender;
                                        if (ep.dimension == e.dimension) {
                                            final double radiusF = Double.parseDouble(argString[1]);
                                            if (cooldown.containsKey(ep)) {
                                                if (type.get(ep) != 1) {
                                                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Wrong type!"));
                                                    return;
                                                } else {
                                                    if (cooldown.get(ep) + 10000 > System.currentTimeMillis()) {

                                                        double distance = distanceSquareToCenterCO(x.get(ep), y.get(ep), z.get(ep), e.posX, e.posY, e.posZ);

                                                        if ((radius.get(ep) * radius.get(ep)) < 0 || distance <= (radius.get(ep) * radius.get(ep))) {
                                                            number++;
                                                        }

                                                    } else {
                                                        cooldown.remove(ep);
                                                        type.remove(ep);
                                                        x.remove(ep);
                                                        y.remove(ep);
                                                        z.remove(ep);
                                                        radius.remove(ep);
                                                        NoItemDespawn.packetPipeline.sendTo(new PacketGlowing(0, 0, 0, 0, -1), ((EntityPlayerMP) sender));
                                                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Your verification took longer than 10 seconds"));
                                                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Process has been interrupted."));
                                                        return;
                                                    }
                                                }
                                            } else {
                                                NoItemDespawn.packetPipeline.sendTo(new PacketGlowing(ep.posX, ep.posY, ep.posZ, radiusF, System.currentTimeMillis()), ((EntityPlayerMP) sender));
                                                x.put(ep, ep.posX);
                                                y.put(ep, ep.posY);
                                                z.put(ep, ep.posZ);
                                                radius.put(ep, radiusF);
                                                type.put(ep, (byte) 1);
                                                cooldown.put(ep, System.currentTimeMillis());
                                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.YELLOW + " Please verify your command by"));
                                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.YELLOW + " sending it again (10 seconds remaining)"));
                                                return;
                                            }


                                        }
                                    } catch (Exception exc) {

                                        NoItemDespawn.log(Level.ERROR, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(exc));
                                        break;
                                    }


                                } else {
                                    number++;
                                }

                            } else {
                                number++;
                            }

                        }

                    }

                }
                if (sender instanceof EntityPlayer) {
                    cooldown.remove((EntityPlayer) sender);
                    type.remove((EntityPlayer) sender);
                    x.remove((EntityPlayer) sender);
                    y.remove((EntityPlayer) sender);
                    z.remove((EntityPlayer) sender);
                    radius.remove((EntityPlayer) sender);
                    NoItemDespawn.packetPipeline.sendTo(new PacketGlowing(0, 0, 0, 0, -1), ((EntityPlayerMP) sender));
                }

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.AQUA + " Currently " + EnumChatFormatting.GOLD + number + EnumChatFormatting.AQUA + " dropped items exist"));
                return;
            } else {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.YELLOW + " Please enter a valid value"));
                return;
            }
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "NID" + EnumChatFormatting.DARK_GRAY + "]" + EnumChatFormatting.RED + " Invalid arguments!"));
        }

        if (argString.length == 2) {


        } else {

        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1) {
        return true;

    }

    @Override
    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {

        return var2.length == 1
                ? CommandBase.getListOfStringsMatchingLastWord(var2, new String[]{"dstime", "dsconfig", "updateTime", "count", "despawn"})
                : null;

    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2) {

        return false;

    }

    public static double distanceSquareToCenterCO(final double x, final double y, final double z, final double x1, final double y1, final double z1) {
        double dx = x + 0.5D - x1;
        double dy = y + 0.5D - y1;
        double dz = z + 0.5D - z1;
        return dx * dx + dy * dy + dz * dz;
    }


}
