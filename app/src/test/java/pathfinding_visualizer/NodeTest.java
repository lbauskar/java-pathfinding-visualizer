package pathfinding_visualizer;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the {@link Node} class.
 */
public class NodeTest {
    private Node a = new Node(0, 0);
    private Node b = new Node(0, 1);
    private Node c = new Node(1, 0);
    private Node d = new Node(0, 0, false);
    
    /**
     * Tests that the {@code Node} constructor works and returns 
     * a non-null {@code Node} object.
     */
    @Test
    public void makeNode() {
        assertNotNull(new Node(0, 0));
    }

    /**
     * Tests the {@code equals} function of {@code Node}.
     * <p>
     * The following should be true for any Nodes {@code a} and {@code b}:
     * <ul>
     * <li> If {@code a} and {@code b} are the exact same node, they are equal
     * <li> If {@code a} and {@code b} are different nodes with the same field values, they are equal
     * <li> If any field of {@code a} and {@code b} are not equal, {@code a} and {@code b} are not equal
     * <li> {@code a} is never equal to {@code null}
     * <li> {@code a} is never equal to an object of a different class.
     * </ul>
     */
    @Test
    public void nodeEquality() {
        assertEquals(a, new Node(0, 0));

        assertNotEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, d);

        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
    }


    /**
     * Tests the {@code compareTo} function of {@code Node}.
     * <p>
     * For any Nodes {@code a} and {@code b}:
     * <ul>
     * <li> if {@code a.row = b.row} and {@code a.col = b.col}, compare by the {@code reachable} field.
     * <li> if {@code a.row = b.row} and {@code a.col != b.col} compare by the {@code col} field
     * <li> otherwise, compare by the {@code row} field
     * </ul>
     */
    @Test
    public void nodeCompare() {
        assertEquals(0, a.compareTo(a));
        assertEquals(-1, a.compareTo(b));
        assertEquals(1, b.compareTo(a));

        assertEquals(-1, a.compareTo(c));
        assertEquals(1, a.compareTo(d));
    }

    /**
     * Tests the {@code toString} and {@code hashCode} functions of {@code Node}.
     * <ul>
     * <li> {@code toString} should have the format {@code "(row, col, reachable)"}
     * <li> {@code hashCode} should equal the hash of {@code toString}
     * </ul>
     */
    @Test
    public void nodeStringAndHash() {
        String aStr = String.format("(%d, %d, %b)", a.row, a.col, a.reachable);
        assertEquals(aStr, a.toString());

        assertEquals(aStr.hashCode(), a.hashCode());
    }
}
