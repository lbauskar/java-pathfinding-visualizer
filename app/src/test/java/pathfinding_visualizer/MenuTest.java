package pathfinding_visualizer;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tests the {@link Menu} class.
 */
public class MenuTest {
    private SynchronizedQueue sq = new SynchronizedQueue();
    private Menu m = new Menu(sq);

    private JPanel resize, source, dest, diag, alg, clear, erase, maze;

    /**
     * Tests that the constructor creates a non-null Menu object
     */
    @Test
    public void makeMenu() {
        Menu m = new Menu(sq);
        assertNotNull(m);
        assertSame(Menu.class, m.getClass());
    }

    public MenuTest() {
        Component[] comps = m.getComponents();
        
        // give names to each of the components/panels
        resize = (JPanel) comps[0];
        source = (JPanel) comps[1];
        dest = (JPanel) comps[2];
        diag = (JPanel) comps[3];
        alg = (JPanel) comps[4];
        clear = (JPanel) comps[5];
        erase = (JPanel) comps[6];
        maze = (JPanel) comps[7];
    }

    @Test
    public void testGridResize() throws InterruptedException {
        testTextPanel(resize);
    }

    @Test
    public void testSourcePanel() throws InterruptedException {
        testTextPanel(source);
    }

    @Test
    public void testDestPanel() throws InterruptedException {
        testTextPanel(dest);
    }

    @Test
    public void testDiagonalCheckbox() throws InterruptedException {
        Component[] comps = diag.getComponents();
        JCheckBox checkbox = (JCheckBox) comps[1];

        ActionListener listener = checkbox.getActionListeners()[0];
        ActionEvent event = new ActionEvent(checkbox, ActionEvent.ACTION_PERFORMED, null);

        checkbox.setSelected(true);
        listener.actionPerformed(event);
        assertEquals("diagonal true", sq.getMessage());
        
        checkbox.setSelected(false);
        listener.actionPerformed(event);
        assertEquals("diagonal false", sq.getMessage());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAlgorithmSelector() throws InterruptedException {
        Component[] comps = alg.getComponents();
        JComboBox<String> box = (JComboBox<String>) comps[0];
        JButton button = (JButton) comps[1];

        int i = Math.abs((int) System.currentTimeMillis()) % box.getItemCount();
        box.setSelectedIndex(i);
        String expected = String.format("search %s 5", box.getSelectedItem());

        ActionListener listener = button.getActionListeners()[0];
        ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null);
        listener.actionPerformed(event);

        assertEquals(expected, sq.getMessage());
    }

    @Test
    public void testClearButton() throws InterruptedException {
        testButtonPanel(clear, "clear");
    }

    @Test
    public void testEraseButton() throws InterruptedException {
        testButtonPanel(erase, "erase");
    }

    @Test
    public void testMazeButton() throws InterruptedException {
        JButton button = (JButton) maze.getComponent(0);
        ActionListener listener = button.getActionListeners()[0];
        ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null);
        listener.actionPerformed(event);
        long time = System.currentTimeMillis();
        String[] args = sq.getMessage().split(" ");
        assertEquals("maze", args[0]);
        assertTrue(Math.abs(time - Long.parseLong(args[1])) < 5);
    }

    private void testButtonPanel(JPanel panel, String expected) throws InterruptedException {
        JButton button = (JButton) panel.getComponent(0);
        ActionListener listener = button.getActionListeners()[0];
        ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null);
        listener.actionPerformed(event);

        assertEquals(expected, sq.getMessage());
    }

    private void testTextPanel(JPanel panel) throws InterruptedException {
        Component[] comps = resize.getComponents();
        JTextField xField = (JTextField) comps[1];
        JTextField yField = (JTextField) comps[3];

        testTextField(xField, "5", true, "resize col 5");
        testTextField(xField, "a", false, null);
        testTextField(xField, "-1000", false, null);
        testTextField(xField, "10000", false, null);

        testTextField(yField, "5", true, "resize row 5");
        testTextField(yField, "a", false, null);
        testTextField(yField, "-1000", false, null);
        testTextField(yField, "10000", false, null);
    }

    private void testTextField(JTextField tf, String setTo, boolean shouldWork, String expected)
            throws InterruptedException {
        tf.setText(setTo);
        ActionListener listener = tf.getActionListeners()[0];
        ActionEvent event = new ActionEvent(tf, ActionEvent.ACTION_PERFORMED, null);
        listener.actionPerformed(event);
        if (shouldWork) {
            assertEquals(expected, sq.getMessage());
        } else {
            assertEquals(0, sq.size());
        }
    }


}
