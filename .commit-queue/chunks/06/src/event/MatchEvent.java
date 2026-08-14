package event;

import model.Team;

public abstract class MatchEvent {

    private final int  minute;
    private final Team team;

    protected MatchEvent(int minute, Team team) {
        if (minute < 1 || minute > 120) {
            throw new IllegalArgumentException(
                    "Match minute must be between 1 and 120.");
        }
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        this.minute = minute;
        this.team   = team;
    }

    public int  getMinute() { return minute; }
    public Team getTeam()   { return team; }

    public boolean isYellowCard() { return false; }
    public boolean isRedCard() { return false; }
}