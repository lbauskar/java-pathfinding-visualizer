package pathfinding_visualizer;

/**
 * Retrieves String messages from a {@link Producer}.
 */
public abstract class Consumer extends Thread {
    protected Producer producer;

    /**
     * Creates a Consumer that listens to the {@code producer}.
     * 
     * @param producer Producer this Consumer is listening to
     */
    protected Consumer(Producer producer) {
        this.producer = producer;
    }

    /**
     * Retrieves a message from {@code producer}.
     * 
     * @return String from {@code producer}'s message queue
     * @throws InterruptedException waited too long on an empty message queue
     */
    protected String getMessage() throws InterruptedException {
        return producer.getMessage();
    }

    /**
     * Implement run to listen to and parse messages sent by {@code producer}.
     */
    @Override
    public abstract void run();
}
