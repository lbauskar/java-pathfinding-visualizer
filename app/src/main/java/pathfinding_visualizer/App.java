package pathfinding_visualizer;

import javax.swing.*;

/**
 * Simple class that starts this program
 */
public class App {


    /**
     * Private constructor so default one isn't made
     */
    private App() {
        // Do nothing
    }

    /**
     * Creates and packs a {@link MainWindow} that holds the program.
     * 
     * @param args command line arguments are ignored
     */
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on"); //enable text anti-aliasing if system uses it
        JFrame frame = new MainWindow(1000, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Pathfinding Visualizer");
        frame.setVisible(true);
    }
}

