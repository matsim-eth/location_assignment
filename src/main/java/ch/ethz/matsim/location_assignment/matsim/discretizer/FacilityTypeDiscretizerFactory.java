package ch.ethz.matsim.location_assignment.matsim.discretizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.core.utils.collections.QuadTree;
import org.matsim.facilities.ActivityFacilities;
import org.matsim.facilities.ActivityFacility;
import org.matsim.facilities.ActivityOption;

public class FacilityTypeDiscretizerFactory {
	final private Map<String, Map<Integer, QuadTree<FacilityLocation>>> index = new HashMap<>();
	final private Set<String> relevantActivityTypes = new HashSet<>();

	public FacilityTypeDiscretizerFactory(Set<String> relevantActivityTypes) {
		this.relevantActivityTypes.addAll(relevantActivityTypes);
	}

	public FacilityDiscretizer createDiscretizer(Activity act) {
		int zoneId = (int) act.getAttributes().getAttribute("tzone");
		if (!index.containsKey(act.getType())) {
			throw new IllegalArgumentException(String.format("Activity type '%s' is not registered", act.getType()));
		}
		if (!index.get(act.getType()).containsKey(zoneId)) {
			throw new IllegalArgumentException(String.format("Zone id '%s' is not registered", zoneId));
		}

		return new FacilityDiscretizer(index.get(act.getType()).get(zoneId));
	}

	public void loadFacilities(ActivityFacilities facilities) {
		Set<String> types = facilities.getFacilities().values().stream().map(f -> f.getActivityOptions().values())
				.flatMap(Collection::stream).map(ActivityOption::getType).collect(Collectors.toSet());
		types.retainAll(relevantActivityTypes);

		Set<Integer> zoneIds = new HashSet<>();
		for (Id<ActivityFacility> facilityId : facilities.getFacilities().keySet()) {
			zoneIds.add((int) facilities.getFacilityAttributes().getAttribute(facilityId.toString(), "tzone"));
		}

		double minX = facilities.getFacilities().values().stream().map(ActivityFacility::getCoord)
				.mapToDouble(Coord::getX).min().getAsDouble();
		double maxX = facilities.getFacilities().values().stream().map(ActivityFacility::getCoord)
				.mapToDouble(Coord::getX).max().getAsDouble();
		double minY = facilities.getFacilities().values().stream().map(ActivityFacility::getCoord)
				.mapToDouble(Coord::getY).min().getAsDouble();
		double maxY = facilities.getFacilities().values().stream().map(ActivityFacility::getCoord)
				.mapToDouble(Coord::getY).max().getAsDouble();

		for (String type : types) {
			if (!index.containsKey(type)) {
				index.put(type, new HashMap<>());
			}
			for (int zoneId : zoneIds)	{
				if (!index.get(type).containsKey(zoneId))	{
					index.get(type).put(zoneId, new QuadTree<FacilityLocation>(minX, minY, maxX, maxY));
				}
			}
		}

		for (ActivityFacility facility : facilities.getFacilities().values()) {
			int zoneId = (int) facilities.getFacilityAttributes().getAttribute(facility.getId().toString(), "tzone");
			FacilityLocation facilityLocation = new FacilityLocation(facility, zoneId);
			Coord coord = facility.getCoord();

			for (ActivityOption option : facility.getActivityOptions().values()) {
				String activityType = option.getType();

				if (relevantActivityTypes.contains(activityType)) {
					index.get(activityType).get(zoneId).put(coord.getX(), coord.getY(), facilityLocation);
				}
			}
		}
	}
}
