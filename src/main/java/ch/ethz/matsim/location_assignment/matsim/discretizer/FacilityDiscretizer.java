package ch.ethz.matsim.location_assignment.matsim.discretizer;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.matsim.core.utils.collections.QuadTree;

import ch.ethz.matsim.location_assignment.algorithms.discretizer.DefaultDiscretizationResult;
import ch.ethz.matsim.location_assignment.algorithms.discretizer.Discretizer;
import ch.ethz.matsim.location_assignment.algorithms.discretizer.DiscretizerResult;

public class FacilityDiscretizer implements Discretizer {
	final private QuadTree<FacilityLocation> candidates;

	public FacilityDiscretizer(QuadTree<FacilityLocation> candidates) {
		this.candidates = candidates;
	}

	@Override
	public DiscretizerResult discretize(Vector2D location) {
		return new DefaultDiscretizationResult(candidates.getClosest(location.getX(), location.getY()));
	}
}
