package pathfinding_visualizer;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import pathfinding_visualizer.TileGrid;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

public class Menu extends JPanel {

    protected Color bg = Color.blue;

    public Menu() {
        this.setBackground(bg);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel();
        title.setText("Pathfinding Visualizer");
        this.add(title);

        this.add(gridSizeForm());
    }

    protected JPanel gridSizeForm(){
        JPanel form = new JPanel();
        form.setBackground(bg);

        JLabel xLabel = new JLabel("Tiles Wide: ");
        JFormattedTextField xField = null;
        JLabel yLabel = new JLabel("Tiles Tall: ");
        JFormattedTextField yField = null;
        try {
            xField = new JFormattedTextField(new MaskFormatter("##"));
            xField.setColumns(2);
            xField.setText("20");
            yField = new JFormattedTextField(new MaskFormatter("##"));
            yField.setColumns(2);
            yField.setText("20");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JButton button = new JButton("Set Grid");
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Clicked");
			}
        });

        form.add(xLabel);
        form.add(xField);
        form.add(yLabel);
        form.add(yField);
        form.add(button);

        return form;
    }
}
