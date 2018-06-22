package ch.ethz.matsim.location_assignment.matsim.solver;

import org.matsim.api.core.v01.population.Plan;

import ch.ethz.matsim.location_assignment.matsim.MATSimAssignmentProblem;

public class MATSimSolverProblem {
	final private Plan plan;
	final private MATSimAssignmentProblem assignmentProblem;

	public MATSimSolverProblem(Plan plan, MATSimAssignmentProblem assignmentProblem) {
		this.plan = plan;
		this.assignmentProblem = assignmentProblem;
	}

	public Plan getPlan() {
		return plan;
	}

	public MATSimAssignmentProblem getAssignmentProblem() {
		return assignmentProblem;
	}
}
