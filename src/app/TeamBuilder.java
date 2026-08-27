package app;

import exceptions.InvalidPlayerException;
import exceptions.InvalidTeamException;
import model.Player;
import model.Position;
import model.Team;

import java.util.Scanner;

public class TeamBuilder {

    private static final String DIVIDER      = "=".repeat(60);
    private static final String THIN_DIVIDER = "-".repeat(50);

    private final Scanner scanner;

    public TeamBuilder(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
    }

    public Team build() throws InvalidTeamException {
        System.out.println("\n" + DIVIDER);
        System.out.println("              CUSTOM TEAM BUILDER");
        System.out.println(DIVIDER);

        String teamName = promptTeamName();
        Team   team     = new Team(teamName);

        int squadSize = promptSquadSize();

        System.out.println("\n  Building squad for " + teamName + "...");
        System.out.println("  " + THIN_DIVIDER);
        System.out.println("  Attributes are rated 1-100.");
        System.out.println("  Tip: add at least 1 GK, 2 DEF, 2 MID, 2 FWD");
        System.out.println("  " + THIN_DIVIDER);

        for (int i = 1; i <= squadSize; i++) {
            System.out.println("\n  --- Player " + i
                    + " of " + squadSize + " ---");
            Player player = buildPlayer();
            team.addPlayer(player);
            System.out.println("  >> Added: " + player);
        }

        printTeamSummary(team);
        return team;
    }

    private Player buildPlayer() {
        while (true) {
            try {
                String   name      = promptString("  Name        : ");
                int      number    = promptInt("  Jersey #    : ", 1, 99);
                Position position  = promptPosition();
                int      pace      = promptAttribute("  Pace        ");
                int      shooting  = promptAttribute("  Shooting    ");
                int      passing   = promptAttribute("  Passing     ");
                int      defending = promptAttribute("  Defending   ");
                int      stamina   = promptAttribute("  Stamina     ");

                return new Player(name, number, position,
                        pace, shooting, passing, defending, stamina);

            } catch (InvalidPlayerException e) {
                System.out.println("  ! " + e.getMessage()
                        + " Please try again.");
            }
        }
    }

    private void printTeamSummary(Team team) {
        System.out.println("\n  " + DIVIDER);
        System.out.println("  " + team.getName() + " built successfully!");
        System.out.println("  " + THIN_DIVIDER);
        System.out.printf("  Squad size : %d players%n",
                team.getSquadSize());
        System.out.printf("  Avg OVR    : %.1f%n",
                team.getAverageOverall());
        System.out.println("  " + THIN_DIVIDER);

        System.out.println("  >> STARTING XI");
        for (int i = 0; i < Math.min(11, team.getSquadSize()); i++) {
            Player p = team.getSquad().get(i);
            System.out.printf("     %-4s  %-20s  OVR: %d  STM: %d%n",
                    p.getPosition().getLabel(),
                    p.getName(),
                    p.getOverall(),
                    p.getStamina());
        }

        if (team.getSquadSize() > 11) {
            System.out.println("  >> BENCH");
            for (int i = 11; i < team.getSquadSize(); i++) {
                Player p = team.getSquad().get(i);
                System.out.printf("     %-4s  %-20s  OVR: %d  STM: %d%n",
                        p.getPosition().getLabel(),
                        p.getName(),
                        p.getOverall(),
                        p.getStamina());
            }
        }

        System.out.println("  " + DIVIDER);
    }

    private String promptTeamName() {
        while (true) {
            System.out.print("\n  Team name : ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  ! Team name cannot be empty.");
        }
    }

    private int promptSquadSize() {
        System.out.println("\n  How many players in your squad?");
        System.out.println("  Min: 11 (starting XI only)");
        System.out.println("  Max: 16 (11 starters + 5 bench)");
        return promptInt("  Squad size : ", 11, 16);
    }

    private Position promptPosition() {
        System.out.println("  Position    :");
        System.out.println("    [1] Goalkeeper (GK)");
        System.out.println("    [2] Defender   (DEF)");
        System.out.println("    [3] Midfielder (MID)");
        System.out.println("    [4] Forward    (FWD)");

        int choice = promptInt("  Choice      : ", 1, 4);
        return switch (choice) {
            case 1  -> Position.GOALKEEPER;
            case 2  -> Position.DEFENDER;
            case 3  -> Position.MIDFIELDER;
            default -> Position.FORWARD;
        };
    }

    private int promptAttribute(String label) {
        return promptInt(label + " (1-100) : ", 1, 100);
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