package impl;

import common.StackEmptyException;
import common.StackOverflowException;
import interfaces.IStack;

public class Stack implements IStack {
    private Object[] arr;
    private int maxSize;
    private int startingIndex;
    private int tail;

    public Stack(Object[] sharedArr, int startingIndex, int maxSize) {
        arr = sharedArr;
        this.maxSize = maxSize;
        tail = startingIndex - 1;
        this.startingIndex = startingIndex;
    }

    @Override
    public void push(Object data) throws StackOverflowException {
        //if tail gets incremented to this stack's size/length –> out of bounds
        if (tail + 1 >= startingIndex + maxSize) {
            throw new StackOverflowException();
        }
        tail++;
        arr[tail] = data;
    }

    @Override
    public Object pop() throws StackEmptyException {
        if (isEmpty()) {
            throw new StackEmptyException();
        }
        Object removed = arr[tail];
        arr[tail] = null;
        tail--;
        return removed;
    }

    @Override
    public Object top() throws StackEmptyException {
        if (isEmpty()) {
            throw new StackEmptyException();
        }
        return arr[tail];
    }

    @Override
    public int size() {
        return tail - startingIndex + 1;
    }
    // all calculations of size are relative to tail and startingIndex instead of index 0
    @Override
    public boolean isEmpty() {
        return tail == startingIndex - 1;
    }

    //traversal and setting all elements to null
    @Override
    public void clear() {
        for (int i = startingIndex; i < maxSize + startingIndex; i++) {
            arr[i] = null;
        }
        tail = startingIndex - 1;
    }
}
