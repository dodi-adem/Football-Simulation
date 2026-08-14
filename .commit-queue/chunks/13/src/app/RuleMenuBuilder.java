package app;

import event.CardType;
import exceptions.InvalidRuleException;
import exceptions.InvalidSimulationConfigException;
import rules.ComebackMomentumRule;
import rules.DynamicRule;
import rules.MatchRule;
import rules.RedCardStrengthPenaltyRule;
import rules.RuleAction;
import rules.RuleEngine;
import rules.RuleTrigger;
import rules.YellowCardAccumulationRule;
import rules.actions.AwardGoalAction;
import rules.actions.BoostAttackAction;
import rules.actions.ReduceStrengthAction;
import rules.triggers.CardReceivedTrigger;
import rules.triggers.LosingByGoalsTrigger;
import rules.triggers.MinuteThresholdTrigger;
import util.SimulationConfig;
import util.SimulationConfigBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RuleMenuBuilder {

    private static final String DIVIDER      = "=".repeat(60);
    private static final String THIN_DIVIDER = "-".repeat(60);

    private final Scanner scanner;

    public RuleMenuBuilder(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
    }

    public RuleEngine build(SimulationConfig config) {
        System.out.println("\n" + DIVIDER);
        System.out.println("           MATCH RULES CONFIGURATION");
        System.out.println(DIVIDER);
        System.out.println("  How would you like to configure rules?");
        System.out.println(DIVIDER);
        System.out.println("  [1] Preset Rules  "
                + "- choose from ready-made rules");
        System.out.println("  [2] Custom Rules  "
                + "- build your own rules from scratch");
        System.out.println("  [3] No Rules      "
                + "- standard match, no special rules");
        System.out.println(DIVIDER);

        int choice = promptInt("  Your choice : ", 1, 3);

        return switch (choice) {
            case 1  -> buildPresetRules(config);
            case 2  -> buildCustomRules(config);
            default -> {
                System.out.println(
                    "\n  >> No rules. Standard match rules apply.");
                yield RuleEngine.empty();
            }
        };
    }

    private RuleEngine buildPresetRules(SimulationConfig config) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  PRESET RULES");
        System.out.println(DIVIDER);
        System.out.println("  Enable or disable each rule.");
        System.out.println("  Enabled rules let you set their parameter.");
        System.out.println(DIVIDER);

        List<MatchRule> activeRules = new ArrayList<>();

        MatchRule yellowRule = promptYellowCardRule(config);
        if (yellowRule != null) activeRules.add(yellowRule);

        MatchRule redCardRule = promptRedCardRule(config);
        if (redCardRule != null) activeRules.add(redCardRule);

        MatchRule comebackRule = promptComebackRule(config);
        if (comebackRule != null) activeRules.add(comebackRule);

        printRuleSummary(activeRules);
        return new RuleEngine(activeRules);
    }

    private MatchRule promptYellowCardRule(SimulationConfig config) {
        System.out.println("\n" + THIN_DIVIDER);
        System.out.println("  RULE 1: Yellow Card Accumulation Goal");
        System.out.println(THIN_DIVIDER);
        System.out.println(
            "  After N yellow cards, the opponent is awarded a goal.");
        System.out.println();

        if (!promptYesNo("  Enable this rule? (Y/N) : ")) {
            System.out.println("  >> Rule disabled.");
            return null;
        }

        int threshold = promptInt(
                "  Yellow card threshold (1-20) : ", 1, 20);

        try {
            SimulationConfig updated = new SimulationConfigBuilder()
                    .yellowCardAccumulationThreshold(threshold)
                    .build();
            System.out.println("  >> Enabled! Threshold: "
                    + threshold + " yellow cards");
            return new YellowCardAccumulationRule(updated);
        } catch (InvalidSimulationConfigException e) {
            System.out.println("  ! " + e.getMessage());
            return null;
        }
    }

    private MatchRule promptRedCardRule(SimulationConfig config) {
        System.out.println("\n" + THIN_DIVIDER);
        System.out.println("  RULE 2: Red Card Strength Penalty");
        System.out.println(THIN_DIVIDER);
        System.out.println(
            "  A red card reduces team attack and defense by X%.");
        System.out.println();

        if (!promptYesNo("  Enable this rule? (Y/N) : ")) {
            System.out.println("  >> Rule disabled.");
            return null;
        }

        int percent = promptInt(
                "  Strength reduction % (5-50) : ", 5, 50);

        try {
            SimulationConfig updated = new SimulationConfigBuilder()
                    .redCardStrengthPenalty(percent / 100.0)
                    .build();
            System.out.println("  >> Enabled! Penalty: "
                    + percent + "% strength reduction");
            return new RedCardStrengthPenaltyRule(updated);
        } catch (InvalidSimulationConfigException e) {
            System.out.println("  ! " + e.getMessage());
            return null;
        }
    }

    private MatchRule promptComebackRule(SimulationConfig config) {
        System.out.println("\n" + THIN_DIVIDER);
        System.out.println("  RULE 3: Comeback Momentum");
        System.out.println(THIN_DIVIDER);
        System.out.println(
            "  A team losing by 2+ goals gets a progressive attack boost.");
        System.out.println();

        if (!promptYesNo("  Enable this rule? (Y/N) : ")) {
            System.out.println("  >> Rule disabled.");
            return null;
        }

        int boostPercent = promptInt(
                "  Attack boost per minute % (1-20) : ", 1, 20);

        try {
            SimulationConfig updated = new SimulationConfigBuilder()
                    .comebackMomentumBoost(boostPercent / 100.0)
                    .build();
            System.out.println("  >> Enabled! Boost: +"
                    + boostPercent + "% per minute when losing by 2+");
            return new ComebackMomentumRule(updated);
        } catch (InvalidSimulationConfigException e) {
            System.out.println("  ! " + e.getMessage());
            return null;
        }
    }

    private RuleEngine buildCustomRules(SimulationConfig config) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  CUSTOM RULE BUILDER");
        System.out.println(DIVIDER);
        System.out.println("  Define your own rules from scratch!");
        System.out.println("  Each rule has a TRIGGER (when) "
                + "and ACTION (what).");
        System.out.println("  Match duration: "
                + config.getMatchDuration() + " minutes");
        System.out.println(DIVIDER);

        List<MatchRule> activeRules = new ArrayList<>();

        System.out.println("\n  How many custom rules? (0-5)");
        int ruleCount = promptInt("  Number of rules : ", 0, 5);

        if (ruleCount == 0) {
            System.out.println(
                "\n  >> No rules. Standard match rules apply.");
            return RuleEngine.empty();
        }

        for (int i = 1; i <= ruleCount; i++) {
            System.out.println("\n" + DIVIDER);
            System.out.println("  RULE " + i + " OF " + ruleCount);
            System.out.println(DIVIDER);

            MatchRule rule = buildOneCustomRule(config);
            activeRules.add(rule);

            System.out.println("\n  >> Rule created!");
            System.out.println("     " + rule);
        }

        printRuleSummary(activeRules);
        return new RuleEngine(activeRules);
    }

    private MatchRule buildOneCustomRule(SimulationConfig config) {
        while (true) {
            try {
                String ruleName = promptString(
                        "\n  Give this rule a name : ");

                System.out.println(
                        "\n  STEP 1: WHEN should this rule fire?");
                RuleTrigger trigger = buildTrigger(config);

                System.out.println(
                        "\n  STEP 2: WHAT should happen?");
                RuleAction action = buildAction();

                return new DynamicRule(ruleName, trigger, action);

            } catch (InvalidRuleException e) {
                System.out.println("  ! Invalid rule: "
                        + e.getMessage());
                System.out.println("  ! Please try again.");
            }
        }
    }

    private RuleTrigger buildTrigger(SimulationConfig config) {
        System.out.println("\n  Available triggers:");
        System.out.println("  [1] Team receives a Yellow Card");
        System.out.println("  [2] Team receives a Red Card");
        System.out.println("  [3] Team is losing by X goals");
        System.out.println("  [4] Match minute reaches X");

        int choice = promptInt("  Your choice : ", 1, 4);

        return switch (choice) {
            case 1 -> {
                System.out.println(
                    "\n  >> Fires when team receives a Yellow Card");
                yield new CardReceivedTrigger(CardType.YELLOW);
            }
            case 2 -> {
                System.out.println(
                    "\n  >> Fires when team receives a Red Card");
                yield new CardReceivedTrigger(CardType.RED);
            }
            case 3 -> {
                int goals = promptInt(
                    "  Losing by how many goals? (1-5) : ", 1, 5);
                System.out.println(
                    "\n  >> Fires when losing by " + goals + "+ goals");
                yield new LosingByGoalsTrigger(goals);
            }
            case 4 -> {
                int maxMinute = config.getMatchDuration();
                int minute    = promptInt(
                    "  From which minute? (1-" + maxMinute + ") : ",
                    1, maxMinute);
                System.out.println(
                    "\n  >> Fires from minute " + minute + " onwards");
                yield new MinuteThresholdTrigger(minute);
            }
            default -> throw new IllegalStateException(
                "Unexpected trigger choice: " + choice);
        };
    }

    private RuleAction buildAction() {
        System.out.println("\n  Available actions:");
        System.out.println("  [1] Award a goal to the opponent");
        System.out.println("  [2] Reduce team strength by X%");
        System.out.println("  [3] Boost team attack by X%");

        int choice = promptInt("  Your choice : ", 1, 3);

        return switch (choice) {
            case 1 -> {
                System.out.println(
                    "\n  >> Action: opponent awarded a goal");
                yield new AwardGoalAction();
            }
            case 2 -> {
                int percent = promptInt(
                    "  Reduce by how much %? (5-50) : ", 5, 50);
                System.out.println(
                    "\n  >> Action: strength reduced by "
                    + percent + "%");
                yield new ReduceStrengthAction(percent / 100.0);
            }
            case 3 -> {
                int percent = promptInt(
                    "  Boost by how much %? (5-50) : ", 5, 50);
                System.out.println(
                    "\n  >> Action: attack boosted by "
                    + percent + "%");
                yield new BoostAttackAction(percent / 100.0);
            }
            default -> throw new IllegalStateException(
                "Unexpected action choice: " + choice);
        };
    }

    private void printRuleSummary(List<MatchRule> activeRules) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  ACTIVE RULES SUMMARY");
        System.out.println(DIVIDER);

        if (activeRules.isEmpty()) {
            System.out.println("  >> No rules enabled.");
        } else {
            for (int i = 0; i < activeRules.size(); i++) {
                System.out.println("  [" + (i + 1) + "] "
                        + activeRules.get(i).toString());
                System.out.println("  " + THIN_DIVIDER);
            }
        }
        System.out.println(DIVIDER);
    }

    private boolean promptYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("  ! Please enter Y or N.");
        }
    }

    private String promptString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  ! Cannot be empty, try again.");
        }
    }

    private int promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf(
                    "  ! Enter a number between %d and %d.%n",
                    min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ! Numbers only.");
            }
        }
    }
}