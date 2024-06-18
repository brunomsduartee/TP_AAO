package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class ProblemInstanceLoader {
    public ProblemInstance loadInstance(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
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
        return problemInstance;
    }
}
