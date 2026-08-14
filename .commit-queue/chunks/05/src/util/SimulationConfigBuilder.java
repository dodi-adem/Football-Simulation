package util;

import exceptions.InvalidSimulationConfigException;

public class SimulationConfigBuilder {

    private int    matchDuration                   = 90;
    private int    simulationSpeedMs               = 1000;
    private int    staminaDrainRate                = 1;
    private int    fatigueThreshold                = 30;
    private int    maxSubstitutions                = 3;
    private int    yellowCardAccumulationThreshold = 5;
    private double redCardStrengthPenalty          = 0.20;
    private double comebackMomentumBoost           = 0.05;

    public SimulationConfigBuilder matchDuration(int value)
            throws InvalidSimulationConfigException {
        if (value < 10 || value > 120) {
            throw new InvalidSimulationConfigException(
                "Match duration must be between 10 and 120 minutes.");
        }
        this.matchDuration = value;
        return this;
    }

    public SimulationConfigBuilder simulationSpeedMs(int value)
            throws InvalidSimulationConfigException {
        if (value < 0) {
            throw new InvalidSimulationConfigException(
                "Simulation speed cannot be negative.");
        }
        this.simulationSpeedMs = value;
        return this;
    }

    public SimulationConfigBuilder staminaDrainRate(int value)
            throws InvalidSimulationConfigException {
        if (value < 0 || value > 10) {
            throw new InvalidSimulationConfigException(
                "Stamina drain rate must be between 0 and 10.");
        }
        this.staminaDrainRate = value;
        return this;
    }

    public SimulationConfigBuilder fatigueThreshold(int value)
            throws InvalidSimulationConfigException {
        if (value < 0 || value > 100) {
            throw new InvalidSimulationConfigException(
                "Fatigue threshold must be between 0 and 100.");
        }
        this.fatigueThreshold = value;
        return this;
    }

    public SimulationConfigBuilder maxSubstitutions(int value)
            throws InvalidSimulationConfigException {
        if (value < 0 || value > 5) {
            throw new InvalidSimulationConfigException(
                "Max substitutions must be between 0 and 5.");
        }
        this.maxSubstitutions = value;
        return this;
    }

    public SimulationConfigBuilder yellowCardAccumulationThreshold(int value)
            throws InvalidSimulationConfigException {
        if (value < 1) {
            throw new InvalidSimulationConfigException(
                "Yellow card threshold must be at least 1.");
        }
        this.yellowCardAccumulationThreshold = value;
        return this;
    }

    public SimulationConfigBuilder redCardStrengthPenalty(double value)
            throws InvalidSimulationConfigException {
        if (value < 0.0 || value > 1.0) {
            throw new InvalidSimulationConfigException(
                "Red card penalty must be between 0.0 and 1.0.");
        }
        this.redCardStrengthPenalty = value;
        return this;
    }

    public SimulationConfigBuilder comebackMomentumBoost(double value)
            throws InvalidSimulationConfigException {
        if (value < 0.0 || value > 1.0) {
            throw new InvalidSimulationConfigException(
                "Comeback boost must be between 0.0 and 1.0.");
        }
        this.comebackMomentumBoost = value;
        return this;
    }

    public int    getMatchDuration()                   { return matchDuration; }
    public int    getSimulationSpeedMs()               { return simulationSpeedMs; }
    public int    getStaminaDrainRate()                { return staminaDrainRate; }
    public int    getFatigueThreshold()                { return fatigueThreshold; }
    public int    getMaxSubstitutions()                { return maxSubstitutions; }
    public int    getYellowCardAccumulationThreshold() { return yellowCardAccumulationThreshold; }
    public double getRedCardStrengthPenalty()          { return redCardStrengthPenalty; }
    public double getComebackMomentumBoost()           { return comebackMomentumBoost; }

    public SimulationConfig build() {
        return new SimulationConfig(this);
    }
}