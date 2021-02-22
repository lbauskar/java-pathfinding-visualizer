package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

public class Menu extends JPanel {
    private static final long serialVersionUID = -2183382640701870707L;
    protected Color bg = Color.blue;
    private Producer producer;

    public Menu(Producer producer) {
        this.producer = producer;

        this.setBackground(bg);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel();
        title.setText("Pathfinding Visualizer");
        this.add(title);

        this.add(gridSizeForm());
    }

    protected JPanel gridSizeForm() {
        JPanel form = new JPanel();
        form.setBackground(bg);

        JLabel xLabel = new JLabel("Tiles Wide: ");
        JLabel yLabel = new JLabel("Tiles Tall: ");
        final JFormattedTextField xField = new JFormattedTextField(makeFormatter("##"));
        xField.setColumns(2);
        xField.setText("20");
        final JFormattedTextField yField = new JFormattedTextField(makeFormatter("##"));
        yField.setColumns(2);
        yField.setText("20");

        JButton button = new JButton("Set Grid");
        button.setActionCommand("resize");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String message = String.format("%s %s %s", event.getActionCommand(), xField.getText(),
                        yField.getText());
                try {
                    producer.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
			}
        });

        form.add(xLabel);
        form.add(xField);
        form.add(yLabel);
        form.add(yField);
        form.add(button);

        return form;
    }

    private MaskFormatter makeFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formatter;
    }

}
