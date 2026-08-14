package simulation;

import app.HalfTimeHandler;
import event.MatchEvent;
import model.MatchResult;
import model.Player;
import model.Team;
import rules.ComebackMomentumRule;
import rules.MatchRule;
import rules.RuleEngine;
import service.PlayerSelector;
import service.ProbabilityService;
import service.TeamStrengthCalculator;
import util.RandomNumberGenerator;
import util.SimulationConfig;

import java.util.List;
import java.util.Scanner;

public class MatchSimulator {

    private final List<EventGenerator>  generators;
    private final RandomNumberGenerator rng;
    private final HalfTimeHandler halfTimeHandler;
    private final RuleEngine ruleEngine;
    private final SimulationConfig config;

    public MatchSimulator(List<EventGenerator> generators, RandomNumberGenerator rng, HalfTimeHandler halfTimeHandler, RuleEngine ruleEngine, SimulationConfig config) {
        if (generators == null) {
            throw new IllegalArgumentException(
                "Generators cannot be null.");
        }
        if (rng == null) {
            throw new IllegalArgumentException("RNG cannot be null.");
        }
        if (halfTimeHandler == null) {
            throw new IllegalArgumentException(
                "HalfTimeHandler cannot be null.");
        }
        if (ruleEngine == null) {
            throw new IllegalArgumentException(
                "RuleEngine cannot be null.");
        }
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.generators      = generators;
        this.rng             = rng;
        this.halfTimeHandler = halfTimeHandler;
        this.ruleEngine      = ruleEngine;
        this.config          = config;
    }

    public static MatchSimulator createDefault(Scanner scanner,
                                               SimulationConfig config,
                                               RuleEngine ruleEngine) {
        RandomNumberGenerator  rng         = new RandomNumberGenerator();
        TeamStrengthCalculator strength    = new TeamStrengthCalculator();
        ProbabilityService     probability = new ProbabilityService(config);
        PlayerSelector         selector    = new PlayerSelector(rng);

        List<EventGenerator> generators = List.of(
                new GoalGenerator(strength, probability, selector, rng),
                new OffsideGenerator(probability, selector, rng),
                new CardGenerator(probability, selector, rng),
                new PenaltyGenerator(probability, selector, rng)
        );

        HalfTimeHandler halfTimeHandler = new HalfTimeHandler(
                scanner, config);

        return new MatchSimulator(generators, rng, halfTimeHandler,
                ruleEngine, config);
    }

    public MatchResult simulate(Team homeTeam, Team awayTeam) {
        MatchContext context = new MatchContext(homeTeam, awayTeam, config);
        int matchDuration  = config.getMatchDuration();
        int halftimeMinute = matchDuration / 2;

        System.out.println("\n  Match starting: " + homeTeam.getName() + "  vs  " + awayTeam.getName());
        System.out.println("  " + "-".repeat(50));

        for (int minute = 1; minute <= matchDuration; minute++) {

            if (minute == halftimeMinute + 1) {
                halfTimeHandler.handle(context);
            }

            resetComebackBoosts(context, homeTeam, awayTeam);

            printMinuteTicker(minute, context, halftimeMinute);

            for (Team team : List.of(homeTeam, awayTeam)) {
                for (EventGenerator generator : generators) {
                    List<MatchEvent> generated = generator.generate(context, team, minute);

                    for (MatchEvent event : generated) {
                        context.addEvent(event);
                        System.out.println("\n  >>> " + event.toString());
                        ruleEngine.onEvent(event, context, team);
                    }
                }
            }

            drainStamina(homeTeam, context);
            drainStamina(awayTeam, context);

            pauseOneSecond();
        }

        System.out.println("\n\n  " + "-".repeat(50));
        System.out.println("  Full time!");

        return buildResult(context);
    }

    private void resetComebackBoosts(MatchContext context, Team homeTeam, Team awayTeam) {
        for (MatchRule rule : ruleEngine.getActiveRules()) {
            rule.resetIfNeeded(context, homeTeam);
            rule.resetIfNeeded(context, awayTeam);
        }
    }

    private void printMinuteTicker(int minute, MatchContext context,
                                   int halftimeMinute) {
        String half = (minute <= halftimeMinute) ? "1st Half" : "2nd Half";
        System.out.printf("\r  %s  |  %d'  |  %s  ",
                half, minute, context.getScoreLine());
        System.out.flush();
    }

    private void pauseOneSecond() {
        try {
            Thread.sleep(config.getSimulationSpeedMs());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void drainStamina(Team team, MatchContext context) {
        for (Player player : context.getActivePlayers(team)) {
            player.drainStamina(config.getStaminaDrainRate());
        }
    }

    private MatchResult buildResult(MatchContext context) {
        return new MatchResult(
                context.getHomeTeam(),
                context.getAwayTeam(),
                context.getHomeGoals(),
                context.getAwayGoals(),
                context.getEvents()
        );
    }
}