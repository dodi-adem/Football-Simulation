package model;

public class DefenderCalculator implements OverallCalculator {

    @Override
    public int calculate(int pace, int shooting, int passing,
                         int defending, int stamina) {
        return (defending * 45 + stamina * 30 + pace * 15 + passing * 10) / 100;
    }
}