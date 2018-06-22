package ch.ethz.matsim.location_assignment.assignment.relaxation;

import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentProblem;
import ch.ethz.matsim.location_assignment.assignment.distance.FeasibleDistanceResult;

public interface RelaxedLocationSolver {
	RelaxedLocationResult solve(LocationAssignmentProblem problem, FeasibleDistanceResult feasibleDistanceResult);
}
