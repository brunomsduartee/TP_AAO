package org.example;

import java.util.ArrayList;

public class SwitchHeuristic {

    public static Solution applySwitch(ProblemInstance currentInstance) {
        long startTime = System.currentTimeMillis();
        initializeSolution(currentInstance);
        boolean improvement;
        int iterations = 0;

        do {
            improvement = false;
            iterations++;
            ArrayList<Facility> facilities = currentInstance.getFacilities();
            ArrayList<Integer> openedFacilities = new ArrayList<>();
            ArrayList<Integer> closedFacilities = new ArrayList<>();

            for (int i = 0; i < facilities.size(); i++) {
                if (trySwitch(currentInstance, i)) {
                    improvement = true;
                    if (facilities.get(i).isOpen()) {
                        openedFacilities.add(i);
                    } else {
                        closedFacilities.add(i);
                    }
                }
            }

            double currentCost = calculateTotalCost(currentInstance);
            System.out.println("Iteração " + iterations + ": Custo total = " + currentCost);
            if (!openedFacilities.isEmpty()) {
                System.out.println("Instalações abertas na iteração " + iterations + ": " + openedFacilities);
            }
            if (!closedFacilities.isEmpty()) {
                System.out.println("Instalações fechadas na iteração " + iterations + ": " + closedFacilities);
            }
            if (openedFacilities.isEmpty() && closedFacilities.isEmpty()) {
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

    private static boolean trySwitch(ProblemInstance instance, int indexFacility) {
        Facility facility = instance.getFacilities().get(indexFacility);

        // Calcular o custo antes da troca
        double currentCost = calculateTotalCost(instance);

        // Trocar o estado da instalação
        toggleFacility(facility);

        // Calcular o custo após a troca
        double newCost = calculateTotalCost(instance);

        if (newCost < currentCost) {
            return true; // A troca reduz o custo total, então mantém a troca
        } else {
            // Desfazer a troca se não reduzir o custo
            toggleFacility(facility);
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
