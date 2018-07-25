package ch.ethz.matsim.location_assignment.matsim.discretizer;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.matsim.facilities.ActivityFacility;

import ch.ethz.matsim.location_assignment.assignment.discretization.DiscreteLocation;

public class FacilityLocation implements DiscreteLocation {
	final private Vector2D location;
	final private ActivityFacility facility;
	private final int zoneId;

	public FacilityLocation(ActivityFacility facility, int zoneId) {
		this.location = new Vector2D(facility.getCoord().getX(), facility.getCoord().getY());
		this.facility = facility;
		this.zoneId = zoneId;
	}

	@Override
	public Vector2D getLocation() {
		return location;
	}

	public ActivityFacility getFacility() {
		return facility;
	}

	public int getZoneId()	{
		return zoneId;
	}
}
