package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TileGrid extends JPanel implements MouseInputListener {
    private static final long serialVersionUID = 2988009908866290833L; // auto-generated
    private ArrayList<JPanel> tiles;
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
        if (!(width >= 4 && width <= 99 && height >= 4 && height <= 99)) {
            return;
        }
        this.removeAll();
        tiles = new ArrayList<>();
        tileX = width;
        tileY = height;

        GridLayout layout = new GridLayout(width, height);
        this.setLayout(layout);

        addTiles(width, height);
        makeGraph(connectDiagonals);

        changeSource(0, 0);
        changeDest(tileX - 1, tileY - 1);
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
                if (connectDiagonals != graph.diagonalsConnected()) {
                    makeGraph(connectDiagonals);
                }
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
                System.out.println("Unknown message " + message);
                break;
        }
    }

    private void clearGrid() {
        for (JPanel tile : tiles) {
            Color bg = tile.getBackground();
            if (bg == Pallete.VISIT || bg == Pallete.PATH) {
                tile.setBackground(Pallete.CLEAR);
            }
        }
    }

    private void visualizeAlgorithm(List<String> actions) {
        Thread t = new Thread() {
            @Override
            public void run() {
                for (String action : actions) {
                   /* try {
                        sleep(milliDelay, 0);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        Thread.currentThread().interrupt();
                    }*/
                    String[] args = action.split(" ");
                    if (args[0].equals("visit")) {
                        try {
                            int x = Integer.parseInt(args[1]);
                            int y = Integer.parseInt(args[2]);
                            SwingUtilities.invokeLater(
                                () -> tiles.get(graph.coordToIndex(x, y)).setBackground(Pallete.VISIT)
                            );
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            for (int i = 3; i < args.length - 2; i += 2) {
                                int x = Integer.parseInt(args[i]);
                                int y = Integer.parseInt(args[i + 1]);
                                SwingUtilities.invokeLater(
                                    () -> tiles.get(graph.coordToIndex(x, y)).setBackground(Pallete.PATH)
                                );
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
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

    private void changeSource(int x, int y) {
        if (x >= 0 && x < tileX && y >= 0 && y < tileY) {
            tiles.get(graph.coordToIndex(sourceCoord.getFirst(), sourceCoord.getSecond())).setBackground(Pallete.CLEAR);
            tiles.get(graph.coordToIndex(x, y)).setBackground(Pallete.SOURCE);
            sourceCoord = new Pair<>(x, y);
        }
    }

    private void changeDest(int x, int y) {
        if (x >= 0 && x < tileX && y >= 0 && y < tileY) {
            tiles.get(graph.coordToIndex(destCoord.getFirst(), destCoord.getSecond())).setBackground(Pallete.CLEAR);
            tiles.get(graph.coordToIndex(x, y)).setBackground(Pallete.DEST);
            destCoord = new Pair<>(x, y);
        }  
    }
}
