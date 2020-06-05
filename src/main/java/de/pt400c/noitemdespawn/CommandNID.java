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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class CommandNID {

	private static boolean first = true;

	protected static void register(FMLServerStartingEvent event) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("noitemdespawn").requires((player) -> {
	         return player.hasPermissionLevel(2);
	    });
		
		literalargumentbuilder.then(Commands.literal("count").executes((command) -> {
	         return countRange(command.getSource(), -1);
	      }).then(Commands.argument("range", StringArgumentType.string()).executes((command) -> {
	         return countRange(command.getSource(), Integer.parseInt(StringArgumentType.getString(command, "range")));
	      }))).then(Commands.literal("despawn").executes((command) -> {
		         return deleteRange(command.getSource(), -1);
		      }).then(Commands.argument("range", StringArgumentType.string()).executes((command) -> {
		         return deleteRange(command.getSource(), Integer.parseInt(StringArgumentType.getString(command, "range")));
		      })));

		event.getCommandDispatcher().register(literalargumentbuilder);
	}
	
	private static int deleteRange(CommandSource source, int range) throws CommandSyntaxException {
		int number = 0;
		
		if(first) {
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "In this session you didn't use the despawn command before! Please be really careful concerning the range you choose. Deleting the items cannot be undone!"), true);
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
		source.getServer().getPlayerList().sendMessage(new StringTextComponent(TextFormatting.GREEN + "" + source.getName().toString() + TextFormatting.GOLD + " despawned " + TextFormatting.AQUA + number + TextFormatting.GOLD + " dropped Items!"), true);
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