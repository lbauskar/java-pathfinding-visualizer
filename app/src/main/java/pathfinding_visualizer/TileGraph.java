package pathfinding_visualizer;

import java.util.ArrayList;
import java.util.List;

public class TileGraph {
    private int width;
    private int height;
    private List<List<Pair>> edges = new ArrayList<>();

    public TileGraph(int width, int height, boolean connectDiagonals) {
        this.width = width;
        this.height = height;

        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                edges.add(adjacentPairs(new Pair(row, col), connectDiagonals));
            }
        }
    }

    public int pairToIndex(Pair p) {
        return p.r * height + p.c;
    }

    public int pairToIndex(int r, int c) {
        return r * height + c;
    }

    List<Pair> adjacentPairs(Pair p, boolean diag) {
        List<Pair> ans = new ArrayList<>();
        boolean top = p.r > 0;
        boolean bottom = p.r < height - 1;
        boolean left = p.c > 0;
        boolean right = p.c < width - 1;

        if (top) {
            ans.add(new Pair(p.r - 1, p.c));
        }
        if (diag && top && left) {
            ans.add(new Pair(p.r - 1, p.c - 1));
        }
        if (diag && top && right) {
            ans.add(new Pair(p.r - 1, p.c + 1));
        }

        if (bottom) {
            ans.add(new Pair(p.r + 1, p.c));
        }
        if (diag && bottom && left) {
            ans.add(new Pair(p.r + 1, p.c - 1));
        }
        if (diag && bottom && right) {
            ans.add(new Pair(p.r + 1, p.c + 1));
        }

        if (left) {
            ans.add(new Pair(p.r, p.c - 1));
        }
        if (right) {
            ans.add(new Pair(p.r, p.c + 1));
        }

        return ans;
    }

    class Pair {
        public int r;
        public int c;

        Pair(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", r, c);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                sb.append(new Pair(row, col).toString());
                sb.append(':');
                sb.append(edges.get(pairToIndex(row, col)).toString());
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
