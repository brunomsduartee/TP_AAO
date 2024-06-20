package org.example;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProblemInstanceLoader loader = new ProblemInstanceLoader();

        try {
            ProblemInstance instance = loader.loadInstance("dados/ORLIB/ORLIB-uncap/70/cap74.txt");

            System.out.println("Escolha o algoritmo a ser aplicado:");
            System.out.println("1 - Swap Heuristic");
            System.out.println("2 - Switch Heuristic");
            System.out.println("3 - Exact Algorithm (Branch and Bound)");
            System.out.println("4 - Variable Neighborhood Search");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.close();

            Solution solution = null;

            if (choice == 1) {

                solution = SwapHeuristic.applySwap(instance);
            } else if (choice == 2) {

                solution = SwitchHeuristic.applySwitch(instance);
            } else if (choice == 3) {

                solution = BranchAndBound.solveExact(instance);
            } else if (choice == 4) {

                solution = VariableNeighborhoodSearch.solve(instance);
            } else {
                System.out.println("Escolha inválida. Por favor, selecione 1, 2, 3 ou 4.");
                return;
            }


            solution.printSolution();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
    }
}
