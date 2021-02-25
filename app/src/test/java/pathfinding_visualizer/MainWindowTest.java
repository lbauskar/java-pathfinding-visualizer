package pathfinding_visualizer;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests the {@link MainWindow} class.
 */
public class MainWindowTest {
    
    /**
     * Tests that the constructor for {@code MainWindow} throws no exceptions
     * and creates a non-null MainWindow object.
     */
    @Test
    public void makeMainWindow() {
        assertNotNull(new MainWindow(10, 10));
    }
}
