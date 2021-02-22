package pathfinding_visualizer;

import java.io.Serializable;

public class Pair<T extends Serializable, U extends Serializable> implements Serializable {
    private static final long serialVersionUID = -8529217112653744086L;
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.setFirst(first);
        this.setSecond(second);
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
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
            boolean firstEquals = this.first.equals(p.getFirst());
            boolean secondEquals = this.second.equals(p.getSecond());
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
