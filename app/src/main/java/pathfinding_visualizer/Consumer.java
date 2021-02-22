package pathfinding_visualizer;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class Consumer extends Thread {
    protected Producer p;
    protected TileGrid grid;

    public Consumer(Producer producer, TileGrid grid) {
        p = producer;
        this.grid = grid;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = p.getMessage();
                SwingUtilities.invokeAndWait(() -> grid.consume(message)); //run on Event Dispatcher Thread
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
