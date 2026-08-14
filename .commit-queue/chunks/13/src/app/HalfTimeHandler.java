package app;

import event.SubstitutionEvent;
import exceptions.InvalidSubstitutionException;
import model.Player;
import model.Position;
import model.Tactic;
import model.Team;
import util.SimulationConfig;
import simulation.MatchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HalfTimeHandler {

    private static final String DIVIDER      = "=".repeat(60);
    private static final String THIN_DIVIDER = "-".repeat(60);

    private final Scanner          scanner;
    private final SimulationConfig config;

    public HalfTimeHandler(Scanner scanner, SimulationConfig config) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.scanner = scanner;
        this.config  = config;
    }

    public void handle(MatchContext context) {
        printHalfTimeHeader(context);
        printSquadStatus(context, context.getHomeTeam());
        printSquadStatus(context, context.getAwayTeam());

        chooseTactic(context, context.getHomeTeam());
        chooseTactic(context, context.getAwayTeam());

        offerSubstitution(context, context.getHomeTeam());
        offerSubstitution(context, context.getAwayTeam());

        printSecondHalfKickoff(context);
    }

    private void printHalfTimeHeader(MatchContext context) {
        System.out.println("\n\n" + DIVIDER);
        System.out.println("                    HALF TIME");
        System.out.println(DIVIDER);
        System.out.printf("%n  %-25s %d  -  %-2d %s%n%n",
                context.getHomeTeam().getName(),
                context.getHomeGoals(),
                context.getAwayGoals(),
                context.getAwayTeam().getName());
        System.out.println(DIVIDER);
    }

    private void printSquadStatus(MatchContext context, Team team) {
        System.out.println("\n  " + team.getName() + " - Squad Status");
        System.out.println("  " + THIN_DIVIDER);
        System.out.printf("  %-4s  %-20s  %-5s  %-5s  %s%n",
                "POS", "NAME", "STM", "OVR", "STATUS");
        System.out.println("  " + THIN_DIVIDER);

        List<Player> activePlayers = context.getActivePlayers(team);
        List<Player> squad         = team.getSquad();

        System.out.println("  >> STARTING XI");
        for (int i = 0; i < Math.min(11, squad.size()); i++) {
            printPlayerRow(squad.get(i), activePlayers, context);
        }

        if (squad.size() > 11) {
            System.out.println("  " + THIN_DIVIDER);
            System.out.println("  >> BENCH");
            for (int i = 11; i < squad.size(); i++) {
                printPlayerRow(squad.get(i), activePlayers, context);
            }
        }

        System.out.println("  " + THIN_DIVIDER);
    }

    private void printPlayerRow(Player player,
                                List<Player> activePlayers,
                                MatchContext context) {
        String status;
        if (context.isSentOff(player)) {
            status = "xx Sent Off";
        } else if (!activePlayers.contains(player)) {
            status = "-- Subbed Off";
        } else if (player.isFatigued()) {
            status = "!! Fatigued";
        } else {
            status = ">> Active";
        }

        System.out.printf("  %-4s  %-20s  %-5d  %-5d  %s%n",
                player.getPosition().getLabel(),
                player.getName(),
                player.getStamina(),
                player.getOverall(),
                status);
    }

    private void chooseTactic(MatchContext context, Team team) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  " + team.getName()
                + " - Choose 2nd Half Tactic");
        System.out.println(DIVIDER);
        System.out.println("  [1] Attacking  - more goals, higher card risk");
        System.out.println("  [2] Balanced   - default rates (recommended)");
        System.out.println("  [3] Defensive  - fewer goals conceded, lower card risk");
        System.out.println();

        Tactic chosen = switch (promptInt("  Your choice : ", 1, 3)) {
            case 1  -> Tactic.ATTACKING;
            case 2  -> Tactic.BALANCED;
            default -> Tactic.DEFENSIVE;
        };

        context.setTactic(team, chosen);
        System.out.println("  >> Tactic set to: " + chosen.getLabel());
    }

    private void offerSubstitution(MatchContext context, Team team) {
        List<Player> squad = team.getSquad();

        List<Player> canGoOff = new ArrayList<>();
        for (int i = 0; i < Math.min(11, squad.size()); i++) {
            Player p = squad.get(i);
            if (context.getActivePlayers(team).contains(p)
                    && !context.isSentOff(p)) {
                canGoOff.add(p);
            }
        }

        List<Player> canComeOn = new ArrayList<>();
        for (int i = 11; i < squad.size(); i++) {
            Player p = squad.get(i);
            if (!context.isSentOff(p)
                    && p.getPosition() != Position.GOALKEEPER) {
                canComeOn.add(p);
            }
        }

        int subsRemaining = config.getMaxSubstitutions()
                - context.getSubsUsed(team);

        int maxPossibleSubs = Math.min(subsRemaining,
                Math.min(canGoOff.size(), canComeOn.size()));

        System.out.println("\n" + DIVIDER);
        System.out.println("  " + team.getName() + " - Substitutions");
        System.out.println(DIVIDER);

        if (maxPossibleSubs <= 0) {
            System.out.println("  No substitutions possible.");
            System.out.printf("  (Subs used: %d/%d, bench available: %d)%n",
                    context.getSubsUsed(team),
                    config.getMaxSubstitutions(),
                    canComeOn.size());
            return;
        }

        System.out.printf("  Subs used so far : %d/%d%n",
                context.getSubsUsed(team),
                config.getMaxSubstitutions());
        System.out.printf("  You can make up to %d substitution(s).%n",
                maxPossibleSubs);

        int howMany = promptInt(
                "  How many substitutions? (0-" + maxPossibleSubs + ") : ",
                0, maxPossibleSubs);

        if (howMany == 0) {
            System.out.println("  No substitutions made.");
            return;
        }

        for (int subNumber = 1; subNumber <= howMany; subNumber++) {
            System.out.println("\n  " + THIN_DIVIDER);
            System.out.println("  Substitution " + subNumber
                    + " of " + howMany);
            System.out.println("  " + THIN_DIVIDER);

            System.out.println("\n  Players on the pitch:");
            printNumberedList(canGoOff);
            Player playerOut = canGoOff.get(
                    promptInt("  Sub OFF (#) : ", 1, canGoOff.size()) - 1);

            System.out.println("\n  Players on the bench:");
            printNumberedList(canComeOn);
            Player playerIn = canComeOn.get(
                    promptInt("  Sub ON  (#) : ", 1, canComeOn.size()) - 1);

            int halftimeMinute = config.getMatchDuration() / 2;
            context.deactivatePlayer(playerOut);
            context.activatePlayer(playerIn);
            context.recordSubstitution(team);

            try {
                context.addEvent(new SubstitutionEvent(
                        halftimeMinute + 1, team, playerOut, playerIn));
            } catch (InvalidSubstitutionException e) {
                System.out.println("  ! Substitution error: "
                        + e.getMessage());
                return;
            }

            System.out.printf("%n  >> %s -> %s  (%s)%n",
                    playerOut.getName(),
                    playerIn.getName(),
                    team.getName());

            canGoOff.remove(playerOut);
            canComeOn.remove(playerIn);
        }

        System.out.println("\n  >> " + howMany
                + " substitution(s) completed for " + team.getName());
    }

    private void printSecondHalfKickoff(MatchContext context) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  Tactics for 2nd half:");
        System.out.printf("    %-25s - %s%n",
                context.getHomeTeam().getName(),
                context.getTactic(context.getHomeTeam()).getLabel());
        System.out.printf("    %-25s - %s%n",
                context.getAwayTeam().getName(),
                context.getTactic(context.getAwayTeam()).getLabel());
        System.out.println(DIVIDER);
        System.out.println("\n  2nd Half kicks off...");
        System.out.println("  " + THIN_DIVIDER + "\n");
    }

    private void printNumberedList(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.printf("    [%d] %-4s  %-20s  STM: %-3d  OVR: %d%n",
                    i + 1,
                    p.getPosition().getLabel(),
                    p.getName(),
                    p.getStamina(),
                    p.getOverall());
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