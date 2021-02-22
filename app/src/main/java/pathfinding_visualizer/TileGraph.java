package pathfinding_visualizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void makeEdges() {
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                setNeighbors(findNode(row, col), makeNeighbors(findNode(row, col), connectDiagonals));
            }
        }
    }

    public int coordToIndex(int r, int c) {
        return r * height + c;
    }

    private Node findNode(int row, int col) {
        return graph.get(row).get(col).getFirst();
    }

    private List<Edge> findNeighbors(Node n) {
        return graph.get(n.getRow()).get(n.getCol()).getSecond();
    }

    private void setNeighbors(Node n, ArrayList<Edge> neighbors) {
        graph.get(n.getRow()).get(n.getCol()).setSecond(neighbors);
    }

    private ArrayList<Edge> makeNeighbors(Node source, boolean diag) {
        ArrayList<Edge> ans = new ArrayList<>();
        int r = source.getRow();
        int c = source.getCol();
        boolean top = r > 0;
        boolean bottom = r < height - 1;
        boolean left = c > 0;
        boolean right = c < width - 1;

        if (diag && top && left) {
            ans.add(new Edge(source, findNode(r - 1, c - 1)));
        }
        if (top) {
            ans.add(new Edge(source, findNode(r - 1, c)));
        }
        if (diag && top && right) {
            ans.add(new Edge(source, findNode(r - 1, c + 1)));
        }

        if (left) {
            ans.add(new Edge(source, findNode(r, c - 1)));
        }
        if (right) {
            ans.add(new Edge(source, findNode(r, c + 1)));
        }

        if (diag && bottom && left) {
            ans.add(new Edge(source, findNode(r + 1, c - 1)));
        }
        if (bottom) {
            ans.add(new Edge(source, findNode(r + 1, c)));
        }
        if (diag && bottom && right) {
            ans.add(new Edge(source, findNode(r + 1, c + 1)));
        }


        return ans;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < graph.size(); ++row) {
            List<Pair<Node, ArrayList<Edge>>> list = graph.get(row);
            for (Pair<Node, ArrayList<Edge>> p : list) {
                Collections.sort(p.getSecond()); //sort so that string output is consistent
                sb.append(p.getFirst().toString());
                sb.append(':');
                sb.append(p.getSecond().toString());
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public void setNodeReachability(int row, int col, boolean reachable) {
        Node n = findNode(row, col);
        boolean oldReach = n.isReachable();
        n.setReachable(reachable);

        if (!reachable && reachable != oldReach) {
            List<Edge> neighbors = findNeighbors(n);
            setNeighbors(n, new ArrayList<>());

            for (Edge edge : neighbors) {
                Node dest = edge.getDest();
                List<Edge> destNeighbors = findNeighbors(dest);
                Edge toRemove = null;
                for (Edge destEdge : destNeighbors) {
                    if (destEdge.getDest() == n) {
                        toRemove = destEdge;
                        break;
                    }
                }
                destNeighbors.remove(toRemove);
            }
        } else if (reachable && reachable != oldReach) {
            ArrayList<Edge> neighbors = makeNeighbors(n, connectDiagonals);
            setNeighbors(n, neighbors);

            for (Edge edge : neighbors) {
                Node dest = edge.getDest();
                Edge destEdge = new Edge(dest, n, edge.getWeight());
                findNeighbors(dest).add(destEdge);
            }
        }
    }
}
