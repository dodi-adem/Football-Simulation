package model;

public enum Tactic {

    ATTACKING("Attacking", 1.35, 0.80, 1.20),
    BALANCED ("Balanced",  1.00, 1.00, 1.00),
    DEFENSIVE("Defensive", 0.70, 1.30, 0.85);

    private final String label;
    private final double goalMultiplier;
    private final double defenseMultiplier;
    private final double cardMultiplier;

    Tactic(String label,
           double goalMultiplier,
           double defenseMultiplier,
           double cardMultiplier) {
        this.label             = label;
        this.goalMultiplier    = goalMultiplier;
        this.defenseMultiplier = defenseMultiplier;
        this.cardMultiplier    = cardMultiplier;
    }

    public String getLabel()             { return label; }
    public double getGoalMultiplier()    { return goalMultiplier; }
    public double getDefenseMultiplier() { return defenseMultiplier; }
    public double getCardMultiplier()    { return cardMultiplier; }

    @Override
    public String toString() { return label; }
}