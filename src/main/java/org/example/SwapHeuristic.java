package org.example;

import java.util.ArrayList;

public class SwapHeuristic {

    public static Solution applySwap(ProblemInstance currentInstance) {
        long startTime = System.currentTimeMillis();
        initializeSolution(currentInstance);
        boolean improvement;
        int iterations = 0;

        do {
            improvement = false;
            iterations++;
            ArrayList<Facility> facilities = currentInstance.getFacilities();
            ArrayList<String> swapsMade = new ArrayList<>();

            for (int i = 0; i < facilities.size(); i++) {
                for (int j = i + 1; j < facilities.size(); j++) {
                    if (trySwap(currentInstance, i, j)) {
                        improvement = true;
                        swapsMade.add("Instalação " + i + " e " + j);
                    }
                }
            }

            double currentCost = calculateTotalCost(currentInstance);
            System.out.println("Iteração " + iterations + ": Custo total = " + currentCost);
            if (!swapsMade.isEmpty()) {
                System.out.println("Trocas feitas na iteração " + iterations + ": " + swapsMade);
            } else {
                System.out.println("Nenhuma melhoria encontrada na iteração " + iterations);
            }
        } while (improvement);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        double totalCost = calculateTotalCost(currentInstance);

        ArrayList<Facility> openFacilities = new ArrayList<>();
        for (Facility facility : currentInstance.getFacilities()) {
            if (facility.isOpen()) {
                openFacilities.add(facility);
            }
        }

        return new Solution(openFacilities, totalCost, executionTime);
    }

    private static void initializeSolution(ProblemInstance instance) {
        // Alternar entre abrir e fechar instalações
        for (int i = 0; i < instance.getFacilities().size(); i++) {
            instance.getFacilities().get(i).setOpen(i % 2 == 0);
        }
    }

    private static boolean trySwap(ProblemInstance instance, int indexFacility1, int indexFacility2) {
        Facility facility1 = instance.getFacilities().get(indexFacility1);
        Facility facility2 = instance.getFacilities().get(indexFacility2);

        // Calcular o custo antes da troca
        double currentCost = calculateTotalCost(instance);

        // Trocar o estado das instalações
        toggleFacility(facility1);
        toggleFacility(facility2);

        // Calcular o custo após a troca
        double newCost = calculateTotalCost(instance);

        if (newCost < currentCost) {
            return true; // A troca reduz o custo total, então mantém a troca
        } else {
            // Desfazer a troca se não reduzir o custo
            toggleFacility(facility1);
            toggleFacility(facility2);
            return false;
        }
    }

    private static void toggleFacility(Facility facility) {
        facility.setOpen(!facility.isOpen());
    }

    private static double calculateTotalCost(ProblemInstance instance) {
        double totalCost = 0;
        for (Facility facility : instance.getFacilities()) {
            if (facility.isOpen()) {
                totalCost += facility.getFixedCost();
            }
        }
        for (int i = 0; i < instance.getCustomers().size(); i++) {
            double minCost = Double.MAX_VALUE;
            for (int j = 0; j < instance.getFacilities().size(); j++) {
                if (instance.getFacilities().get(j).isOpen()) {
                    minCost = Math.min(minCost, instance.getAllocationCosts()[i][j]);
                }
            }
            totalCost += minCost;
        }
        return totalCost;
    }
}
