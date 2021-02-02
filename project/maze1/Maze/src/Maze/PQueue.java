package Maze;

public class PQueue {

    int size = 0;
    int first = 0;
    State[] data;

    PQueue(int capacity) {
        data = new State[capacity];

    }

    PQueue() {
        data = new State[100];
    }

    public void enqueue(State element) {
        if (size == data.length) {
            return;
        }
        data[(first + size) % data.length] = element;
        size++;
        sortLastElm();
    }

    public void sortLastElm() {
        int last = (first + size - 1) % data.length;
        int iterate = size - 1;
        while (iterate > 0 && data[last].getF() >= data[(last + data.length - 1) % data.length].getF()) {
            if (data[last].getF() == data[(last + data.length - 1) % data.length].getF()) {
                if (data[last].getG() > data[(last + data.length - 1) % data.length].getG()) { 
                }
            }
            iterate--;
            State temp = data[last];
            data[last] = data[(last + data.length - 1) % data.length];
            data[(last + data.length - 1) % data.length] = temp;
            last = (last + data.length - 1) % data.length;
        }
    }

    public State dequeue() {
        if (size == 0) {
            return null;
        }
        State result = data[(first + size - 1) % data.length];
        size--;
        //first++;
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public State getPositionSuccessor(State element) {
        int i = first;
        int iterated = 0;
        while (iterated < size && data[i] != null) {
            if (data[i].getRow() == element.getRow() && data[i].getCol() == element.getCol()) {
                return data[i];
            }

            i = (i + 1) % data.length;
            iterated++;
        }

        return null;
    }

    public String toString() {
        String result = "";
        int i = first;
        int iterated = 0;
        while (data[i] != null && iterated <= size) {
            result += "(" + data[i].getRow() + "," + data[i].getCol() + ") ";
            i = (i + 1) % data.length;
            iterated++;
        }

        return result;
    }

   

}
