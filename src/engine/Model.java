package engine;
import model.*;
import java.util.*;


public class Model {
    private final List<Operator> operators;
    private final CallQueueBuffer buffer;
    private final List<CallGenerator> generators;
    private final CallDispatcher dispatcher;
    private final FutureEventList futureEventList;
    private final List<Call> calls;
    private double currentTime;


    public Model(int numOperators, int bufferCapacity, double lambda, int numGenerators) {
        operators = new ArrayList<>();
        for (int i = 1; i <= numOperators; i++) {
            operators.add(new Operator(i));
        }
        generators = new ArrayList<>();
        for (int i = 1; i <= numGenerators; i++) {
            generators.add(new CallGenerator(i, lambda));
        }
        buffer = new CallQueueBuffer(bufferCapacity);
        dispatcher = new CallDispatcher(operators, buffer);
        futureEventList = new FutureEventList();
        calls = new ArrayList<>();
        currentTime = 0.0;
    }

    public void runStepByStep(double simulationTime) {
        for (CallGenerator generator : generators) {
            futureEventList.scheduleEvent(0.0, Events.Generate, null, generator);
        }
        Scanner scanner = new Scanner(System.in);
        while (!futureEventList.isEmpty() && currentTime < simulationTime) {
            String input = scanner.nextLine();
            if (!input.equalsIgnoreCase("q")) {
                continue;
            }
            Event event = futureEventList.deleteEvent();
            currentTime = event.eventTime();
            if (currentTime > simulationTime) {
                break;
            }
            System.out.println("Current time in the system: " + currentTime);
            if (event.action() == Events.Generate) {
                Call newCall = event.generator().generateCall(currentTime);
                System.out.println("Call " + newCall.getId() + " was generated");
                calls.add(newCall);
                double time = dispatcher.findPlaceForCall(newCall, currentTime);
                if (time != currentTime) {
                    System.out.println("Call is being processed by operator.");
                    futureEventList.scheduleEvent(time, Events.Release, newCall, null);
                } else {
                    System.out.println("Call was added to buffer.");
                }
                futureEventList.scheduleEvent(event.generator().getNextCall(),
                        Events.Generate, null, event.generator());
            }
            else if (event.action() == Events.Release) {
                if (event.call() == null) {
                    continue;
                }
                System.out.println("Call " + event.call().getId() + " has been processed.");
                Operator operator = operators.get(event.call().getOperatorId() - 1);
                operator.releaseCall(currentTime);
                System.out.println("Dispatcher is trying to find new call from buffer");
                Call call = dispatcher.dispatchCall();
                if (call != null) {
                    System.out.println("Call " + call.getId() + " was moved from buffer.");
                    double time = dispatcher.findPlaceForCall(call, currentTime);
                    System.out.println("Call is being processed by operator.");
                    futureEventList.scheduleEvent(time, Events.Release, call, null);
                }
            }
            stepByStepStats();
        }
        System.out.println("Simulation ended.");
    }

    private void stepByStepStats() {
        System.out.println("Buffer information:");
        buffer.info();
//        System.out.println("\nGenerators information");
//        for (var generator : generators) {
//            System.out.println("Generator " + generator.getId() +
//                    " has generated " + generator.generatedItemsAmount + " calls");
//        }
//        System.out.println("\nOperators information");
//        for (var operator : operators) {
//            int currentCallId = operator.getCurrentCallId();
//            System.out.println("Operator " + operator.getId() +
//                    " Current call id: " + (currentCallId == 0 ? "None" : currentCallId) +
//                    ", Total calls: " +  operator.getCallAmount() +
//                    ", Total time work: " + operator.getTotalWorkTime());
//        }
        System.out.println("Generators information");
        System.out.println("+----+-------------+");
        System.out.printf("| %-2s | %-11s |\n", "ID", "Total Calls");
        System.out.println("+----+-------------+");
        for (var generator : generators) {
            System.out.printf("| %-2d | %-11d |\n", generator.getId(), generator.generatedItemsAmount);
        }
        System.out.println("+----+-------------+");

        System.out.println("Operators information");
        System.out.println("+----+---------+-------------+-----------------+");
        System.out.printf("| %-2s | %-7s | %-11s | %-15s |\n", "ID", "Call ID", "Total Calls", "Total Work Time");
        System.out.println("+----+---------+-------------+-----------------+");

        for (var operator : operators) {
            int currentCallId = operator.getCurrentCallId();
            System.out.printf("| %-2d | %-7s | %-11d | %-15.2f |\n",
                    operator.getId(),
                    (currentCallId == 0 ? "None" : currentCallId),
                    operator.getCallAmount(),
                    operator.getTotalWorkTime());
        }
        System.out.println("+----+---------+-------------+-----------------+");
    }

