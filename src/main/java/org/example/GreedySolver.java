package org.example;

import java.util.HashSet;
import java.util.Set;

public class GreedySolver implements AllocationStrategy {
    @Override
    public void solve(ProblemInstance problemInstance) {
        Set<Integer> openFacilities = new HashSet<>();
        double totalCost = 0.0;

        for (int i = 0; i < problemInstance.getCustomers().size(); i++) {
            double minCost = Double.MAX_VALUE;
            int bestFacility = -1;

            for (int j = 0; j < problemInstance.getFacilities().size(); j++) {
                double thisCost = problemInstance.getAllocationCosts()[i][j];
                if (!openFacilities.contains(j)) {
                    thisCost += problemInstance.getFacilities().get(j).getFixedCost();
                }

                if (thisCost < minCost) {
                    minCost = thisCost;
                    bestFacility = j;
                }
            }

            totalCost += minCost;
            openFacilities.add(bestFacility);
        }

        System.out.println("Greedy Total cost: " + totalCost);
        System.out.println("Opened facilities by Greedy:");
        openFacilities.forEach(f -> System.out.println("Facility " + f + " is open"));
    }
}
