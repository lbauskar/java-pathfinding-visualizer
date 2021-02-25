package pathfinding_visualizer;

import java.io.Serializable;
import java.util.*;

/**
 * Mathematical graph that should correspond to the tiles in the parent {@link TileGrid}.
 */
public class TileGraph implements Serializable {
    private static final long serialVersionUID = -4976456041837817365L;
    /**
     * Number of columns in the {@link #graph} field
     */
    private int width;
    /**
     * Number of rows in the {@link #graph} field
     */
    private int height;
    /**
     * Whether diagonal edges should be created. An example of a diagonal edge would
     * be one that connects the Node at (0, 0) and the Node at (1, 1).
     */
    private boolean connectDiagonals;
    private List<List<Pair<Node, ArrayList<Edge>>>> graph = new ArrayList<>();

    /**
     * Creates a graph that corresponds to a {@link TileGrid}. The {@code width} and {@code height} 
     * parameters should be equal to the parent {@code TileGrid}'s {@code tileX} and {@code tileY} fields
     * respectively. The {@code connectDiagonals} parameter determines if tiles that touch diagonally
     * should be counted as neighbors and have edges between them.
     * 
     * @param width Nodes wide the graph will be
     * @param height Nodes tall the graph will be
     * @param connectDiagonals boolean for if diagonal edges should be made
     */
    public TileGraph(int width, int height, boolean connectDiagonals) {
        this.width = width;
        this.height = height;
        this.connectDiagonals = connectDiagonals;
        
        for (int row = 0; row < height; ++row) {
            List<Pair<Node, ArrayList<Edge>>> list = new ArrayList<>();
            graph.add(list);
            for (int col = 0; col < width; ++col) {
                list.add(new Pair<>(new Node(row, col), new ArrayList<>()));
            }
        }

        makeEdges();
    }

    /**
     * The number of Nodes in this graph. This should be equal to the number of tiles in the parent {@code TileGrid}.
     * 
     * @return the number of Nodes in this graph
     */
    public int numNodes() {
        return width * height;
    }

