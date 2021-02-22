package pathfinding_visualizer;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 8073827579515641067L; // auto-generated

    JLayeredPane pane;

    public MainWindow(int width, int height) {

        // initialize pane
        pane = new JLayeredPane();
        getContentPane().add(pane);
        pane.setPreferredSize(new Dimension(width, height)); // set window size in pixels

        // create subpanels
        Producer producer = new Producer();
        TileGrid grid = new TileGrid(20, 20);
        Consumer consumer = new Consumer(producer, grid);
        consumer.start();
        JPanel menu = new Menu(producer);

        // Create layout with 2 panels side by side
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // fill up display area horizontally and vertically
        constraints.weighty = 1; // make display area take up whole window's height

        //put menu on left with 25% window width
        constraints.weightx = 0.25;
        pane.add(menu, constraints);
        
        //put grid on right with 75% window width
        constraints.weightx = 0.75;
        pane.add(grid, constraints);

    }
    
}
