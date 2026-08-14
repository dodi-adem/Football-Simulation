package rules;

import event.MatchEvent;
import model.Team;
import simulation.MatchContext;

public interface RuleTrigger {

    boolean isMet(MatchEvent event, MatchContext context, Team team);

    String describe();
}