package pathfinding_visualizer;

import javax.swing.*;
import java.awt.*;

public class TileGrid extends JPanel {
    protected static final long serialVersionUID = 2988009908866290833L; //auto-generated

    public TileGrid(int width, int height) {
        resizeGrid(width, height);
    }

    protected void addTiles(int width, int height) {
        for (int i = 0; i < width * height; ++i)  {
            JPanel square = new JPanel();
            this.add(square);

            int row = i / width;
            Color bg;
            if (row % 2 == 0) {
                bg = i % 2 == 0 ? Color.black : Color.white;
            } else {
                bg = i % 2 == 0 ? Color.white : Color.black;
            }
            square.setBackground(bg);
        }
    }

    protected void resizeGrid(int width, int height) {
        this.removeAll();

        GridLayout layout = new GridLayout(width, height);
        this.setLayout(layout);

        addTiles(width, height); 
    }

    public void consume(String message) {
        System.out.println("consumed " + message);
    }
}
