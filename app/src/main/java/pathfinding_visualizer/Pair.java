package pathfinding_visualizer;

public class Pair<T, U> {
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
