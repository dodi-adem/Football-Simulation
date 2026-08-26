package simulation;

import event.MatchEvent;
import event.OffsideEvent;
import model.Player;
import model.Position;
import model.Team;
import service.PlayerSelector;
import service.ProbabilityService;
import util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class OffsideGenerator extends AbstractEventGenerator {

    private final ProbabilityService    probabilityService;
    private final PlayerSelector        playerSelector;
    private final RandomNumberGenerator rng;

    public OffsideGenerator(ProbabilityService probabilityService,
                            PlayerSelector playerSelector,
                            RandomNumberGenerator rng) {
        if (probabilityService == null) {
            throw new IllegalArgumentException(
                "ProbabilityService cannot be null.");
        }
        if (playerSelector == null) {
            throw new IllegalArgumentException(
                "PlayerSelector cannot be null.");
        }
        if (rng == null) {
            throw new IllegalArgumentException("RNG cannot be null.");
        }
        this.probabilityService = probabilityService;
        this.playerSelector     = playerSelector;
        this.rng                = rng;
    }

    @Override
    protected List<MatchEvent> tryGenerate(MatchContext context,
                                           Team team,
                                           int minute,
                                           List<Player> activePlayers) {
        List<MatchEvent> result = new ArrayList<>();

        List<Player> attackingPlayers = getAttackingPlayers(activePlayers);
        if (attackingPlayers.isEmpty()) return result;

        double chance = probabilityService.offsideProbability(
                context.getTactic(team));

        if (minute > 70)      chance *= 1.25;
        else if (minute > 45) chance *= 1.10;

        if (rng.rollChance(chance)) {
            Player caught = playerSelector.selectOffsidePlayer(
                    attackingPlayers);
            result.add(new OffsideEvent(minute, team, caught));
        }

        return result;
    }

    private List<Player> getAttackingPlayers(List<Player> activePlayers) {
        List<Player> result = new ArrayList<>();
        for (Player player : activePlayers) {
            if (player.getPosition() == Position.FORWARD ||
                player.getPosition() == Position.MIDFIELDER) {
                result.add(player);
            }
        }
        return result;
    }
}