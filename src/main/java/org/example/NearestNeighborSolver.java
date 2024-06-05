package org.example;

import java.util.HashSet;
import java.util.Set;

public class NearestNeighborSolver implements AllocationStrategy {
    @Override
    public void solve(ProblemInstance problemInstance) {
        Set<Integer> openFacilities = new HashSet<>();
        double totalCost = 0.0;

        for (int i = 0; i < problemInstance.getCustomers().size(); i++) {
            double minCost = Double.MAX_VALUE;
            int bestFacility = -1;

            for (int j = 0; j < problemInstance.getFacilities().size(); j++) {
                double thisCost = problemInstance.getAllocationCosts()[i][j];
                if (thisCost < minCost) {
                    minCost = thisCost;
                    bestFacility = j;
                }
            }

            if (!openFacilities.contains(bestFacility)) {
                totalCost += problemInstance.getFacilities().get(bestFacility).getFixedCost();
            }
            totalCost += minCost;
            openFacilities.add(bestFacility);
        }

        System.out.println("Nearest Neighbor Total cost: " + totalCost);
        System.out.println("Opened facilities by Nearest Neighbor:");
        openFacilities.forEach(f -> System.out.println("Facility " + f + " is open"));
    }
}
