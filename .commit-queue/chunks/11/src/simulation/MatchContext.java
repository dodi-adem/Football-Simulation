package simulation;

import event.MatchEvent;
import model.Player;
import model.Tactic;
import model.Team;
import util.SimulationConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchContext {

    private final Team homeTeam;
    private final Team awayTeam;

    private int homeGoals = 0;
    private int awayGoals = 0;

    private int       homeSubsUsed = 0;
    private int       awaySubsUsed = 0;
    private final int maxSubstitutions;

    private Tactic homeTactic = Tactic.BALANCED;
    private Tactic awayTactic = Tactic.BALANCED;

    private final Set<Player> activePlayers       = new HashSet<>();
    private final Set<Player> sentOffPlayers      = new HashSet<>();
    private final Set<Player> yellowCardedPlayers = new HashSet<>();

    private int homeYellowCards = 0;
    private int awayYellowCards = 0;

    private double homeAttackModifier  = 1.0;
    private double homeDefenseModifier = 1.0;
    private double awayAttackModifier  = 1.0;
    private double awayDefenseModifier = 1.0;

    private double homeAttackBoost = 0.0;
    private double awayAttackBoost = 0.0;

    private final List<MatchEvent> events = new ArrayList<>();

    public MatchContext(Team homeTeam, Team awayTeam, SimulationConfig config) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Teams cannot be null.");
        }
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null.");
        }
        this.homeTeam         = homeTeam;
        this.awayTeam         = awayTeam;
        this.maxSubstitutions = config.getMaxSubstitutions();

        initializeActivePlayers(homeTeam);
        initializeActivePlayers(awayTeam);
    }

    public boolean isActive(Player player) {
        return activePlayers.contains(player);
    }

    public void deactivatePlayer(Player player) {
        activePlayers.remove(player);
    }

    public void activatePlayer(Player player) {
        activePlayers.add(player);
    }

    public List<Player> getActivePlayers(Team team) {
        List<Player> result = new ArrayList<>();
        for (Player player : team.getSquad()) {
            if (activePlayers.contains(player)) {
                result.add(player);
            }
        }
        return result;
    }

    public boolean recordYellowCard(Player player, Team team) {
        if (team == homeTeam) homeYellowCards++;
        else if (team == awayTeam) awayYellowCards++;

        if (yellowCardedPlayers.contains(player)) {
            deactivatePlayer(player);
            sentOffPlayers.add(player);
            return true;
        } else {
            yellowCardedPlayers.add(player);
            return false;
        }
    }

    public void recordRedCard(Player player) {
        deactivatePlayer(player);
        yellowCardedPlayers.remove(player);
        sentOffPlayers.add(player);
    }

    public boolean isSentOff(Player player) {
        return sentOffPlayers.contains(player);
    }

    public int getYellowCards(Team team) {
        if (team == homeTeam) return homeYellowCards;
        if (team == awayTeam) return awayYellowCards;
        return 0;
    }

    public double getAttackModifier(Team team) {
        if (team == homeTeam) return homeAttackModifier;
        if (team == awayTeam) return awayAttackModifier;
        return 1.0;
    }

    public double getDefenseModifier(Team team) {
        if (team == homeTeam) return homeDefenseModifier;
        if (team == awayTeam) return awayDefenseModifier;
        return 1.0;
    }

    public void setAttackModifier(Team team, double modifier) {
        if (team == homeTeam)      homeAttackModifier  = modifier;
        else if (team == awayTeam) awayAttackModifier  = modifier;
    }

    public void setDefenseModifier(Team team, double modifier) {
        if (team == homeTeam)      homeDefenseModifier = modifier;
        else if (team == awayTeam) awayDefenseModifier = modifier;
    }

    public double getAttackBoost(Team team) {
        if (team == homeTeam) return homeAttackBoost;
        if (team == awayTeam) return awayAttackBoost;
        return 0.0;
    }

    public void setAttackBoost(Team team, double boost) {
        if (team == homeTeam)      homeAttackBoost = boost;
        else if (team == awayTeam) awayAttackBoost = boost;
    }

    public Tactic getTactic(Team team) {
        if (team == homeTeam) return homeTactic;
        if (team == awayTeam) return awayTactic;
        return Tactic.BALANCED;
    }

    public void setTactic(Team team, Tactic tactic) {
        if (team == homeTeam)      homeTactic = tactic;
        else if (team == awayTeam) awayTactic = tactic;
    }

    public void addHomeGoal() { homeGoals++; }
    public void addAwayGoal() { awayGoals++; }
    public int  getHomeGoals() { return homeGoals; }
    public int  getAwayGoals() { return awayGoals; }

    public int getGoalDifference(Team team) {
        if (team == homeTeam) return homeGoals - awayGoals;
        if (team == awayTeam) return awayGoals - homeGoals;
        return 0;
    }

    public boolean canSubstitute(Team team) {
        if (team == homeTeam) return homeSubsUsed < maxSubstitutions;
        if (team == awayTeam) return awaySubsUsed < maxSubstitutions;
        return false;
    }

    public void recordSubstitution(Team team) {
        if (team == homeTeam)      homeSubsUsed++;
        else if (team == awayTeam) awaySubsUsed++;
    }

    public int getSubsUsed(Team team) {
        if (team == homeTeam) return homeSubsUsed;
        if (team == awayTeam) return awaySubsUsed;
        return 0;
    }

    public void addEvent(MatchEvent event) {
        if (event != null) events.add(event);
    }

    public List<MatchEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }

    public String getScoreLine() {
        return homeTeam.getName() + "  " + homeGoals
                + " - " + awayGoals + "  " + awayTeam.getName();
    }

    private void initializeActivePlayers(Team team) {
        List<Player> squad = team.getSquad();
        for (int i = 0; i < Math.min(11, squad.size()); i++) {
            activePlayers.add(squad.get(i));
        }
    }
}