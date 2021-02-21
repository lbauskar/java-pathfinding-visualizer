package pathfinding_visualizer;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        JFrame frame = new mainWindow(1000, 500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
