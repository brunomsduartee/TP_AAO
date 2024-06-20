package org.example;

import java.util.ArrayList;
import java.util.Random;

public class VariableNeighborhoodSearch {

    private static final int MAX_ITERATIONS = 1000;
    private static final int MAX_NEIGHBORHOODS = 3;

    public static Solution solve(ProblemInstance instance) {
        long startTime = System.currentTimeMillis();

        ArrayList<Facility> currentSolution = initializeSolution(instance);
        double currentCost = calculateTotalCost(instance, currentSolution);

        ArrayList<Facility> bestSolution = new ArrayList<>(currentSolution);
        double bestCost = currentCost;

        int iterations = 0;

        System.out.println("Variable Neighborhood Search a começar...");
        System.out.println("Custo inicial: " + currentCost);

        while (iterations < MAX_ITERATIONS) {
            boolean improvement = false;
            int k = 1;

            while (k <= MAX_NEIGHBORHOODS) {
                ArrayList<Facility> newSolution = shake(instance, new ArrayList<>(currentSolution), k);
                newSolution = localSearch(instance, newSolution);
                double newCost = calculateTotalCost(instance, newSolution);

                if (newCost < currentCost) {
                    currentSolution = new ArrayList<>(newSolution);
                    currentCost = newCost;

                    if (newCost < bestCost) {
                        bestSolution = new ArrayList<>(newSolution);
                        bestCost = newCost;
                        System.out.println("Nova melhor solução encontrada: Custo = " + bestCost);
                    }

                    improvement = true;
                    k = 1;
                } else {
                    k++;
                }
            }

            if (!improvement) {
                iterations++;
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Filtrar instalações abertas
        ArrayList<Facility> openFacilities = new ArrayList<>();
        for (Facility facility : bestSolution) {
            if (facility.isOpen()) {
                openFacilities.add(facility);
            }
        }

        return new Solution(openFacilities, bestCost, executionTime);
    }

    private static ArrayList<Facility> initializeSolution(ProblemInstance instance) {
        ArrayList<Facility> solution = new ArrayList<>();
        Random random = new Random();
        for (Facility facility : instance.getFacilities()) {
            Facility newFacility = new Facility(facility.getId(), facility.getFixedCost());
            newFacility.setOpen(random.nextBoolean());
            solution.add(newFacility);
        }
        return solution;
    }

    private static ArrayList<Facility> shake(ProblemInstance instance, ArrayList<Facility> solution, int k) {
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(solution.size());
            Facility facility = solution.get(index);
            facility.setOpen(!facility.isOpen());
        }
        return solution;
    }

    private static ArrayList<Facility> localSearch(ProblemInstance instance, ArrayList<Facility> solution) {
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            double currentCost = calculateTotalCost(instance, solution);

            for (Facility facility : solution) {
                facility.setOpen(!facility.isOpen());
                double newCost = calculateTotalCost(instance, solution);

                if (newCost < currentCost) {
                    currentCost = newCost;
                    improvement = true;
                } else {
                    facility.setOpen(!facility.isOpen());
                }
            }
        }
        return solution;
    }

    private static double calculateTotalCost(ProblemInstance instance, ArrayList<Facility> facilities) {
        double totalCost = 0;
        for (Facility facility : facilities) {
            if (facility.isOpen()) {
                totalCost += facility.getFixedCost();
            }
        }
        for (Customer customer : instance.getCustomers()) {
            double minCost = Double.MAX_VALUE;
            for (Facility facility : facilities) {
                if (facility.isOpen()) {
                    double cost = instance.getAllocationCosts()[customer.getId()][facility.getId()];
                    minCost = Math.min(minCost, cost);
                }
            }
            totalCost += minCost;
        }
        return totalCost;
    }
}
