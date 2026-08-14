package util;

public class SimulationConfig {

    private final int    matchDuration;
    private final int    simulationSpeedMs;
    private final int    staminaDrainRate;
    private final int    fatigueThreshold;
    private final int    maxSubstitutions;
    private final int    yellowCardAccumulationThreshold;
    private final double redCardStrengthPenalty;
    private final double comebackMomentumBoost;

    SimulationConfig(SimulationConfigBuilder builder) {
        this.matchDuration                   = builder.getMatchDuration();
        this.simulationSpeedMs               = builder.getSimulationSpeedMs();
        this.staminaDrainRate                = builder.getStaminaDrainRate();
        this.fatigueThreshold                = builder.getFatigueThreshold();
        this.maxSubstitutions                = builder.getMaxSubstitutions();
        this.yellowCardAccumulationThreshold = builder.getYellowCardAccumulationThreshold();
        this.redCardStrengthPenalty          = builder.getRedCardStrengthPenalty();
        this.comebackMomentumBoost           = builder.getComebackMomentumBoost();
    }

    public int    getMatchDuration()                   { return matchDuration; }
    public int    getSimulationSpeedMs()               { return simulationSpeedMs; }
    public int    getStaminaDrainRate()                { return staminaDrainRate; }
    public int    getFatigueThreshold()                { return fatigueThreshold; }
    public int    getMaxSubstitutions()                { return maxSubstitutions; }
    public int    getYellowCardAccumulationThreshold() { return yellowCardAccumulationThreshold; }
    public double getRedCardStrengthPenalty()          { return redCardStrengthPenalty; }
    public double getComebackMomentumBoost()           { return comebackMomentumBoost; }

    public static SimulationConfig defaultConfig() {
        return new SimulationConfigBuilder().build();
    }

    @Override
    public String toString() {
        return String.format(
            "SimulationConfig {%n" +
            "  matchDuration                  = %d min%n" +
            "  simulationSpeedMs              = %d ms%n" +
            "  staminaDrainRate               = %d per min%n" +
            "  fatigueThreshold               = %d stamina%n" +
            "  maxSubstitutions               = %d%n" +
            "  yellowCardAccumulationThreshold = %d cards%n" +
            "  redCardStrengthPenalty         = %.0f%%%n" +
            "  comebackMomentumBoost          = %.2f%n" +
            "}",
            matchDuration,
            simulationSpeedMs,
            staminaDrainRate,
            fatigueThreshold,
            maxSubstitutions,
            yellowCardAccumulationThreshold,
            redCardStrengthPenalty * 100,
            comebackMomentumBoost);
    }
}