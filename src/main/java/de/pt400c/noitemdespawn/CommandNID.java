/* 
 *      NoItemDespawn - 1.15.2 <> Idea and codedesign by PT400C - Command class
 *      © Jomcraft Network 2020
 */
package de.pt400c.noitemdespawn;

import java.util.Iterator;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.versions.mcp.MCPVersion;

public class CommandNID {

	private static boolean is115 = MCPVersion.getMCVersion().startsWith("1.15");
	private static boolean first = true;

	protected static void register(FMLServerStartingEvent event) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("noitemdespawn").requires((player) -> {
	         return player.hasPermissionLevel(2);
	    });
		
		literalargumentbuilder.then(Commands.literal("count").executes((command) -> {
	         return countRange(command.getSource(), -1);
	      }).then(Commands.argument("range", StringArgumentType.string()).executes((command) -> {
	         return countRange(command.getSource(), Integer.parseInt(StringArgumentType.getString(command, "range")));
	      }))).then(Commands.literal("mark").executes((command) -> {
		         return markRange(command.getSource(), -1);
		      }).then(Commands.argument("range", StringArgumentType.string()).executes((command) -> {
		         return markRange(command.getSource(), Integer.parseInt(StringArgumentType.getString(command, "range")));
		      }))).then(Commands.literal("despawn").executes((command) -> {
		         return deleteRange(command.getSource(), -1);
		      }).then(Commands.argument("range", StringArgumentType.string()).executes((command) -> {
		         return deleteRange(command.getSource(), Integer.parseInt(StringArgumentType.getString(command, "range")));
		      }))/*.then(Commands.argument("type", StringArgumentType.string()).executes((command) -> {
			         return deleteRange(command.getSource(), Integer.parseInt(StringArgumentType.getString(command, "range")));
			      }))*/);

		event.getServer().getCommandManager().getDispatcher().register(literalargumentbuilder);
	}
	
	private static int deleteRange(CommandSource source, int range) throws CommandSyntaxException {
		int number = 0;
		
		if(first) {
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "In this session you didn't use the despawn command before! Please be really careful concerning the range you choose. Deleting the items cannot be undone!"), true);
			source.sendFeedback(new StringTextComponent(TextFormatting.AQUA + "Better try " + TextFormatting.GOLD + "/noitemdespawn mark" + TextFormatting.AQUA + " to inspect the range!"), true);
			first = false;
			return 0;
		}
		
		if (range == -1 || range > NIDConfig.COMMON.maxDespawnRadius.get()) {
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "You have to specify a valid range / radius! (Maximum: " + NIDConfig.COMMON.maxDespawnRadius.get() + ")"), true);
			return 0;
		} else {
				Iterator<Entity> iterator = source.getWorld().getEntities().iterator();
				while (iterator.hasNext()) {
					Entity e = (Entity) iterator.next();
					if (e != null && e instanceof ItemEntity) {
						
						if(distanceBetweenTwoPoints(e.getPosX(), e.getPosY(), e.getPosZ(), source.getEntity().getPosX(), source.getEntity().getPosY(), source.getEntity().getPosZ()) <= range) {
							number++;
							e.remove();
						}

					}

				}

		}
		StringTextComponent component = new StringTextComponent(TextFormatting.GREEN + "" + source.getName().toString() + TextFormatting.GOLD + " despawned " + TextFormatting.AQUA + number + TextFormatting.GOLD + " dropped Items!");
		MinecraftServer.LOGGER.info(component.getString());
		
		if(!is115) {
			source.getServer().getPlayerList().sendPacketToAllPlayers(new SChatPacket(component, ChatType.SYSTEM, source.asPlayer().getUniqueID()));
		} else {
			source.getServer().getPlayerList().sendPacketToAllPlayers(new SChatPacket(component, ChatType.SYSTEM));
		}
		return 1;

	}
	
	private static int markRange(CommandSource source, int range) throws CommandSyntaxException {
		int number = 0;
		if (range == -1) {
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "No range specified!"), true);
			return 0;
		} else {
				Iterator<Entity> iterator = source.getWorld().getEntities().iterator();
				while (iterator.hasNext()) {
					Entity e = (Entity) iterator.next();
					if (e != null && e instanceof ItemEntity) {
						if(distanceBetweenTwoPoints(e.getPosX(), e.getPosY(), e.getPosZ(), source.getEntity().getPosX(), source.getEntity().getPosY(), source.getEntity().getPosZ()) <= range)
							number++;

					}

				}

		}
		
		if (is115) {
			NoItemDespawn.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> source.getEntity().dimension), new MarkPacket(source.getEntity().getPosX(), source.getEntity().getPosY(), source.getEntity().getPosZ(), range));

		} else {

			for (ServerPlayerEntity player : source.getEntity().getServer().getPlayerList().getPlayers()) {
				if (player.world.func_234923_W_().func_240901_a_().toString().equals(source.getEntity().world.func_234923_W_().func_240901_a_().toString())) {
					NoItemDespawn.CHANNEL.sendTo(new MarkPacket(source.getEntity().getPosX(), source.getEntity().getPosY(), source.getEntity().getPosZ(), range), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
				}
			}

		}

		source.sendFeedback(new StringTextComponent(TextFormatting.YELLOW + "Currently " + TextFormatting.AQUA + number + TextFormatting.YELLOW + " dropped Items exist!"), true);
		return 1;
	}
	
	private static int countRange(CommandSource source, int range) throws CommandSyntaxException {
		int number = 0;
		boolean global = false;
		if (range == -1) {
			global = true;
			for (ServerWorld wS : source.getWorld().getServer().getWorlds()) {
				Iterator<Entity> iterator = wS.getEntities().iterator();
				while (iterator.hasNext()) {
					Entity e = (Entity) iterator.next();
					if (e != null && e instanceof ItemEntity) {
						number++;

					}

				}

			}
		} else {
				Iterator<Entity> iterator = source.getWorld().getEntities().iterator();
				while (iterator.hasNext()) {
					Entity e = (Entity) iterator.next();
					if (e != null && e instanceof ItemEntity) {
						if(distanceBetweenTwoPoints(e.getPosX(), e.getPosY(), e.getPosZ(), source.getEntity().getPosX(), source.getEntity().getPosY(), source.getEntity().getPosZ()) <= range)
							number++;

					}

				}

		}
		
		String s = (global ? (TextFormatting.RED + " (All dimensions included)") : "");
		source.sendFeedback(new StringTextComponent(TextFormatting.YELLOW + "Currently " + TextFormatting.AQUA + number + TextFormatting.YELLOW + " dropped Items exist!" + s), true);
		return 1;
	}
	
	public static double distanceBetweenTwoPoints(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt((z2 - z1) * (z2 - z1) + (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
}