package rules;

import event.MatchEvent;
import model.Team;
import simulation.MatchContext;
import util.SimulationConfig;

public class RedCardStrengthPenaltyRule implements MatchRule {

    private final double penaltyPercentage;

    public RedCardStrengthPenaltyRule(SimulationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.penaltyPercentage = config.getRedCardStrengthPenalty();
    }

    @Override
    public boolean isTriggered(MatchEvent event, MatchContext context, Team team) {
        return event.isRedCard();   // ← no instanceof needed
    }

    @Override
    public void apply(MatchContext context, Team team) {
        double currentAttack  = context.getAttackModifier(team);
        double currentDefense = context.getDefenseModifier(team);

        double newAttack  = currentAttack  * (1.0 - penaltyPercentage);
        double newDefense = currentDefense * (1.0 - penaltyPercentage);

        context.setAttackModifier(team, newAttack);
        context.setDefenseModifier(team, newDefense);

        System.out.printf(
                "%n  [RULE] %s >> %s strength reduced by %.0f%%%n" +
                        "         Attack modifier : %.2f -> %.2f%n" +
                        "         Defense modifier: %.2f -> %.2f%n",
                getRuleName(),
                team.getName(),
                penaltyPercentage * 100,
                currentAttack,  newAttack,
                currentDefense, newDefense);
    }

    @Override
    public String getRuleName() {
        return "Red Card Strength Penalty ("
                + (int)(penaltyPercentage * 100) + "% reduction)";
    }
}