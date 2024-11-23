package model;

public class Operator {
    private final int id;
    private boolean isBusy;
    private Call currentCall = null;
    private double totalWorkTime = 0.0;
    private int callAmount = 0;

    public Operator(int id) {
        this.id = id;
        this.isBusy = false;
    }

    public double generateServiceTime() {
        double minServiceTime = 1.0;
        double maxServiceTime = 5.0;
        return minServiceTime + (maxServiceTime - minServiceTime) * Math.random();
    }

    public int getId() {
        return id;
    }

    public boolean isBusy(double currentTime) {
        return isBusy;
    }

    public void assignCall(Call call, double currentTime) {
        callAmount++;
        this.isBusy = true;
        this.currentCall = call;
        call.setServiceStartTime(currentTime);
        call.setOperatorId(this.id);
        double serviceTime = generateServiceTime();
        call.setServiceTime(serviceTime);
    }

    public void releaseCall(double currentTime) {
        if (currentCall != null) {
            double departureTime = currentCall.getServiceStartTime() + currentCall.getServiceTime();
            currentCall.setDepartureTime(departureTime);
            totalWorkTime += currentCall.getServiceTime();
        }
        this.isBusy = false;
        this.currentCall = null;
    }

    public double getTotalWorkTime() {
        return totalWorkTime;
    }

    public int getCallAmount() {
        return this.callAmount;
    }

    public int getCurrentCallId() {
        return (currentCall != null ? currentCall.getId() : 0);
    }
}
