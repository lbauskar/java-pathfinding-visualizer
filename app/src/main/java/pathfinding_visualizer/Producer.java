package pathfinding_visualizer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Producer implements Serializable {
    private static final long serialVersionUID = -5768889538764837228L;
    static final int MAX_QUEUE_SIZE = 10;
    private Queue<String> messages = new LinkedList<>();

    public synchronized String getMessage() throws InterruptedException {
        notifyAll();
        while (messages.isEmpty()) {
            wait();
        }

        return messages.remove();
    }

    public synchronized void sendMessage(String message) throws InterruptedException {
        while (messages.size() >= MAX_QUEUE_SIZE) {
            wait();
        }

        messages.add(message);
        notifyAll();
    }
}
