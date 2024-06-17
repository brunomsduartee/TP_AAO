package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalSearchSolver {
    private ProblemInstance instance;
    private Map<Integer, Integer> customerAssignments;

    public LocalSearchSolver(ProblemInstance instance) {
        this.instance = instance;
        customerAssignments = new HashMap<>();
        initializeAssignments();
    }

    private void initializeAssignments() {
        // Inicializar as atribuições de clientes para as instalações abertas na solução inicial
        double[][] costs = instance.getAllocationCosts();
        ArrayList<Facility> facilities = instance.getFacilities();

        for (int customer = 0; customer < instance.getCustomers().size(); customer++) {
            double minCost = Double.MAX_VALUE;
            int bestFacility = -1;

            for (int facility = 0; facility < facilities.size(); facility++) {
                double totalFacilityCost = costs[customer][facility];

                if (totalFacilityCost < minCost) {
                    minCost = totalFacilityCost;
                    bestFacility = facility;
                }
            }

            customerAssignments.put(customer, bestFacility);
        }
    }

    public double solve() {
        boolean improved = true;
        double totalCost = calculateTotalCost();

        while (improved) {
            improved = false;
            for (int customer = 0; customer < instance.getCustomers().size(); customer++) {
                int currentFacility = customerAssignments.get(customer);
                for (int newFacility = 0; newFacility < instance.getFacilities().size(); newFacility++) {
                    if (newFacility != currentFacility) {
                        if (tryMoveCustomer(customer, currentFacility, newFacility)) {
                            double newCost = calculateTotalCost();
                            if (newCost < totalCost) {
                                totalCost = newCost;
                                moveCustomer(customer, newFacility);
                                improved = true;
                                break; // Break if improvement is found
                            } else {
                                // Reverter a mudança se não houver melhora
                                moveCustomer(customer, currentFacility);
                            }
                        }
                    }
                }
                if (improved) break; // Break if improvement is found
            }
        }

        System.out.println("Improved Solution Cost after Local Search: " + totalCost);
        return totalCost;
    }

    private boolean tryMoveCustomer(int customer, int currentFacility, int newFacility) {
        // Implement logic to check if moving customer is feasible or beneficial
        return true; // Return true if move is feasible
    }

    private void moveCustomer(int customer, int newFacility) {
        customerAssignments.put(customer, newFacility);
    }

    private double calculateTotalCost() {
        double totalCost = 0;
        boolean[] isFacilityOpen = new boolean[instance.getFacilities().size()];

        for (int customer : customerAssignments.keySet()) {
            int facility = customerAssignments.get(customer);
            totalCost += instance.getAllocationCosts()[customer][facility];
            isFacilityOpen[facility] = true;
        }

        for (int facility = 0; facility < isFacilityOpen.length; facility++) {
            if (isFacilityOpen[facility]) {
                totalCost += instance.getFacilities().get(facility).getFixedCost();
            }
        }

        return totalCost;
    }

    private int findCurrentFacility(ProblemInstance instance, int customer) {
        return customerAssignments.get(customer);
    }
}
