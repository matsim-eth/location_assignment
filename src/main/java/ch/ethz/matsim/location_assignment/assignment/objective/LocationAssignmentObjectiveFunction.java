package ch.ethz.matsim.location_assignment.assignment.objective;

import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentProblem;
import ch.ethz.matsim.location_assignment.assignment.discretization.DiscreteLocationResult;
import ch.ethz.matsim.location_assignment.assignment.distance.FeasibleDistanceResult;
import ch.ethz.matsim.location_assignment.assignment.relaxation.RelaxedLocationResult;

public interface LocationAssignmentObjectiveFunction {
	LocationAssignmentObjective computeObjective(LocationAssignmentProblem problem,
			FeasibleDistanceResult feasibleDistanceResult, RelaxedLocationResult relaxedResult,
			DiscreteLocationResult discreteResult);
}
