package assemblyline.prefab.utils;

import java.util.Collections;
import java.util.List;

import voltaic.api.misc.ILocationStorage;
import voltaic.prefab.utilities.object.Location;

public class AssemblyCapabilityUtils {

    public static final ILocationStorage EMPTY_LOCATION = new ILocationStorage() {

	@Override
	public void setLocation(int arg0, double arg1, double arg2, double arg3) {

	}

	@Override
	public void removeLocation(Location arg0) {

	}

	@Override
	public List<Location> getLocations() {
	    return Collections.emptyList();
	}

	@Override
	public Location getLocation(int arg0) {
	    return new Location();
	}

	@Override
	public void clearLocations() {

	}

	@Override
	public void addLocation(double arg0, double arg1, double arg2) {

	}
    };

}
