package pathfinding_visualizer;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel with UI elements that modifies a {@link TileGrid}.
 */
@SuppressWarnings("serial")
public class Menu extends JPanel {
    /**
     * SynchronizedQueue this Menu sends messages to
     */
    private transient SynchronizedQueue<String> syncQueue;

    /**
     * Creates a Menu that sends messages using {@code syncQueue}. The
     * {@link TileGrid} you want to modify should also use {@code syncQueue} in its
     * constructor.
     * 
     * @param syncQueue SynchronizedQueue you want this Menu to send messages to
     */
    public Menu(SynchronizedQueue<String> syncQueue) {
        
        //Use system theme for the menu
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // Do nothing. Use default look and feel.
        }
        
        this.syncQueue = syncQueue;

        Color bg = Color.gray;
        this.setBackground(bg);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(gridResizer());
        this.add(sourcePanel());
        this.add(destPanel());
        this.add(diagonalCheckbox());
        this.add(algorithmSelector());
        this.add(clearButton());
        this.add(eraseButton());
        this.add(mazeButton());

        //make all component backgrounds match this Menu
        for (Component component : this.getComponents()) {
            component.setBackground(bg);
        }
    }

    /**
     * Creates a panel with two text fields and one button. The text fields accept
     * integers between 4 and 99. When the button is clicked, the connected TileGrid
     * will change its width and height to match the text fields.
     * 
     * @return JPanel that modifies the size of the connected TileGrid.
     */
    private JPanel gridResizer() {
        JPanel form = new JPanel();

        JLabel xLabel = new JLabel("Tiles Wide: ");
        JLabel yLabel = new JLabel("Tiles Tall: ");
        JTextField xField = makeIntegerField(0, 100, 20, 3, "resize col");
        JTextField yField = makeIntegerField(0, 100, 20, 3, "resize row");

        form.add(xLabel);
        form.add(xField);
        form.add(yLabel);
        form.add(yField);

        return form;
    }

    /**
     * Creates a JPanel with 2 text fields and 1 button. The text fields accept integers between 0
     * and 98. When the button is clicked the location of the TileGrid's source tile changes to match 
     * the numbers in the text fields.
     * 
     * @return JPanel that let's you change the location of a source tile.
     */
    private JPanel sourcePanel() {
        JLabel l1 = new JLabel("Source: (");
        JLabel l2 = new JLabel(",");
        JLabel l3 = new JLabel(")");
        JTextField xField = makeIntegerField(0, 99, 0, 2, "source col");
        JTextField yField = makeIntegerField(0, 99, 0, 2, "source row");

        JPanel panel = new JPanel();
        panel.add(l1);
        panel.add(xField);
        panel.add(l2);
        panel.add(yField);
        panel.add(l3);

        return panel;
    }

    /**
     * @see #sourcePanel
     * 
     * @return JPanel that let's you change the location of a destination tile
     */
    private JPanel destPanel() {
        JLabel l1 = new JLabel("Destination: (");
        JLabel l2 = new JLabel(",");
        JLabel l3 = new JLabel(")");
        JTextField xField = makeIntegerField(0, 99, 19, 2, "destination col");
        JTextField yField = makeIntegerField(0, 99, 19, 2, "destination row");

        JPanel panel = new JPanel();
        panel.add(l1);
        panel.add(xField);
        panel.add(l2);
        panel.add(yField);
        panel.add(l3);

        return panel;
    }

    /**
     * Creates JPanel with a single check box. The checkbox allows diagonal movement in the TileGrid
     * when checked, and disallows it when unchecked.
     * 
     * @return JPanel that let's you choose whether the TileGrid should allow diagonal traversal
     */
    private JPanel diagonalCheckbox() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Allow Diagonal Movement: ");
        JCheckBox checkBox = new JCheckBox();
        checkBox.addActionListener(
            event -> {
                String message = String.format("diagonal %b", checkBox.isSelected());
                syncQueue.send(message);
            }
        );

        panel.add(label);
        panel.add(checkBox);
        return panel;
    }

    /**
     * Creates JPanel with a drop down menu, and a button. The drop down menu lets you
     * select with pathfinding algorithm to visualize, and the button tells the TileGrid
     * to start visualizing that algorithm.
     * 
     * @return JPanel that lets you select and visualize a pathfinding algorithm
     */
    private JPanel algorithmSelector() {
        String[] algorithms = {"BFS", "Djikstra", "A*"};
        JComboBox<String> box = new JComboBox<>(algorithms);
        JButton button = new JButton("Start");
        button.addActionListener(
            event -> {
                String message = String.format("search %s 5", box.getSelectedItem());
                syncQueue.send(message);
            }
        );

        JPanel panel = new JPanel();
        panel.add(box);
        panel.add(button);
        return panel;
    }

    /**
     * Creates JPanel with a single button. The button set every tile in the TileGrid that
     * was colored by a pathfinding algorithm back to its original color.
     * 
     * @return JPanel that gets rid of coloring done by a pathfinding algorithm
     */
    private JPanel clearButton() {
        JButton button = new JButton("Clear Pathfinder");
        button.addActionListener(
            event -> {
                String message = "clear";
                syncQueue.send(message);
            }
        );

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    /**
     * Craetes JPanel with a single button. This buttons sets every wall, visited, and path tile
     * back to it's original color.
     * 
     * @return JPanel that gets rid of walls and coloring done by a pathfinding algorithm.
     */
    private JPanel eraseButton() {
        JButton button = new JButton("Erase Everything");
        button.addActionListener(
            event -> {
                String message = "erase";
                syncQueue.send(message);
            }
        );

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    /**
     * Creates JPanel with a single button. This turns the associated TileGrid into a
     * randomly generated maze.
     * 
     * @return JPanel that creates mazes when clicked
     */
    private JPanel mazeButton() {
        JButton button = new JButton ("Make Maze");
        button.addActionListener(
            event -> {
                String message = "maze " + System.currentTimeMillis();
                syncQueue.send(message);
            }
        );

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    /**
     * Creates a JTextField that attempts to send a message via {@code syncQueue} when enter is pressed.
     * The message is only sent if this text field's text evaluates to an integer between {@code min} and {@code max}.
     * 
     * @param min smallest allowable integer value
     * @param max largest allowable integer value
     * @param defaultValue value thios text field starts with
     * @param columns number of columns this text field has
     * @param command String prepended to this text field's value when a message is sent to the associated TileGrid
     * @return JTextField that checks if it's value is an integer between {@code min} and {@code max} before sending a message to a TileGrid
     */
    private JTextField makeIntegerField(int min, int max, int defaultValue, int columns, String command) {
        JTextField tf = new JTextField(Integer.toString(defaultValue), columns);
        tf.addActionListener(
            event -> {
                String s = tf.getText().trim();
                if (s.matches("-?\\d+")) { // s is an integer
                    int x = Integer.parseInt(s);
                    if (x > min && x <= max) {
                            syncQueue.send(String.format("%s %d", command, x));
                    }
                }
            }
        );
        return tf;
    }
}
