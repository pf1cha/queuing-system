package model;

public class CallGenerator {
    private final double lambda;
    private static int callId = 0;
    private final int id;
    public int generatedItemsAmount = 0;
    private double nextCall = 0.0;

    public CallGenerator(int id, double lambda) {
        this.id = id;
        this.lambda = lambda; // Интенсивность заявок
    }

    public Call generateCall(double currentTime) {
        callId++;
        generatedItemsAmount++; // TODO Delete
        nextCall += -1.0 * Math.log(Math.random()) / lambda;
        return new Call(callId, currentTime, this.id);
    }

    public double getNextCall() {
        return nextCall;
    }

    public int getId() {
        return this.id;
    }

    public static int getCallsNumber() {
        return callId;
    }
}
