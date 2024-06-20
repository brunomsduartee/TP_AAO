package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AntColonyOptimization {
    private static final int NUM_ANTS = 50;
    private static final int MAX_ITERATIONS = 100;
    private static final double ALPHA = 1.0; // Influência dos feromônios
    private static final double BETA = 5.0;  // Influência da visibilidade (heurística)
    private static final double EVAPORATION_RATE = 0.5; // Taxa de evaporação dos feromônios
    private static final double INITIAL_PHEROMONE = 1.0; // Feromônio inicial

    public static Solution solve(ProblemInstance instance) {
        long startTime = System.currentTimeMillis();

        ArrayList<Facility> facilities = instance.getFacilities();
        int numFacilities = facilities.size();
        int numCustomers = instance.getCustomers().size();
        double[][] allocationCosts = instance.getAllocationCosts();
        double[][] pheromones = new double[numCustomers][numFacilities];

        // Inicializar feromônios
        for (int i = 0; i < numCustomers; i++) {
            for (int j = 0; j < numFacilities; j++) {
                pheromones[i][j] = INITIAL_PHEROMONE;
            }
        }

        ArrayList<Facility> bestSolution = null;
        double bestCost = Double.MAX_VALUE;

        System.out.println("Iniciando Ant Colony Optimization...");

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            ArrayList<ArrayList<Facility>> allSolutions = new ArrayList<>();
            double[] solutionCosts = new double[NUM_ANTS];

            // Construir soluções para cada formiga
            for (int ant = 0; ant < NUM_ANTS; ant++) {
                ArrayList<Facility> solution = constructSolution(instance, pheromones);
                double cost = calculateTotalCost(instance, solution);
                allSolutions.add(solution);
                solutionCosts[ant] = cost;

                if (cost < bestCost) {
                    bestSolution = new ArrayList<>(solution);
                    bestCost = cost;
                    System.out.println("Nova melhor solução encontrada: Custo = " + bestCost);
                }
            }

            // Atualizar feromônios
            updatePheromones(pheromones, allSolutions, solutionCosts);

            // Evaporar feromônios
            evaporatePheromones(pheromones);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        return new Solution(bestSolution, bestCost, executionTime);
    }

    private static ArrayList<Facility> constructSolution(ProblemInstance instance, double[][] pheromones) {
        Random random = new Random();
        ArrayList<Facility> facilities = instance.getFacilities();
        ArrayList<Facility> solution = new ArrayList<>();
        boolean[] isFacilityOpen = new boolean[facilities.size()];

        Set<Integer> uncoveredCustomers = new HashSet<>();
        for (int i = 0; i < instance.getCustomers().size(); i++) {
            uncoveredCustomers.add(i);
        }

        while (!uncoveredCustomers.isEmpty()) {
            int customer = random.nextInt(uncoveredCustomers.size());
            double[] probabilities = new double[facilities.size()];
            double sum = 0.0;

            for (int i = 0; i < facilities.size(); i++) {
                if (!isFacilityOpen[i]) {
                    double pheromone = pheromones[customer][i];
                    double cost = 1.0 / instance.getAllocationCosts()[customer][i];
                    probabilities[i] = Math.pow(pheromone, ALPHA) * Math.pow(cost, BETA);
                    sum += probabilities[i];
                }
            }

            double threshold = random.nextDouble() * sum;
            double cumulativeProbability = 0.0;
            int chosenFacility = -1;

            for (int i = 0; i < facilities.size(); i++) {
                if (!isFacilityOpen[i]) {
                    cumulativeProbability += probabilities[i];
                    if (cumulativeProbability >= threshold) {
                        chosenFacility = i;
                        break;
                    }
                }
            }

            if (chosenFacility != -1) {
                isFacilityOpen[chosenFacility] = true;
                facilities.get(chosenFacility).setOpen(true);
                solution.add(facilities.get(chosenFacility));

                // Marcar os clientes cobertos por esta instalação
                int finalChosenFacility = chosenFacility;
                uncoveredCustomers.removeIf(customerIndex -> instance.getAllocationCosts()[customerIndex][finalChosenFacility] < Double.MAX_VALUE);
            }
        }

        return solution;
    }

    private static void updatePheromones(double[][] pheromones, ArrayList<ArrayList<Facility>> allSolutions, double[] solutionCosts) {
        for (int ant = 0; ant < allSolutions.size(); ant++) {
            ArrayList<Facility> solution = allSolutions.get(ant);
            double cost = solutionCosts[ant];

            for (Facility facility : solution) {
                if (facility.isOpen()) {
                    for (int customer = 0; customer < pheromones.length; customer++) {
                        pheromones[customer][facility.getId()] += 1.0 / cost;
                    }
                }
            }
        }
    }

    private static void evaporatePheromones(double[][] pheromones) {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= (1 - EVAPORATION_RATE);
            }
        }
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
