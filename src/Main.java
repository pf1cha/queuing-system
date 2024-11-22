import engine.*;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        findOptimal();
//        Model model = new Model(3, 2, 0.4, 3);
//        model.run(10, Simulation.StepByStep);
//        Model model = new Model(8, 11, 0.2, 11);
//        model.runSimulation(2500);
    }

    public static void findOptimal() {
        int minNumOperators = 2;
        int maxNumOperators = 6;
        int minNumGenerators = 4;
        int maxNumGenerators = 11;
        double lambda = 0.3;
        int minBufferCapacity = 10;
        int maxBufferCapacity = 31;
        System.out.println("Generator | Buffer | Operators | Refuse % | Operator Usage | Time in System");
        System.out.println("---------------------------------------------------------------");

        for (int i = minNumGenerators; i < maxNumGenerators; ++i) {
            for (int j = minBufferCapacity; j < maxBufferCapacity; ++j) {
                for (int k = minNumOperators; k < maxNumOperators; ++k) {
                    Model model = new Model(k, j, lambda, i);
                    Map<String, Double> result = model.runOptimal(2500);
                    double refuse = result.get("refusePercent") * 100;
                    double operatorUsage = result.get("operatorsUsage") * 100;
                    double timeInSystem = result.get("timeInSystem");
                    if (refuse > 10 || operatorUsage < 90 || timeInSystem > 10) continue;
                    System.out.printf("%-9d | %-6d | %-9d | %-8.2f | %-14.2f | %-15.2f%n",
                            i, j, k, refuse, operatorUsage, timeInSystem);
                }
            }
        }
    }
}