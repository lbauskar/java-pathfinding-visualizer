package pathfinding_visualizer;

import static org.junit.Assert.*;
import java.io.*;
import java.util.Random;

import org.junit.Test;

/**
 * Tests features of {@link Algorithms}.
 */
public class AlgorithmsTest {
    private Pair<Integer, Integer> source = new Pair<>(0, 0);
    private Pair<Integer, Integer> dest = new Pair<>(9, 9);
    private TileGraph noWalls = new TileGraph(10, 10, false);
    private TileGraph noWallsDiag = new TileGraph(10, 10, true);
    private TileGraph impossible = makeImpossible();

    private static String outDir = "src/test/java/pathfinding_visualizer/";
    private static String resDir = outDir + "AlgorithmsTestResults/";

    /**
     * Creates a TileGraph with a wall between the source and destination nodes. 
     * Making it so there is no path between those two nodes.
     * 
     * @return TileGraph with no path between {@code source} and {@code dest}
     */
    private TileGraph makeImpossible() {
        TileGraph g = new TileGraph(10, 10, false);
        for (int i = 0; i < 10; ++i) {
            g.setNodeReachability(i, 3, false);
        }
        return g;
    }

    /**
     * Runs the Breadth-First Search Algorithm on the TileGraphs {@code noWalls}, 
     * {@code noWallsDiag}, and {@code impossible}. Compares the output of the algorithm 
     * to files in the "TileGraphTestResults" directory.
     * 
     * @throws IOException one of the files does not exist
     */
    @Test
    public void bfsTest() throws IOException {
        FileWriter out = new FileWriter(outDir + "out.txt");
        String bfsGrid = Algorithms.bfs(source, dest, noWalls).toString();
        out.write(bfsGrid);
        out.close();
        assertTrue(fileEquality("out.txt", "bfs_grid.txt"));

        out = new FileWriter(outDir + "out.txt");
        String bfsDiag = Algorithms.bfs(source, dest, noWallsDiag).toString();
        out.write(bfsDiag);
        out.close();
        assertTrue(fileEquality("out.txt", "bfs_diag.txt"));

        out = new FileWriter(outDir + "out.txt");
        String bfsImp = Algorithms.bfs(source, dest, impossible).toString();
        out.write(bfsImp);
        out.close();
        assertTrue(fileEquality("out.txt", "bfs_imp.txt"));
    }

    /**
     * Runs Djikstra's Algorithm on the TileGraphs {@code noWalls}, 
     * {@code noWallsDiag}, and {@code impossible}. Compares the output of the algorithm 
     * to files in the "TileGraphTestResults" directory.
     * 
     * @throws IOException one of the files does not exist
     */
    @Test
    public void djikstraTest() throws IOException {
        FileWriter out = new FileWriter(outDir + "out.txt");
        String djikstraGrid = Algorithms.djikstra(source, dest, noWalls).toString();
        out.write(djikstraGrid);
        out.close();
        assertTrue(fileEquality("out.txt", "djikstra_grid.txt"));

        out = new FileWriter(outDir + "out.txt");
        String djikstraDiag = Algorithms.djikstra(source, dest, noWallsDiag).toString();
        out.write(djikstraDiag);
        out.close();
        assertTrue(fileEquality("out.txt", "djikstra_diag.txt"));

        out = new FileWriter(outDir + "out.txt");
        String djikstraImp = Algorithms.djikstra(source, dest, impossible).toString();
        out.write(djikstraImp);
        out.close();
        assertTrue(fileEquality("out.txt", "djikstra_imp.txt"));
    }

    /**
     * Runs the A* Algorithm on the TileGraphs {@code noWalls}, 
     * {@code noWallsDiag}, and {@code impossible}. Compares the output of the algorithm 
     * to files in the "TileGraphTestResults" directory.
     * 
     * @throws IOException one of the files does not exist
     */
    @Test
    public void aStarTest() throws IOException {
        FileWriter out = new FileWriter(outDir + "out.txt");
        String aStarGrid = Algorithms.aStar(source, dest, noWalls).toString();
        out.write(aStarGrid);
        out.close();
        assertTrue(fileEquality("out.txt", "astar_grid.txt"));

        out = new FileWriter(outDir + "out.txt");
        String aStarDiag = Algorithms.aStar(source, dest, noWallsDiag).toString();
        out.write(aStarDiag);
        out.close();
        assertTrue(fileEquality("out.txt", "astar_diag.txt"));

        out = new FileWriter(outDir + "out.txt");
        String aStarImp = Algorithms.aStar(source, dest, impossible).toString();
        out.write(aStarImp);
        out.close();
        assertTrue(fileEquality("out.txt", "astar_imp.txt"));
    }

    /**
     * Makes 3 mazes for 3 TileGraphs, and compares their output to files in "TileGraphTestResults".
     * The mazes differ in the following ways"
     * <ul>
     * <li> Maze 1 starts at (0, 0) and ends at (9, 9)
     * <li> Maze 2 starts at (9, 9) and ends at (0, 0)
     * <li> Maze 3 starts at (2, 2) and ends at (9, 9)
     * </ul>
     * 
     * @throws IOException one of the files does not exist
     */
    @Test
    public void mazeTest() throws IOException {
        TileGraph g = new TileGraph(10, 10, false);
        Random random = new Random(2021);
        FileWriter out = new FileWriter(outDir + "out.txt");
        String s = Algorithms.makeMaze(source, dest, g, 10, 10, random).toString();
        out.write(s);
        out.close();
        assertTrue(fileEquality("out.txt", "10x10_maze.txt"));

        g = new TileGraph(10, 10, false);
        random = new Random(2021);
        out = new FileWriter("out.txt");
        s = Algorithms.makeMaze(dest, source, g, 10, 10, random).toString();
        out.write(s);
        out.close();
        assertTrue(fileEquality("out.txt", "10x10_maze2.txt"));


        g = new TileGraph(10, 10, false);
        random = new Random(2021);
        out = new FileWriter("out.txt");
        Pair<Integer, Integer> source2 = new Pair<>(2, 2);
        s = Algorithms.makeMaze(source2, dest, g, 10, 10, random).toString();
        out.write(s);
        out.close();
        assertTrue(fileEquality("out.txt", "10x10_maze3.txt"));
    }

    private boolean fileEquality(String file1, String file2) throws IOException {
        BufferedReader in1 = new BufferedReader(new FileReader(outDir + file1));
        BufferedReader in2 = new BufferedReader(new FileReader(resDir + file2));
        
        StringBuilder sb1 = new StringBuilder();
        String s;
        while ((s = in1.readLine()) != null) {
            sb1.append(s);
        }
        in1.close();

        StringBuilder sb2 = new StringBuilder();
        while ((s = in2.readLine()) != null) {
            sb2.append(s);
        }
        in2.close();


        return sb2.toString().equals(sb1.toString());
    }
}
