package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular, gridded tilemap used for pathfinding visualization.
 */
public class TileGrid extends JPanel implements MouseInputListener {
    private static final long serialVersionUID = 2988009908866290833L; // auto-generated

    /**
     * A 2D array of JPanels that keeps track of the tiles in the grid. 
     * This array should have {@link #tileY} rows and {@link #tileX} columns.
     */
    private ArrayList<ArrayList<JPanel>> tiles;

    /**
     * How many tiles wide the grid is, or number of tiles along the x-axis.
     */
    private int tileX;
    /**
     * How many tiles tall the grid is, or number of tiles along the y-axis.
     */
    private int tileY;
    /**
     * The paint a user uses when dragging their mouse across the grid.
     */
    private Color paintColor = Pallete.WALL;
    /**
     * The undlerlying logical graph used for pathfinding.
     */
    private TileGraph graph;
    /**
     * The location of the source tile.
     */
    private Pair<Integer, Integer> sourceCoord = new Pair<>(0, 0);
    /**
     * The location of the destination tile.
     */
    private Pair<Integer, Integer> destCoord = new Pair<>(19, 19);
    /**
     * Whether the {@link #graph} field should connect nodes that are touching diagonally. 
     */
    private boolean connectDiagonals = false;

    /**
     * A collection of colors labelled by what the tile is supposed to correspond to.
     * For example, walls are black so {@code Pallete.WALL} is the color black.
     * The current labels are:
     * <ul>
     * <li> WALL - a wall tile (black)
     * <li> CLEAR - a tile that can be visited (white)
     * <li> DEST - the destination tile (red)
     * <li> SOURCE - the source tile (blue)
     * <li> VISIT - a tile that has been visited (yellow)
     * <li> PATH - a tile that's part of the shortest path (pink)
     * </ul>
     */
    private static final class Pallete {
        static final Color WALL = Color.BLACK;
        static final Color CLEAR = Color.WHITE;
        static final Color SOURCE = Color.BLUE;
        static final Color DEST = Color.RED;
        static final Color VISIT = Color.YELLOW;
        static final Color PATH = Color.PINK;
    }

    /**
     * Creates a grid of JPanel tiles that is {@code width} tiles wide and {@code height} tiles tall. 
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
     * <li> If the {@link #sourceCoord} or {@link #destCoord} fields of this TileGrid fall outside the grid after resizing, 
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

    /**
     * Creates a grid of JPanel tiles that fill in this {@link TileGrid}.
     * There will be {@code width} number of columns and {@code height} number of rows of tiles.
     * 
     * If tiles were already present, this function will destroy those tiles and make new ones.
     * @param width number of tiles the grid will have along the x-axis
     * @param height number of tiles the grid will have along the y-axis
     */
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

    /**
     * Parses messages sent to a {@link Consumer} object.
     * <p>
     * Valid commands are:
     * <ul>
     * <li> "resize width height" - calls {@link #resizeGrid}
     * <li> "paint color" - changes the {@link #paintColor} field to the corresponding color from the {@link Pallete}
     * <li> "source row col" - calls {@link #changeSource}
     * <li> "destination row col" - calls {@link #changeDest}
     * <li> "diagonal boolean" - sets the value of {@link #connectDiagonals} and calls {@code graph.makeEdges()} 
     * <li> "search algorithm" - calls {@link #chooseAlgorithm}
     * <li> "clear" - calls {@link #clearGrid}
     * </ul>
     * 
     * @param message a String sent to the parent Consumer
     * @throws NumberFormatException if the message parameter is not a valid command
     * @throws IndexOutOfBoundsException if the message parameter is not a valid command
     */
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
                int row = Integer.parseInt(args[1]);
                int col = Integer.parseInt(args[2]);
                changeSource(row, col);
                break;

            case "destination":
                row = Integer.parseInt(args[1]);
                col = Integer.parseInt(args[2]);
                changeDest(row, col);
                break;

            case "diagonal":
            connectDiagonals = args[1].equals("true");
            graph.makeEdges(connectDiagonals);
            break;

            case "search":
                chooseAlgorithm(args[1]);
                break;

            case "clear":
                clearGrid();
                break;


