package rules;

import event.MatchEvent;
import exceptions.InvalidRuleException;
import model.Team;
import simulation.MatchContext;

public class DynamicRule implements MatchRule {

    private final String      ruleName;
    private final RuleTrigger trigger;
    private final RuleAction  action;

    public DynamicRule(String ruleName,
                       RuleTrigger trigger,
                       RuleAction action) throws InvalidRuleException {
        if (ruleName == null || ruleName.isBlank()) {
            throw new InvalidRuleException(
                "Rule name cannot be empty.");
        }
        if (trigger == null) {
            throw new InvalidRuleException(
                "Rule trigger cannot be null.");
        }
        if (action == null) {
            throw new InvalidRuleException(
                "Rule action cannot be null.");
        }
        this.ruleName = ruleName;
        this.trigger  = trigger;
        this.action   = action;
    }

    @Override
    public boolean isTriggered(MatchEvent event,
                               MatchContext context,
                               Team team) {
        return trigger.isMet(event, context, team);
    }

    @Override
    public void apply(MatchContext context, Team team) {
        action.execute(context, team);
    }

    @Override
    public String getRuleName() { return ruleName; }

    @Override
    public String toString() {
        return ruleName + "\n"
             + "  Trigger : " + trigger.describe() + "\n"
             + "  Action  : " + action.describe();
    }
}