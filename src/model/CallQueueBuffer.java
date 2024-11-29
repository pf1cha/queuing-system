package model;

public class CallQueueBuffer {
    private final Call[] buffer;
    private final int capacity;
    private int size;
    private int pointer;

    public CallQueueBuffer(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.pointer = 0;
        this.buffer = new Call[capacity];
        for (int i = 0; i < capacity; i++) {
            buffer[i] = null;
        }
    }

    public boolean addCall(Call call) {
        if (size == capacity) {
            Call rejectedCall = buffer[pointer];
            rejectedCall.callRejected();
            buffer[pointer] = call;
            pointer = (pointer + 1) % capacity;
            return true;
        }

        int startPointer = pointer;
        int counter = 0;
        do {
            if (buffer[pointer] == null) {
                buffer[pointer] = call;
                size++;
                pointer = (pointer + 1) % capacity;
                return false;
            }
            pointer = (pointer + 1) % capacity;
        } while (pointer != startPointer);
        return false;
    }

    public void removeCall(int index) {
        buffer[index] = null;
        size--;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public Call getCall(int index) {
        return buffer[index];
    }

    public boolean isElementNull(int index) {
        return buffer[index] != null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void info() {
        System.out.println("+--------------+--------+");
        System.out.printf("| %-12s | %-6s |\n", "Buffer Index", "Status");
        System.out.println("+--------------+--------+");

        for (int i = 0; i < capacity; ++i) {
            String index = (i == pointer) ? i + "(*)" : String.valueOf(i);
            String status = (buffer[i] == null) ? "empty" : "Call " + buffer[i].getId();
//            String pointerIndicator = (pointer == i) ? " <- pointer" : "";
            System.out.printf("| %-12s | %-6s |\n", index, status);
        }

        System.out.println("+--------------+--------+");
    }
}