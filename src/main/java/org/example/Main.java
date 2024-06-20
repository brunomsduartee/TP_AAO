package org.example;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProblemInstanceLoader loader = new ProblemInstanceLoader();

        try {
            ProblemInstance instance = loader.loadInstance("dados/M/Kcapmq5.txt");

            // Perguntar ao usuário qual algoritmo ele deseja aplicar
            System.out.println("Escolha o algoritmo a ser aplicado:");
            System.out.println("1 - Swap Heuristic");
            System.out.println("2 - Switch Heuristic");
            System.out.println("3 - Exact Algorithm (Branch and Bound)");
            System.out.println("4 - Variable Neighborhood Search");
            System.out.println("5 - Ant Colony Optimization");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.close();

            Solution solution = null;

            if (choice == 1) {
                // Aplicar heurística de Swap
                solution = SwapHeuristic.applySwap(instance);
            } else if (choice == 2) {
                // Aplicar heurística de Switch
                solution = SwitchHeuristic.applySwitch(instance);
            } else if (choice == 3) {
                // Aplicar algoritmo exato (Branch and Bound)
                solution = BranchAndBound.solveExact(instance);
            } else if (choice == 4) {
                // Aplicar Variable Neighborhood Search
                solution = VariableNeighborhoodSearch.solve(instance);
            } else if (choice == 5) {
                // Aplicar Ant Colony Optimization
                solution = AntColonyOptimization.solve(instance);
            } else {
                System.out.println("Escolha inválida. Por favor, selecione 1, 2, 3, 4 ou 5.");
                return;
            }

            // Imprimir a solução
            solution.printSolution();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
