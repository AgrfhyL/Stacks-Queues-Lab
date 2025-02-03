package test;

import common.QueueEmptyException;
import common.QueueFullException;
import impl.DoubleStackQueue;
import org.junit.jupiter.api.Test;
import common.AbstractFactoryClient;
import interfaces.IQueue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests double stack queue implementation.
 */
public class TestDoubleStackQueue extends AbstractFactoryClient {

    private static final int DEFAULT_MAX_SIZE = 10;

    /**
     * Tests that the factory constructs a non-null object.
     */
    @Test
    public void factoryReturnsNonNullDoubleStackQueue() {
        IQueue queue = getFactory().makeDoubleStackQueue(DEFAULT_MAX_SIZE);
        assertNotNull(queue, "Failure: IFactory.makeDoubleStackQueue returns null, expected non-null object");
    }

    @Test
    public void testInvalidInputSizes() {
        //-1
        assertThrows(IllegalArgumentException.class, () -> getFactory().makeDoubleStackQueue(-1));
        //0
        assertThrows(IllegalArgumentException.class, () -> getFactory().makeDoubleStackQueue(0));
        //1
        assertThrows(IllegalArgumentException.class, () -> getFactory().makeDoubleStackQueue(1));
    }

    @Test
    public void fullTest() throws QueueFullException, QueueEmptyException {
        IQueue queue = new DoubleStackQueue(DEFAULT_MAX_SIZE);
        //isEmpty when EMPTY
        assertTrue(queue.isEmpty());

        //size() when EMPTY
        assertEquals(0, queue.size());

        //dequeue() when EMPTY
        assertThrows(QueueEmptyException.class, queue::dequeue);

        //clear() when EMPTY
        queue.clear();
        assertThrows(QueueEmptyException.class, queue::dequeue);

        //enqueueing
        // fills up queue with: [0,1,2,3,4]
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);

            //meanwhile also checking isEmpty() when NOT EMPTY
            assertFalse(queue.isEmpty());
        }

        //Checks exception thrown when enqueueing into full stack
        assertThrows(QueueFullException.class, () -> queue.enqueue(5));

        //checking enqueued elements + dequeue()
        //current queue: [0,1,2,3,4]
        assertEquals(5, queue.size());
        assertEquals(0, queue.dequeue());

        //current: [null,1,2,3,4]
        assertEquals(4, queue.size());
        assertEquals(1, queue.dequeue());

        //current: [null,null,2,3,4]
        assertEquals(3, queue.size());
        assertEquals(2, queue.dequeue());

        //current: [null,null,null,3,4]
        assertEquals(2, queue.size());

        //Clearing
        queue.clear();
        assertTrue(queue.isEmpty());

        //interweaving operations
        queue.enqueue(117); //Expected: [117, null, null, null, null]
        assertFalse(queue.isEmpty());
        assertEquals(117, queue.dequeue()); //Expected: [null, null, null, null, null]
        assertTrue(queue.isEmpty());

        queue.enqueue(10.0); // different data types
        queue.enqueue(false);
        queue.enqueue(521);
        //Expected: [10.0, false, 521, null, null]
        assertEquals(10.0, queue.dequeue());

        ArrayList<Character> arrayList = new ArrayList<>();
        arrayList.add('c');
        queue.enqueue(arrayList);
        //Expected: [false, 521, {'c'}, null, null]
        assertEquals(false, queue.dequeue());
        //Expected: [521, {'c'}, null, null, null]
        assertEquals(521, queue.dequeue());
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        queue.enqueue("strElement1");
        queue.enqueue("strElement2");
        queue.enqueue(true);
        queue.enqueue('f');
        //Expected: [{'c'}, "strElement1", "strElement2", true, 'f']
        assertThrows(QueueFullException.class, () -> queue.enqueue('e'));
        assertEquals(5, queue.size());
        assertEquals(arrayList, queue.dequeue());
        assertEquals("strElement1", queue.dequeue());
        assertEquals("strElement2", queue.dequeue());
        assertEquals(true, queue.dequeue());
        assertEquals('f', queue.dequeue());
    }
}
