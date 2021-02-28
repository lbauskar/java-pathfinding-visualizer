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

    @Test
    public void makeTileGrid() throws IOException {
        SynchronizedQueue sq = new SynchronizedQueue();
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

        SynchronizedQueue sq = new SynchronizedQueue();
        TileGrid tg = new TileGrid(7, 5, sq);
        
        writeToOut(tg.toString());
        outTxtEquals("7x5.txt");

        sq.sendMessage("resize row 3");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("7x5.txt");

        sq.sendMessage("resize row 1000");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("7x5.txt");

        sq.sendMessage("resize col 3");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("7x5.txt");

        sq.sendMessage("resize col 1000");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("7x5.txt");

        sq.sendMessage("resize row 4");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("4x5.txt");

        sq.sendMessage("resize col 4");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("4x4.txt");
    }

    @Test
    public void testAlgorithmsDiagAndClear() throws IOException, InterruptedException {
        SynchronizedQueue sq = new SynchronizedQueue();
        TileGrid tg = new TileGrid(10, 10, sq);

        writeToOut(tg.toString());
        outTxtEquals("10x10.txt");

        sq.sendMessage("search Djikstra 1");
        Thread.sleep(100);
        writeToOut(tg.toString());
        outTxtEquals("djikstra.txt");

        sq.sendMessage("diagonal true");
        Thread.sleep(50);
        sq.sendMessage("search BFS 1");
        Thread.sleep(100);
        writeToOut(tg.toString());

        sq.sendMessage("diagonal false");
        sq.sendMessage("search A* 1");
        Thread.sleep(100);
        writeToOut(tg.toString());
        outTxtEquals("astar.txt");

        sq.sendMessage("clear");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("10x10.txt");
    }

    @Test
    public void testMazeAndErase() throws IOException, InterruptedException {
        SynchronizedQueue sq = new SynchronizedQueue();
        TileGrid tg = new TileGrid(20, 20, sq);

        writeToOut(tg.toString());
        outTxtEquals("20x20.txt");

        sq.sendMessage("maze 2021");
        Thread.sleep(100);
        writeToOut(tg.toString());
        outTxtEquals("maze.txt");

        sq.sendMessage("erase");
        Thread.sleep(50);
        writeToOut(tg.toString());
        outTxtEquals("20x20.txt");
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
