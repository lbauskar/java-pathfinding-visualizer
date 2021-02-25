package pathfinding_visualizer;

import java.util.*;

/**
 * A container for pathfinding algorithms to be used on a {@link TileGraph}.
 */
public class Algorithms {
    /**
     * Creates a String with the format "path x1 y1 x2 y2 ...". Every x and y value
     * corresponds to the coordinates of a node that is part of the shortest path.
     * 
     * @param prev Map where prev[n] is the Node that preceded n on the shortest path
     * @param end Node the shortest path ended at 
     * @return String representation of the shortest path
     */
    private static String reconstructPath(Map<Node, Node> prev, Node end) {
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

    /**
     * Private constructor to override the default constructor. Does nothing.
     */
    private Algorithms() {
        // Do nothing
    }

    /**
     * Breadth-First Search (BFS) algorithm. Finds the shortest path between two Nodes by using a queue.
     * <p>
     * Whenever a Node is visited it is added to the queue. Whenever a Node is visited, it is added to the queue. This means
     * Nodes that are visited earlier are processed earlier. Once the destination node is reached the algorithm terminates.
     * <p>
     * BFS only works on unweighted graphs. It will probably get the wrong answer if a graph with varying edge weights is fed into it.
     * 
     * @param sourceCoord Pair of Integers that equal the location of the source tile
     * @param destCoord Pair of Integers that equal the location of the destination tile
     * @param graph TileGraph this algorithm will run on
     * @return List of Strings that show what the pathfinding algorithm did at each step
     */
    public static List<String> bfs(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph) {
        List<String> actions = new LinkedList<>();
        Node start = graph.getNode(sourceCoord.first, sourceCoord.second);
        Node end = graph.getNode(destCoord.first, destCoord.second);

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
                actions.add(visit(curr));
            }

            for (Edge e : graph.getNeighbors(curr)) {
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

    /**
     * Djikstra's Algorithm. Finds the shortest path between two nodes on a graph by using priority queue.
     * <p>
     * The priority queue contains Nodes sorted on the basis of the total distance it took to reach that Node,
     * with the minimum distance being at the top of the queue. Whenever a Node is visited, it is added to the 
     * priority queue. Once the destination node is reached the algorithm terminates.
     * 
     * @param sourceCoord Pair of Integers that equal the location of the source tile
     * @param destCoord Pair of Integers that equal the location of the destination tile
     * @param graph TileGraph this algorithm will run on
     * @return List of Strings that show what the pathfinding algorithm did at each step
     */
    public static List<String> djikstra(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph) {
        List<String> actions = new LinkedList<>();
        Node start = graph.getNode(sourceCoord.first, sourceCoord.second);
        Node end = graph.getNode(destCoord.first, destCoord.second);

        Map<Node, Double> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(graph.numNodes(), (a, b) -> Double.compare(dist.get(a), dist.get(b)));

        dist.put(start, 0.0);
        prev.put(start, null);
        visited.add(start);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node curr = pq.remove();
            
            if (curr.equals(end)) {
                actions.add(reconstructPath(prev, end));
                break;
            } else if (!curr.equals(start)) {
                actions.add(visit(curr));
            }

            for (Edge e : graph.getNeighbors(curr)) {
                Node next = e.dest;
                if (visited.contains(next)) {
                    continue;
                }

                prev.put(next, curr);
                dist.put(next, dist.get(curr) + e.weight);
                pq.add(next);
                visited.add(next);
            }
        }

        return actions;
    }

    /**
     * A* Algorithm. Similar to Djikstra's Algorithm, but when two nodes have and equal distance value, a heuristic is used to pick
     * the node "closer" to the destination. 
     * <p>
     * If diagonal traversal is allowed the euclidian distance between the current and destination node is used. If diagonal
     * traversal is not allowed, manhattan distance is used instead.
     * 
     * @param sourceCoord Pair of Integers that equal the location of the source tile
     * @param destCoord Pair of Integers that equal the location of the destination tile
     * @param graph TileGraph this algorithm will run on
     * @return List of Strings that show what the pathfinding algorithm did at each step
     * 
     * @see #djikstra
     */
    public static List<String> aStar(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph) {
        List<String> actions = new ArrayList<>();
        Node start = graph.getNode(sourceCoord.first, sourceCoord.second);
        Node end = graph.getNode(destCoord.first, destCoord.second);

        Map<Node, Double> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(graph.numNodes(), 
            (a, b) -> {
                int comparison = Double.compare(dist.get(a), dist.get(b));
                if (comparison == 0) {
                    double predictedARemaining = heuristic(a, end, graph);
                    double predictedBRemaining = heuristic(b, end, graph);
                    return Double.compare(predictedARemaining, predictedBRemaining);
                }
                return comparison;
            }
        );

        dist.put(start, 0.0);
        prev.put(start, null);
        visited.add(start);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node curr = pq.remove();
            
            if (curr.equals(end)) {
                actions.add(reconstructPath(prev, end));
                break;
            } else if (!curr.equals(start)) {
                actions.add(visit(curr));
            }

            for (Edge e : graph.getNeighbors(curr)) {
                Node next = e.dest;
                if (visited.contains(next)) {
                    continue;
                }

                prev.put(next, curr);
                dist.put(next, dist.get(curr) + e.weight);
                pq.add(next);
                visited.add(next);
            }
        }

        return actions;
    }

    /**
     * Creates an action String when you visit a node
     * @param n Node visited
     * @return "visit x y" where x and y are the coordinates of {@code n}
     */
    private static String visit(Node n) {
        return String.format("visit %d %d", n.row, n.col);
    }

    /**
     * Estimates the distance between {@code a} and {@code b}. If {@code graph} allows
     * diagonal traversal, this estimation is the euclidean distance between the two Nodes. 
     * Otherwise, manhattan distance is used for estimation.
     *  
     * @param a Node in {@code graph}
     * @param b Node in {@code graph}
     * @param graph TileGraph containing both Nodes
     * @return double value for estimated distance between {@code a} and {@code b}
     */
    private static double heuristic(Node a, Node b, TileGraph graph) {
        if (graph.diagonalsConnected()) {
            //euclidean distance
            int deltaX = a.col - b.col;
            int deltaY = a.row - b.row;
            return Math.sqrt((double) deltaX * deltaX + deltaY * deltaY);
        } else {
            //manhattan distance
            int deltaX = Math.abs(a.col - b.col);
            int deltaY = Math.abs(a.row - b.row);
            return (double) deltaX + deltaY;
        }
    }
}
