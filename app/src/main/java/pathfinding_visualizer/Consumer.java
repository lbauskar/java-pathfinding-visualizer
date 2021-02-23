package pathfinding_visualizer;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * Retrieves String messages from a {@link Producer} object
 * and gives commands to a {@link TileGrid} object.
 */
public class Consumer extends Thread {
    protected Producer producer;
    protected TileGrid grid;

    /**
     * Creates a Consumer that listens to the {@code producer} and modifies the {@code grid}.
     * 
     * @param producer Producer this Consumer is listening to
     * @param grid TileGrid this Consumer sends messages to
     */
    public Consumer(Producer producer, TileGrid grid) {
        this.producer = producer;
        this.grid = grid;
    }

    /**
     * Listens for messages on a separate thread. When messages are recieved, send those messages to the {@link #grid}.
     */
    @Override
    public void run() {
        try {
            while (true) {
                String message = producer.getMessage();
                SwingUtilities.invokeAndWait(() -> grid.consume(message)); //run grid.consume on Event Dispatcher Thread
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
