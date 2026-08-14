package rules.actions;

import model.Team;
import rules.RuleAction;
import simulation.MatchContext;

public class ReduceStrengthAction implements RuleAction {

    private final double reductionPercent;

    public ReduceStrengthAction(double reductionPercent) {
        if (reductionPercent < 0.0 || reductionPercent > 1.0) {
            throw new IllegalArgumentException(
                "Reduction percent must be between 0.0 and 1.0.");
        }
        this.reductionPercent = reductionPercent;
    }

    @Override
    public void execute(MatchContext context, Team team) {
        double currentAttack  = context.getAttackModifier(team);
        double currentDefense = context.getDefenseModifier(team);

        double newAttack  = currentAttack  * (1.0 - reductionPercent);
        double newDefense = currentDefense * (1.0 - reductionPercent);

        context.setAttackModifier(team, newAttack);
        context.setDefenseModifier(team, newDefense);

        System.out.printf(
            "%n  [ACTION] %s strength reduced by %.0f%%%n" +
            "           Attack : %.2f -> %.2f%n" +
            "           Defense: %.2f -> %.2f%n",
            team.getName(),
            reductionPercent * 100,
            currentAttack,  newAttack,
            currentDefense, newDefense);
    }

    @Override
    public String describe() {
        return "Reduce team strength by "
                + (int)(reductionPercent * 100) + "%";
    }
}