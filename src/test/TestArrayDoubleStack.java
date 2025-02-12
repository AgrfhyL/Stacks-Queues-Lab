package test;

import common.StackEmptyException;
import common.StackOverflowException;
import org.junit.jupiter.api.Test;
import common.AbstractFactoryClient;
import interfaces.IDoubleStack;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests array collection implementation.
 */
public class TestArrayDoubleStack extends AbstractFactoryClient {

    private static final int DEFAULT_MAX_SIZE = 10;

    /**
     * Tests that the factory constructs a non-null double stack.
     */
    @Test
    public void factoryReturnsNonNullDoubleStackObject() {
        IDoubleStack doubleStack1 = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
        assertNotNull(doubleStack1, "Failure: IFactory.makeDoubleStack returns null, expected non-null object");
    }

    /*

     */



    @Test
    public void factoryChecksForInvalidInputSize() {
        //-1
        assertThrows(IllegalArgumentException.class, () -> getFactory().makeDoubleStack(-1));
        //0
        assertThrows(IllegalArgumentException.class, () -> getFactory().makeDoubleStack(0));
        //1
        assertThrows(IllegalArgumentException.class, () -> getFactory().makeDoubleStack(1));
    }

    @Test
    public void fullTest() throws StackOverflowException, StackEmptyException {
        IDoubleStack doubleStack = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
        var firstStack = doubleStack.getFirstStack();
        var secondStack = doubleStack.getSecondStack();
        //Edge cases
        //Checks exception when popping empty stack
        assertThrows(StackEmptyException.class, firstStack::pop);
        assertThrows(StackEmptyException.class, secondStack::pop);

        //Checks exception when peeking top of empty stack
        assertThrows(StackEmptyException.class, firstStack::top);
        assertThrows(StackEmptyException.class, secondStack::top);

        //Check correct size() when EMPTY
        assertEquals(0, firstStack.size());
        assertEquals(0, secondStack.size());

        //Check isEmpty() when EMPTY
        assertTrue(firstStack.isEmpty());
        assertTrue(firstStack.isEmpty());

        //Pushing (firstStack: [0,1,2,3,4])
        for (int i = 0; i < 5; i++) {
            firstStack.push(i);
            //check whether "i" correctly added to top of stack
            assertEquals(i, firstStack.top());
        }

        //Pushing full stack: firstStack
        assertThrows(StackOverflowException.class, () -> firstStack.push(5));

        //Pushing (secondStack: [5,6,7,8,9])
        for (int i = 5; i < 10; i++) {
            secondStack.push(i);
            //check whether "i" correctly added to top of stack
            assertEquals(i, secondStack.top());
        }
        //Pushing full stack: secondStack
        assertThrows(StackOverflowException.class, () -> secondStack.push(10));
        //checking size, should = 5
        assertEquals(5, firstStack.size());
        assertEquals(5, secondStack.size());

        //Popping: firstStack: [0,1,2,null,null] from [0,1,2,3,4]
        //System.out.println(firstStack.top());
        for (int i = 4; i > 2; i--) {
            assertEquals(i, firstStack.pop());
        }
        //secondStack: [5,6,7,null,null] from [5,6,7,8,9]
        for (int i = 9; i > 7; i--) {
            assertEquals(i, secondStack.pop());
        }

        //checking size, should = 3
        assertEquals(3, firstStack.size());
        assertEquals(3, secondStack.size());

        //Check isEmpty() when stack filled
        assertFalse(firstStack.isEmpty());
        assertFalse(secondStack.isEmpty());

        //Clear filled stack
        firstStack.clear();
        secondStack.clear();
        assertTrue(firstStack.isEmpty());
        assertTrue(secondStack.isEmpty());
        //Clear empty stack
        firstStack.clear();
        secondStack.clear();
        assertTrue(firstStack.isEmpty());
        assertTrue(secondStack.isEmpty());

        //Interweaved Operations, currently both stacks are empty

        firstStack.push(7);
        secondStack.push(113);
        //Shared array: [7(top),null,null,null,null,113(top),null,null,null,null]
        assertEquals(7, firstStack.top());
        assertEquals(113, secondStack.top());

        firstStack.pop();
        secondStack.push(12);
        secondStack.push(null);
        //Shared array: [null,null,null,null,null,113,12,null(pushed|top),null,null]
        assertTrue(firstStack.isEmpty());
        assertNull(secondStack.top());

        secondStack.pop();
        firstStack.push(666);
        //Shared array: [666(top),null,null,null,null,113,12(top),null,null]
        assertEquals(666, firstStack.top());
        assertEquals(12, secondStack.top());

        secondStack.clear();
        firstStack.push('e');//different data types
        //Shared array: [666,'e'(top),null,null,null,null,null,null,null]
        assertEquals(2, firstStack.size());
        assertEquals('e', firstStack.top());
        assertTrue(secondStack.isEmpty());
    }



}
