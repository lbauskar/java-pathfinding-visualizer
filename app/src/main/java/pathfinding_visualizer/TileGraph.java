package pathfinding_visualizer;

import java.io.Serializable;
import java.util.*;

/**
 * Mathematical graph that should correspond to the tiles in the parent {@link TileGrid}.
 */
public class TileGraph implements Serializable {
    private static final long serialVersionUID = -4976456041837817365L;
    private int width;
    private int height;
    private boolean connectDiagonals;
    private List<List<Pair<Node, ArrayList<Edge>>>> graph = new ArrayList<>();

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

    private void makeEdges() {
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                graph.get(row).get(col).second = makeNeighbors(findNode(row, col));
            }
        }
    }

    public void makeEdges(boolean connectDiagonals) {
        if (connectDiagonals == this.connectDiagonals) {
            return;
        }
        this.connectDiagonals = connectDiagonals;
        makeEdges();
    }

    private Node findNode(int row, int col) {
        return graph.get(row).get(col).first;
    }

    private List<Edge> findNeighbors(Node n) {
        return graph.get(n.row).get(n.col).second;
    }

    private void setNeighbors(Node n, ArrayList<Edge> neighbors) {
        graph.get(n.row).get(n.col).second = neighbors;
    }

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

        if (diag && top && left) {
            dest = findNode(r - 1, c -1);
            tryAddingEdge(source, dest, edgeList);
        }
        if (top) {
            dest = findNode(r - 1, c);
            tryAddingEdge(source, dest, edgeList);
        }
        if (diag && top && right) {
            dest = findNode(r - 1, c + 1);
            tryAddingEdge(source, dest, edgeList);
        }

        if (left) {
            dest = findNode(r, c - 1);
            tryAddingEdge(source, dest, edgeList);
        }
        if (right) {
            dest = findNode(r, c + 1);
            tryAddingEdge(source, dest, edgeList);
        }

        if (diag && bottom && left) {
            dest = findNode(r + 1, c - 1);
            tryAddingEdge(source, dest, edgeList);
        }
        if (bottom) {
            dest = findNode(r + 1, c);
            tryAddingEdge(source, dest, edgeList);
        }
        if (diag && bottom && right) {
            dest = findNode(r + 1, c + 1);
            tryAddingEdge(source, dest, edgeList);
        }

        return edgeList;
    }

    private void tryAddingEdge(Node source, Node dest, List<Edge> edgeList) {
        if (source.reachable && dest.reachable) {
            edgeList.add(new Edge(source, dest));
        }
    }

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

    public void setNodeReachability(int row, int col, boolean reachable) {
        Node n = findNode(row, col);
        boolean oldReach = n.reachable;
        n.reachable = reachable;

        if (!reachable && reachable != oldReach) {
            List<Edge> neighbors = findNeighbors(n);
            setNeighbors(n, new ArrayList<>());

            for (Edge edge : neighbors) {
                Node dest = edge.dest;
                List<Edge> destNeighbors = findNeighbors(dest);
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
                findNeighbors(dest).add(destEdge);
            }
        }
    }

    public boolean diagonalsConnected() {
        return connectDiagonals;
    }

    private String reconstructPath(Map<Node, Node> prev, Node end) {
        StringBuilder sb = new StringBuilder("path");
        Node curr = end;
        while (curr != null) {
            sb.append(' ');
            sb.append(curr.row);
            sb.append(' ');
            sb.append(curr.col);
            curr = prev.get(curr);
        }

        return sb.toString();
    }

    public List<String> bfs(Pair<Integer, Integer> source, Pair<Integer, Integer> dest) {
        List<String> actions = new LinkedList<>();
        Node start = findNode(source.first, source.second);
        Node end = findNode(dest.first, dest.second);

        Queue<Node> q = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        Map<Node, Node> previous = new HashMap<>();

        q.add(start);
        visited.add(start);
        previous.put(start, null);

        while (!q.isEmpty()) {
            Node curr = q.remove();
            if (curr.equals(end)) {
                actions.add(reconstructPath(previous, end));
                break;
            } else if (!curr.equals(start)) {
                actions.add(String.format("visit %d %d", curr.row, curr.col));
            }

            for (Edge e : findNeighbors(curr)) {
                Node next = e.dest;
                if (visited.contains(next)) {
                    continue;
                }

                previous.put(next, curr);
                q.add(next);
                visited.add(next);
            }
        }

        return actions;
    }
}
