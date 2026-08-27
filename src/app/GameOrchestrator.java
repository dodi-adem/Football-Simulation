package app;

import event.MatchEvent;
import exceptions.InvalidTeamException;
import exceptions.InvalidSimulationConfigException;
import model.MatchResult;
import model.Team;
import rules.MatchRule;
import rules.RuleEngine;
import simulation.MatchSimulator;
import util.PresetTeams;
import util.SimulationConfig;
import util.SimulationConfigBuilder;

import java.util.Scanner;

public class GameOrchestrator implements Orchestrator {

    private static final String DIVIDER      = "=".repeat(60);
    private static final String THIN_DIVIDER = "-".repeat(60);

    private final Scanner scanner;

    public GameOrchestrator(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
    }

    @Override
    public void start() {
        printWelcome();

        Team homeTeam = selectTeam("HOME");
        Team awayTeam = selectTeam("AWAY");

        printMatchPreview(homeTeam, awayTeam);

        SimulationConfigBuilder configBuilder = new SimulationConfigBuilder();
        configureSimulation(configBuilder);

        SimulationConfig config = configBuilder.build();

        RuleMenuBuilder ruleMenuBuilder = new RuleMenuBuilder(scanner);
        RuleEngine ruleEngine = ruleMenuBuilder.build(config);

        printConfigSummary(config, ruleEngine);

        System.out.println("\n" + DIVIDER);
        System.out.println("  Ready to simulate!");
        System.out.println("  Press ENTER to start the match...");
        scanner.nextLine();

        MatchSimulator simulator = MatchSimulator.createDefault(
                scanner, config, ruleEngine);
        MatchResult result = simulator.simulate(homeTeam, awayTeam);

        printFinalResult(result);
    }

    private Team selectTeam(String side) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  SELECT " + side + " TEAM");
        System.out.println(DIVIDER);
        System.out.println("  [1] FC Barcelona");
        System.out.println("  [2] Real Madrid");
        System.out.println("  [3] Manchester City");
        System.out.println("  [4] AS Monaco");
        System.out.println("  [5] Build Custom Team");
        System.out.println(DIVIDER);

