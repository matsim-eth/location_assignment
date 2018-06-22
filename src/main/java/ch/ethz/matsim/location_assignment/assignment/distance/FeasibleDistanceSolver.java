package ch.ethz.matsim.location_assignment.assignment.distance;

import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentProblem;

public interface FeasibleDistanceSolver {
	FeasibleDistanceResult solve(LocationAssignmentProblem problem);
}
