package pathfinding_visualizer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread-safe Queue
 */
public class SynchronizedQueue<T> {
    private Queue<T> messageQueue = new LinkedList<>();

    /**
     * Gets an object from the front of the queue.
     * 
     * @return object at the front of this queue
     * @throws InterruptedException waits too long on an empty queue
     */
    public synchronized T get() throws InterruptedException {
        while (messageQueue.isEmpty()) {
            wait();
        }
        return messageQueue.remove();
    }

    public synchronized int size() {
        return messageQueue.size();
    }

    public synchronized boolean isEmpty() {
        return messageQueue.isEmpty();
    }

    /**
     * Puts an object at the back of this queue.
     * 
     * @param obj object to add to the queue
     */
    public synchronized void send(T obj) {
        messageQueue.add(obj);
        notifyAll();
    }
}
