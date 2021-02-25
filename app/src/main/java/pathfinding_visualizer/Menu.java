package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * JPanel with UI elements that modifies a {@link TileGrid}.
 */
public class Menu extends JPanel {
    private static final long serialVersionUID = -2183382640701870707L;
    /**
     * Default background color of all JPanels in this Menu
     */
    private static final Color bg = Color.gray;
    /**
     * Producer this Menu sends messages to
     */
    private Producer producer;

    /**
     * Creates a Menu that sends messages using the {@code producer}. The {@link TileGrid}
     * you want to modify use the same {@code producer} in its constructor.
     */
    public Menu(Producer producer) {
        this.producer = producer;

        this.setBackground(bg);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel();
        title.setText("Pathfinding Visualizer");
        this.add(title);

        this.add(gridSizeForm());
        this.add(paintSelector());
        this.add(sourcePanel());
        this.add(destPanel());
        this.add(diagonalCheckbox());
        this.add(algorithmSelector());
        this.add(clearButton());
        this.add(eraseButton());
        this.add(mazeButton());

        for (Component component : this.getComponents()) {
            component.setBackground(bg);
        }
    }

    /**
     * Creates a panel with two text fields and one button.
     * The text fields accept integers between 4 and 99. When the button is clicked,
     * the connected TileGrid will change its width and height to match the text fields.
     * 
     * @return JPanel that modifies the size of the connected TileGrid.
     */
    private JPanel gridSizeForm() {
        JPanel form = new JPanel();

        JLabel xLabel = new JLabel("Tiles Wide: ");
        JLabel yLabel = new JLabel("Tiles Tall: ");
        final JTextField xField = makeIntTextField(4, 99, 2, 20);
        final JTextField yField = makeIntTextField(4, 99, 2, 20);

        JButton button = new JButton("Set Grid");
        button.addActionListener(event -> {
            String message = String.format("resize %s %s", xField.getText().isEmpty() ? "-1" : xField.getText(),
                    yField.getText().isEmpty() ? "-1" : yField.getText());
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        form.add(xLabel);
        form.add(xField);
        form.add(yLabel);
        form.add(yField);
        form.add(button);

        return form;
    }

    /**
     * Creates a JPanel that let's you pick paint colors via radio buttons.
     * The paint colors let you modify the TileGrid by adding and removing walls.
     * 
     * @return JPanel that let's you pick which paint color to use
     */
    private JPanel paintSelector() {
        final JRadioButton clear = new JRadioButton("erase");
        final JRadioButton wall = new JRadioButton("wall", true);

        clear.addActionListener(event -> {
            String message = "paint clear";
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
        wall.addActionListener(event -> {
            String message = "paint wall";
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(clear);
        group.add(wall);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(clear);
        panel.add(wall);

        return panel;
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
        JTextField xField = makeIntTextField(0, 98, 2, 0);
        JTextField yField = makeIntTextField(0, 98, 2, 0);

        JButton button = new JButton("Set Source");
        button.addActionListener(event -> {
            String message = String.format("source %s %s", xField.getText().isEmpty() ? "0" : xField.getText(),
                    yField.getText().isEmpty() ? "0" : yField.getText());
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        JPanel panel = new JPanel();
        panel.add(l1);
        panel.add(xField);
        panel.add(l2);
        panel.add(yField);
        panel.add(l3);
        panel.add(button);

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
        JTextField xField = makeIntTextField(0, 99, 2, 19);
        JTextField yField = makeIntTextField(0, 99, 2, 19);

        JButton button = new JButton("Set Destination");
        button.addActionListener(event -> {
            String message = String.format("destination %s %s", xField.getText().isEmpty() ? "0" : xField.getText(),
                    yField.getText().isEmpty() ? "0" : yField.getText());
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        JPanel panel = new JPanel();
        panel.add(l1);
        panel.add(xField);
        panel.add(l2);
        panel.add(yField);
        panel.add(l3);
        panel.add(button);

        return panel;
    }

    /**
     * Makes JTextField that accepts integers between {@code min} and {@code max} (inclusive),
     * with {@code cols} number of columns, and a default value of {@code def}.
     * 
     * @param min minimum accepted value of this text field
     * @param max maximum accepted value of this text field
     * @param cols number of columns this text field has 
     * @param def this text field's default value
     * @return JTextField that only accepts integers between {@code min} and {@code max}.
     */
    private JTextField makeIntTextField(int min, int max, int cols, int def) {
        JTextField textField = new JTextField(cols);
        PlainDocument doc = (PlainDocument) textField.getDocument();
        doc.setDocumentFilter(new IntFilter(min, max));
        textField.setText(Integer.toString(def));
        textField.setEditable(true);
        return textField;
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
                try {
                    producer.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
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
                String message = String.format("search %s", box.getSelectedItem());
                try {
                    producer.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
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
                try {
                    producer.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
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
        JButton button = new JButton("Erase Walls");
        button.addActionListener(
            event -> {
                String message = "erase";
                try {
                    producer.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        );

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    private JPanel mazeButton() {
        JButton button = new JButton ("Make Maze");
        button.addActionListener(
            event -> {
                String message = "maze";
                try {
                    producer.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        );

        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }
}
