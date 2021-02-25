package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the {@link Pair} class.
 */
public class PairTest {
    private Pair<Integer, Integer> a = new Pair<>(0, 0);
    private Pair<Integer, Integer> b = new Pair<>(0, 1);

    /**
     * Tests that the constructors of {@code Pair} don't throw exceptions
     * and that they return non-null Pair objects.
     */
    @Test
    public void makePair() {
        assertNotNull(new Pair<Integer, Integer>(0, 0));
        assertNotNull(new Pair<>(a));
    }

    /**
     * Tests the {@code equals} function of {@code Pair}.
     * <p>
     * For any Pair {@code a} and {@code b}.
     * <ul>
     * <li> if {@code a} and {@code b} are the same Pair, they are equal
     * <li> if every field in {@code a} and {@code b} are equal, {@code a} and {@code b} are equal.
     * <li> if {@code a} and  {@code b} contain a field that is not equal to the other, {@code a} and {@code b} are not equal
     * <li> {@code a} is never equal to {@code null}
     * <li> {@code a} is never equal to an object of a different class
     * <li> {@code a} is never equal to a Pair with different template parameters
     * </ul>
     */
    @Test
    public void pairEquality() {
        assertEquals(a, new Pair<Integer, Integer>(0, 0));

        assertNotEquals(a, b);
        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
        assertNotEquals(a, new Pair<Double, Double>(0.0, 0.0));
    }

    /**
     * Tests the {@code toString} and {@code hashCode} functions of {@code Pair}.
     * <ul>
     * <li> {@code toString} should return a String with the format {@code "(first, second)"}
     * <li> {@code hashCode} should be equal to the has to {@code toString}.
     */
    @Test
    public void pairStringAndHash() {
        String aStr = String.format("(%s, %s)", a.first.toString(), a.second.toString());
        assertEquals(aStr, a.toString());

        assertEquals(aStr.hashCode(), a.hashCode());
    }

}
