package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TileGrid extends JPanel implements MouseInputListener {
    private static final long serialVersionUID = 2988009908866290833L; // auto-generated
    protected ArrayList<JPanel> tiles;
    protected int tileX;
    protected int tileY;
    protected Color paintColor = Color.white;
    protected TileGraph graph;

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
        this.tileX = width;
        this.tileY = height;

        GridLayout layout = new GridLayout(width, height);
        this.setLayout(layout);

        addTiles(width, height);
        graph = new TileGraph(width, height, false);
        this.revalidate();
    }

    public void consume(String message) {
        String[] args = message.split(" ");
        String command = args[0];
        switch (command) {
            case "resize":
                int width = Integer.parseInt(args[1]);
                int height = Integer.parseInt(args[2]);
                resizeGrid(width, height);
                break;

            case "paint":
                if (args[1].equals("clear")) {
                    paintColor = Color.white;
                } else {
                    paintColor = Color.black;
                }
                break;

            default:
                System.out.println("Unknown command " + command);
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
        int maxX = tiles.get(tileX - 1).getX() + tile.getWidth() - tile.getX();
        int maxY = tiles.get(tileX * tileY - 1).getY() + tile.getHeight() - tile.getY();
        if (x >= maxX || x < 0 || y >= maxY || y < 0) {
            return;
        }

        //convert pixel coordinates to tile index 
        int row = y / tile.getHeight();
        int col = x / tile.getWidth();
        paintTile(row, col);
    }

    protected void paintTile(int row, int col) {
        int index = graph.coordToIndex(row, col);
        graph.setNodeReachability(row, col, paintColor != Color.black);
        tiles.get(index).setBackground(paintColor);
    }
}
