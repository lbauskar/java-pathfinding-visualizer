package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.text.AttributeSet.ColorAttribute;

import java.awt.*;

public class mainWindow extends JFrame {
    private static final long serialVersionUID = 8073827579515641067L; //auto-generated

    JLayeredPane pane;
    JPanel grid;

    public mainWindow(int pixelWidth, int pixelHeight) {

        // initialize pane
        Dimension gridSize = new Dimension(pixelWidth, pixelHeight);
        pane = new JLayeredPane();
        getContentPane().add(pane);
        pane.setPreferredSize(gridSize);

        // add tile grid to pane
        grid = new JPanel();
        pane.add(grid, JLayeredPane.DEFAULT_LAYER);
        int tileWidth = 10;
        int tileHeight = 10;
        GridLayout layout = new GridLayout(tileWidth, tileHeight);
        grid.setLayout(layout);
        grid.setPreferredSize(gridSize);
        grid.setBounds(0, 0, gridSize.width, gridSize.height);

        // add in square panels to make grid an actual grid
        for (int i = 0; i < tileWidth * tileHeight; ++i) {
            JPanel square = new JPanel(new BorderLayout());
            grid.add(square);

            //make checkerboard pattern
            int row = i / tileWidth;
            Color background;
            if (row % 2 == 0) {
                background = i % 2 == 0 ? Color.black : Color.white;
            } else {
                background = i % 2 == 0 ? Color.white : Color.black;
            }
            square.setBackground(background);
        }
    }
    
}
