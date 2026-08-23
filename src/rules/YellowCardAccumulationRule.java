package rules;

import event.MatchEvent;
import model.Team;
import simulation.MatchContext;
import util.SimulationConfig;

public class YellowCardAccumulationRule implements MatchRule {

    private final int threshold;
    private int homeRuleFiredCount = 0;
    private int awayRuleFiredCount = 0;

    public YellowCardAccumulationRule(SimulationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.threshold = config.getYellowCardAccumulationThreshold();
    }

    @Override
    public boolean isTriggered(MatchEvent event, MatchContext context, Team team) {
        if (!event.isYellowCard()) return false;   // ← no instanceof needed

        int yellowCards = context.getYellowCards(team);
        int firedCount  = getFiredCount(context, team);
        int nextTrigger = yellowCards / threshold;

        return nextTrigger > firedCount;
    }

    @Override
    public void apply(MatchContext context, Team team) {
        if (team == context.getHomeTeam()) homeRuleFiredCount++;
        else                               awayRuleFiredCount++;

        Team opponent = (team == context.getHomeTeam())
                ? context.getAwayTeam()
                : context.getHomeTeam();

        if (opponent == context.getHomeTeam()) context.addHomeGoal();
        else                                   context.addAwayGoal();

        System.out.println("\n  [RULE] " + getRuleName()
                + " >> " + opponent.getName()
                + " awarded a goal! (" + team.getName()
                + " reached " + context.getYellowCards(team)
                + " yellow cards)");
    }

    @Override
    public String getRuleName() {
        return "Yellow Card Accumulation Goal (threshold: " + threshold + ")";
    }

    private int getFiredCount(MatchContext context, Team team) {
        if (team == context.getHomeTeam()) return homeRuleFiredCount;
        return awayRuleFiredCount;
    }
}