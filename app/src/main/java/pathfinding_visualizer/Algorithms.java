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
     * A* Algorithm. Similar to Djikstra's Algorithm, but uses a heuristic in addition to the total distance travelled to 
     * determine which node to pick next.
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
            (a, b) -> Double.compare(
                    dist.get(a) + heuristic(a, end, graph), 
                    dist.get(b) + heuristic(b, end, graph)
            )
        );

        dist.put(start, heuristic(start, end, graph));
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
                    /*
                     * TileGraphs exist in metric space, that is:
                     * - every edge weight is positive
                     * - dist(a, b) <= dist(a, c) + dist(c, b). Where dist() is the distance between two nodes.
                     * - dist(a, a) = 0
                     * - dist(a, b) = dist(b, a)
                     * 
                     * Because TileGraphs are in metric space, the heuristic is consistent/monotone rather than just admissible.
                     * This means the distance travelled to visit a node the first time is the shortest path to that node - even 
                     * with the heuristic.
                     * 
                     * Under normal circumstances, you would need to recheck a node's distance every time you visit it because 
                     * it could have been visited by a shorter path than a previous one you took to reach that node. However, we know
                     * the first path used to reach the node is the shortest one, so we can safely ignore the node and continue.
                     */
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

    /**
     * Turns {@code graph} into a gridded maze via randomized depth-first search
     * 
     * @param sourceCoord Pair of Integers equal to location of source tile
     * @param destCoord Pair of Integers equal to location of destination tile
     * @param graph TileGraph this algorithm will run on
     * @return Set of Integer Pairs that correspond to the tiles that should be walls
     */
    public static Set<Pair<Integer, Integer>> makeMaze(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph, Random random) {
        Set<Pair<Integer, Integer>> walls = new HashSet<>();
        Node start = prepareGraphForMaze(sourceCoord, destCoord, graph, walls);
        
        Set<Node> visited = new HashSet<>();
        Deque<Node> stack = new LinkedList<>();  // use Deque because Stack is synchronized

        stack.add(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            List<Node> neighbors = unvisitedMazeNeighbors(curr, graph, visited);
            
            if (neighbors.isEmpty()) {
                continue;
            }

            stack.add(curr);
            Node next = neighbors.get(random.nextInt(neighbors.size()));

            Pair<Integer, Integer> wallCoord;
            if (next.row < curr.row) { // neighbor above
                wallCoord = new Pair<>(curr.row - 1, curr.col);
            } else if (next.row > curr.row) { // below
                wallCoord = new Pair<>(curr.row + 1, curr.col);
            } else if (next.col < curr.col) { // left 
                wallCoord = new Pair<>(curr.row, curr.col - 1);
            } else { // right
                wallCoord = new Pair<>(curr.row, curr.col + 1);
            }

            graph.setNodeReachability(wallCoord.first, wallCoord.second, true);
            walls.remove(wallCoord);

            visited.add(next);
            stack.add(next);
        }

        return walls;
    }

    /**
     * Returns a list of nodes that satisfy two properties.
     * <ol>
     * <li> The Node is distance 2 away, or has one Node between it and {@code curr}
     * <li> The Node is not in {@code visited}
     * </ol>
     * This was written as a support function for {@link #makeMaze}
     * 
     * @param curr Node to find "neighbors" of 
     * @param graph TileGraph the Nodes are in
     * @param visited Set of Nodes that have been visited by {@code makeMaze}
     * @return List of Nodes that satisfy properties in the description
     */
    private static List<Node> unvisitedMazeNeighbors(Node curr, TileGraph graph, Set<Node> visited) {
        int r = curr.row;
        int c = curr.col;

        boolean top = r >= 2;
        boolean bottom = r < graph.getHeight() - 2;
        boolean left = c >= 2;
        boolean right = c < graph.getWidth() - 2;

        List<Node> neighbors = new ArrayList<>();

        if (top) {
            neighbors.add(graph.getNode(r - 2, c));
        }
        if (left) {
            neighbors.add(graph.getNode(r, c - 2));
        }
        if (right) {
            neighbors.add(graph.getNode(r, c + 2));
        }
        if (bottom) {
            neighbors.add(graph.getNode(r + 2, c));
        }

        List<Node> toRemove = new ArrayList<>();
        for (Node n : neighbors) {
            if (visited.contains(n)) {
                toRemove.add(n);
            }
        }

        for (Node n : toRemove) {
            neighbors.remove(n);
        }

        return neighbors;
    }

    /**
     * Determines if two pairs are within sqrt(2) euclidean distance from each other
     * 
     * @param a Pair of Integers being checked
     * @param b Pair of Integers being checked
     * @return true if {@code a} and {@code b} are within sqrt(2) euclidean distance, false otherwise
     */
    private static boolean adjacentPairs(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        int rowDiff = Math.abs(a.first - b.first);
        int colDiff = Math.abs(a.second - b.second);

        return rowDiff <= 1 && colDiff <= 1;
    }

    /**
     * Turns even row and every even column of Nodes in {@code graph} to walls, or makes them unreachable.
     * <p>
     * This is done to prepare {@code graph} for the algorithm used in {@link #makeMaze}, and this method
     * was written as a support function for {@code makeMaze}. Because of this, it is not recommended to 
     * call this method on its own.
     * 
     * @param sourceCoord Pair of Integers equal to the location of the source tile
     * @param destCoord Pair of Integers equal to the location of the destination tile
     * @param graph TileGraph being modified
     * @param walls empty Set of Nodes that will later contain all unreachable Nodes
     * @return Node the maze creation algorithm should start from
     */
    private static Node prepareGraphForMaze(Pair<Integer, Integer> sourceCoord, Pair<Integer, Integer> destCoord, TileGraph graph, Set<Pair<Integer, Integer>> walls) {
        // Create grid of walls aligned to even rows and columns
        for (int row = 0; row < graph.getHeight(); ++row) {
            for (int col = 0; col < graph.getWidth(); ++col) {
                Pair<Integer, Integer> p = new Pair<>(row, col);
                if (adjacentPairs(p, sourceCoord) || adjacentPairs(p, destCoord)) {
                    continue; //source and destination nodes must never be walls or surrounded by them
                }

                if (row % 2 == 0 || col % 2 == 0) {
                    graph.setNodeReachability(row, col, false);
                    walls.add(p);
                }
            }
        }

        // If source is aligned with walls, start near source instead
        Pair<Integer, Integer> startCoord = new Pair<>(sourceCoord);
        if (startCoord.first % 2 == 0) {
            startCoord.first = startCoord.first > 0 ? startCoord.first - 1 : startCoord.first + 1;
        }
        if (startCoord.second % 2 == 0) {
            startCoord.second = startCoord.second > 0 ? startCoord.second - 1 : startCoord.second + 1;
        }

        return graph.getNode(startCoord.first, startCoord.second);
    }

}
