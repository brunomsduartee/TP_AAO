package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class UFLPSolver {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\bruno\\IdeaProjects\\TP_AAO\\src\\main\\java\\org\\example\\cap71.txt");
            Scanner scanner = new Scanner(file);
            scanner.useLocale(Locale.US);

            int numFacilities = scanner.nextInt();
            int numCustomers = scanner.nextInt();
            ProblemInstance problemInstance = new ProblemInstance(numFacilities, numCustomers);

            for (int i = 0; i < numFacilities; i++) {
                double capacity = scanner.nextDouble();  // Ignored in solver
                double fixedCost = scanner.nextDouble();
                problemInstance.addFacility(new Facility(i, fixedCost));
            }

            for (int i = 0; i < numCustomers; i++) {
                double demand = scanner.nextDouble();  // Ignored in solver
                problemInstance.addCustomer(new Customer(i));
                for (int j = 0; j < numFacilities; j++) {
                    double cost = scanner.nextDouble();
                    problemInstance.setAllocationCost(i, j, cost);
                }
            }

            scanner.close();

            GreedySolver greedySolver = new GreedySolver(problemInstance);
            double initialCost = greedySolver.solve();
            System.out.println("Initial Greedy Solution Cost: " + initialCost);

            // Apply Local Search Solver to improve the solution
            LocalSearchSolver localSearchSolver = new LocalSearchSolver(problemInstance);
            double improvedCost = localSearchSolver.solve();
            System.out.println("Improved Solution Cost after Local Search: " + improvedCost);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error processing file: " + e.getMessage());
        }
    }
}
