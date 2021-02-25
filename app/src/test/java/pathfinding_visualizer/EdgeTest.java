package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link Edge} class.
 */
public class EdgeTest {
    private Node n1 = new Node(1, 1);
    private Node n2 = new Node(1, 2);
    private Node n3 = new Node(2, 2);

    private Edge a = new Edge(n1, n2, 1.0);
    private Edge b = new Edge(n1, n3, 1.0);
    private Edge c = new Edge(n2, n3, 1.0);
    private Edge d = new Edge(n1, n2, 2.0);

    /**
     * Tests that the constructor makes a non-null Edge object.
     */
    @Test
    public void makeEdge() {
        assertNotNull(new Edge(n1, n2, 1.0));
    }

    /**
     * Tests the {@code equals} function of {@code Edge}.
     * The following should be true for any Edges {@code a} and {@code b}.
     * <ul>
     * <li> If {@code a} and {@code b} are the same Edge, they are equal
     * <li> If {@code a} and {@code b} have the same field values, they are equal
     * <li> If any field of {@code a} or {@code b} is not equal to the other, {@code a} and {@code b} are not equal
     * <li> {@code a} never equals {@code null}
     * <li> {@code a} never equals an object of a different class
     * </ul>
     */
    @Test
    public void edgeEquality() {
        assertEquals(new Edge(n1, n2, 1.0), a);

        assertNotEquals(a, b);
        assertNotEquals(a, d);
        assertNotEquals(a, c);

        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
    }

    /**
     * Tests the {@code compareTo} function of {@code Edge}.
     * For edges {@code a} and {@code b},
     * <ul>
     * <li> if {@code a} and {@code b} have equal {@code source}s and {@code dests}s, they should be compared by {@code weight}
     * <li> if {@code a} and {@code b} have equals {@code sources} but unequal {@code dest}s, they should be compared by {@code dest}
     * <li> otherwise, they should be compared by {@code source}
     * </ul>
     */
    @Test
    public void edgeComparison() {
        assertEquals(0, a.compareTo(a));
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(a));

        assertEquals(-1, a.compareTo(c));
        assertEquals(-1, a.compareTo(d));
    }

    /**
     * Tests the {@code toString} and {@code hashCode} functions of {@code Edge}.
     * {@code toString} should have the format {@code "(source, dest, weight)"}.
     * {@code hashCode} should be equal to the hash of {@code toString}.
     */
    @Test
    public void edgeStringAndHash() {
        String aStr = String.format("(%s, %s, %f)", a.source, a.dest, a.weight);

        assertEquals(aStr, a.toString());
        assertEquals(aStr.hashCode(), a.hashCode());
    }
}
