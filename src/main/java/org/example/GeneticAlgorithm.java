package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;
    private static final int TOURNAMENT_SIZE = 5;

    public static Solution applyGeneticAlgorithm(ProblemInstance instance, int seed) {
        Random random = new Random(seed);
        List<boolean[]> population = initializePopulation(instance, random);

        boolean[] bestIndividual = null;
        double bestFitness = Double.MAX_VALUE;

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            List<boolean[]> newPopulation = new ArrayList<>();

            for (int i = 0; i < POPULATION_SIZE; i++) {
                boolean[] parent1 = selectParent(instance, population, random);
                boolean[] parent2 = selectParent(instance, population, random);
                boolean[] offspring = crossover(parent1, parent2, random);
                mutate(offspring, random);
                newPopulation.add(offspring);
            }

            population = newPopulation;

            for (boolean[] individual : population) {
                double fitness = calculateFitness(instance, individual);
                if (fitness < bestFitness) {
                    bestIndividual = individual.clone();
                    bestFitness = fitness;
                    System.out.println("Nova melhor solução na geração " + generation + ": Custo = " + bestFitness);
                }
            }
        }

        long executionTime = System.currentTimeMillis();

        ArrayList<Facility> openFacilities = new ArrayList<>();
        for (int i = 0; i < bestIndividual.length; i++) {
            if (bestIndividual[i]) {
                openFacilities.add(instance.getFacilities().get(i));
            }
        }

        return new Solution(openFacilities, bestFitness, executionTime);
    }

    private static List<boolean[]> initializePopulation(ProblemInstance instance, Random random) {
        List<boolean[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            boolean[] individual = new boolean[instance.getFacilities().size()];
            for (int j = 0; j < individual.length; j++) {
                individual[j] = random.nextBoolean();
            }
            population.add(individual);
        }
        return population;
    }

    private static boolean[] selectParent(ProblemInstance instance, List<boolean[]> population, Random random) {
        List<boolean[]> tournament = new ArrayList<>();
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            tournament.add(population.get(random.nextInt(population.size())));
        }
        return Collections.min(tournament, Comparator.comparingDouble(individual -> calculateFitness(instance, individual)));
    }

    private static boolean[] crossover(boolean[] parent1, boolean[] parent2, Random random) {
        boolean[] offspring = new boolean[parent1.length];
        for (int i = 0; i < parent1.length; i++) {
            offspring[i] = random.nextBoolean() ? parent1[i] : parent2[i];
        }
        return offspring;
    }

    private static void mutate(boolean[] individual, Random random) {
        for (int i = 0; i < individual.length; i++) {
            if (random.nextDouble() < MUTATION_RATE) {
                individual[i] = !individual[i];
            }
        }
    }

    private static double calculateFitness(ProblemInstance instance, boolean[] individual) {
        double totalCost = 0;
        for (int i = 0; i < individual.length; i++) {
            if (individual[i]) {
                totalCost += instance.getFacilities().get(i).getFixedCost();
            }
        }
        for (int j = 0; j < instance.getCustomers().size(); j++) {
            double minCost = Double.MAX_VALUE;
            for (int i = 0; i < individual.length; i++) {
                if (individual[i]) {
                    minCost = Math.min(minCost, instance.getAllocationCosts()[j][i]);
                }
            }
            totalCost += minCost;
        }
        return totalCost;
    }
}
