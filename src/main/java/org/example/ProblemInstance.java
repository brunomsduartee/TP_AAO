package org.example;

import java.util.ArrayList;

public class ProblemInstance {
    private ArrayList<Facility> facilities;
    private ArrayList<Customer> customers;
    private double[][] allocationCosts;

    public ProblemInstance(int numFacilities, int numCustomers) {
        facilities = new ArrayList<>();
        customers = new ArrayList<>();
        allocationCosts = new double[numCustomers][numFacilities];
    }

    public void addFacility(Facility facility) {
        facilities.add(facility);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void setAllocationCost(int customerIndex, int facilityIndex, double cost) {
        allocationCosts[customerIndex][facilityIndex] = cost;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public double[][] getAllocationCosts() {
        return allocationCosts;
    }
}
