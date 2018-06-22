package ch.ethz.matsim.location_assignment.matsim.solver;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.router.StageActivityTypes;

import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentResult;
import ch.ethz.matsim.location_assignment.assignment.LocationAssignmentSolver;
import ch.ethz.matsim.location_assignment.matsim.MATSimAssignmentProblem;
import ch.ethz.matsim.location_assignment.matsim.utils.ActivityIndicesFinder;

public class MATSimAssignmentSolver {
	final private ActivityIndicesFinder indicesFinder;
	final private LocationAssignmentSolver solver;

	public MATSimAssignmentSolver(LocationAssignmentSolver solver, Set<String> variableActivityTypes,
			StageActivityTypes stageActivityTypes) {
		this.solver = solver;
		this.indicesFinder = new ActivityIndicesFinder(variableActivityTypes, stageActivityTypes);
	}

	public Collection<MATSimAssignmentProblem> createProblems(Plan plan) {
		return indicesFinder.findChainIndices(plan.getPlanElements()).stream().map(indices -> {
			return MATSimAssignmentProblem.create(plan, indices);
		}).collect(Collectors.toList());
	}

	public MATSimSolverResult solveProblem(MATSimAssignmentProblem problem) {
		LocationAssignmentResult assignmentResult = solver.solve(problem);
		return new MATSimSolverResult(problem, assignmentResult);
	}

	public Collection<MATSimSolverResult> solvePlan(Plan plan) {
		return createProblems(plan).stream().map(this::solveProblem).collect(Collectors.toList());
	}
}
