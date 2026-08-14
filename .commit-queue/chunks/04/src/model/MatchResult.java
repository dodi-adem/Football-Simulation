package model;

import event.MatchEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchResult {

    private final Team homeTeam;
    private final Team awayTeam;
    private final int  homeGoals;
    private final int  awayGoals;
    private final List<MatchEvent> events;

    public MatchResult(Team homeTeam, Team awayTeam,
                       int homeGoals, int awayGoals,
                       List<MatchEvent> events) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Teams cannot be null.");
        }
        if (homeGoals < 0 || awayGoals < 0) {
            throw new IllegalArgumentException(
                "Goal counts cannot be negative.");
        }
        if (events == null) {
            throw new IllegalArgumentException(
                "Events list cannot be null.");
        }
        this.homeTeam  = homeTeam;
        this.awayTeam  = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.events    = new ArrayList<>(events);
    }

    public Team getWinner() {
        if (homeGoals > awayGoals) return homeTeam;
        if (awayGoals > homeGoals) return awayTeam;
        return null;
    }

    public boolean isDraw() {
        return homeGoals == awayGoals;
    }

    public String getScoreLine() {
        return String.format("%s  %d - %d  %s",
                homeTeam.getName(), homeGoals,
                awayGoals, awayTeam.getName());
    }

    public Team getHomeTeam()  { return homeTeam; }
    public Team getAwayTeam()  { return awayTeam; }
    public int  getHomeGoals() { return homeGoals; }
    public int getAwayGoals() { return awayGoals; }

    public List<MatchEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public String toString() {
        String outcome = isDraw()
                ? "DRAW"
                : "WINNER: " + getWinner().getName();
        return getScoreLine() + "  [" + outcome + "]";
    }
}