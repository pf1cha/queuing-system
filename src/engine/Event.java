package engine;

import model.Call;
import model.CallGenerator;

/**
 * @param eventTime Время, когда должно быть выполнено действие
 * @param action    Действие, которое нужно выполнить
 * @param call Звонок, над которым мы проводим действия
 * @param generator Генератор для заявок
 */
public record Event(double eventTime, Events action, Call call, CallGenerator generator) implements Comparable<Event> {
    @Override
    public int compareTo(Event other) {
        // Очередь с приоритетом будет отсортирована по времени события
        return Double.compare(this.eventTime, other.eventTime);
    }
}