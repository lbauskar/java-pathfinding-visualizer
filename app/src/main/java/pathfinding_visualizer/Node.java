package pathfinding_visualizer;

import java.io.Serializable;

/**
 * Representation of a graph's node or vertex.
 */
public class Node implements Comparable<Node>, Serializable {
    private static final long serialVersionUID = -6429126713188723800L;
    /**
     * Row of 2D array this is in
     */
    public int row;
    /**
     * Column of 2D array this is in
     */
    public int col;
    /**
     * Whether other edges should be able to have this as a destination node or whether this has neighbors
     */
    public boolean reachable;


    /**
     * Creates a Node that may or may not be reachable.
     * 
     * @param row row of 2D array this is in
     * @param col column of 2D array this is in 
     * @param reachable boolean for if an Edge can contain this Node
     */
    public Node(int row, int col, boolean reachable) {
        this.row = row;
        this.col = col;
        this.reachable = reachable;
    }

    /**
     * Creates a Node that is reachable.
     * 
     * @param row row of 2D array this is in
     * @param col column of 2D array this is in
     */
    public Node(int row, int col) {
        this(row, col, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @return String representation of this Node - {@code "(row, col)"}
     */
    @Override
    public String toString() {
        return String.format("(%s, %s)", row, col);
    }

    /**
     * Compares {@code this} Node to the {@code other} Node on the basis of their {@code row}
     * field, then their {@code col} field - if necessary.
     * 
     * @return -1 if {@code this} is less than {@code other}, 0 if {@code this} equals {@code other},
     * or 1 if {@code this} is greater than {@code other}
     */
    @Override
    public int compareTo(Node other) {
        int rowComp = ((Integer) this.row).compareTo(other.row);
        int colComp = ((Integer) this.col).compareTo(other.col);
        if (rowComp == 0) {
            return colComp;
        } else {
            return rowComp;
        }
    }

    /**
     * Checks if the {@code other} object is also a {@code Node}, then if {@code this}
     * and {@code other} have equal {@code row}, {@code col}, and {@code reachable} values.
     * 
     * @return {@code true} if {@code other} is the same class as {@code this}, and
     * all of their fields are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            Node n = (Node) other;
            boolean rowEqual = this.row == n.row;
            boolean colEqual = this.col == n.col;
            boolean reachEqual = this.reachable == n.reachable;

            return rowEqual && colEqual && reachEqual;
        }
    }

    /**
     * Hashes the string representation of this Node.
     * 
     * @return hash code of string representation
     */
    @Override
    public int hashCode() {
        char reachChar = reachable ? 't' : 'f';
        return (this.toString() + reachChar).hashCode();
    }
}
