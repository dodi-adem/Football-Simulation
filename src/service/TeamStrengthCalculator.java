package service;

import model.Player;
import model.Position;
import model.Team;

import java.util.List;

public class TeamStrengthCalculator {

    public double calculateAttackStrength(Team team) {
        List<Player> forwards    = team.getPlayersByPosition(
                Position.FORWARD);
        List<Player> midfielders = team.getPlayersByPosition(
                Position.MIDFIELDER);

        if (forwards.isEmpty()) return team.getAverageOverall() * 0.5;

        double forwardScore      = averageShooting(forwards) * 0.6
                                 + averagePace(forwards)     * 0.2;
        double midfielderSupport = averagePassing(midfielders) * 0.2;

        return forwardScore + midfielderSupport;
    }

    public double calculateDefenseStrength(Team team) {
        List<Player> defenders   = team.getPlayersByPosition(
                Position.DEFENDER);
        List<Player> goalkeepers = team.getPlayersByPosition(
                Position.GOALKEEPER);

        if (defenders.isEmpty()) return team.getAverageOverall() * 0.5;

        double defenderScore   = averageDefending(defenders) * 0.6
                               + averageStamina(defenders)   * 0.2;
        double goalkeeperBonus = averageDefending(goalkeepers) * 0.2;

        return defenderScore + goalkeeperBonus;
    }

    private double averageShooting(List<Player> players) {
        if (players.isEmpty()) return 0.0;
        int total = 0;
        for (Player player : players) {
            total += player.getShooting();
        }
        return (double) total / players.size();
    }

    private double averagePace(List<Player> players) {
        if (players.isEmpty()) return 0.0;
        int total = 0;
        for (Player player : players) {
            total += player.getPace();
        }
        return (double) total / players.size();
    }

    private double averagePassing(List<Player> players) {
        if (players.isEmpty()) return 0.0;
        int total = 0;
        for (Player player : players) {
            total += player.getPassing();
        }
        return (double) total / players.size();
    }

    private double averageDefending(List<Player> players) {
        if (players.isEmpty()) return 0.0;
        int total = 0;
        for (Player player : players) {
            total += player.getDefending();
        }
        return (double) total / players.size();
    }

    private double averageStamina(List<Player> players) {
        if (players.isEmpty()) return 0.0;
        int total = 0;
        for (Player player : players) {
            total += player.getStamina();
        }
        return (double) total / players.size();
    }
}