package model;

import exceptions.InvalidTeamException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {

    private static final int MAX_SQUAD_SIZE = 16;

    private final String       name;
    private final List<Player> squad;

    public Team(String name) throws InvalidTeamException {
        if (name == null || name.isBlank()) {
            throw new InvalidTeamException(
                "Team name cannot be empty.");
        }
        this.name  = name;
        this.squad = new ArrayList<>();
    }

    public void addPlayer(Player player) throws InvalidTeamException {
        if (player == null) {
            throw new InvalidTeamException(
                "Cannot add a null player.");
        }
        if (squad.contains(player)) {
            throw new InvalidTeamException(
                "Player already in squad: " + player.getName());
        }
        if (isNumberTaken(player.getNumber())) {
            throw new InvalidTeamException(
                "Jersey number " + player.getNumber()
                + " is already taken.");
        }
        if (squad.size() >= MAX_SQUAD_SIZE) {
            throw new InvalidTeamException(
                "Squad is full. Maximum size is "
                + MAX_SQUAD_SIZE + ".");
        }
        squad.add(player);
    }

    public List<Player> getSquad() {
        return Collections.unmodifiableList(squad);
    }

    public List<Player> getPlayersByPosition(Position position) {
        List<Player> result = new ArrayList<>();
        for (Player player : squad) {
            if (player.getPosition() == position) {
                result.add(player);
            }
        }
        return result;
    }

    public int getSquadSize() {
        return squad.size();
    }

    public double getAverageOverall() {
        if (squad.isEmpty()) return 0.0;
        int total = 0;
        for (Player player : squad) {
            total += player.getOverall();
        }
        return (double) total / squad.size();
    }

    public String getName() { return name; }

    private boolean isNumberTaken(int number) {
        for (Player p : squad) {
            if (p.getNumber() == number) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Team{%s | players=%d | avgOVR=%.1f}",
                name, squad.size(), getAverageOverall());
    }
}