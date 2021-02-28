package pathfinding_visualizer;

/**
 * Retrieves String messages from a {@link SynchronizedQueue}.
 */
public abstract class Consumer extends Thread {
    private SynchronizedQueue syncQueue;

    /**
     * Creates a Consumer that listens to the {@code producer}.
     * 
     * @param syncQueue Producer this Consumer is listening to
     */
    protected Consumer(SynchronizedQueue syncQueue) {
        this.syncQueue = syncQueue;
        this.start();
    }

    /**
     * Retrieves a message from {@code producer}.
     * 
     * @return String from {@code producer}'s message queue
     * @throws InterruptedException waited too long on an empty message queue
     */
    protected String getMessage() throws InterruptedException {
        return syncQueue.getMessage();
    }

    /**
     * Implement run to listen to and parse messages sent by {@code producer}.
     */
    @Override
    public abstract void run();
}
