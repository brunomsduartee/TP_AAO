package org.example;

import java.util.HashSet;
import java.util.Set;

public class CheapestInsertionSolver implements AllocationStrategy {
    @Override
    public void solve(ProblemInstance problemInstance) {
        Set<Integer> openFacilities = new HashSet<>();
        double totalCost = 0.0;

        boolean[] allocatedCustomers = new boolean[problemInstance.getCustomers().size()];
        while (!allCustomersAllocated(allocatedCustomers)) {
            double minCost = Double.MAX_VALUE;
            int bestFacility = -1;
            int bestCustomer = -1;

            for (int i = 0; i < problemInstance.getCustomers().size(); i++) {
                if (!allocatedCustomers[i]) {
                    for (int j = 0; j < problemInstance.getFacilities().size(); j++) {
                        double thisCost = problemInstance.getAllocationCosts()[i][j];
                        if (!openFacilities.contains(j)) {
                            thisCost += problemInstance.getFacilities().get(j).getFixedCost();
                        }

                        if (thisCost < minCost) {
                            minCost = thisCost;
                            bestFacility = j;
                            bestCustomer = i;
                        }
                    }
                }
            }

            if (bestFacility != -1) {
                allocatedCustomers[bestCustomer] = true;
                openFacilities.add(bestFacility);
                totalCost += minCost;
            }
        }

        System.out.println("Cheapest Insertion Total cost: " + totalCost);
        System.out.println("Opened facilities by Cheapest Insertion:");
        openFacilities.forEach(f -> System.out.println("Facility " + f + " is open"));
    }

    private boolean allCustomersAllocated(boolean[] allocatedCustomers) {
        for (boolean allocated : allocatedCustomers) {
            if (!allocated) return false;
        }
        return true;
    }
}
