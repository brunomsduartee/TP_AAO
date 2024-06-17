package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GreedySolver {
    private ProblemInstance instance;

    public GreedySolver(ProblemInstance instance) {
        this.instance = instance;
    }

    public double solve() {
        ArrayList<Facility> facilities = instance.getFacilities();
        double[][] costs = instance.getAllocationCosts();
        boolean[] isFacilityOpen = new boolean[facilities.size()];
        double totalCost = 0;

        Set<Integer> uncoveredCustomers = new HashSet<>();
        for (int i = 0; i < instance.getCustomers().size(); i++) {
            uncoveredCustomers.add(i);
        }

        // Iterar sobre cada cliente para decidir a melhor instalação para cada um individualmente
        for (int customer = 0; customer < instance.getCustomers().size(); customer++) {
            double minCost = Double.MAX_VALUE;
            int bestFacility = -1;

            // Avaliar cada instalação para determinar o custo mínimo de atendimento para este cliente
            for (int facility = 0; facility < facilities.size(); facility++) {
                double totalFacilityCost = costs[customer][facility];

                // Incluir o custo fixo apenas se a instalação ainda não estiver aberta
                if (!isFacilityOpen[facility]) {
                    totalFacilityCost += facilities.get(facility).getFixedCost();
                }

                // Se o custo total para este cliente nesta instalação for menor, escolha esta instalação
                if (totalFacilityCost < minCost) {
                    minCost = totalFacilityCost;
                    bestFacility = facility;
                }
            }

            // Atualizar custos e status de instalações com base na escolha feita
            if (!isFacilityOpen[bestFacility]) {
                isFacilityOpen[bestFacility] = true;
                totalCost += facilities.get(bestFacility).getFixedCost();
            }
            totalCost += costs[customer][bestFacility];
            uncoveredCustomers.remove(customer);  // Este cliente agora está coberto
        }

        System.out.println("Greedy Total cost: " + totalCost);
        System.out.println("Opened facilities by Greedy:");
        for (int i = 0; i < isFacilityOpen.length; i++) {
            if (isFacilityOpen[i]) {
                System.out.println("Facility " + i + " is open");
            }
        }

        return totalCost;
    }
}
