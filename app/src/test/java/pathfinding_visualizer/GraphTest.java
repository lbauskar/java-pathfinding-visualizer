package pathfinding_visualizer;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class GraphTest {
    final String dir = "src/test/java/pathfinding_visualizer/";


    @Test
    public void testBasicGraphGeneration() throws IOException {
        FileWriter out = new FileWriter(dir + "out.txt");
        out.write(new TileGraph(5, 5, false).toString());
        out.close();

        boolean matches = fileEquality("out.txt", "graph_results/5x5_output.txt");
        new File("out.txt").delete();

        assertTrue(matches);
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