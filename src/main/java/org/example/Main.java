package org.example;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProblemInstanceLoader loader = new ProblemInstanceLoader();
        Scanner scanner = new Scanner(System.in);

        try {
            ProblemInstance instance = loader.loadInstance("dados/ORLIB/ORLIB-uncap/70/cap71.txt");

            // Perguntar ao usuário qual algoritmo ele deseja aplicar
            System.out.println("Escolha o algoritmo a ser aplicado:");
            System.out.println("1 - Swap Heuristic");
            System.out.println("2 - Switch Heuristic");
            System.out.println("3 - Exact Algorithm (Branch and Bound)");
            System.out.println("4 - Genetic Algorithm");
            int choice = scanner.nextInt();

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
                // Aplicar Algoritmo Genético
                System.out.println("Digite a seed para o Algoritmo Genético:");
                int seed = scanner.nextInt();
                solution = GeneticAlgorithm.applyGeneticAlgorithm(instance, seed);
            } else {
                System.out.println("Escolha inválida. Por favor, selecione 1, 2, 3, 4, 5 ou 6.");
                return;
            }

            // Imprimir a solução
            solution.printSolution();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
