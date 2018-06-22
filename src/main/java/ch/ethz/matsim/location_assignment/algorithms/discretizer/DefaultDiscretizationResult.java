package ch.ethz.matsim.location_assignment.algorithms.discretizer;

import ch.ethz.matsim.location_assignment.assignment.discretization.DiscreteLocation;

public class DefaultDiscretizationResult implements DiscretizerResult {
	final private DiscreteLocation location;

	public DefaultDiscretizationResult(DiscreteLocation location) {
		this.location = location;
	}

	@Override
	public DiscreteLocation getLocation() {
		return location;
	}
}
