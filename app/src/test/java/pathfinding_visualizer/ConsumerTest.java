package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link Consumer} class
 */
public class ConsumerTest {
    /**
     * Tests that a Consumer can retrieve messages from its producer, and that the
     * messages have the same value as what was put into the producer. Also tests
     * that the consumer will wait for new messages to appear if the message queue
     * is empty.
     * 
     * @throws InterruptedException this function waited too long for the consumer
     *                              to get a message
     */
    @Test
    public void getMessages() throws InterruptedException {
        String message = Long.toString(System.currentTimeMillis());
        SynchronizedQueue sq = new SynchronizedQueue();
        sq.sendMessage(message);

        Consumer c = new Consumer(sq) {
            public void run() {
                while (true) {
                    // Do nothing
                }
            }
        };

        assertEquals(message, c.getMessage());

        c = new Consumer(sq) {
            public void run() {
                try {
                    this.getMessage();
                } catch (InterruptedException e) {
                    return;
                }
            }
        };

        Thread.sleep(50);
        assertTrue(c.isAlive());
    }
}
