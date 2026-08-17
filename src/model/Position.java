package model;

public enum Position {

    GOALKEEPER("GK",  new GoalkeeperCalculator()),
    DEFENDER  ("DEF", new DefenderCalculator()),
    MIDFIELDER("MID", new MidfielderCalculator()),
    FORWARD   ("FWD", new ForwardCalculator());

    private final String          label ;
    private final OverallCalculator calculator;

    Position(String label, OverallCalculator calculator) {
        this.label      = label;
        this.calculator = calculator;
    }

    public String getLabel() {
        return label;
    }

    public int calculateOverall(int pace, int shooting, int passing,
                                int defending, int stamina) {
        return calculator.calculate(
                pace, shooting, passing, defending, stamina);
    }
}