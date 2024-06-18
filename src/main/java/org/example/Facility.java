package org.example;

public class Facility {
    private int id;
    private double fixedCost;
    private boolean isOpen;

    public Facility(int id, double fixedCost) {
        this.id = id;
        this.fixedCost = fixedCost;
        this.isOpen = false;
    }

    public int getId() {
        return id;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
