package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int POPULATION_SIZE = 100;
    private static final int NUM_GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;
    private static final int TOURNAMENT_SIZE = 5;
    private static final double ELITISM_RATE = 0.1;

    public static Solution solve(ProblemInstance instance) {
        ArrayList<ArrayList<Facility>> population = initializePopulation(instance);
        ArrayList<Facility> bestSolution = null;
        double bestCost = Double.MAX_VALUE;

        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            // Avaliar a aptidão de cada solução na população
            ArrayList<Double> fitnessScores = evaluatePopulation(instance, population);

            // Encontrar a melhor solução da geração atual
            for (int i = 0; i < population.size(); i++) {
                double cost = fitnessScores.get(i);
                if (cost < bestCost) {
                    bestCost = cost;
                    bestSolution = new ArrayList<>(population.get(i));
                }
            }

            // Selecionar as melhores soluções para reprodução
            ArrayList<ArrayList<Facility>> matingPool = selectMatingPool(population, fitnessScores);

            // Gerar uma nova população através de crossover e mutação
            ArrayList<ArrayList<Facility>> newPopulation = reproduce(matingPool, instance);

            // Aplicar elitismo
            applyElitism(population, fitnessScores, newPopulation);

            // Substituir a população antiga pela nova
            population = newPopulation;
        }

        return new Solution(bestSolution, bestCost, NUM_GENERATIONS);
    }

    private static ArrayList<ArrayList<Facility>> initializePopulation(ProblemInstance instance) {
        ArrayList<ArrayList<Facility>> population = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            ArrayList<Facility> individual = new ArrayList<>();
            for (Facility facility : instance.getFacilities()) {
                Facility newFacility = new Facility(facility.getId(), facility.getFixedCost());
                newFacility.setOpen(random.nextBoolean());
                individual.add(newFacility);
            }
            population.add(individual);
        }
        return population;
    }

    private static ArrayList<Double> evaluatePopulation(ProblemInstance instance, ArrayList<ArrayList<Facility>> population) {
        ArrayList<Double> fitnessScores = new ArrayList<>();
        for (ArrayList<Facility> individual : population) {
            double cost = calculateTotalCost(instance, individual);
            fitnessScores.add(cost);
        }
        return fitnessScores;
    }

    private static ArrayList<ArrayList<Facility>> selectMatingPool(ArrayList<ArrayList<Facility>> population, ArrayList<Double> fitnessScores) {
        ArrayList<ArrayList<Facility>> matingPool = new ArrayList<>();
        Random random = new Random();

        // Seleção por torneio
        while (matingPool.size() < POPULATION_SIZE / 2) {
            ArrayList<Facility> best = null;
            double bestFitness = Double.MAX_VALUE;
            for (int i = 0; i < TOURNAMENT_SIZE; i++) {
                int index = random.nextInt(population.size());
                double fitness = fitnessScores.get(index);
                if (fitness < bestFitness) {
                    bestFitness = fitness;
                    best = population.get(index);
                }
            }
            matingPool.add(new ArrayList<>(best));
        }

        // Seleção por roleta
        double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();
        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            double rand = random.nextDouble() * totalFitness;
            double partialSum = 0;
            for (int j = 0; j < population.size(); j++) {
                partialSum += 1 / fitnessScores.get(j); // Corrigido para inverso do fitness
                if (partialSum >= rand) {
                    matingPool.add(new ArrayList<>(population.get(j)));
                    break;
                }
            }
        }

        return matingPool;
    }

    private static ArrayList<ArrayList<Facility>> reproduce(ArrayList<ArrayList<Facility>> matingPool, ProblemInstance instance) {
        ArrayList<ArrayList<Facility>> newPopulation = new ArrayList<>();
        Random random = new Random();
        while (newPopulation.size() < POPULATION_SIZE) {
            int parent1Index = random.nextInt(matingPool.size());
            int parent2Index = random.nextInt(matingPool.size());
            ArrayList<Facility> parent1 = matingPool.get(parent1Index);
            ArrayList<Facility> parent2 = matingPool.get(parent2Index);
            ArrayList<Facility> child = crossover(parent1, parent2);
            mutate(child);
            newPopulation.add(child);
        }
        return newPopulation;
    }

    private static ArrayList<Facility> crossover(ArrayList<Facility> parent1, ArrayList<Facility> parent2) {
        ArrayList<Facility> child = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < parent1.size(); i++) {
            Facility facility = random.nextBoolean() ? parent1.get(i) : parent2.get(i);
            Facility newFacility = new Facility(facility.getId(), facility.getFixedCost());
            newFacility.setOpen(facility.isOpen());
            child.add(newFacility);
        }
        return child;
    }

    private static void mutate(ArrayList<Facility> individual) {
        Random random = new Random();
        for (Facility facility : individual) {
            if (random.nextDouble() < MUTATION_RATE) {
                facility.setOpen(!facility.isOpen());
            }
        }
    }

    private static void applyElitism(ArrayList<ArrayList<Facility>> oldPopulation, ArrayList<Double> fitnessScores, ArrayList<ArrayList<Facility>> newPopulation) {
        int numElites = (int) (POPULATION_SIZE * ELITISM_RATE);
        ArrayList<Integer> eliteIndexes = new ArrayList<>();
        for (int i = 0; i < numElites; i++) {
            int bestIndex = -1;
            double bestFitness = Double.MAX_VALUE;
            for (int j = 0; j < fitnessScores.size(); j++) {
                if (fitnessScores.get(j) < bestFitness && !eliteIndexes.contains(j)) {
                    bestFitness = fitnessScores.get(j);
                    bestIndex = j;
                }
            }
            if (bestIndex != -1) {
                eliteIndexes.add(bestIndex);
                newPopulation.add(new ArrayList<>(oldPopulation.get(bestIndex)));
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