            default:
                //System.out.println("Unknown message " + message);
                break;
        }
    }

    /**
     * Visualizes a corresponding algorithm based on the string you enter. For
     * example, "Djikstra" will visualize Djikstra's Algorithm.
     * 
     * @param algorithm String representation of algorithm you want visualized
     */
    private void chooseAlgorithm(String algorithm) {
        switch (algorithm) {
            case "BFS":
                visualizeAlgorithm(Algorithms.bfs(sourceCoord, destCoord, graph));
                break;
            case "Djikstra":
                visualizeAlgorithm(Algorithms.djikstra(sourceCoord, destCoord, graph));
                break;
            default:
                break;
        }
    }

    /**
     * Sets each tile that was visited by a pathfinding algorithm back to it's original "clear" color.
     * Does not recolor source, destination, or wall tiles.
     */
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

    /**
     * Animates the process and result of a pathfinding algorithm onto this {@link TileGrid}
     * <p>
     * The actions parameter can contain two types of strings:
     * <ol>
     * <li> 'visit x y', where x and y are the coordinates of the tile visited
     * <li> 'path x1 y1 x2 y2 ...' where the x and y values are coordinates of each tile part of the final, optimal path
     * </ol>
     * 
     * @param actions a List of Strings that tells the function what a pathfinding algorithm did for each iteration
     * 
     * @throws NumberFormatException if an invalid string is in the {@code actions} parameter
     * @throws IndexOutOfBoundsException if an invalid string is in the {@code actions} parameter
     */
    private void visualizeAlgorithm(List<String> actions) {
        Thread t = new Thread() { // Run this on a different thread
            @Override
            public void run() {

                // Actual logic of algorithm here
                for (String action : actions) {
                    String[] args = action.split(" ");
                    if (args[0].equals("visit")) {
                        int x = Integer.parseInt(args[1]);
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
        paintTile(row, col);
    }

    /**
     * Attempts to paint the tile located at the coordinate {@code (row, col)}. 
     * Will not paint over tiles located at {@link #sourceCoord} or {@link #destCoord}.
     * This function will also not paint tiles that are outside the bounds of the grid.
     * <p>
     * Also modified properties of underlying graph depending on what color is painted. 
     * For example, if you paint a tile as a wall, then the corresponding node will be marked 
     * as unreachable.
     * 
     * @param row row on which tile to paint is located
     * @param col column on which tile to paint is located
     */
    private void paintTile(int row, int col) {
        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(sourceCoord) || coord.equals(destCoord)) {
            return;
        }
        if (row < 0 || row >= tileY || col < 0 && col >= tileX) {
            return;
        }


        graph.setNodeReachability(row, col, paintColor != Color.black);
        tiles.get(row).get(col).setBackground(paintColor);
    }

    /**
     * Changes the value of {@link #sourceCoord} and the visual location of the source tile.
     * The source tile is where the pathfinding algorithm will start. The previous source tile will be 
     * colored as clear and visitable.
     * <p>
     * This function does nothing if {@code row} or {@code col} are outside the bounds of the grid.
     * If the incoming {@code sourceCoord} value is equal to {@link #destCoord}, {@code sourceCoord}
     * and {@code destCoord} will be swapped instead.  
     * @param row row of new source tile location
     * @param col column of new source tile location
     */
    private void changeSource(int row, int col) {
        if (row < 0 || row >= tileX || col < 0 || col >= tileY) {
            return;
        }

        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(destCoord)) {
            destCoord = sourceCoord;
            sourceCoord = coord;
            tiles.get(sourceCoord.first).get(sourceCoord.second).setBackground(Pallete.SOURCE);
            tiles.get(destCoord.first).get(destCoord.second).setBackground(Pallete.DEST);
        } else {
            tiles.get(sourceCoord.first).get(sourceCoord.second).setBackground(Pallete.CLEAR);
            tiles.get(row).get(col).setBackground(Pallete.SOURCE);
            sourceCoord = coord;
        }
    }

    /**
     * Works like {@link #changeSource} but applies to {@code destCoord} instead. 
     * The destination tile is the location the pathfinding algorithm will try to reach.
     * @param row row of new destination tile location
     * @param col column of new destination tile location
     */
    private void changeDest(int row, int col) {
        if (row < 0 || row >= tileX || col < 0 || col >= tileY) {
            return;
        }

        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(sourceCoord)) {
            sourceCoord = destCoord;
            destCoord = coord;
            tiles.get(sourceCoord.first).get(sourceCoord.second).setBackground(Pallete.SOURCE);
            tiles.get(destCoord.first).get(destCoord.second).setBackground(Pallete.DEST);
        } else {
            tiles.get(destCoord.first).get(destCoord.second).setBackground(Pallete.CLEAR);
            tiles.get(row).get(col).setBackground(Pallete.DEST);
            destCoord = coord;
        }
    }
}
