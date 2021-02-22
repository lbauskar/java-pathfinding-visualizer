package pathfinding_visualizer;

import java.io.Serializable;

public class Edge implements Comparable<Edge>, Serializable {
    private static final long serialVersionUID = 4320064514277253414L;
    private Node source;
    private Node dest;
    private double weight;

    Edge(Node a, Node b) {
        this(a, b, 1);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Edge(Node source, Node dest, double weight) {
        this.setSource(source);
        this.setDest(dest);
        this.setWeight(weight);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %f)", source, dest, weight);
    }

    @Override
    public int compareTo(Edge other) {
        int sourceComp = this.source.compareTo(other.getSource());
        int destComp = this.dest.compareTo(other.getDest());
        int weightComp = ((Double)this.weight).compareTo(other.getWeight());

        if (sourceComp == 0 && destComp == 0) {
            return weightComp;
        } else if (sourceComp == 0) {
            return destComp;
        } else {
            return sourceComp;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            Edge e = (Edge) other;
            boolean sourceEqual = this.source.equals(e.getSource());
            boolean destEqual = this.dest.equals(e.getDest());
            boolean weightEqual = this.weight == e.getWeight();
            return sourceEqual && destEqual && weightEqual;
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
