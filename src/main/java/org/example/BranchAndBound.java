package org.example;

import java.util.ArrayList;
import java.util.List;

public class BranchAndBound {

    private static double bestCost = Double.MAX_VALUE;
    private static List<Facility> bestSolution = new ArrayList<>();
    private static long lastLogTime = 0;

    public static Solution solveExact(ProblemInstance instance) {
        long startTime = System.currentTimeMillis();

        int numFacilities = instance.getFacilities().size();
        boolean[] openFacilities = new boolean[numFacilities];

        System.out.println("Iniciando Branch and Bound...");
        // Inicializa a busca no nível 0
        branchAndBound(instance, openFacilities, 0);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Branch and Bound concluído.");
        return new Solution(new ArrayList<>(bestSolution), bestCost, executionTime);
    }

    private static void branchAndBound(ProblemInstance instance, boolean[] openFacilities, int level) {
        if (level == openFacilities.length) {
            double currentCost = calculateTotalCost(instance, openFacilities);
            if (currentCost < bestCost) {
                bestCost = currentCost;
                bestSolution = getOpenFacilities(instance, openFacilities);
                System.out.println("Nova melhor solução encontrada: Custo = " + bestCost);
                logProgress();
            }
            return;
        }

        // Não abre a instalação atual
        openFacilities[level] = false;
        branchAndBound(instance, openFacilities, level + 1);

        // Abre a instalação atual
        openFacilities[level] = true;
        branchAndBound(instance, openFacilities, level + 1);
    }

    private static double calculateTotalCost(ProblemInstance instance, boolean[] openFacilities) {
        double totalCost = 0;
        int numFacilities = openFacilities.length;
        int numCustomers = instance.getCustomers().size();
        double[][] allocationCosts = instance.getAllocationCosts();

        // Adiciona os custos fixos das instalações abertas
        for (int i = 0; i < numFacilities; i++) {
            if (openFacilities[i]) {
                totalCost += instance.getFacilities().get(i).getFixedCost();
            }
        }

        // Adiciona os menores custos de alocação para cada cliente
        for (int j = 0; j < numCustomers; j++) {
            double minCost = Double.MAX_VALUE;
            for (int i = 0; i < numFacilities; i++) {
                if (openFacilities[i]) {
                    minCost = Math.min(minCost, allocationCosts[j][i]);
                }
            }
            totalCost += minCost;
        }

        return totalCost;
    }

    private static List<Facility> getOpenFacilities(ProblemInstance instance, boolean[] openFacilities) {
        List<Facility> openFacilitiesList = new ArrayList<>();
        for (int i = 0; i < openFacilities.length; i++) {
            if (openFacilities[i]) {
                openFacilitiesList.add(instance.getFacilities().get(i));
            }
        }
        return openFacilitiesList;
    }

    private static void logProgress() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLogTime >= 1000) { // Log progress every second
            System.out.println("Progresso: Custo atual = " + bestCost);
            lastLogTime = currentTime;
        }
    }
}
