package pathfinding_visualizer;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class TileGraphTest {
    final String dir = "src/test/java/pathfinding_visualizer/";


    @Test
    public void fiveByFive() throws IOException {
        FileWriter out = new FileWriter(dir + "out.txt");
        out.write(new TileGraph(5, 5, false).toString());
        out.close();

        boolean matches = fileEquality("out.txt", "graph_results/5x5_output.txt");
        assertTrue(matches);
        new File("out.txt").delete();
    }

    @Test
    public void fourByFourDiag() throws IOException {
        FileWriter out = new FileWriter(dir + "out.txt");
        out.write(new TileGraph(4, 4, true).toString());
        out.close();


        assertTrue(fileEquality("out.txt", "graph_results/4x4diag_output.txt"));
    }

    @Test
    public void FiveByFiveRemoved() throws IOException {
        FileWriter out = new FileWriter(dir + "out.txt");
        TileGraph g = new TileGraph(5, 5, true);
        for (int i = 0; i < 5; ++i) {
            g.setNodeReachability(1, i, false);
            g.setNodeReachability(i, 2, false);
            g.setNodeReachability(i, i, true);
        }
        out.write(g.toString());
        out.close();

        assertTrue(fileEquality("out.txt", "graph_results/5x5rem_output.txt"));
    }

    private boolean fileEquality(String file1, String file2) throws IOException {
        BufferedReader in1 = new BufferedReader(new FileReader(dir + file1));
        BufferedReader in2 = new BufferedReader(new FileReader(dir + file2));
        
        StringBuilder sb1 = new StringBuilder();
        String s;
        while ((s = in1.readLine()) != null) {
            sb1.append(s);
        };
        in1.close();

        StringBuilder sb2 = new StringBuilder();
        while ((s = in2.readLine()) != null) {
            sb2.append(s);
        }
        in2.close();


        return sb2.toString().equals(sb1.toString());
    }
    
}
