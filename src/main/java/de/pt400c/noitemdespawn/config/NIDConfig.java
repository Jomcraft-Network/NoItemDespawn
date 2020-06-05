package de.pt400c.noitemdespawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NIDConfig {

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	public static final Common COMMON = new Common(COMMON_BUILDER);

	public static final ForgeConfigSpec COMMON_SPEC = COMMON_BUILDER.build();

	public static class Common {
		public final ForgeConfigSpec.IntValue maxDespawnRadius;
		
		public final ForgeConfigSpec.IntValue maxClumpSize;
		
		public final ForgeConfigSpec.IntValue clumpRadius;

		Common(ForgeConfigSpec.Builder builder) {

			builder.push("Common");

			String desc = "Radius in which your users can despawn items";
			maxDespawnRadius = builder.comment(desc).defineInRange("maxDespawnRadius", 10, 0, 900000000);
			
			desc = "Maximum size of item clumps with the same itemtype";
			maxClumpSize = builder.comment(desc).defineInRange("maxClumpSize", 10, 1, 10000);
			
			desc = "Radius which is used to determine whether item groups belong to clumps";
			clumpRadius = builder.comment(desc).defineInRange("clumpRadius", 3, 1, 10);

			builder.pop();
		}
	}

}