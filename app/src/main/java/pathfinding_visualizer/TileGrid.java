package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;


/**
 * Rectangular, gridded tilemap used for pathfinding visualization.
 */
@SuppressWarnings("serial")
public class TileGrid extends JPanel implements MouseInputListener {

    /**
     * A 2D array of JPanels that keeps track of the tiles in the grid. This array
     * should have {@link #numCols} rows and {@link #numRows} columns.
     */
    private ArrayList<ArrayList<JPanel>> tiles;

    /**
     * How many tiles wide the grid is, or number of tiles along the x-axis.
     */
    private int numRows;
    /**
     * How many tiles tall the grid is, or number of tiles along the y-axis.
     */
    private int numCols;
    /**
     * The undlerlying logical graph used for pathfinding.
     */
    private transient TileGraph graph;
    /**
     * The location of the source tile.
     */
    private transient Pair<Integer, Integer> sourceCoord = new Pair<>(0, 0);
    /**
     * The location of the destination tile.
     */
    private transient Pair<Integer, Integer> destCoord = new Pair<>(19, 19);
    /**
     * Whether {@link #graph} should connect nodes that are touching diagonally.
     */
    private boolean connectDiagonals = false;

    private transient Thread algorithmDrawingThread = null;

    /**
     * A collection of colors labelled by what the tile is supposed to represent.
     * For example, walls are black, so Pallete.WALL is the color black. The current
     * labels are:
     * <ul>
     * <li>WALL - unvisitable wall tile
     * <li>CLEAR - tile that can be visited
     * <li>DEST - destination tile
     * <li>SOURCE - source tile
     * <li>VISIT - tile that has been visited
     * <li>PATH - tile that's part of the shortest path
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
     * Creates a grid of JPanel tiles that is {@code width} tiles wide and
     * {@code height} tiles tall. {@code width} and {@code height} are not pixel
     * measurements.
     * <p>
     * If either {@code width} or {@code height} is less than 4 or greater than 99,
     * the default values of {@code width = 4} and {@code height = 4} will be used.
     * 
     * @param cols      number of tiles this grid should have along the x-axis
     * @param rows      number of tiles this grid should have along the y-axis
     * @param syncQueue SynchronizedQueue you want this TileGrid to listen to
     */
    public TileGrid(int rows, int cols, SynchronizedQueue<String> syncQueue) {
        this.addMouseMotionListener(this);
        this.setBackground(Pallete.WALL);

        if (cols < 4 || cols > 99 || rows < 4 || rows > 99) {
            cols = 4;
            rows = 4;
        }

        resizeGrid(rows, cols);
        changeSource(0, 0);
        changeDest(numRows - 1, numCols - 1);

        new Consumer<String>(syncQueue) {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = this.getMessage();
                        SwingUtilities.invokeAndWait(() -> parseMessages(message));
                    } catch (InvocationTargetException | InterruptedException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        };
    }

