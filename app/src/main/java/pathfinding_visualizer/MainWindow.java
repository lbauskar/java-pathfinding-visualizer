package pathfinding_visualizer;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame window this app runs in. 
 */
public class MainWindow extends JFrame {
    private static final long serialVersionUID = 8073827579515641067L; // auto-generated

    private JLayeredPane pane;

    /**
     * Creates a window holding a single pane that is {@code width} by {@code height} in pixels.
     * The pane has a {@link Menu} on the left and a {@link TileGrid} on the right.
     * 
     * @param width how many pixels wide the window is
     * @param height how many pixels tall the window is
     */
    public MainWindow(int width, int height) {

        // initialize pane
        pane = new JLayeredPane();
        getContentPane().add(pane);
        pane.setPreferredSize(new Dimension(width, height)); // set window size in pixels

        // create subpanels
        SynchronizedQueue syncQueue = new SynchronizedQueue();
        JPanel menu = new Menu(syncQueue);
        TileGrid grid = new TileGrid(20, 20, syncQueue);

        // Create layout with 2 panels side by side
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // fill up display area horizontally and vertically
        constraints.weighty = 1; // make display area take up whole window's height

        //put menu on left as small as possible
        constraints.weightx = 0;
        pane.add(menu, constraints);
        
        //put grid on right, filling up the rest of the window
        constraints.weightx = 0.5;
        pane.add(grid, constraints);

    }
    
}
