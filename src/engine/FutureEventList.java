package engine;
import java.util.PriorityQueue;
import engine.Event;
import model.Call;
import model.CallGenerator;
import engine.Events;

public class FutureEventList {
    private final PriorityQueue<Event> queue;

    public FutureEventList() {
        this.queue = new PriorityQueue<>();
    }

    // Добавление события в очередь
    public void scheduleEvent(double eventTime, Events action, Call call, CallGenerator generator) {
        Event event = new Event(eventTime, action, call, generator);
        queue.add(event);
    }

    public Event deleteEvent() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
