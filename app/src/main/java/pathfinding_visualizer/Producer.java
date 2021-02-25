package pathfinding_visualizer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Sends String messages to a {@link Consumer}.
 */
public class Producer implements Serializable {
    private static final long serialVersionUID = -5768889538764837228L;
    /**
     * Limit to how large {@code messageQueue} can be. Attempts to add
     * messages to the messageQueue when it reaches this size will stall
     * until messageQueue has shrunk.
     */
    public static final int MAX_QUEUE_SIZE = 10;
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
        notifyAll();
        while (messageQueue.isEmpty()) {
            wait();
        }

        return messageQueue.remove();
    }

    public synchronized int size() {
        return messageQueue.size();
    }

    /**
     * Puts a String in {@code messageQueue}. 
     * If {@code messageQueue} already contains {@value #MAX_QUEUE_SIZE} Strings, this
     * function will wait for {@code messageQueue} to get smaller before adding {@code message}.
     * 
     * @param message String to add to {@code messageQueue}
     * @throws InterruptedException the function waited too long and got interrupted
     */
    public synchronized void sendMessage(String message) throws InterruptedException {
        while (messageQueue.size() >= MAX_QUEUE_SIZE) {
            wait();
        }

        messageQueue.add(message);
        notifyAll();
    }
}
