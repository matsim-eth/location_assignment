package ch.ethz.matsim.location_assignment.matsim.setup;

import java.util.List;

import ch.ethz.matsim.location_assignment.algorithms.DistanceSampler;
import ch.ethz.matsim.location_assignment.algorithms.SamplingFeasibleDistanceSolver.DistanceSamplerProvider;
import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentProblem;
import ch.ethz.matsim.location_assignment.matsim.MATSimAssignmentProblem;

public interface MATSimDistanceSamplerProvider {
	List<DistanceSampler> getDistanceSamplers(MATSimAssignmentProblem problem);

	static public class Adapter implements DistanceSamplerProvider {
		final private MATSimDistanceSamplerProvider delegate;

		public Adapter(MATSimDistanceSamplerProvider delegate) {
			this.delegate = delegate;
		}

		@Override
		public List<DistanceSampler> getDistanceSamplers(LocationAssignmentProblem problem) {
			return delegate.getDistanceSamplers((MATSimAssignmentProblem) problem);
		}
	}
}
