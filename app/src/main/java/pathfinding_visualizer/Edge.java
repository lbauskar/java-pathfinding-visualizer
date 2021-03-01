package pathfinding_visualizer;
/**
 * Representation of a graph's edge.
 */
public class Edge implements Comparable<Edge> {
    /**
     * Node this Edge is leaving (source node)
     */
    public Node source;
    /**
     * Node this Edge is going towards (destination node)
     */
    public Node dest;
    /**
     * The cost of traversing this Edge
     */
    public double weight;

    /**
     * Creates a directed edge from a source node to a destination node with a weight of 1.
     * @param source Node this edge is leaving
     * @param dest Node this edge is going towards
     */
    public Edge(Node source, Node dest) {
        this(source, dest, 1);
    }

    /**
     * Creates a directed edge from a source node to a destination node with a custom weight.
     * 
     * @param source Node this edge is leaving
     * @param dest Node this edge is going towards
     * @param weight double value for the weight of this edge
     */
    public Edge(Node source, Node dest, double weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    /**
     * {@inheritDoc}
     * 
     * @return String representation of this edge - {@code "(source, dest, weight)"}
     */
    @Override
    public String toString() {
        return String.format("(%s, %s, %f)", source, dest, weight);
    }

    /**
     * Compares this edge to the {@code other} by comparing the source node,
     * the destination node, and the edge weight in that order.
     * 
     * @param other Edge being compared to
     * @return -1 if less than other, 0 if equal, and 1 if greater than other
     */
    @Override
    public int compareTo(Edge other) {
        int sourceComp = this.source.compareTo(other.source);
        int destComp = this.dest.compareTo(other.dest);
        int weightComp = ((Double)this.weight).compareTo(other.weight);

        if (sourceComp == 0 && destComp == 0) {
            return weightComp;
        } else if (sourceComp == 0) {
            return destComp;
        } else {
            return sourceComp;
        }
    }

    /**
     * Checks if {@code other} is an Edge, then checks if the values of 
     * {@code source}, {@code dest}, and {@code weight} are equal 
     * across both Edges.
     * 
     * @param other Object being compared to
     * @return true if {@code other} is an Edge with the same fields as this Edge, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            Edge e = (Edge) other;
            boolean sourceEqual = this.source.equals(e.source);
            boolean destEqual = this.dest.equals(e.dest);
            boolean weightEqual = this.weight == e.weight;
            return sourceEqual && destEqual && weightEqual;
        }
    }

    /**
     * @return hash code of this Edge's String representation
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
