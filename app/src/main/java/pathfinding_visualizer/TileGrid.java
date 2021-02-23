package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TileGrid extends JPanel implements MouseInputListener {
    private static final long serialVersionUID = 2988009908866290833L; // auto-generated
    private ArrayList<ArrayList<JPanel>> tiles;
    private int tileX;
    private int tileY;
    private Color paintColor = Color.black;
    private TileGraph graph;
    private Pair<Integer, Integer> sourceCoord = new Pair<>(0, 0);
    private Pair<Integer, Integer> destCoord = new Pair<>(19, 19);
    private boolean connectDiagonals = false;

    private static final class Pallete {
        static final Color WALL = Color.BLACK;
        static final Color CLEAR = Color.WHITE;
        static final Color SOURCE = Color.BLUE;
        static final Color DEST = Color.RED;
        static final Color VISIT = Color.YELLOW;
        static final Color PATH = Color.PINK;
    }

    /**
     * Creates a grid of {@link JPanel} tiles that is {@code width} tiles wide and {@code height} tiles tall. 
     * {@code width} and {@code height} are not pixel measurements.
     * <p>
     * If either {@code width} or {@code height} is less than 4 or greater than 99, the default values of 
     * {@code width = 4} and {@code height = 4} will be used.
     * 
     * @param width number of tiles this grid should have along the x-axis
     * @param height number of tiles this grid should have along the y-axis
     */
    public TileGrid(int width, int height) {
        this.addMouseMotionListener(this);

        if (width < 4 || width > 99 || height < 4 || height > 99) {
            width = 4;
            height = 4;
        }

        resizeGrid(width, height);
        changeSource(0, 0);
        changeDest(tileX - 1, tileY - 1);
    }

    /**
     * Resizes this {@link TileGrid} to make it {@code width} tiles wide and {@code height} tiles tall.
     * {@code width} and {@code height} are not pixel measurements.
     * <p>
     * <ul>
     * <li> This function will do nothing if either {@code width} or {@code height} is less than 4
     * or greater than 99.
     * <li> If {@code sourceCoord} or {@code destCoord} fields of this {@link TileGrid} fall outside the grid after resizing, 
     * their values will be changed to put them back in the grid.
     * </ul> 
     * <p>
     * @param width number of tiles this resized grid should have along the x-axis
     * @param height number of tiles this resized grid should have along the y-axis
     */
    private void resizeGrid(int width, int height) {
        if (width < 4 || width > 99 || height < 4 || height > 99) {
            return;
        }
        this.removeAll();
        tileX = width;
        tileY = height;

        GridLayout layout = new GridLayout(width, height);
        this.setLayout(layout);

        makeTiles(width, height);
        graph = new TileGraph(width, height, connectDiagonals);

        int x = sourceCoord.first;
        int y = sourceCoord.second;
        if (x < 0 || x >= tileX || y < 0 || y >= tileY) {
            changeSource(0, 0);
        }
        
        x = destCoord.first;
        y = destCoord.second;
        if (x < 0 || x >= tileX || y < 0 || y >= tileY) {
            changeDest(tileX - 1, tileY - 1);
        }

        if (sourceCoord.equals(destCoord) && sourceCoord.equals(new Pair<>(0, 0))) {
            changeDest(1, 1);
        } else if (sourceCoord.equals(destCoord)) {
            changeDest(0, 0);
        }

        this.revalidate();
    }

    private void makeTiles(int width, int height) {
        tiles = new ArrayList<>();
        for (int row = 0; row < height; ++row) {
            ArrayList<JPanel> list = new ArrayList<>();
            tiles.add(list);
            for (int col = 0; col < width; ++col) {
                JPanel tile = new JPanel();
                tile.setBackground(Pallete.CLEAR);
                list.add(tile);
                this.add(tile);
            }
        }
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
                    paintColor = Pallete.CLEAR;
                } else {
                    paintColor = Pallete.WALL;
                }
                break;

            case "source":
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                changeSource(x, y);
                break;

            case "diagonal":
                connectDiagonals = args[1].equals("true");
                graph.makeEdges(connectDiagonals);
                break;

            case "destination":
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
                changeDest(x, y);
                break;

            case "search":
                String algorithm = args[1];
                if (algorithm.equals("BFS")) {
                    visualizeAlgorithm(graph.bfs(sourceCoord, destCoord));
                }
                break;

            case "clear":
                clearGrid();
                break;


            default:
                //System.out.println("Unknown message " + message);
                break;
        }
    }

    private void clearGrid() {
        for (List<JPanel> row : tiles) {
            for (JPanel tile : row) {
                Color bg = tile.getBackground();
                if (bg.equals(Pallete.VISIT) || bg.equals(Pallete.PATH)) {
                    tile.setBackground(Pallete.CLEAR);
                }
            }
        }
    }

    private void visualizeAlgorithm(List<String> actions) {
        Thread t = new Thread() { // Run this on a different thread
            @Override
            public void run() {

                // Actual logic of algorithm here
                for (String action : actions) {
                    String[] args = action.split(" ");
                    if (args[0].equals("visit")) {
                        int x = Integer.parseInt(args[1]); // Assume no NumberFormatException
                        int y = Integer.parseInt(args[2]);
                        SwingUtilities.invokeLater(
                            () -> tiles.get(x).get(y).setBackground(Pallete.VISIT)
                        );
                    } else {
                        for (int i = 3; i < args.length - 2; i += 2) {
                            int x = Integer.parseInt(args[i]);
                            int y = Integer.parseInt(args[i + 1]);
                            SwingUtilities.invokeLater(
                                () -> tiles.get(x).get(y).setBackground(Pallete.PATH)
                            );
                        }
                    }
                }

            }
        };
        t.start();
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
        JPanel tile = tiles.get(0).get(0);
        int x = event.getX() - tile.getX();
        int y = event.getY() - tile.getY();

        //limit get row and column indices based off pixel x and y values
        int row = y / tile.getHeight();
        int col = x / tile.getWidth();

        if (row >= 0 && row < tileY && col >= 0 && col < tileX) { //only attempt to paint tiles in grid
            paintTile(row, col);
        }
    }

    private void paintTile(int row, int col) {
        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(sourceCoord) || coord.equals(destCoord)) {
            return;
        }


        graph.setNodeReachability(row, col, paintColor != Color.black);
        tiles.get(row).get(col).setBackground(paintColor);
    }

    private void changeSource(int x, int y) {
        if (x >= 0 && x < tileX && y >= 0 && y < tileY) {
            tiles.get(sourceCoord.first).get(sourceCoord.second).setBackground(Pallete.CLEAR);
            tiles.get(x).get(y).setBackground(Pallete.SOURCE);
            sourceCoord = new Pair<>(x, y);
        }
    }

    private void changeDest(int x, int y) {
        if (x >= 0 && x < tileX && y >= 0 && y < tileY) {
            tiles.get(destCoord.first).get(destCoord.second).setBackground(Pallete.CLEAR);
            tiles.get(x).get(y).setBackground(Pallete.DEST);
            destCoord = new Pair<>(x, y);
        }  
    }
}
