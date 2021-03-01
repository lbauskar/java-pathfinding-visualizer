package pathfinding_visualizer;

/**
 * Listens to and retrieves objects from a {@link SynchronizedQueue}
 */
public abstract class Consumer<T> extends Thread {
    private SynchronizedQueue<T> syncQueue;

    /**
     * Creates a Consumer that listens to {@code syncQueue}.
     * 
     * @param syncQueue SynchronizedQueue this Consumer is listening to
     */
    protected Consumer(SynchronizedQueue<T> syncQueue) {
        this.syncQueue = syncQueue;
        this.start();
    }

    /**
     * Gets a message from internal SynchronizedQueue.
     * 
     * @return object at the front of the queue
     * @throws InterruptedException waited too long on an empty queue
     */
    protected T getMessage() throws InterruptedException {
        return syncQueue.get();
    }

    /**
     * Implement run to listen for and parse object retrieved with {@link #getMessage}.
     */
    @Override
    public abstract void run();
}
