package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TileGrid extends JPanel implements MouseInputListener {
    private static final long serialVersionUID = 2988009908866290833L; // auto-generated
    private ArrayList<JPanel> tiles;
    private int tileX;
    private int tileY;
    private Color paintColor = Color.black;
    private TileGraph graph;
    private Pair<Integer, Integer> sourceCoord;
    private Pair<Integer, Integer> destCoord;
    private boolean connectDiagonals = false;

    private static class Pallete {
        static final Color WALL = Color.black;
        static final Color CLEAR = Color.white;
        static final Color SOURCE = Color.blue;
    }

    public TileGrid(int width, int height) {
        this.addMouseMotionListener(this);
        resizeGrid(width, height);
    }

    private void addTiles(int width, int height) {
        for (int i = 0; i < width * height; ++i) {
            JPanel square = new JPanel();
            this.add(square);
            tiles.add(square);

            square.setBackground(Color.white);
        }
    }

    private void resizeGrid(int width, int height) {
        this.removeAll();
        tiles = new ArrayList<>();
        tileX = width;
        tileY = height;

        GridLayout layout = new GridLayout(width, height);
        this.setLayout(layout);

        addTiles(width, height);
        makeGraph(connectDiagonals);

        sourceCoord = new Pair<>(0, 0);
        destCoord = new Pair<>(tileX - 1, tileY - 1);
        this.revalidate();
    }

    private void makeGraph(boolean diag) {
        graph = new TileGraph(tileX, tileY, diag);
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
                    paintColor = Pallete.CLEAR;
                } else {
                    paintColor = Pallete.WALL;
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

            case "diagonal":
                connectDiagonals = args[1].equals("true") ? true : false;
                if (connectDiagonals != graph.diagonalsConnected()) {
                    makeGraph(connectDiagonals);
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

    private void paintTile(int row, int col) {
        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(sourceCoord) || coord.equals(destCoord)) {
            return;
        }


        int index = graph.coordToIndex(row, col);
        graph.setNodeReachability(row, col, paintColor != Color.black);
        tiles.get(index).setBackground(paintColor);
    }
}
