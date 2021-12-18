package assemblyline.common.settings;

import electrodynamics.api.configuration.Configuration;
import electrodynamics.api.configuration.DoubleValue;

@Configuration(name = "Assembly Line")
public class Constants {
	@DoubleValue(def = 8.0, comment = "Usage is joules per tick")
	public static double CONVEYORBELT_USAGE = 8.0;
	@DoubleValue(def = 8.0, comment = "Usage is joules per entity movement.")
	public static double SORTERBELT_USAGE = 8.0;
	@DoubleValue(def = 200.0, comment = "Usage is per craft.")
	public static double AUTOCRAFTER_USAGE = 200.0;
	@DoubleValue(def = 20.0, comment = "Usage is per tick.")
	public static double BLOCKBREAKER_USAGE = 20.0;
	@DoubleValue(def = 40.0, comment = "Usage is per usage.")
	public static double BLOCKPLACER_USAGE = 40.0;

}