    /**
     * Resizes this {@link TileGrid} to make it {@code cols} tiles wide and
     * {@code rows} tiles tall. {@code rows} and {@code cols} are not pixel
     * measurements.
     * <p>
     * <ul>
     * <li>This function will do nothing if either {@code rows} or {@code cols} is
     * less than 4 or greater than 99.
     * <li>If the {@link #sourceCoord} or {@link #destCoord} fields of this TileGrid
     * fall outside the grid after resizing, their values will be changed to put
     * them back in the grid.
     * </ul>
     * <p>
     * 
     * @param rows number of tiles this resized grid should have along the y-axis
     * @param cols number of tiles this resized grid should have along the x-axis
     */
    private void resizeGrid(int rows, int cols) {
        stopDrawingThread();
        if (rows < 4 || rows > 99 || cols < 4 || cols > 99) {
            return;
        }
        this.removeAll();
        numRows = rows;
        numCols = cols;

        GridLayout layout = new GridLayout(rows, cols);
        this.setLayout(layout);

        makeTiles(rows, cols);
        graph = new TileGraph(rows, cols, connectDiagonals);

        int x = sourceCoord.first;
        int y = sourceCoord.second;
        if (x < 0 || x >= numRows || y < 0 || y >= numCols) {
            Pair<Integer, Integer> temp = new Pair<>(0, 0);
            sourceCoord = (destCoord == temp) ? new Pair<>(numRows - 1, numCols - 1) : temp;
        }
        forcePaintTile(sourceCoord.first, sourceCoord.second, Pallete.SOURCE);

        x = destCoord.first;
        y = destCoord.second;
        if (x < 0 || x >= numRows || y < 0 || y >= numCols) {
            Pair<Integer, Integer> temp = new Pair<>(numRows - 1, numCols - 1);
            destCoord = (sourceCoord == temp) ? new Pair<>(0, 0) : temp;
        }
        forcePaintTile(destCoord.first, destCoord.second, Pallete.DEST);

        this.revalidate();
    }

