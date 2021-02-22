package pathfinding_visualizer;

public class Node implements Comparable<Node> {
    private int row;
    private int col;
    private boolean reachable;

    public Node(int row, int col, boolean reachable) {
        this.setRow(row);
        this.setCol(col);
        this.reachable = reachable;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Node(int row, int col) {
        this(row, col, true);
    }

    public boolean isReachable() {
        return reachable;
    }
    
    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", row, col);
    }

    @Override
    public int compareTo(Node other) {
        int rowComp = ((Integer) this.row).compareTo(other.getRow());
        int colComp = ((Integer) this.col).compareTo(other.getCol());
        if (rowComp == 0) {
            return colComp;
        } else {
            return rowComp;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            Node n = (Node) other;
            boolean rowEqual = this.row == n.getRow();
            boolean colEqual = this.col == n.getCol();
            boolean reachEqual = this.reachable == n.isReachable();

            return rowEqual && colEqual && reachEqual;
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
