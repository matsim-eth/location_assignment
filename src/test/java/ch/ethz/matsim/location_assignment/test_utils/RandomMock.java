package ch.ethz.matsim.location_assignment.test_utils;

import java.util.Random;
import java.util.function.DoubleSupplier;

public class RandomMock extends Random {
	private static final long serialVersionUID = 1L;

	final private DoubleSupplier supplier;

	public RandomMock(DoubleSupplier supplier) {
		this.supplier = supplier;
	}

	public double nextDouble() {
		return supplier.getAsDouble();
	}
}
