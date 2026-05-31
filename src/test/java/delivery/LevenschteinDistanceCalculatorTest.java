package delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LevenschteinDistanceCalculatorTest {
    private static final double EPSILON = 0.0001;

    @Test
    void exactMatchHasZeroDistance() {
        DistanceCalculator calculator = new LevenschteinDistanceCalculator();

        assertEquals(0.0, calculator.calculateDistance("same address", "same address"), EPSILON);
    }

    @Test
    void worstCaseHasOneHundredKilometersDistance() {
        DistanceCalculator calculator = new LevenschteinDistanceCalculator();

        assertEquals(100.0, calculator.calculateDistance("", "14 Mail Pierre Potier, Gif sur Yvette"), EPSILON);
        assertEquals(100.0, calculator.calculateDistance("abc", "xyz"), EPSILON);
    }

    @Test
    void partialDifferenceIsNormalizedByLongestAddressLength() {
        DistanceCalculator calculator = new LevenschteinDistanceCalculator();

        assertEquals(25.0, calculator.calculateDistance("abcd", "abxd"), EPSILON);
    }
}
