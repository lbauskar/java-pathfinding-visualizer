package pathfinding_visualizer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Sends String messages to a {@link Consumer}.
 */
public class SynchronizedQueue {
    /**
     * Queue of Strings to be sent to a Consumer. 
     */
    private Queue<String> messageQueue = new LinkedList<>();

    /**
     * Retrieves a message from {@code messageQueue}.
     * @return the first String message in the message queue
     * @throws InterruptedException if the function waits too long
     */
    public synchronized String getMessage() throws InterruptedException {
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
     * Puts a String in {@code messageQueue}. 
     * If {@code messageQueue} already contains {@value #MAX_QUEUE_SIZE} Strings, this
     * function will wait for {@code messageQueue} to get smaller before adding {@code message}.
     * 
     * @param message String to add to {@code messageQueue}
     * @throws InterruptedException the function waited too long and got interrupted
     */
    public synchronized void sendMessage(String message) {
        messageQueue.add(message);
        notifyAll();
    }
}
