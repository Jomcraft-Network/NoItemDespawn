/* 
 *		NoItemDespawn - 1.19.x <> Codedesign by Jomcraft Network
 *		© Jomcraft-Network 2022
 */
package net.jomcraft.noitemdespawn;

import java.util.Iterator;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.jomcraft.noitemdespawn.config.NIDConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.network.PacketDistributor;

public class CommandNID {

	private static boolean first = true;

	protected static void register(ServerStartingEvent event) {
		LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = Commands.literal("noitemdespawn").requires((player) -> {
			return player.hasPermission(2);
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
		})));

		LiteralCommandNode<CommandSourceStack> node = event.getServer().getCommands().getDispatcher().register(literalargumentbuilder);

		event.getServer().getCommands().getDispatcher().register(Commands.literal("nid").redirect(node));
	}

	private static int deleteRange(CommandSourceStack source, int range) throws CommandSyntaxException {
		int number = 0;

		if (first) {
			source.sendSuccess(Component.literal(ChatFormatting.RED + "In this session you didn't use the despawn command before! Please be really careful concerning the range you choose. Deleting the items cannot be undone!"), true);
			source.sendSuccess(Component.literal(ChatFormatting.AQUA + "Better try " + ChatFormatting.GOLD + "/noitemdespawn mark" + ChatFormatting.AQUA + " to inspect the range!"), true);
			first = false;
			return 0;
		}

		if (range == -1 || range > NIDConfig.COMMON.maxDespawnRadius.get()) {
			source.sendSuccess(Component.literal(ChatFormatting.RED + "You have to specify a valid range / radius! (Maximum: " + NIDConfig.COMMON.maxDespawnRadius.get() + ")"), true);
			return 0;
		} else {
			Iterator<Entity> iterator = source.getLevel().getEntities().getAll().iterator();
			while (iterator.hasNext()) {
				Entity e = (Entity) iterator.next();
				if (e != null && e instanceof ItemEntity) {

					if (distanceBetweenTwoPoints(e.getX(), e.getY(), e.getZ(), source.getEntity().getX(), source.getEntity().getY(), source.getEntity().getZ()) <= range) {
						number++;
						e.remove(Entity.RemovalReason.KILLED);
					}

				}

			}

		}
		Component component = Component.literal(ChatFormatting.GREEN + "" + source.getTextName().toString() + ChatFormatting.GOLD + " despawned " + ChatFormatting.AQUA + number + ChatFormatting.GOLD + " dropped Items!");
		MinecraftServer.LOGGER.info(component.getString());

		source.getServer().getPlayerList().broadcastAll(new ClientboundSystemChatPacket(component, false));

		return 1;

	}

	private static int markRange(CommandSourceStack source, int range) throws CommandSyntaxException {
		int number = 0;
		if (range == -1) {
			source.sendSuccess(Component.literal(ChatFormatting.RED + "No range specified!"), true);
			return 0;
		} else {
			Iterator<Entity> iterator = source.getLevel().getEntities().getAll().iterator();
			while (iterator.hasNext()) {
				Entity e = (Entity) iterator.next();
				if (e != null && e instanceof ItemEntity) {
					if (distanceBetweenTwoPoints(e.getX(), e.getY(), e.getZ(), source.getEntity().getX(), source.getEntity().getY(), source.getEntity().getZ()) <= range)
						number++;
				}

			}

		}

		NoItemDespawn.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> source.getEntity().level.dimension()), new MarkPacket(source.getEntity().getX(), source.getEntity().getY(), source.getEntity().getZ(), range));

		source.sendSuccess(Component.literal(ChatFormatting.YELLOW + "Currently " + ChatFormatting.AQUA + number + ChatFormatting.YELLOW + " dropped Items exist!"), true);
		return 1;
	}

	private static int countRange(CommandSourceStack source, int range) throws CommandSyntaxException {
		int number = 0;
		boolean global = false;
		if (range == -1) {
			global = true;
			for (ServerLevel wS : source.getLevel().getServer().getAllLevels()) {
				Iterator<Entity> iterator = wS.getEntities().getAll().iterator();
				while (iterator.hasNext()) {
					Entity e = (Entity) iterator.next();
					if (e != null && e instanceof ItemEntity)
						number++;

				}

			}
		} else {
			Iterator<Entity> iterator = source.getLevel().getEntities().getAll().iterator();
			while (iterator.hasNext()) {
				Entity e = (Entity) iterator.next();
				if (e != null && e instanceof ItemEntity) {
					if (distanceBetweenTwoPoints(e.getX(), e.getY(), e.getZ(), source.getEntity().getX(), source.getEntity().getY(), source.getEntity().getZ()) <= range)
						number++;
				}

			}

		}

		String s = (global ? (ChatFormatting.RED + " (All dimensions included)") : "");
		source.sendSuccess(Component.literal(ChatFormatting.YELLOW + "Currently " + ChatFormatting.AQUA + number + ChatFormatting.YELLOW + " dropped Items exist!" + s), true);
		return 1;
	}

	public static double distanceBetweenTwoPoints(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt((z2 - z1) * (z2 - z1) + (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
}