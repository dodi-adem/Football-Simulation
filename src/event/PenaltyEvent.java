package event;

import model.Player;
import model.Team;

public class PenaltyEvent extends MatchEvent {

    private final Player         taker;
    private final PenaltyOutcome outcome;

    public PenaltyEvent(int minute, Team team,
                        Player taker, PenaltyOutcome outcome) {
        super(minute, team);
        if (taker == null) {
            throw new IllegalArgumentException(
                "Penalty taker cannot be null.");
        }
        if (outcome == null) {
            throw new IllegalArgumentException("Outcome cannot be null.");
        }
        this.taker   = taker;
        this.outcome = outcome;
    }

    public Player         getTaker()   { return taker; }
    public PenaltyOutcome getOutcome() { return outcome; }
    public boolean        isScored()   { return outcome == PenaltyOutcome.SCORED; }
    public boolean        isMissed()   { return outcome == PenaltyOutcome.MISSED; }

    @Override
    public String toString() {
        if (isScored()) {
            return String.format(
                "[%d'] PENALTY SCORED! %s - %s converts from the spot",
                    getMinute(), getTeam().getName(), taker.getName());
        }
        return String.format(
            "[%d'] PENALTY MISSED! %s - %s fails to convert",
                getMinute(), getTeam().getName(), taker.getName());
    }
}