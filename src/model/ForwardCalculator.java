package model;

public class ForwardCalculator implements OverallCalculator {

    @Override
    public int calculate(int pace, int shooting, int passing,
                         int defending, int stamina) {
        return (shooting * 45 + pace * 30 + stamina * 25) / 100;
    }
}