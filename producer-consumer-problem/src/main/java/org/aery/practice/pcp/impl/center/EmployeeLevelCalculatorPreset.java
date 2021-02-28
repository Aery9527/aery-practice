package org.aery.practice.pcp.impl.center;

import org.aery.practice.pcp.api.center.EmployeeLevelCalculator;
import org.aery.practice.pcp.api.channel.enums.EmployeeHandleResult;
import org.aery.practice.pcp.error.EmployeeLevelException;
import org.springframework.stereotype.Component;

@Component
public class EmployeeLevelCalculatorPreset implements EmployeeLevelCalculator {

	/* [static] field */

	public static final int LEVEL_FACTOR_CEILING = 100;

	public static final int LEVEL_FACTOR_FLOOR = 1;

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/* [instance] constructor */

	/* [instance] method */

	/**
	 * <pre>
	 * if levelCount=3, then factor of level below
	 * level 0 = 100%
	 * level 1 = 66%
	 * level 2 = 33%
	 * 
	 * if levelCount=5, then factor of level below
	 * level 0 = 100%
	 * level 1 = 80%
	 * level 2 = 60%
	 * level 3 = 40%
	 * level 4 = 20%
	 * 
	 * </pre>
	 */
	@Override
	public int calculateLevelFactor(int lowestLevel, int currentLevel) {
		if (currentLevel < 0 || currentLevel > lowestLevel) {
			throw new EmployeeLevelException("lowestLevel(" + lowestLevel + "), currentLevel(" + currentLevel + ")");
		}

		boolean highestLevel = currentLevel == 0;
		if (highestLevel) {
			return LEVEL_FACTOR_CEILING;
		}

		final int levelCount = lowestLevel + 1;

		int piece = LEVEL_FACTOR_CEILING / levelCount;
		int pieceNumber = levelCount - currentLevel;
		int factor = piece * pieceNumber;
		return factor;
	}

	@Override
	public EmployeeHandleResult dice(int levelFactor) {
		double dice = (Math.random() * LEVEL_FACTOR_CEILING);
		boolean canProcessThisEvent = dice < levelFactor;
		return canProcessThisEvent ? EmployeeHandleResult.SUCCESS : EmployeeHandleResult.FAILURE;
	}

	/* [instance] getter/setter */

}
