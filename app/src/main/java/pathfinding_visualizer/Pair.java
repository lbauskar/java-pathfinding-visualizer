package pathfinding_visualizer;

import java.io.Serializable;

public class Pair<T extends Serializable, U extends Serializable> implements Serializable {
    private static final long serialVersionUID = -8529217112653744086L;
    public T first;
    public U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }


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

    @Override
    public String toString() {
        return String.format("(%s, %s)", first.toString(), second.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
