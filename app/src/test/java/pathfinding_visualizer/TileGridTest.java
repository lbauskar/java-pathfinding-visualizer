package pathfinding_visualizer;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

/**
 * Tests the {@link TileGrid}.
 */
public class TileGridTest {

    private final String outDir = "src/test/java/pathfinding_visualizer/";
    private final String resDir = outDir + "TileGridTestResults/";
    private SynchronizedQueue<String> sq = new SynchronizedQueue<>();

    @Test
    public void makeTileGrid() throws IOException {
        TileGrid fourByFour = new TileGrid(4, 4, sq);
        writeToOut(fourByFour.toString());
        outTxtEquals("4x4.txt");

        writeToOut(new TileGrid(3, 4, sq).toString());
        outTxtEquals("4x4.txt");

        writeToOut(new TileGrid(4, 3, sq).toString());
        outTxtEquals("4x4.txt");

        writeToOut(new TileGrid(1000, 5, sq).toString());
        outTxtEquals("4x4.txt");

        writeToOut(new TileGrid(10, 200, sq).toString());
        outTxtEquals("4x4.txt");
    }

    @Test
    public void testResizing() throws IOException, InterruptedException {
        TileGrid tg = new TileGrid(7, 5, sq);
        
        writeToOut(tg.toString());
        outTxtEquals("7x5.txt");

        testMessage(tg, "resize row 3", 50, "7x5.txt");
        testMessage(tg, "resize row 1000", 50, "7x5.txt");
        testMessage(tg, "resize col 3", 50, "7x5.txt");
        testMessage(tg, "resize col 1000", 50, "7x5.txt");

        testMessage(tg, "resize row 4", 50, "4x5.txt");
        testMessage(tg, "resize col 4", 50, "4x4.txt");
    }

    @Test
    public void testAlgorithmsDiagAndClear() throws IOException, InterruptedException {
        TileGrid tg = new TileGrid(10, 10, sq);

        writeToOut(tg.toString());
        outTxtEquals("10x10.txt");

        testMessage(tg, "search Djikstra 0", 100, "djikstra.txt");

        sq.send("diagonal true");
        testMessage(tg, "search BFS 0", 100, "bfs.txt");

        sq.send("diagonal false");
        testMessage(tg, "search A* 0", 100, "astar.txt");

        testMessage(tg, "clear", 50, "10x10.txt");
    }

    @Test
    public void testMazeAndErase() throws IOException, InterruptedException {
        TileGrid tg = new TileGrid(20, 20, sq);

        writeToOut(tg.toString());
        outTxtEquals("20x20.txt");

        testMessage(tg, "maze 2021", 100, "maze.txt");
        testMessage(tg, "erase", 50, "20x20.txt");
    }

    @Test
    public void testChangingSourceAndDest() throws IOException, InterruptedException {
        TileGrid tg = new TileGrid(5, 5, sq);

        testMessage(tg, "destination row 0", 10, "moved_dest.txt");
        testMessage(tg, "destination col 0", 10, "swapped_dest.txt");
        testMessage(tg, "source col 3", 10, "moved_source.txt");
        testMessage(tg, "source row 4", 10, "moved_source2.txt");

        sq.send("source col 0");
        testMessage(tg, "source row 0", 10, "swapped_source.txt");

        testMessage(tg, "source col 100", 10, "swapped_source.txt");
        testMessage(tg, "source row 100", 10, "swapped_source.txt");
        testMessage(tg, "destination col 100", 10, "swapped_source.txt");
        testMessage(tg, "destination row 100", 10, "swapped_source.txt");

        testMessage(tg, "source col -1", 10, "swapped_source.txt");
        testMessage(tg, "source row -1", 10, "swapped_source.txt");
        testMessage(tg, "destination col -1", 10, "swapped_source.txt");
        testMessage(tg, "destination row -1", 10, "swapped_source.txt");
    }

    private void testMessage(TileGrid tg, String message, long wait, String file) throws IOException, InterruptedException {
        sq.send(message);
        Thread.sleep(wait);
        writeToOut(tg.toString());
        outTxtEquals(file);
    }

    private void writeToOut(String s) throws IOException {
        FileWriter out = new FileWriter(outDir + "out.txt");
        out.write(s);
        out.close();
    }

    private void outTxtEquals(String file2) throws IOException {
        fileEquality("out.txt", file2);
    }

    private void fileEquality(String file1, String file2) throws IOException {
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


        assertEquals(sb2.toString(), sb1.toString());
    }
}
