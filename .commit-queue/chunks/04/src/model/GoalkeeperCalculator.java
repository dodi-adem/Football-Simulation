package model;

public class GoalkeeperCalculator implements OverallCalculator {

    @Override
    public int calculate(int pace, int shooting, int passing,
                         int defending, int stamina) {
        return (defending * 50 + stamina * 30 + passing * 20) / 100;
    }
}