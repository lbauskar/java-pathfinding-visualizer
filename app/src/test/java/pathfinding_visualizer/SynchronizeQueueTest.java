package pathfinding_visualizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SynchronizeQueueTest {
    @Test
    public void testSendMessage() throws InterruptedException {
        SynchronizedQueue<String> sq = new SynchronizedQueue<>();
        assertTrue(sq.isEmpty());
        
        String expected = Long.toString(System.currentTimeMillis());
        sq.send(expected);

        assertEquals(1, sq.size());
        assertEquals(expected, sq.get());
    }

    @Test
    public void testGetMessage() throws InterruptedException {
        final SynchronizedQueue<String> sq = new SynchronizedQueue<>();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    sq.get();
                } catch (InterruptedException e) {
                    sq.send("interrupted");
                }
            }
        });
        t.start();

        Thread.sleep(50);
        t.interrupt();
        assertEquals("interrupted", sq.get());
    }
}
