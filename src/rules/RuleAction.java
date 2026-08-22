package rules;

import model.Team;
import simulation.MatchContext;

public interface RuleAction {

    void execute(MatchContext context, Team team);

    String describe();
}