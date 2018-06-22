package ch.ethz.matsim.location_assignment.assignment.objective;

public interface LocationAssignmentObjective {
	boolean isConverged();

	double getValue();
}
