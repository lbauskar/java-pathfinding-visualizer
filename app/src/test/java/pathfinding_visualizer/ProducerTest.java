package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProducerTest {

    @Test
    public void makeProducer() {
        assertNotNull(new Producer());
    }

    @Test
    public void sendMessages() throws InterruptedException {
        Producer p = new Producer();
        p.sendMessage("hello");

        assertEquals(p.size(), 1);

        Thread t = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < Producer.MAX_QUEUE_SIZE + 1; ++i) {
                    try {
                        p.sendMessage("msg");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        t.start();

        // check to see if thread is still trying to add messages after some time
        Thread.sleep(50);
        assertTrue(t.isAlive());
    }

    @Test
    public void getMessages() throws InterruptedException {
        Producer p = new Producer();
        String message = Long.toString(System.currentTimeMillis());
        p.sendMessage(message);

        assertEquals(message, p.getMessage());
        assertEquals(0, p.size());

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    p.getMessage();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();

        // check to see if thread is still trying to get a message after some time
        Thread.sleep(50);
        assertTrue(t.isAlive());
    }

    @Test
    public void interruptTest() throws InterruptedException {
        Producer p = new Producer();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    p.getMessage();
                } catch (InterruptedException e) {
                    try {
                        p.sendMessage("interrupted");
                    } catch (InterruptedException e1) {
                        return;
                    }
                }
            }
        });
        t.start();

        Thread.sleep(50);
        t.interrupt();
        String message = p.getMessage();
        assertEquals("interrupted", message);

        t = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        p.sendMessage("blah");
                    }
                } catch (InterruptedException e) {
                    try {
                        p.getMessage();
                    } catch (InterruptedException e1) {
                        return;
                    }
                }
            }
        });
        t.start();

        Thread.sleep(50);
        t.interrupt();
        Thread.sleep(25);
        assertEquals(Producer.MAX_QUEUE_SIZE - 1, p.size()); //check that 1 message was removed 
    }
}