        while (true) {
            try {
                int choice = promptInt("  Your choice : ", 1, 5);
                return switch (choice) {
                    case 1  -> PresetTeams.buildBarcelona();
                    case 2  -> PresetTeams.buildRealMadrid();
                    case 3  -> PresetTeams.buildManCity();
                    case 4  -> PresetTeams.buildMonaco();
                    default -> new TeamBuilder(scanner).build();
                };
            } catch (InvalidTeamException e) {
                System.out.println("  ! Error building team: " + e.getMessage());
                System.out.println("  ! Please try again.");
            }
        }
    }

    private void configureSimulation(SimulationConfigBuilder configBuilder) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  SIMULATION CONFIGURATION");
        System.out.println(DIVIDER);
        System.out.println("  [1] Default settings (90 min, normal speed)");
        System.out.println("  [2] Custom settings");
        System.out.println(DIVIDER);

        int choice = promptInt("  Your choice : ", 1, 2);
        if (choice == 1) {
            System.out.println("  >> Using default settings.");
            return;
        }

        System.out.println("\n  Match duration:");
        System.out.println("  [1] 45 minutes  (quick)");
        System.out.println("  [2] 90 minutes  (standard)");
        System.out.println("  [3] Custom");

        int durationChoice = promptInt("  Your choice : ", 1, 3);
        try {
            switch (durationChoice) {
                case 1 -> configBuilder.matchDuration(45);
                case 2 -> configBuilder.matchDuration(90);
                case 3 -> {
                    int custom = promptInt(
                        "  Enter duration (10-120 min) : ", 10, 120);
                    configBuilder.matchDuration(custom);
                }
            }
        } catch (InvalidSimulationConfigException e) {
            System.out.println("  ! " + e.getMessage());
            System.out.println("  ! Using default duration of 90 minutes.");
        }

        System.out.println("\n  Simulation speed:");
        System.out.println("  [1] Fast    (250ms per minute)");
        System.out.println("  [2] Normal  (1000ms per minute)");
        System.out.println("  [3] Slow    (2000ms per minute)");

        int speedChoice = promptInt("  Your choice : ", 1, 3);
        try {
            switch (speedChoice) {
                case 1 -> configBuilder.simulationSpeedMs(250);
                case 2 -> configBuilder.simulationSpeedMs(1000);
                case 3 -> configBuilder.simulationSpeedMs(2000);
            }
        } catch (InvalidSimulationConfigException e) {
            System.out.println("  ! " + e.getMessage());
            System.out.println("  ! Using default speed of 1000ms.");
        }

        System.out.println("  >> Custom settings applied.");
    }

    private void printWelcome() {
        System.out.println("\n" + DIVIDER);
        System.out.println("        FOOTBALL MATCH SIMULATOR");
        System.out.println("        Powered by Java OOP + SOLID");
        System.out.println(DIVIDER);
        System.out.println("  Simulate a football match between two teams.");
        System.out.println("  Watch goals, cards and more unfold");
        System.out.println("  minute by minute in real time.");
        System.out.println(DIVIDER);
    }

    private void printMatchPreview(Team homeTeam, Team awayTeam) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  MATCH PREVIEW");
        System.out.println(DIVIDER);
        System.out.printf("  %-25s  vs  %s%n", homeTeam.getName(), awayTeam.getName());
        System.out.println(THIN_DIVIDER);
        System.out.printf("  %-25s  vs  %s%n", "Squad size: " + homeTeam.getSquadSize(), "Squad size: " + awayTeam.getSquadSize());
        System.out.printf("  %-25s  vs  %s%n", "Avg OVR: " + String.format("%.1f", homeTeam.getAverageOverall()), "Avg OVR: " + String.format("%.1f", awayTeam.getAverageOverall()));
        System.out.println(DIVIDER);
    }

    private void printConfigSummary(SimulationConfig config,
                                    RuleEngine ruleEngine) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  CONFIGURATION SUMMARY");
        System.out.println(DIVIDER);
        System.out.printf("  Match duration    : %d minutes%n",
                config.getMatchDuration());
        System.out.printf("  Simulation speed  : %d ms per minute%n",
                config.getSimulationSpeedMs());
        System.out.printf("  Max substitutions : %d per team%n",
                config.getMaxSubstitutions());
        System.out.printf("  Stamina drain     : %d per minute%n",
                config.getStaminaDrainRate());
        System.out.println(THIN_DIVIDER);
        System.out.println("  Active Rules:");
        if (!ruleEngine.hasActiveRules()) {
            System.out.println("  >> No special rules enabled.");
        } else {
            for (MatchRule rule : ruleEngine.getActiveRules()) {
                System.out.println("  >> " + rule.toString());
                System.out.println("  " + THIN_DIVIDER);
            }
        }
        System.out.println(DIVIDER);
    }

    private void printFinalResult(MatchResult result) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                  FULL TIME RESULT");
        System.out.println(DIVIDER);
        System.out.println("  " + result.getScoreLine());
        System.out.println(THIN_DIVIDER);

        if (result.isDraw()) {
            System.out.println("  It's a DRAW!");
        } else {
            System.out.println("  WINNER: "
                    + result.getWinner().getName());
        }

        System.out.println(THIN_DIVIDER);
        System.out.println("  MATCH EVENTS TIMELINE");
        System.out.println(THIN_DIVIDER);

        for (MatchEvent event : result.getEvents()) {
            System.out.println("  " + event.toString());
        }

        System.out.println(DIVIDER);
        System.out.println(
            "  Thanks for playing Football Match Simulator!");
        System.out.println(DIVIDER);
    }

    private int promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf("  ! Enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ! Numbers only.");
            }
        }
    }
}