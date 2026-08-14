package service;

import model.Tactic;
import util.SimulationConfig;

public class ProbabilityService {

    private static final double BASE_GOAL_CHANCE        = 0.018;
    private static final double BASE_YELLOW_CARD_CHANCE = 0.060;
    private static final double BASE_RED_CARD_CHANCE    = 0.008;
    private static final double BASE_OFFSIDE_CHANCE     = 0.025;
    private static final double BASE_PENALTY_CHANCE     = 0.008;

    private final SimulationConfig config;

    public ProbabilityService(SimulationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.config = config;
    }

    public double goalProbability(double attackStrength,
                                  double defenseStrength,
                                  Tactic attackingTactic,
                                  Tactic defendingTactic) {
        double adjustedAttack  = attackStrength
                               * attackingTactic.getGoalMultiplier();
        double adjustedDefense = defenseStrength
                               * defendingTactic.getDefenseMultiplier();

        double ratio = (adjustedDefense == 0)
                ? 2.0
                : adjustedAttack / adjustedDefense;

        return clamp(BASE_GOAL_CHANCE * ratio, 0.005, 0.10);
    }

    public double yellowCardProbability(int minute, Tactic tactic) {
        double halfwayPoint    = config.getMatchDuration() * 0.5;
        double secondHalfBonus = (minute > halfwayPoint) ? 1.2 : 1.0;
        double base = BASE_YELLOW_CARD_CHANCE
                * secondHalfBonus
                * tactic.getCardMultiplier();
        return clamp(base, 0.01, 0.05);
    }

    public double redCardProbability(int minute, Tactic tactic) {
        double lateGamePoint = config.getMatchDuration() * 0.667;
        double lateGameBonus = (minute > lateGamePoint) ? 1.3 : 1.0;
        double base = BASE_RED_CARD_CHANCE
                * lateGameBonus
                * tactic.getCardMultiplier();
        return clamp(base, 0.0, 0.003);
    }

    public double offsideProbability(Tactic tactic) {
        double base = BASE_OFFSIDE_CHANCE * tactic.getGoalMultiplier();
        return clamp(base, 0.005, 0.05);
    }

    public double penaltyProbability(Tactic tactic) {
        double base = BASE_PENALTY_CHANCE * tactic.getGoalMultiplier();
        return clamp(base, 0.001, 0.015);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}