package org.example;

public class Facility {
    private int id;
    private double fixedCost;

    public Facility(int id, double fixedCost) {
        this.id = id;
        this.fixedCost = fixedCost;
    }

    public int getId() {
        return id;
    }

    public double getFixedCost() {
        return fixedCost;
    }
}
