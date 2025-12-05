package assemblyline.common.settings;

import net.neoforged.neoforge.common.ModConfigSpec;

public class AssemblyLineConfig {
    public static AssemblyLineConfig INSTANCE;

    public ModConfigSpec SPEC;
    public ModConfigSpec.DoubleValue CONVEYORBELT_USAGE;
    public ModConfigSpec.DoubleValue SORTERBELT_USAGE;
    public ModConfigSpec.DoubleValue AUTOCRAFTER_USAGE;
    public ModConfigSpec.DoubleValue BLOCKBREAKER_USAGE;
    public ModConfigSpec.DoubleValue BLOCKPLACER_USAGE;
    public ModConfigSpec.DoubleValue RANCHER_USAGE;
    public ModConfigSpec.DoubleValue MOBGRINDER_USAGE;
    public ModConfigSpec.DoubleValue FARMER_USAGE;
    public ModConfigSpec.IntValue CONVEYOR_MAX_SPREAD;

    public AssemblyLineConfig() {
	ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
	builder.push("common");
	CONVEYORBELT_USAGE = builder.comment("Usage is joules per tick").defineInRange("conveyorbelt_usage", 8.0, 0,
		Double.MAX_VALUE);
	SORTERBELT_USAGE = builder.comment("Usage is joules per entity movement").defineInRange("sorterbelt_usage", 8.0,
		0, Double.MAX_VALUE);
	AUTOCRAFTER_USAGE = builder.comment("Usage is joules per craft").defineInRange("autocrafter_usage", 200.0, 0,
		Double.MAX_VALUE);
	BLOCKBREAKER_USAGE = builder.comment("Usage is per tick").defineInRange("blockbreaker_usage", 20.0, 0,
		Double.MAX_VALUE);
	BLOCKPLACER_USAGE = builder.comment("Usage is per usage").defineInRange("blockplacer_usage", 40.0, 0,
		Double.MAX_VALUE);
	RANCHER_USAGE = builder.comment("Usage is per usage").defineInRange("rancher_usage", 40.0, 0, Double.MAX_VALUE);
	MOBGRINDER_USAGE = builder.comment("Usage is per usage").defineInRange("mobgrinder_usage", 40.0, 0,
		Double.MAX_VALUE);
	FARMER_USAGE = builder.comment("Usage is per usage").defineInRange("farmer_usage", 40.0, 0, Double.MAX_VALUE);
	CONVEYOR_MAX_SPREAD = builder.comment("How many additional conveyers a single one can power")
		.defineInRange("conveyor_max_spread", 16, 0, Integer.MAX_VALUE);
	builder.pop();
	SPEC = builder.build();
    }
}
