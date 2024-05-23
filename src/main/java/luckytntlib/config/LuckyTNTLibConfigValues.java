package luckytntlib.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LuckyTNTLibConfigValues {

	public static ModConfigSpec.BooleanValue PERFORMANT_EXPLOSION;
	public static ModConfigSpec.DoubleValue EXPLOSION_PERFORMANCE_FACTOR;
	
	public static void registerConfig(ModConfigSpec.Builder builder) {
		builder.comment("General Explosions").push("Performance");
		PERFORMANT_EXPLOSION = builder.comment("Whether or not an explosion should be used that has more performance at the cost of detail").define("performantExplosion", true);
		EXPLOSION_PERFORMANCE_FACTOR = builder.comment("Higher values give more performance at the cost of details, lower values give more details at the cost of performance").defineInRange("explosionPerformanceFactor", 0.3d, 0.3d, 0.6d);
	}
}
