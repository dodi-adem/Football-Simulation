package rules.actions;

import model.Team;
import rules.RuleAction;
import simulation.MatchContext;

public class AwardGoalAction implements RuleAction {

    @Override
    public void execute(MatchContext context, Team team) {
        Team opponent = (team == context.getHomeTeam())
                ? context.getAwayTeam()
                : context.getHomeTeam();

        if (opponent == context.getHomeTeam()) context.addHomeGoal();
        else                                   context.addAwayGoal();

        System.out.printf("%n  [ACTION] Goal awarded to %s!%n",
                opponent.getName());
    }

    @Override
    public String describe() {
        return "Award a goal to the opponent";
    }
}