    public void runSimulation(double simulationTime) {
        for (CallGenerator generator : generators) {
            futureEventList.scheduleEvent(0.0, Events.Generate, null, generator);
        }
        while (!futureEventList.isEmpty() && currentTime < simulationTime) {
            Event event = futureEventList.deleteEvent();
            currentTime = event.eventTime();

            if (currentTime > simulationTime) {
                break;
            }

            if (event.action() == Events.Generate) {
                Call newCall = event.generator().generateCall(currentTime);
                calls.add(newCall);
                double time = dispatcher.findPlaceForCall(newCall, currentTime);
                if (time != currentTime) {
                    futureEventList.scheduleEvent(time, Events.Release, newCall, null);
                }
                futureEventList.scheduleEvent(event.generator().getNextCall(),
                        Events.Generate, null, event.generator());
            }
            else if (event.action() == Events.Release) {
                if (event.call() == null) {
                    continue;
                }
                Operator operator = operators.get(event.call().getOperatorId() - 1);
                operator.releaseCall(currentTime);
                Call call = dispatcher.dispatchCall();
                if (call != null) {
                    double time = dispatcher.findPlaceForCall(call, currentTime);
                    futureEventList.scheduleEvent(time, Events.Release, call, null);
                }
            }
        }

        printSimulationResults();
    }


    public Map<String, Double> runOptimal(double simulationTime) {
        for (CallGenerator generator : generators) {
            futureEventList.scheduleEvent(0.0, Events.Generate, null, generator);
        }
        while (!futureEventList.isEmpty() && currentTime < simulationTime) {
            Event event = futureEventList.deleteEvent();
            currentTime = event.eventTime();

            if (currentTime > simulationTime) {
                break;
            }

            if (event.action() == Events.Generate) {
                Call newCall = event.generator().generateCall(currentTime);
                calls.add(newCall);
                double time = dispatcher.findPlaceForCall(newCall, currentTime);
                if (time != currentTime) {
                    futureEventList.scheduleEvent(time, Events.Release, newCall, null);
                }
                futureEventList.scheduleEvent(event.generator().getNextCall(),
                        Events.Generate, null, event.generator());
            }
            else if (event.action() == Events.Release) {
                if (event.call() == null) {
                    continue;
                }
                Operator operator = operators.get(event.call().getOperatorId() - 1);
                operator.releaseCall(currentTime);
                Call call = dispatcher.dispatchCall();
                if (call != null) {
                    double time = dispatcher.findPlaceForCall(call, currentTime);
                    futureEventList.scheduleEvent(time, Events.Release, call, null);
                }
            }
        }

        return getStringDoubleMap(simulationTime);
    }

    private Map<String, Double> getStringDoubleMap(double simulationTime) {
        double refusePercent = (double) dispatcher.getRejectedCalls() / calls.size();
        double operatorsUsage = 0.0;
        for (var operator : operators) {
            operatorsUsage += operator.getTotalWorkTime() / simulationTime;
        }
        operatorsUsage /= operators.size();

        double timeInSystem = 0.0;
        int totalProcessedCalls = 0;
        for (var call : calls) {
            if (!call.isRejected() && call.getArrivalTime() < 2500 && call.getDepartureTime() != 0.0) {
                timeInSystem += call.getDepartureTime() - call.getArrivalTime();
                totalProcessedCalls++;
            }
        }
        timeInSystem /= totalProcessedCalls;
        Map<String, Double> result = new HashMap<>();
        result.put("refusePercent", refusePercent);
        result.put("operatorsUsage", operatorsUsage);
        result.put("timeInSystem", timeInSystem);
        return result;
    }

    private void printSimulationResults() {
        System.out.printf("Simulation ended at time %.2f \n", currentTime);
        System.out.println("Total number of generated calls: " + CallGenerator.getCallsNumber());
        System.out.println("Total number of rejected calls: " + dispatcher.getRejectedCalls());
        double temp = (double) dispatcher.getRejectedCalls() / calls.size() * 100;
        System.out.printf("Rejected rate is %.2f %% percent\n", temp);
        operatorsTable();
        printGeneratorsStats();
    }

