package ch.ethz.matsim.location_assignment.matsim.setup;

import java.util.List;

import ch.ethz.matsim.location_assignment.algorithms.DiscretizerSolver.DiscretizerProvider;
import ch.ethz.matsim.location_assignment.algorithms.discretizer.Discretizer;
import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentProblem;
import ch.ethz.matsim.location_assignment.matsim.MATSimAssignmentProblem;

public interface MATSimDiscretizerProvider {
	List<Discretizer> getDiscretizers(MATSimAssignmentProblem problem);

	static public class Adapter implements DiscretizerProvider {
		final private MATSimDiscretizerProvider delegate;

		public Adapter(MATSimDiscretizerProvider delegate) {
			this.delegate = delegate;
		}

		@Override
		public List<Discretizer> getDiscretizers(LocationAssignmentProblem problem) {
			return delegate.getDiscretizers((MATSimAssignmentProblem) problem);
		}
	}
}
