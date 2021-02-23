package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Menu extends JPanel {
    private static final long serialVersionUID = -2183382640701870707L;
    private Color bg = Color.blue;
    private Producer producer;

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
    }

    private JPanel gridSizeForm() {
        JPanel form = new JPanel();
        form.setBackground(bg);

        JLabel xLabel = new JLabel("Tiles Wide: ");
        JLabel yLabel = new JLabel("Tiles Tall: ");
        final JTextField xField = makeIntTextField(1, 99, 2, 20);
        final JTextField yField = makeIntTextField(1, 99, 2, 20);

        JButton button = new JButton("Set Grid");
        button.addActionListener(event -> {
            String message = String.format("resize %s %s", xField.getText().isEmpty() ? "-1" : xField.getText(),
                    yField.getText().isEmpty() ? "-1" : yField.getText());
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        form.add(xLabel);
        form.add(xField);
        form.add(yLabel);
        form.add(yField);
        form.add(button);

        return form;
    }

    private JPanel paintSelector() {
        final JRadioButton clear = new JRadioButton("clear");
        final JRadioButton wall = new JRadioButton("wall", true);

        clear.addActionListener(event -> {
            String message = "paint clear";
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        wall.addActionListener(event -> {
            String message = "paint wall";
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(clear);
        group.add(wall);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.add(clear);
        panel.add(wall);

        return panel;
    }

    private JPanel sourcePanel() {
        JLabel l1 = new JLabel("Source: (");
        JLabel l2 = new JLabel(",");
        JLabel l3 = new JLabel(")");
        JTextField xField = makeIntTextField(0, 99, 2, 0);
        JTextField yField = makeIntTextField(0, 99, 2, 0);

        JButton button = new JButton("Set Source");
        button.addActionListener(event -> {
            String message = String.format("source %s %s", xField.getText().isEmpty() ? "0" : xField.getText(),
                    yField.getText().isEmpty() ? "0" : yField.getText());
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.add(l1);
        panel.add(xField);
        panel.add(l2);
        panel.add(yField);
        panel.add(l3);
        panel.add(button);

        return panel;
    }

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
                Thread.currentThread().interrupt();
            }
        });

        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.add(l1);
        panel.add(xField);
        panel.add(l2);
        panel.add(yField);
        panel.add(l3);
        panel.add(button);

        return panel;
    }

    private JTextField makeIntTextField(int min, int max, int cols, int def) {
        JTextField textField = new JTextField(cols);
        PlainDocument doc = (PlainDocument) textField.getDocument();
        doc.setDocumentFilter(new IntFilter(min, max));
        textField.setText(Integer.toString(def));
        textField.setEditable(true);
        return textField;
    }

    private class IntFilter extends DocumentFilter {
        private int min;
        private int max;

        IntFilter(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (valid(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (valid(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
            sb.delete(offset, offset + length);

            if (valid(sb.toString())) {
                super.remove(fb, offset, length);
            }
        }

        private boolean valid(String s) {
            if (s.isEmpty()) {
                return true;
            }
            try {
                int x = Integer.parseInt(s);
                return x >= min && x < max;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

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
                    Thread.currentThread().interrupt();
                }
            }
        );

        panel.add(label);
        panel.add(checkBox);
        return panel;
    }
}
