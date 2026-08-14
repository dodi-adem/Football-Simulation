package event;

import model.Player;
import model.Team;

public class GoalEvent extends MatchEvent {

    private final Player scorer;
    private final Player assistant;


    public GoalEvent(int minute, Team team, Player scorer) {
        this(minute, team, scorer, null);
    }

    public GoalEvent(int minute, Team team, Player scorer, Player assistant) {
        super(minute, team);
        if (scorer == null) {
            throw new IllegalArgumentException("Scorer cannot be null.");
        }
        this.scorer    = scorer;
        this.assistant = assistant;
    }

    public Player  getScorer()    { return scorer; }
    public Player  getAssistant() { return assistant; }
    public boolean hasAssist()    { return assistant != null; }

    @Override
    public String toString() {
        if (hasAssist()) {
            return String.format("[%d'] GOAL! %s - %s (assist: %s)",
                    getMinute(), getTeam().getName(),
                    scorer.getName(), assistant.getName());
        }
        return String.format("[%d'] GOAL! %s - %s",
                getMinute(), getTeam().getName(), scorer.getName());
    }
}