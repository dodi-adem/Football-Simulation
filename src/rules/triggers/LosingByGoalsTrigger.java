package rules.triggers;

import event.MatchEvent;
import model.Team;
import rules.RuleTrigger;
import simulation.MatchContext;

public class LosingByGoalsTrigger implements RuleTrigger {

    private final int goalDifference;

    public LosingByGoalsTrigger(int goalDifference) {
        if (goalDifference < 1) {
            throw new IllegalArgumentException(
                "Goal difference must be at least 1.");
        }
        this.goalDifference = goalDifference;
    }

    @Override
    public boolean isMet(MatchEvent event, MatchContext context, Team team) {
        return context.getGoalDifference(team) <= -goalDifference;
    }

    @Override
    public String describe() {
        return "When team is losing by " + goalDifference + "+ goals";
    }
}