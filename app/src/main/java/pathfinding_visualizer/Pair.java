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
}
