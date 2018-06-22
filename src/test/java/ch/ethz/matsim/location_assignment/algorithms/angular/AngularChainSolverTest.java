package ch.ethz.matsim.location_assignment.algorithms.angular;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

import ch.ethz.matsim.location_assignment.algorithms.angular.AngularTailProblem;
import ch.ethz.matsim.location_assignment.algorithms.angular.AngularTailResult;
import ch.ethz.matsim.location_assignment.algorithms.angular.AngularTailSolver;
import ch.ethz.matsim.location_assignment.test_utils.RandomMock;

public class AngularChainSolverTest {
	@Test
	public void testAngularChainSolver() {
		Vector2D anchorLocation = new Vector2D(0.0, 0.0);

		List<Double> targetDistances = Arrays.asList(8.0, 6.0, 5.0);

		List<Double> values = new LinkedList<>(Arrays.asList(0.0, 0.25, 0.0));
		Random random = new RandomMock(() -> values.remove(0));

		AngularTailProblem problem = new AngularTailProblem(anchorLocation, targetDistances);
		AngularTailSolver solver = new AngularTailSolver(random);
		AngularTailResult result = solver.solve(problem);

		Assert.assertEquals(8.0, result.getLocations().get(0).getX(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(0).getY(), 1e-3);

		Assert.assertEquals(8.0, result.getLocations().get(1).getX(), 1e-3);
		Assert.assertEquals(6.0, result.getLocations().get(1).getY(), 1e-3);

		Assert.assertEquals(13.0, result.getLocations().get(2).getX(), 1e-3);
		Assert.assertEquals(6.0, result.getLocations().get(2).getY(), 1e-3);
	}
}
