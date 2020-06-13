/* 
 *      NoItemDespawn - 1.15.2 <> Idea and codedesign by PT400C - Config class
 *      © Jomcraft Network 2020
 */
package de.pt400c.noitemdespawn.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class NIDConfig {

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	public static final Common COMMON = new Common(COMMON_BUILDER);

	public static final ForgeConfigSpec COMMON_SPEC = COMMON_BUILDER.build();

	public static class Common {
		public final ForgeConfigSpec.IntValue maxDespawnRadius;
		
		public final ForgeConfigSpec.IntValue maxClumpSize;
		
		public final ForgeConfigSpec.IntValue clumpRadius;
		
		public final ForgeConfigSpec.ConfigValue<List<String>> despawnWhitelist;
		
		public final ForgeConfigSpec.IntValue markTicks;

		Common(ForgeConfigSpec.Builder builder) {

			builder.push("Common");

			String desc = "Radius in which your users can despawn items";
			maxDespawnRadius = builder.comment(desc).defineInRange("maxDespawnRadius", 10, 0, 900000000);
			
			desc = "Maximum size of item clumps with the same itemtype";
			maxClumpSize = builder.comment(desc).defineInRange("maxClumpSize", 10, 1, 10000);
			
			desc = "Radius which is used to determine whether item groups belong to clumps";
			clumpRadius = builder.comment(desc).defineInRange("clumpRadius", 3, 1, 10);
			
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add("minecraft:egg");
			
			desc = "Whitelist of items to despawn in clumps. If all items in clumps should despawn, just place '*' in here.";
			despawnWhitelist = builder.comment(desc).define("despawnWhitelist", arrayList);
			
			desc = "How many ticks items will be marked for";
			markTicks = builder.comment(desc).defineInRange("markTicks", 1000, 10, 100000);

			builder.pop();
		}
	}

}