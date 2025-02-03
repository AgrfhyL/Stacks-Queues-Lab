package impl;

import interfaces.IDoubleStack;
import interfaces.IStack;
import org.hamcrest.core.Is;

public class DoubleStack implements IDoubleStack {
    Object[] sharedArr; //array in which 2 stacks share data in
    Stack firstStack;
    Stack secondStack;
    int maxSize;

    /*
    all elements of shared array will be used
    first stack begins at index 0, ends at maxSize/2-1
    second stack begins in the middle, maxSize/2, ends at end of array
     */
    public DoubleStack(int maxSize) {
        // check for valid maxSize, must be positive
        if (maxSize <= 1) {
            throw new IllegalArgumentException("Please give non-zero positive size greater than 1");
        }
        this.maxSize = maxSize;
        sharedArr = new Object[maxSize];
        firstStack = new Stack(sharedArr, 0, maxSize/2);
        secondStack = new Stack(sharedArr, maxSize/2, (int)(maxSize+0.5)/2);
        // if odd length, will be 1 element extra storage for secondStack
    }

    @Override
    public IStack getFirstStack() {
        return firstStack;
    }

    @Override
    public IStack getSecondStack() {
        return secondStack;
    }
}
