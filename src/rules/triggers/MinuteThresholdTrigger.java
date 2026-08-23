package rules.triggers;

import event.MatchEvent;
import model.Team;
import rules.RuleTrigger;
import simulation.MatchContext;

public class MinuteThresholdTrigger implements RuleTrigger {

    private final int minuteThreshold;

    public MinuteThresholdTrigger(int minuteThreshold) {
        if (minuteThreshold < 1) {
            throw new IllegalArgumentException(
                "Minute threshold must be at least 1.");
        }
        this.minuteThreshold = minuteThreshold;
    }

    @Override
    public boolean isMet(MatchEvent event, MatchContext context, Team team) {
        return event.getMinute() >= minuteThreshold;
    }

    @Override
    public String describe() {
        return "When match minute >= " + minuteThreshold;
    }
}