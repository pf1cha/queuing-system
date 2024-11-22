//package engine;
//import model.CallDispatcher;
//import model.CallGenerator;
//import model.Call;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.LinkedList;
//import java.util.PriorityQueue;
//
//public class Modeling {
//    private final CallGenerator generator;
//    private final CallDispatcher dispatcher;
//    private final double simulationTime;
//    private double currentTime = 0.0;
//
//    public Modeling(CallGenerator generator, CallDispatcher dispatcher, double simulationTime) {
//        this.generator = generator;
//        this.dispatcher = dispatcher;
//        this.simulationTime = simulationTime;
//    }
//
//    public void run() {
//        List<Call> allCalls = new ArrayList<>();
//        PriorityQueue<Call> queue = new PriorityQueue<>();
//        Call call = generator.generateCall(currentTime);
//        allCalls.add(call);
//        queue.add(call);
//        while (!queue.isEmpty() && currentTime < simulationTime) {
//            Call current = queue.poll();
//            currentTime = current.getArrivalTime();
//            allCalls.add(current);
//            dispatcher.dispatchCall(current, currentTime);
//
//        }
//
//        while (currentTime < simulationTime) {
//            Call call = generator.generateCall(currentTime);
//            allCalls.add(call);
//            dispatcher.dispatchCall(call, currentTime);
//            dispatcher.processCompletedCalls(currentTime);
//            currentTime += 0.1; // Шаг моделирования
//        }
//        System.out.println("Simulation completed. Total calls generated: " + allCalls.size());
//    }
//}
