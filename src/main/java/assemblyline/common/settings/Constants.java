package assemblyline.common.settings;

import electrodynamics.api.configuration.Configuration;
import electrodynamics.api.configuration.DoubleValue;

@Configuration(name = "Assembly Line")
public class Constants {
	@DoubleValue(def = 0.5, comment = "Usage is joules per entity movement.")
	public static double CONVEYORBELT_USAGE = 0.5;
	@DoubleValue(def = 0.5, comment = "Usage in watts.")
	public static double MANIPULATOR_USAGE = 0.5;
}
