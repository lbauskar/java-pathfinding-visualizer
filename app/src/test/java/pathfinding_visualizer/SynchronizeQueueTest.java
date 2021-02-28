package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SynchronizeQueueTest {
    @Test
    public void testSendMessage() throws InterruptedException {
        SynchronizedQueue sq = new SynchronizedQueue();
        assertTrue(sq.isEmpty());
        
        String expected = Long.toString(System.currentTimeMillis());
        sq.sendMessage(expected);

        assertEquals(1, sq.size());
        assertEquals(expected, sq.getMessage());
    }

    @Test
    public void testGetMessage() throws InterruptedException {
        final SynchronizedQueue sq = new SynchronizedQueue();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    sq.getMessage();
                } catch (InterruptedException e) {
                    sq.sendMessage("interrupted");
                }
            }
        });
        t.start();

        Thread.sleep(50);
        t.interrupt();
        assertEquals("interrupted", sq.getMessage());
    }
}
