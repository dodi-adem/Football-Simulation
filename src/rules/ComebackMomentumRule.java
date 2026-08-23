package rules;

import event.MatchEvent;
import model.Team;
import simulation.MatchContext;
import util.SimulationConfig;

public class ComebackMomentumRule implements MatchRule {

    private static final double MAX_BOOST        = 0.50;
    private static final int    LOSING_THRESHOLD = -2;

    private final double momentumBoostPerMinute;

    public ComebackMomentumRule(SimulationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.momentumBoostPerMinute = config.getComebackMomentumBoost();
    }

    @Override
    public boolean isTriggered(MatchEvent event, MatchContext context, Team team) {
        return context.getGoalDifference(team) <= LOSING_THRESHOLD;
    }

    @Override
    public void apply(MatchContext context, Team team) {
        double currentBoost = context.getAttackBoost(team);
        double newBoost     = Math.min(currentBoost + momentumBoostPerMinute, MAX_BOOST);

        context.setAttackBoost(team, newBoost);

        if (newBoost > currentBoost) {
            System.out.printf(
                    "%n  [RULE] %s >> %s comeback momentum building!%n" +
                            "         Attack boost: %.2f -> %.2f%n",
                    getRuleName(), team.getName(), currentBoost, newBoost);
        }
    }

    @Override
    public void resetIfNeeded(MatchContext context, Team team) {
        if (context.getGoalDifference(team) > LOSING_THRESHOLD) {
            if (context.getAttackBoost(team) > 0.0) {
                context.setAttackBoost(team, 0.0);
                System.out.printf(
                        "%n  [RULE] %s >> %s have equalised! Momentum boost reset.%n",
                        getRuleName(), team.getName());
            }
        }
    }

    @Override
    public String getRuleName() {
        return "Comeback Momentum (+" + momentumBoostPerMinute
                + " attack boost per minute when losing by 2+)";
    }
}