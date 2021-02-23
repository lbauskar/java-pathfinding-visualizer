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
     * The limit to how large {@link #messageQueue} can be. Attempts to add
     * messages to the queue when it reaches this size will stall
     * until the queue has shrunk.
     */
    static final int MAX_QUEUE_SIZE = 10;
    /**
     * The message queue of Strings. 
     */
    private Queue<String> messageQueue = new LinkedList<>();

    /**
     * Retrieves a message from the message queue. This be used by a {@link Consumer}
     * object rather than the Producer directly.
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

    /**
     * Puts a String in a message queue for another thread to retrieve and read later. 
     * If the message queue already contains {@value #MAX_QUEUE_SIZE} messages, this
     * function will wait for the message queue to get smaller before adding the new message.
     * 
     * @param message String to send to another thread
     * @throws InterruptedException if the function waits too long
     */
    public synchronized void sendMessage(String message) throws InterruptedException {
        while (messageQueue.size() >= MAX_QUEUE_SIZE) {
            wait();
        }

        messageQueue.add(message);
        notifyAll();
    }
}
