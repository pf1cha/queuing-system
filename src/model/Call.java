package model;

public class Call {
    private final int id;
    private final double arrivalTime;
    private double serviceStartTime;
    private double serviceTime;
    private double departureTime;
    private boolean rejected;
    private int operatorId;
    private final int generatorId;

    public Call(int id, double arrivalTime, int generatorId) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.rejected = false;
        this.generatorId = generatorId;
    }

    public void printInfo() {
        System.out.println("Call{id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", serviceStartTime=" + serviceStartTime +
                ", serviceTime=" + serviceTime +
                ", departureTime=" + departureTime +
                ", rejected=" + rejected +
                ", operatorId=" + operatorId + "}");
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(double serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public int getGeneratorId() {
        return generatorId;
    }

    public void callRejected() {
        this.rejected = true;
    }

    public boolean isRejected() {
        return this.rejected;
    }
}