    private void operatorsTable() {
        System.out.println("Stats for operators:");
        System.out.println("+----+--------------+");
        System.out.println("| ID | Work Time (%)|");
        System.out.println("+----+--------------+");
        for (Operator operator : operators) {
            System.out.printf("| %-2d | %-12.2f |\n", operator.getId(), operator.getTotalWorkTime() / currentTime * 100);
        }
        System.out.println("+----+--------------+");
    }

    private void printGeneratorsStats() {
        int[] rejectedCalls = new int[generators.size()];
        double[] avrTimeSystem = new double[generators.size()];
        double[] avrWaitTime = new double[generators.size()];
        double[] avrProcessTime = new double[generators.size()];
        double[] waitTimeVariance = new double[generators.size()];
        double[] processTimeVariance = new double[generators.size()];
        int[] totalCalls = new int[generators.size()];

        for (int i = 0; i < generators.size(); ++i) {
            rejectedCalls[i] = 0;
            avrTimeSystem[i] = 0;
            avrWaitTime[i] = 0;
            avrProcessTime[i] = 0;
            waitTimeVariance[i] = 0;
            processTimeVariance[i] = 0;
            totalCalls[i] = generators.get(i).generatedItemsAmount;
        }
        System.out.println("Stats for each generator:");
        for (Call call : calls) {
            int index = call.getGeneratorId() - 1;
            if (call.isRejected()) {
                rejectedCalls[index] += 1;
            } else {
                if (call.getDepartureTime() > 2500 || call.getDepartureTime() == 0.0) {
                    continue;
                }
                avrTimeSystem[index] += call.getDepartureTime() - call.getArrivalTime();
                avrWaitTime[index] += call.getServiceStartTime() - call.getArrivalTime();
                avrProcessTime[index] += call.getServiceTime();
            }
        }

        for (int i = 0; i < generators.size(); ++i) {
            int workingCalls = totalCalls[i] - rejectedCalls[i];
            avrTimeSystem[i] = avrTimeSystem[i] / workingCalls;
            avrProcessTime[i] = avrProcessTime[i] / workingCalls;
            avrWaitTime[i] = avrWaitTime[i] / workingCalls;
        }

        for (Call call : calls) {
            if (!call.isRejected()) {
                if (call.getDepartureTime() > 2500 || call.getDepartureTime() == 0.0) {
                    continue;
                }
                int index = call.getGeneratorId() - 1;
                double waitTime = call.getServiceStartTime() - call.getArrivalTime();
                double processTime = call.getServiceTime();
                int workingCalls = totalCalls[index] - rejectedCalls[index];
                // Дисперсия для времени ожидания
                waitTimeVariance[index] += (Math.pow(waitTime - avrWaitTime[index], 2) / workingCalls);
                // Дисперсия для времени обслуживания
                processTimeVariance[index] += Math.pow(processTime - avrProcessTime[index], 2) / workingCalls;
            }
        }
        printCombinedStatsTable(rejectedCalls, avrTimeSystem, avrWaitTime, avrProcessTime, waitTimeVariance, processTimeVariance);
    }

    private void printCombinedStatsTable(int[] rejectedCalls, double[] avrTimeSystem, double[] avrWaitTime, double[] avrProcessTime, double[] waitTimeVariance, double[] processTimeVariance) {
        System.out.println("+-----+---------------+--------------------+---------------+------------------+--------------------+-----------------------+");
        System.out.printf("| %-3s | %-13s | %-18s | %-13s | %-16s | %-18s | %-21s |\n",
                "ID", "Total Calls", "Rejected Calls (%)", "Avg Wait Time", "Avg Process Time", "Wait Time Variance", "Process Time Variance");
        System.out.println("+-----+---------------+--------------------+---------------+------------------+--------------------+-----------------------+");

        int i = 0;
        for (CallGenerator generator : generators) {
            int totalCalls = generator.generatedItemsAmount;
            double rejectedPercent = (double) rejectedCalls[i] / totalCalls * 100;
            System.out.printf("| %-3s | %-13d | %-18.2f | %-13.2f | %-16.2f | %-18.2f | %-21.2f |\n",
                    generator.getId(), totalCalls, rejectedPercent, avrWaitTime[i], avrProcessTime[i], waitTimeVariance[i], processTimeVariance[i]);
            i++;
        }
        System.out.println("+-----+---------------+--------------------+---------------+------------------+--------------------+-----------------------+");
    }
}
