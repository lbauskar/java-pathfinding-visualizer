package pathfinding_visualizer;

import java.io.Serializable;

/**
 * A simple structure that contains two serializable objects. Both objects are publicly accessible.
 */
public class Pair<T extends Serializable, U extends Serializable> implements Serializable {
    private static final long serialVersionUID = -8529217112653744086L;
    /**
     * A serializable object.
     */
    public T first;
    /**
     * A serializable object.
     */
    public U second;

    /**
     * Creates a {@link Pair} containing {@code first} and {@code second}.
     * 
     * @param first serializable object
     * @param second serializable object
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }


    /**
     * Checks if {@code other} is the same class as this Pair, and that the {@code first} and {@code second} fields 
     * of both pairs are equal to each other.
     * 
     * @return whether the value of {@code other} equals the value of this pair
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            Pair<T, U> p;
            try {
                p = (Pair<T, U>) other;
            } catch (ClassCastException e) {
                return false;
            }
            boolean firstEquals = this.first.equals(p.first);
            boolean secondEquals = this.second.equals(p.second);
            return firstEquals && secondEquals;
        }
    }


    /**
     * Creates the string representations of the {@code first} and {@code second} fields enclosed 
     * in parenthesis and separated by a comma - like "(x, y)".
     * 
     * @return String in the form {@code (first, second)}
     */
    @Override
    public String toString() {
        return String.format("(%s, %s)", first.toString(), second.toString());
    }

    /**
     * {@inheritDoc}
     * 
     * @return hash code of this Pair's String representation
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
