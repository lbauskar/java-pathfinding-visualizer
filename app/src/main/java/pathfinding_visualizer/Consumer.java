package pathfinding_visualizer;

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
                grid.consume(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