    /**
     * Replaces all edges in the graph with programatically generated new ones.
     */
    private void makeEdges() {
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                graph.get(row).get(col).second = makeNeighbors(getNode(row, col));
            }
        }
    }

    /**
     * Replaces all edges in the graph with programatically generated new ones. The {@code connectDiagonals}
     * parameter determines if tiles that touch diagonally should be counted as neighbors and have edges between them.
     * 
     * @param connectDiagonals boolean for if diagonal edges should be made
     */
    public void makeEdges(boolean connectDiagonals) {
        if (connectDiagonals == this.connectDiagonals) {
            return;
        }
        this.connectDiagonals = connectDiagonals;
        makeEdges();
    }


    /**
     * Finds and returns the Node that corresponds to the tile located on Row {@code row}
     * and Column {@code col}. 
     * 
     * @param row row the Node is located on
     * @param col column the Node is located on
     * @return Node located at {@code [row][col]}
     */
    public Node getNode(int row, int col) {
        return graph.get(row).get(col).first;
    }

    /**
     * Finds all outgoing edges from the Node {@code n}.
     * 
     * @param n the Node you want neighbors of
     * @return a List of all Edges leaving {@code n}
     */
    public List<Edge> getNeighbors(Node n) {
        return graph.get(n.row).get(n.col).second;
    }

    /**
     * Sets {@code n}'s outgoing edges to {@code neighbors}.
     * 
     * @param n Node who's outgoing Edges you want to replace
     * @param neighbors ArrayList of Edges you're replacing with
     */
    private void setNeighbors(Node n, ArrayList<Edge> neighbors) {
        graph.get(n.row).get(n.col).second = neighbors;
    }

    /**
     * Programatically determine which Nodes are adjacent to {@code source}, and
     * which ones of those are reachable. If a Node is both adjacent and reachable
     * a corresponding edge is added to a list. This list is returned once all adjacent
     * Nodes are checked. 
     * 
     * @param source Node you want to find neighbors of
     * @return ArrayList of outgoing Edges for {@code source}
     */
    private ArrayList<Edge> makeNeighbors(Node source) {
        ArrayList<Edge> edgeList = new ArrayList<>();
        int r = source.row;
        int c = source.col;
        boolean top = r > 0;
        boolean bottom = r < height - 1;
        boolean left = c > 0;
        boolean right = c < width - 1;
        boolean diag = connectDiagonals;
        Node dest;
        final double SQRT2 = Math.sqrt(2);

        if (diag && top && left) {
            dest = getNode(r - 1, c -1);
            tryAddingEdge(source, dest, SQRT2, edgeList);
        }
        if (top) {
            dest = getNode(r - 1, c);
            tryAddingEdge(source, dest, edgeList);
        }
        if (diag && top && right) {
            dest = getNode(r - 1, c + 1);
            tryAddingEdge(source, dest, SQRT2, edgeList);
        }

        if (left) {
            dest = getNode(r, c - 1);
            tryAddingEdge(source, dest, edgeList);
        }
        if (right) {
            dest = getNode(r, c + 1);
            tryAddingEdge(source, dest, edgeList);
        }

        if (diag && bottom && left) {
            dest = getNode(r + 1, c - 1);
            tryAddingEdge(source, dest, SQRT2, edgeList);
        }
        if (bottom) {
            dest = getNode(r + 1, c);
            tryAddingEdge(source, dest, edgeList);
        }
        if (diag && bottom && right) {
            dest = getNode(r + 1, c + 1);
            tryAddingEdge(source, dest, SQRT2, edgeList);
        }

        return edgeList;
    }

    /**
     * Check if both the {@code source} and {@code dest} Nodes are reachable. If they are,
     * add a new Edge with weight 1 to the {@code edgeList}.
     * 
     * @param source Node new Edge would be leaving
     * @param dest Node new Edge would be going towards
     * @param edgeList List of Edges new Edge would be added to
     */
    private void tryAddingEdge(Node source, Node dest, List<Edge> edgeList) {
        if (source.reachable && dest.reachable) {
            edgeList.add(new Edge(source, dest));
        }
    }

    /**
     * Check if both the {@code source} and {@code dest} Nodes are reachable. If they are,
     * add a new Edge to the {@code edgeList}.
     * 
     * @param source Node new Edge would be leaving
     * @param dest Node new Edge would be going towards
     * @param weight double for weight of the new Edge
     * @param edgeList List of Edges new Edge would be added to
     */
    private void tryAddingEdge(Node source, Node dest, double weight, List<Edge> edgeList) {
        if (source.reachable && dest.reachable) {
            edgeList.add(new Edge(source, dest, weight));
        }
    }

    /**
     * Returns a String of {@code n} lines, where {@code n} is the number of Nodes in the graph.
     * Each line has the format "Node: [Edges]". An example of a line would be:
     * <p>
     * "(0, 0): [((0, 0), (0, 1), 1.000000), ((0, 0), (1, 0), 1.000000), ... ]" 
     * 
     * @return String representation of the entire 2D graph array
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < graph.size(); ++row) {
            List<Pair<Node, ArrayList<Edge>>> list = graph.get(row);
            for (Pair<Node, ArrayList<Edge>> p : list) {
                Collections.sort(p.second); //sort so that string output is consistent
                sb.append(p.first.toString());
                sb.append(':');
                sb.append(p.second.toString());
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    /**
     * Set's a Node's reachable field to the {@code reachable} parameter. The Node 
     * modified is the one that corresponds to the tile at the coordinates
     * ({@code row}, {@code col}) If {@code reachable} is true, add Edges to this graph
     * that connect the Node to its neighbors. If {@code reachable} is false,
     * destroy all Edges that contain the Node.
     * 
     * @param row Row the Node is on
     * @param col Column the Node is on
     * @param reachable boolean for if a Node can connect to other Nodes or vice-versa
     */
    public void setNodeReachability(int row, int col, boolean reachable) {
        Node n = getNode(row, col);
        boolean oldReach = n.reachable;
        n.reachable = reachable;

        if (!reachable && reachable != oldReach) {
            List<Edge> neighbors = getNeighbors(n);
            setNeighbors(n, new ArrayList<>());

            for (Edge edge : neighbors) {
                Node dest = edge.dest;
                List<Edge> destNeighbors = getNeighbors(dest);
                Edge toRemove = null;
                for (Edge destEdge : destNeighbors) {
                    if (destEdge.dest == n) {
                        toRemove = destEdge;
                        break;
                    }
                }
                destNeighbors.remove(toRemove);
            }
        } else if (reachable && reachable != oldReach) {
            ArrayList<Edge> neighbors = makeNeighbors(n);
            setNeighbors(n, neighbors);

            for (Edge edge : neighbors) {
                Node dest = edge.dest;
                Edge destEdge = new Edge(dest, n, edge.weight);
                getNeighbors(dest).add(destEdge);
            }
        }
    }

    /**
     * Shows whether {@code connectDiagonals} is {@code true} or {@code false}.
     * <p>
     * If {@code connectDiagonals} is {@code true}, Edges can connect tiles
     * that are touching diagonally. If {@code connectDiagonals} is false, only tiles
     * that are cardinally adjacent to each other can have Edges.
     * 
     * @return boolean value of the {@code connectDiagonals} field
     */
    public boolean diagonalsConnected() {
        return connectDiagonals;
    }

}
