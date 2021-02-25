package pathfinding_visualizer;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class TileGraphTest {

    final String outDir = "src/test/java/pathfinding_visualizer/";
    final String resDir = outDir + "TileGraphTestResults/";

    @Test
    public void fiveByFive() throws IOException {
        TileGraph g = new TileGraph(5, 5, false);
        g.makeEdges(false); // edges aren't needlessly remade if same boolean if put in

        FileWriter out = new FileWriter(outDir + "out.txt");
        out.write(g.toString());
        out.close();

        boolean matches = fileEquality("out.txt", "5x5_output.txt");
        assertTrue("Output does not match expect result", matches);
        new File("out.txt").delete();
    }

    @Test 
    public void fourByFourThenDiag() throws IOException {
        TileGraph g = new TileGraph(4, 4, false);
        g.makeEdges(true);

        assertEquals(16, g.numNodes());
        assertTrue(g.diagonalsConnected());

        FileWriter out = new FileWriter(outDir + "out.txt");
        out.write(g.toString());
        out.close();

        assertTrue("Output does not match expect result", fileEquality("out.txt", "4x4diag_output.txt"));

    }

    @Test
    public void FiveByFiveDiagAndRemoved() throws IOException {
        FileWriter out = new FileWriter(outDir + "out.txt");
        TileGraph g = new TileGraph(5, 5, true);

        //make 3x3 grid unreachable, then make middle reachable again
        g.setNodeReachability(0, 0, false);
        g.setNodeReachability(0, 1, false);
        g.setNodeReachability(0, 2, false);
        g.setNodeReachability(1, 0, false);
        g.setNodeReachability(1, 1, false);
        g.setNodeReachability(1, 2, false);
        g.setNodeReachability(2, 0, false);
        g.setNodeReachability(2, 1, false);
        g.setNodeReachability(2, 2, false);

        g.setNodeReachability(1, 1, true);
        g.setNodeReachability(1, 1, false);

        g.setNodeReachability(3, 3, true); // should do nothing

        out.write(g.toString());
        out.close();

        assertTrue("Output does not match expect result", fileEquality("out.txt", "5x5rem_output.txt"));
    }

    @Test
    public void FiveByFiveDiagAndRemovedWithEdgeAdding() throws IOException {
        FileWriter out = new FileWriter(outDir + "out.txt");
        TileGraph g = new TileGraph(5, 5, false);

        //make 3x3 grid unreachable, make middle reachable again, then make it unreachable
        g.setNodeReachability(0, 0, false);
        g.setNodeReachability(0, 1, false);
        g.setNodeReachability(0, 2, false);
        g.setNodeReachability(1, 0, false);
        g.setNodeReachability(1, 1, false);
        g.setNodeReachability(1, 2, false);
        g.setNodeReachability(2, 0, false);
        g.setNodeReachability(2, 1, false);
        g.setNodeReachability(2, 2, false);
        g.setNodeReachability(1, 1, true);
        g.setNodeReachability(1, 1, false);


        g.setNodeReachability(3, 3, false);
        g.setNodeReachability(3, 3, true);

        g.makeEdges(true);
        out.write(g.toString());
        out.close();

        assertTrue("Output does not match expect result", fileEquality("out.txt", "5x5rem_output.txt"));
    }

    private boolean fileEquality(String file1, String file2) throws IOException {
        BufferedReader in1 = new BufferedReader(new FileReader(outDir + file1));
        BufferedReader in2 = new BufferedReader(new FileReader(resDir + file2));
        
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
