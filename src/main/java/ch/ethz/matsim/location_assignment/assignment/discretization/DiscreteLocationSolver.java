package ch.ethz.matsim.location_assignment.assignment.discretization;

import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentProblem;
import ch.ethz.matsim.location_assignment.assignment.distance.FeasibleDistanceResult;
import ch.ethz.matsim.location_assignment.assignment.relaxation.RelaxedLocationResult;

public interface DiscreteLocationSolver {
	DiscreteLocationResult solve(LocationAssignmentProblem problem, FeasibleDistanceResult feasibleDistanceResult,
			RelaxedLocationResult relaxedLocationResult);
}
