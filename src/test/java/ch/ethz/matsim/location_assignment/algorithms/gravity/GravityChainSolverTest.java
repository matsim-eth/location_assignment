package ch.ethz.matsim.location_assignment.algorithms.gravity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

import ch.ethz.matsim.location_assignment.algorithms.gravity.GravityChainProblem;
import ch.ethz.matsim.location_assignment.algorithms.gravity.GravityChainResult;
import ch.ethz.matsim.location_assignment.algorithms.gravity.GravityChainSolver;
import ch.ethz.matsim.location_assignment.algorithms.gravity.initial.GravityInitialLocationGenerator;
import ch.ethz.matsim.location_assignment.algorithms.gravity.initial.LateralDeviationGenerator;
import ch.ethz.matsim.location_assignment.test_utils.RandomMock;

public class GravityChainSolverTest {
	@Test
	public void testMultiTripCase() {
		Vector2D originLocation = new Vector2D(0.0, 0.0);
		Vector2D destinationLocation = new Vector2D(10.0, 0.0);

		List<Double> targetDistances = Arrays.asList(8.0, 6.0, 5.0);
		GravityChainProblem problem = new GravityChainProblem(originLocation, destinationLocation, targetDistances);

		Random random = new Random(0);
		GravityInitialLocationGenerator generator = new LateralDeviationGenerator(random, 0.1);

		GravityChainSolver solver = new GravityChainSolver(0.1, 1000, 1e-3, random, generator);
		GravityChainResult result = solver.solve(problem);

		Assert.assertTrue(result.isConverged());
		Assert.assertTrue(result.isFeasible());

		Assert.assertEquals(8.0, problem.getOriginLocation().distance(result.getLocations().get(0)), 1e-3);
		Assert.assertEquals(6.0, result.getLocations().get(0).distance(result.getLocations().get(1)), 1e-3);
		Assert.assertEquals(5.0, problem.getDestinationLocation().distance(result.getLocations().get(1)), 1e-3);
	}

	@Test
	public void testTwoTripsCase_Intersection() {
		Vector2D originLocation = new Vector2D(0.0, 0.0);
		Vector2D destinationLocation = new Vector2D(10.0, 0.0);

		List<Double> targetDistances = Arrays.asList(8.0, 6.0);
		GravityChainProblem problem = new GravityChainProblem(originLocation, destinationLocation, targetDistances);

		Random random = new RandomMock(() -> 0.0);
		GravityInitialLocationGenerator generator = new LateralDeviationGenerator(random, 0.0);

		GravityChainSolver solver = new GravityChainSolver(0.1, 1000, 1e-3, random, generator);
		GravityChainResult result = solver.solve(problem);

		Assert.assertTrue(result.isConverged());
		Assert.assertTrue(result.isFeasible());
	}

	@Test
	public void testTwoTripsCase_Inclusion() {
		Vector2D originLocation = new Vector2D(0.0, 0.0);
		Vector2D destinationLocation = new Vector2D(10.0, 0.0);

		List<Double> targetDistances = Arrays.asList(100.0, 5.0);
		GravityChainProblem problem = new GravityChainProblem(originLocation, destinationLocation, targetDistances);

		Random random = new RandomMock(() -> 0.0);
		GravityInitialLocationGenerator generator = new LateralDeviationGenerator(random, 0.0);

		GravityChainSolver solver = new GravityChainSolver(0.1, 1000, 1e-3, random, generator);
		GravityChainResult result = solver.solve(problem);

		Assert.assertFalse(result.isConverged());
		Assert.assertFalse(result.isFeasible());

		Assert.assertEquals(1, result.getLocations().size());
	}

	@Test
	public void testTwoTripsCase_NoIntersection() {
		Vector2D originLocation = new Vector2D(0.0, 0.0);
		Vector2D destinationLocation = new Vector2D(10.0, 0.0);

		List<Double> targetDistances = Arrays.asList(3.0, 1.0);
		GravityChainProblem problem = new GravityChainProblem(originLocation, destinationLocation, targetDistances);

		Random random = new RandomMock(() -> 0.0);
		GravityInitialLocationGenerator generator = new LateralDeviationGenerator(random, 0.0);

		GravityChainSolver solver = new GravityChainSolver(0.1, 1000, 1e-3, random, generator);
		GravityChainResult result = solver.solve(problem);

		Assert.assertFalse(result.isConverged());
		Assert.assertFalse(result.isFeasible());

		Assert.assertEquals(1, result.getLocations().size());

		Assert.assertEquals(7.5, result.getLocations().get(0).getX(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(0).getY(), 1e-3);
	}

	@Test
	public void testExactCase() {
		Vector2D originLocation = new Vector2D(0.0, 0.0);
		Vector2D destinationLocation = new Vector2D(10.0, 0.0);

		List<Double> targetDistances = Arrays.asList(2.5, 2.5, 2.5, 2.5);
		GravityChainProblem problem = new GravityChainProblem(originLocation, destinationLocation, targetDistances);

		Random random = new RandomMock(() -> 0.0);
		GravityInitialLocationGenerator generator = new LateralDeviationGenerator(random, 0.0);

		GravityChainSolver solver = new GravityChainSolver(0.1, 1000, 1e-3, random, generator);
		GravityChainResult result = solver.solve(problem);

		Assert.assertTrue(result.isConverged());
		Assert.assertTrue(result.isFeasible());

		Assert.assertEquals(3, result.getLocations().size());

		Assert.assertEquals(2.5, result.getLocations().get(0).getX(), 1e-3);
		Assert.assertEquals(5.0, result.getLocations().get(1).getX(), 1e-3);
		Assert.assertEquals(7.5, result.getLocations().get(2).getX(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(0).getY(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(1).getY(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(2).getY(), 1e-3);
	}
	
	@Test
	public void testExactCaseWithZeroDistances() {
		Vector2D originLocation = new Vector2D(0.0, 0.0);
		Vector2D destinationLocation = new Vector2D(0.0, 0.0);

		List<Double> targetDistances = Arrays.asList(0.0, 0.0, 0.0, 0.0);
		GravityChainProblem problem = new GravityChainProblem(originLocation, destinationLocation, targetDistances);

		Random random = new RandomMock(() -> 0.0);
		GravityInitialLocationGenerator generator = new LateralDeviationGenerator(random, 0.0);

		GravityChainSolver solver = new GravityChainSolver(0.1, 1000, 1e-3, random, generator);
		GravityChainResult result = solver.solve(problem);

		Assert.assertTrue(result.isConverged());
		Assert.assertTrue(result.isFeasible());

		Assert.assertEquals(3, result.getLocations().size());

		Assert.assertEquals(0.0, result.getLocations().get(0).getX(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(1).getX(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(2).getX(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(0).getY(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(1).getY(), 1e-3);
		Assert.assertEquals(0.0, result.getLocations().get(2).getY(), 1e-3);
	}
}
