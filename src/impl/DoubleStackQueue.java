package impl;

import common.QueueEmptyException;
import common.QueueFullException;
import common.StackEmptyException;
import common.StackOverflowException;
import interfaces.IQueue;

public class DoubleStackQueue implements IQueue {

    //when pop from top of firstStack, secondStack will be reverse of it
    //use firstStack as actual storage, secondStack will be used when dequeueing
    private DoubleStack doubleStack;
    private Stack firstStack;
    private Stack secondStack;

    public DoubleStackQueue(int maxSize) {
        doubleStack = new DoubleStack(maxSize);
        firstStack = doubleStack.firstStack;
        secondStack = doubleStack.secondStack;
    }

    @Override
    public void enqueue(Object element) throws QueueFullException {
        try {
            firstStack.push(element);
        } catch (StackOverflowException e) {
            throw new QueueFullException();
        }
    }

    @Override
    public Object dequeue() throws QueueEmptyException {
        if (isEmpty()) throw new QueueEmptyException();

        try {
            if (secondStack.isEmpty()) {
                while (!isEmpty()) { //pushes all firstStack elements into secondStack in reversed order
                    secondStack.push(firstStack.pop());
                }
            }
            //pops the top element of secondStack, which is 1st element of first (actual) stack
            //thus FIFO
            Object removed = secondStack.pop();
            //pop out everything from secondStack & push back to firstStack
            // now in the right order but 1st element removed
            while (!secondStack.isEmpty()) {
                    firstStack.push(secondStack.pop());
            }
            return removed;
        } catch (StackEmptyException e) {
            throw new QueueEmptyException();
        } catch (StackOverflowException e){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean isEmpty() {
        return firstStack.isEmpty();
    }

    @Override
    public int size() {
        return firstStack.size();
    }

    @Override
    public void clear() {
        firstStack.clear();
        //secondStack is always cleared, only temporarily filled (and then cleared) when executing dequeue()
    }
}