    /**
     * Creates a grid of JPanel tiles that fill in this {@link TileGrid}. There will
     * be {@code rows} number of rows and @code cols} number of columns.
     * 
     * If tiles were already present, this function will destroy those tiles and
     * make new ones.
     * 
     * @param rows number of tiles the grid will have along the x-axis
     * @param cols number of tiles the grid will have along the y-axis
     */
    private void makeTiles(int rows, int cols) {
        tiles = new ArrayList<>();
        for (int row = 0; row < rows; ++row) {
            ArrayList<JPanel> list = new ArrayList<>();
            tiles.add(list);
            for (int col = 0; col < cols; ++col) {
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
     * <li>"resize (row|col) x" - resizes the grid
     * <li>"paint color" - changes the color you paint with
     * <li>"source row col" - changes the location of the source tile
     * <li>"destination row col" - changes the location of the destination tile
     * <li>"diagonal boolean" - sets whether tiles can be traversed diagonally
     * <li>"search algorithm" - visualizes a pathfinding algorithm
     * <li>"clear" - sets every tile colored by an algorithm back to its original
     * color
     * <li>"erase" - resets every tile except the source and destination tile back
     * to its original color
     * </ul>
     * 
     * @param message String sent to the parent Consumer
     * @throws NumberFormatException     {@code message} is not a valid command
     * @throws IndexOutOfBoundsException {@code message} is not a valid command
     */
    public void parseMessages(String message) {
        String[] args = message.split(" ");
        String command = args[0];
        switch (command) {
            case "resize":
                int x = Integer.parseInt(args[2]);
                if (args[1].equals("row")) {
                    resizeGrid(x, numCols);
                } else {
                    resizeGrid(numRows, x);
                }
                break;

            case "source":
                x = Integer.parseInt(args[2]);
                if (args[1].equals("row")) {
                    changeSource(x, sourceCoord.second);
                } else {
                    changeSource(sourceCoord.first, x);
                }
                break;

            case "destination":
                x = Integer.parseInt(args[2]);
                if (args[1].equals("row")) {
                    changeDest(x, destCoord.second);
                } else {
                    changeDest(destCoord.first, x);
                }
                break;

            case "diagonal":
                connectDiagonals = args[1].equals("true");
                graph.makeEdges(connectDiagonals);
                break;

            case "search":
                clearGrid();
                int step = Integer.parseInt(args[2]);
                chooseAlgorithm(args[1], step);
                break;

            case "clear":
                clearGrid();
                break;

            case "erase":
                resizeGrid(numRows, numCols);
                break;

            case "maze":
                long seed = Long.parseLong(args[1]);
                makeMaze(seed);
                break;

            default:
                break;
        }
    }

    /**
     * Creates a maze using a randomized DFS algorithm.
     * <p>
     * The maze creation is done in a TileGraph, then whatever tiles that are marked
     * as walls become painted as such.
     */
    private void makeMaze(long seed) {
        resizeGrid(numRows, numCols);
        Set<Pair<Integer, Integer>> walls = Algorithms.makeMaze(sourceCoord, destCoord, graph, new Random(seed));

        for (Pair<Integer, Integer> p : walls) {
            paintTile(p.first, p.second, Pallete.WALL);
        }
    }

    /**
     * Visualizes a corresponding algorithm based on the string you enter. For
     * example, "Djikstra" will visualize Djikstra's Algorithm.
     * 
     * @param algorithm String representation of algorithm you want visualized
     */
    private void chooseAlgorithm(String algorithm, int stepLengthMillis) {
        switch (algorithm) {
            case "BFS":
                visualizeAlgorithm(Algorithms.bfs(sourceCoord, destCoord, graph), stepLengthMillis);
                break;
            case "Djikstra":
                visualizeAlgorithm(Algorithms.djikstra(sourceCoord, destCoord, graph), stepLengthMillis);
                break;
            case "A*":
                visualizeAlgorithm(Algorithms.aStar(sourceCoord, destCoord, graph), stepLengthMillis);
                break;
            default:
                break;
        }
    }

    /**
     * Sets each tile that was visited by a pathfinding algorithm back to it's
     * original "clear" color. Does not recolor source, destination, or wall tiles.
     */
    private void clearGrid() {
        stopDrawingThread();
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
     * Animates the process and result of a pathfinding algorithm onto this
     * TileGrid.
     * <p>
     * The actions parameter can contain two types of strings:
     * <ol>
     * <li>"visit x y", where x and y are the coordinates of the tile visited
     * <li>"path x1 y1 x2 y2 ..." where the x and y values are coordinates of each
     * tile part of the shortest path
     * </ol>
     * 
     * @param actions List of Strings that tells the function what the pathfinding
     *                algorithm did for each iteration
     * 
     * @throws NumberFormatException     an invalid String is in {@code actions}
     * @throws IndexOutOfBoundsException an invalid String is in {@code actions}
     */
    private void visualizeAlgorithm(List<String> actions, int stepLengthMillis) {
        stopDrawingThread();
        algorithmDrawingThread = new Thread() { // Run this on a different thread
            @Override
            public void run() {
                // Actual logic of algorithm here
                for (String action : actions) {
                    try {
                        Thread.sleep(stepLengthMillis);
                    } catch (InterruptedException e) {
                        return;
                    }
                    /*while (System.currentTimeMillis() % stepLengthMillis != 0) {
                        /*
                         * Use while loop instead of sleep() because sleep() can end up waiting more time
                         * than the specified milliseconds. This happens fairly often because the thread
                         * needs to get reactivated by the kernel.
                         * /
                    }*/
                    String[] args = action.split(" ");
                    if (args[0].equals("visit")) {
                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        tiles.get(x).get(y).setBackground(Pallete.VISIT);
                        SwingUtilities.invokeLater(() -> tiles.get(x).get(y).setBackground(Pallete.VISIT));
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
        algorithmDrawingThread.start();
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

        //determine which tile mouse is over
        //adjust for fact tiles aren't flush with window borders
        JPanel tile = tiles.get(0).get(0);
        int x = event.getX() - tile.getX();
        int y = event.getY() - tile.getY();

        //limit get row and column indices based off pixel x and y values
        int row = y / tile.getHeight();
        int col = x / tile.getWidth();

        //determine which button was pressed
        int buttonPressed = event.getModifiersEx();
        if (buttonPressed == InputEvent.BUTTON1_DOWN_MASK) {
            //left mouse button pressed down
            paintTile(row, col, Pallete.WALL);
        } else if (buttonPressed == InputEvent.BUTTON3_DOWN_MASK) {
            //right mouse button pressed down
            paintTile(row, col, Pallete.CLEAR);
        }
    }

    /**
     * Attempts to paint the tile located at the coordinate {@code (row, col)}. 
     * Will not paint over tiles located at {@code sourceCoord} or {@code destCoord}.
     * This function will also not paint tiles that are outside the bounds of the grid.
     * <p>
     * Also modified properties of underlying graph depending on what color is painted. 
     * For example, if you paint a tile as a wall, then the corresponding node will be marked 
     * as unreachable.
     * 
     * @param row row on which tile to paint is located
     * @param col column on which tile to paint is located
     * @param color Color with which you want to paint the tile
     */
    private void paintTile(int row, int col, Color color) {
        Pair<Integer, Integer> coord = new Pair<>(row, col);
        if (coord.equals(sourceCoord) || coord.equals(destCoord)) {
            return;
        }
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return;
        }

        forcePaintTile(row, col, color);
    }

    /**
     * Paints the tile at coordinate {@code (row, col)}. 
     * Does no bounds checking or checking of any kind
     * <p>
     * Also modified properties of underlying graph depending on what color is painted. 
     * For example, if you paint a tile as a wall, then the corresponding node will be marked 
     * as unreachable.
     * 
     * @param row row on which tile to paint is located
     * @param col column on which tile to paint is located
     * @param color Color with which you want to paint the tile
     */
    private void forcePaintTile(int row, int col, Color color) {
        graph.setNodeReachability(row, col, color != Pallete.WALL);
        tiles.get(row).get(col).setBackground(color);
    }

    /**
     * Changes the value of {@link #sourceCoord} and the visual location of the source tile.
     * The source tile is where the pathfinding algorithm will start. The previous source tile will be 
     * colored as clear and visitable.
     * <p>
     * This function does nothing if {@code row} or {@code col} are outside the bounds of the grid.
     * If the incoming {@code sourceCoord} value is equal to {@link #destCoord}, {@code sourceCoord}
     * and {@code destCoord} will be swapped instead.  
     * 
     * @param row row of new source tile location
     * @param col column of new source tile location
     */
    private void changeSource(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
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
     * Changes where the destination tile is.
     * 
     * @param row row of new destination tile location
     * @param col column of new destination tile location
     * 
     * @see #changeSource
     */
    private void changeDest(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
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

    /**
     * Creates the String representation of this TileGrid by converting each tile into a character
     * that represents its background color. For example, a 5x5 TileGrid with a source tile at (0, 0),
     * a destination tile at (4, 4), and a wall down the middle would look like
     * <p>
     * scwcc <br>
     * ccwcc <br>
     * ccwcc <br>
     * ccwcc <br>
     * ccwcd <br>
     * <p>
     * A space is appended to the end of each line to help Scanners differentiate between an AxB TileGrid
     * and a BxA TileGrid.
     * 
     * @return String representation of this TileGrid
     */
    @Override
    public String toString() {
        final Map<Color, Character> colorToChar = new HashMap<>(); 
        colorToChar.put(Pallete.CLEAR, 'c');
        colorToChar.put(Pallete.WALL, 'w');
        colorToChar.put(Pallete.SOURCE, 's');
        colorToChar.put(Pallete.DEST, 'd');
        colorToChar.put(Pallete.VISIT, 'v');
        colorToChar.put(Pallete.PATH, 'p');

        StringBuilder sb = new StringBuilder();
        for (List<JPanel> row : tiles) {
            for (JPanel tile : row) {
                sb.append(colorToChar.get(tile.getBackground()));
            }
            sb.append(" \n");
        }

        return sb.toString();
    }

    /**
     * Halt the drawing thread if it is currently running. The intended effect of this
     * function is to stop pathfinding visualization immediately.
     */
    private void stopDrawingThread() {
        if (algorithmDrawingThread != null && algorithmDrawingThread.isAlive()) {
            algorithmDrawingThread.interrupt();
            try {
                Thread.sleep(1); // let algorithmDrawingThread clean up
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
