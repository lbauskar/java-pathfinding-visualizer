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
     * @param prev a Map where prev[n] is the Node that preceded n on the shortest path
     * @param end the Node the shortest path ended at 
     * @return a String representation of the shortest path
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
     * Breadth-First Search (BFS) algorithm. Finds the shortest path between two Nodes by using a queue. Whenever 
     * a Node is visited it is added to the queue. Whenever a Node is visited, it is added to the queue. This means
     * Nodes that are visited earlier are processed earlier. Once the destination node is reached the algorithm terminates.
     * <p>
     * BFS only works on unweighted graphs. It will probably get the wrong answer if a graph with varying edge weights is fed into it.
     * 
     * @param sourceCoord the location of the source tile
     * @param destCoord the location of the destination tile
     * @param graph the TileGraph this algorithm will run on
     * @return a List of Strings that show what the pathfinding algorithm did at each step
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
                actions.add(String.format("visit %d %d", curr.row, curr.col));
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
     * The priority queue contains Nodes sorted on the basis of the total distance it took to reach that Node,
     * with the minimum distance being at the top of the queue. Whenever a Node is visited, it is added to the 
     * priority queue. Once the destination node is reached the algorithm terminates.
     * 
     * @param sourceCoord the location of the source tile
     * @param destCoord the location of the destination tile
     * @param graph the TileGraph this algorithm will run on
     * @return a List of Strings that show what the pathfinding algorithm did at each step
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
                actions.add(String.format("visit %d %d", curr.row, curr.col));
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
}