package org.magneton.test.test.model.generate.base;

import java.util.Random;

public abstract class AbstractGenericGenerator {

	private static Random random = null;

	public abstract String generate();

	protected Random getRandomInstance() {
		if (random == null) {
			random = new Random(System.currentTimeMillis());
		}

		return random;
	}

}
