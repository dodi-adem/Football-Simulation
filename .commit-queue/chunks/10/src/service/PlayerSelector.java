package service;

import model.Player;
import model.Position;
import util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelector {

    private final RandomNumberGenerator rng;

    public PlayerSelector(RandomNumberGenerator rng) {
        if (rng == null) {
            throw new IllegalArgumentException("RNG cannot be null.");
        }
        this.rng = rng;
    }

    public Player selectScorer(List<Player> activePlayers) {
        List<Player> candidates = new ArrayList<>();
        List<Double> weights    = new ArrayList<>();

        for (Player player : activePlayers) {
            double weight = switch (player.getPosition()) {
                case FORWARD    -> player.getShooting();
                case MIDFIELDER -> player.getShooting() / 3.0;
                case DEFENDER   -> player.getShooting() / 8.0;
                case GOALKEEPER -> player.getShooting() / 20.0;
            };
            candidates.add(player);
            weights.add(weight);
        }

        return weightedPick(candidates, weights);
    }

    public Player selectAssistant(List<Player> activePlayers, Player scorer) {
        if (rng.rollChance(0.35)) return null;

        List<Player> candidates = new ArrayList<>();
        List<Double> weights    = new ArrayList<>();

        for (Player player : activePlayers) {
            if (player == scorer) continue;

            double weight = switch (player.getPosition()) {
                case MIDFIELDER -> player.getPassing();
                case FORWARD    -> player.getPassing() / 2.0;
                case DEFENDER   -> player.getPassing() / 5.0;
                case GOALKEEPER -> 0.0;
            };

            if (weight > 0) {
                candidates.add(player);
                weights.add(weight);
            }
        }

        if (candidates.isEmpty()) return null;
        return weightedPick(candidates, weights);
    }

    public Player selectCardRecipient(List<Player> activePlayers) {
        List<Player> candidates = new ArrayList<>();
        List<Double> weights    = new ArrayList<>();

        for (Player player : activePlayers) {
            double weight = switch (player.getPosition()) {
                case DEFENDER   -> 3.0;
                case MIDFIELDER -> 2.5;
                case FORWARD    -> 1.5;
                case GOALKEEPER -> 0.5;
            };
            if (player.isFatigued()) weight *= 1.5;

            candidates.add(player);
            weights.add(weight);
        }

        return weightedPick(candidates, weights);
    }

    public Player selectFoulingPlayer(List<Player> activePlayers) {
        List<Player> candidates = new ArrayList<>();
        List<Double> weights    = new ArrayList<>();

        for (Player player : activePlayers) {
            double weight = switch (player.getPosition()) {
                case DEFENDER   -> 3.5;
                case MIDFIELDER -> 2.5;
                case FORWARD    -> 1.0;
                case GOALKEEPER -> 0.3;
            };
            if (player.isFatigued()) weight *= 1.4;

            candidates.add(player);
            weights.add(weight);
        }

        return weightedPick(candidates, weights);
    }

    public Player selectPenaltyTaker(List<Player> activePlayers) {
        List<Player> candidates = new ArrayList<>();
        List<Double> weights    = new ArrayList<>();

        for (Player player : activePlayers) {
            double weight = switch (player.getPosition()) {
                case FORWARD    -> player.getShooting() * 2.0;
                case MIDFIELDER -> player.getShooting() * 1.0;
                case DEFENDER   -> player.getShooting() * 0.3;
                case GOALKEEPER -> 0.0;
            };

            if (weight > 0) {
                candidates.add(player);
                weights.add(weight);
            }
        }

        if (candidates.isEmpty()) return weightedPick(
                activePlayers, List.of(1.0, 1.0));
        return weightedPick(candidates, weights);
    }

    public Player selectOffsidePlayer(List<Player> activePlayers) {
        List<Player> candidates = new ArrayList<>();
        List<Double> weights    = new ArrayList<>();

        for (Player player : activePlayers) {
            double weight = switch (player.getPosition()) {
                case FORWARD    -> 4.0;
                case MIDFIELDER -> 1.5;
                case DEFENDER   -> 0.3;
                case GOALKEEPER -> 0.0;
            };

            if (weight > 0) {
                candidates.add(player);
                weights.add(weight);
            }
        }

        if (candidates.isEmpty()) return activePlayers.get(0);
        return weightedPick(candidates, weights);
    }

    private <T> T weightedPick(List<T> items, List<Double> weights) {
        double totalWeight = 0;
        for (double w : weights) totalWeight += w;

        double point      = rng.nextDouble() * totalWeight;
        double cumulative = 0.0;

        for (int i = 0; i < items.size(); i++) {
            cumulative += weights.get(i);
            if (point < cumulative) {
                return items.get(i);
            }
        }

        return items.get(items.size() - 1);
    }
}