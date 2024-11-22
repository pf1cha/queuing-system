package model;

public class CallQueueBuffer {
    private final Call[] buffer;  // Массив для хранения заявок
    private final int capacity;
    private int size;              // Текущий размер буфера
    private int pointer;           // Указатель на следующее место для вставки заявки

    public CallQueueBuffer(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.pointer = 0;
        this.buffer = new Call[capacity];
        for (int i = 0; i < capacity; i++) {
            buffer[i] = null;
        }
    }

    // Добавление заявки в буфер
    public boolean addCall(Call call) {
        if (size == capacity) {
            // Если буфер полный, заменяем заявку на месте указателя
            // убираем старую заявку и заменяем её новой
            Call rejectedCall = buffer[pointer];
            // Отмечаем, что заявка была отклонена :(
            rejectedCall.callRejected();
            // Заменяем старую заявку
            buffer[pointer] = call;
            // Передвигаем указатель
            pointer = (pointer + 1) % capacity;
            // Указатель на старую заявку должен сдвигаться в случае переполнения
            return true;
        }

        // Найти первое свободное место начиная с указателя
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

    // Удаление заявки по индексу
    public void removeCall(int index) {
        // Удаляем заявку (просто ставим null)
        // System.out.println("Removing Call " + buffer[index].getId() + " from buffer.");
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

    // Проверка, пуст ли буфер
    public boolean isEmpty() {
        return size == 0;
    }

    // Размер буфера (текущая длина очереди)
    public int size() {
        return size;
    }
}