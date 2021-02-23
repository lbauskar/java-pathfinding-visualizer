package pathfinding_visualizer;

import javax.swing.*;

/**
 * Simple class that runs this program
 */
public class App {

    /**
     * Creates and packs a {@link MainWindow} that holds the program.
     * 
     * @param args command line arguments are ignored
     */
    public static void main(String[] args) {
        JFrame frame = new MainWindow(1000, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
