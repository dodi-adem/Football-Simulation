package rules;

import event.MatchEvent;
import model.Team;
import simulation.MatchContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleEngine {

    private final List<MatchRule> activeRules;

    public RuleEngine(List<MatchRule> activeRules) {
        if (activeRules == null) {
            throw new IllegalArgumentException(
                "Active rules list cannot be null.");
        }
        this.activeRules = new ArrayList<>(activeRules);
    }

    public void onEvent(MatchEvent event, MatchContext context, Team team) {
        for (MatchRule rule : activeRules) {
            if (rule.isTriggered(event, context, team)) {
                rule.apply(context, team);
                System.out.println("\n  [RULE] " + rule.getRuleName() + " triggered for " + team.getName() + "!");
            }
        }
    }

    public boolean hasActiveRules() {
        return !activeRules.isEmpty();
    }

    public List<MatchRule> getActiveRules() {
        return Collections.unmodifiableList(activeRules);
    }

    public static RuleEngine empty() {
        return new RuleEngine(new ArrayList<>());
    }

    @Override
    public String toString() {
        if (activeRules.isEmpty()) {
            return "RuleEngine { no active rules }";
        }
        StringBuilder sb = new StringBuilder("RuleEngine {\n");
        for (MatchRule rule : activeRules) {
            sb.append("  >> ").append(rule.getRuleName()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}