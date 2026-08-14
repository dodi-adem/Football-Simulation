package rules;

import event.MatchEvent;
import model.Team;
import simulation.MatchContext;

public interface MatchRule {

    boolean isTriggered(MatchEvent event, MatchContext context, Team team);

    void apply(MatchContext context, Team team);

    String getRuleName();
    default void resetIfNeeded(MatchContext context, Team team) { }
}