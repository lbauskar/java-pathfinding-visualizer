package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link Consumer} class
 */
public class ConsumerTest {

    /**
     * Tests that calling a Consumer's constructor makes a non-null Consumer object.
     */
    @Test
    public void makeConsumer() {
        Consumer c = new Consumer(new Producer()) {
            public void run() {
                // do nothing
            }
        };

        assertNotNull(c);
    }

    /**
     * Tests that a Consumer can retrieve messages from its producer, and that the 
     * messages have the same value as what was put into the producer. Also tests that 
     * the consumer will wait for new messages to appear if the message queue is empty.
     * 
     * @throws InterruptedException this function waited too long for the consumer to get a message
     */
    @Test
    public void getMessages() throws InterruptedException {
        String message = Long.toString(System.currentTimeMillis());
        Producer p = new Producer();
        p.sendMessage(message);

        Consumer c = new Consumer(p) {
            public void run() {
                while (true) {
                    // Do nothing
                }
            }
        };

        assertEquals(message, c.getMessage());

        c = new Consumer(p) {
            public void run() {
                try {
                    this.getMessage();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread.sleep(50);
        assertTrue(c.isAlive());
    }
}
