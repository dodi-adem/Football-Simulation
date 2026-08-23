package rules.actions;

import model.Team;
import rules.RuleAction;
import simulation.MatchContext;

public class BoostAttackAction implements RuleAction {

    private static final double MAX_BOOST = 0.50;

    private final double boostAmount;

    public BoostAttackAction(double boostAmount) {
        if (boostAmount < 0.0 || boostAmount > 1.0) {
            throw new IllegalArgumentException(
                "Boost amount must be between 0.0 and 1.0.");
        }
        this.boostAmount = boostAmount;
    }

    @Override
    public void execute(MatchContext context, Team team) {
        double currentBoost = context.getAttackBoost(team);
        double newBoost     = Math.min(
                currentBoost + boostAmount, MAX_BOOST);

        context.setAttackBoost(team, newBoost);

        System.out.printf(
            "%n  [ACTION] %s attack boosted by %.0f%%%n" +
            "           Boost: %.2f -> %.2f%n",
            team.getName(),
            boostAmount * 100,
            currentBoost,
            newBoost);
    }

    @Override
    public String describe() {
        return "Boost attack by " + (int)(boostAmount * 100) + "%";
    }
}