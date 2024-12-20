package model;

import java.util.List;

// Диспетчер

public class CallDispatcher {
    private final List<Operator> operators;
    private final CallQueueBuffer buffer;
    private int rejectedCalls = 0;

    public CallDispatcher(List<Operator> operators, CallQueueBuffer buffer) {
        this.operators = operators;
        this.buffer = buffer;
    }

    private Operator findFreeOperator(double currentTime) {
        for (Operator operator : operators) {
            if (!operator.isBusy(currentTime)) {
                return operator;
            }
        }
        return null;
    }

    public double findPlaceForCall(Call call, double currentTime) {
        Operator freeOperator = findFreeOperator(currentTime);
        if (freeOperator != null) {
            freeOperator.assignCall(call, currentTime);
            return call.getServiceTime() + call.getServiceStartTime();
        }
        boolean rejected = buffer.addCall(call);
        if (rejected) {
            rejectedCalls++;
        }
        return currentTime;
    }

    public Call dispatchCall() {
        if (buffer.getSize() == 0) {
            return null;
        }
        // Перебираем все элементы буфера и ищем заявку, которая простояла дольше всех (FIFO)
        Call oldestCall = null;
        int oldestIndex = -1;
        for (int i = 0; i < buffer.getCapacity(); i++) {
            if (buffer.isElementNull(i)) {
                if (oldestCall == null || buffer.getCall(i).getArrivalTime() < oldestCall.getArrivalTime()) {
                    oldestCall = buffer.getCall(i);
                    oldestIndex = i;
                }
            }
        }
        if (oldestCall != null) {
            buffer.removeCall(oldestIndex);
        }
        return oldestCall;
    }

    public int getRejectedCalls() {
        return rejectedCalls;
    }
}
