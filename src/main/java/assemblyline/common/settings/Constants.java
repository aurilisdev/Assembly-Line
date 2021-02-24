package assemblyline.common.settings;

import electrodynamics.api.configuration.Configuration;
import electrodynamics.api.configuration.DoubleValue;

@Configuration(name = "Assembly Line")
public class Constants {
    @DoubleValue(def = 8.0, comment = "Usage is joules per entity movement.")
    public static double CONVEYORBELT_USAGE = 8.0;
    @DoubleValue(def = 8.0, comment = "Usage is joules per entity movement.")
    public static double SORTERBELT_USAGE = 8.0;
    @DoubleValue(def = 1.0, comment = "Usage in watts.")
    public static double MANIPULATOR_USAGE = 1.0;
}
