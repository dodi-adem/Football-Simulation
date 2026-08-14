package model;

public class MidfielderCalculator implements OverallCalculator {

    @Override
    public int calculate(int pace, int shooting, int passing,
                         int defending, int stamina) {
        return (passing * 40 + stamina * 30 + defending * 15
                + shooting * 15) / 100;
    }
}