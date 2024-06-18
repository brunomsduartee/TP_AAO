package org.example;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Solution {
    private ArrayList<Facility> openFacilities;
    private double totalCost;
    private long executionTime;

    public Solution(ArrayList<Facility> openFacilities, double totalCost, long executionTime) {
        this.openFacilities = openFacilities;
        this.totalCost = totalCost;
        this.executionTime = executionTime;
    }

    public ArrayList<Facility> getOpenFacilities() {
        return openFacilities;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void printSolution() {
        System.out.println("Número de instalações abertas: " + openFacilities.size());
        System.out.println("Instalações abertas: " + openFacilities.stream()
                .map(facility -> Integer.toString(facility.getId()))
                .collect(Collectors.joining(", ")));
        System.out.println("Custo total: " + totalCost);
        System.out.println("Tempo de execução: " + executionTime + " ms");
    }
}
