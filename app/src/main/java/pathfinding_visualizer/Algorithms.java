package pathfinding_visualizer;

import java.util.*;

public class Algorithms {
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

    private Algorithms() {
        // Do nothing
    }

    public static List<String> bfs(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph) {
        List<String> actions = new LinkedList<>();
        Node start = graph.findNode(sourceCoord.first, sourceCoord.second);
        Node end = graph.findNode(destCoord.first, destCoord.second);

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

            for (Edge e : graph.findNeighbors(curr)) {
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

    public static List<String> djikstra(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph) {
        List<String> actions = new LinkedList<>();
        Node start = graph.findNode(sourceCoord.first, sourceCoord.second);
        Node end = graph.findNode(destCoord.first, destCoord.second);

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

            for (Edge e : graph.findNeighbors(curr)) {
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
