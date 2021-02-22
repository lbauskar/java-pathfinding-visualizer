package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TileGrid extends JPanel implements MouseInputListener {
    private static final long serialVersionUID = 2988009908866290833L; // auto-generated
    protected ArrayList<JPanel> tiles;
    protected int tileX;
    protected int tileY;
    protected Color paintColor = Color.black;
    protected TileGraph graph;
    Pair<Integer, Integer> sourceCoord;
    Pair<Integer, Integer> destCoord;

    public TileGrid(int width, int height) {
        this.addMouseMotionListener(this);
        resizeGrid(width, height);
    }

    protected void addTiles(int width, int height) {
        for (int i = 0; i < width * height; ++i) {
            JPanel square = new JPanel();
            this.add(square);
            tiles.add(square);

            square.setBackground(Color.white);
        }
    }

    protected void resizeGrid(int width, int height) {
        this.removeAll();
        tiles = new ArrayList<>();
        tileX = width;
        tileY = height;

        GridLayout layout = new GridLayout(width, height);
        this.setLayout(layout);

        addTiles(width, height);
        graph = new TileGraph(width, height, false);

        sourceCoord = new Pair<>(0, 0);
        destCoord = new Pair<>(tileX - 1, tileY - 1);
        this.revalidate();
    }

    public void consume(String message) {
        String[] args = message.split(" ");
        String command = args[0];
        switch (command) {
            case "resize":
                int width = Integer.parseInt(args[1]);
                int height = Integer.parseInt(args[2]);
                if (width < 3 || width > 99 || height < 3 || height > 99) {
                    //Do nothing
                } else {
                    resizeGrid(width, height);
                }
                
                break;

            case "paint":
                if (args[1].equals("clear")) {
                    paintColor = Color.white;
                } else {
                    paintColor = Color.black;
                }
                break;

            case "source":
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                if (x < 0 || x >= tileX || y < 0 || y >= tileY) {
                    // Do nothing
                } else {
                    tiles.get(graph.coordToIndex(x, y)).setBackground(Color.white);
                    sourceCoord = new Pair<>(x, y);
                }
                break;

            default:
                System.out.println("Unknown message " + message);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        //Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        //Do nothing
    }

    @Override
    public void mouseExited(MouseEvent event) {
        //Do nothing
    }

    @Override
    public void mousePressed(MouseEvent event) {
        //Do nothing
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        //Do nothing
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        //Do nothing
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        //adjust for fact tiles aren't flush with window borders
        JPanel tile = tiles.get(0);
        int x = event.getX() - tile.getX();
        int y = event.getY() - tile.getY();

        //limit x and y coordinates to squares in grid
        int row = y / tile.getHeight();
        int col = x / tile.getWidth();

        if (row < 0 || row >= tileY || col < 0 || col >= tileX) {
            //Do nothing
        } else {
            paintTile(row, col);
        }
    }

    protected void paintTile(int row, int col) {
        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(sourceCoord) || coord.equals(destCoord)) {
            return;
        }


        int index = graph.coordToIndex(row, col);
        graph.setNodeReachability(row, col, paintColor != Color.black);
        tiles.get(index).setBackground(paintColor);
    }
}
