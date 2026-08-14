package simulation;

import event.GoalEvent;
import event.MatchEvent;
import event.PenaltyEvent;
import event.PenaltyOutcome;
import model.Player;
import model.Team;
import service.PlayerSelector;
import service.ProbabilityService;
import util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class PenaltyGenerator extends AbstractEventGenerator {

    private final ProbabilityService    probabilityService;
    private final PlayerSelector        playerSelector;
    private final RandomNumberGenerator rng;

    public PenaltyGenerator(ProbabilityService probabilityService,
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

        double chance = probabilityService.penaltyProbability(
                context.getTactic(team));

        if (!rng.rollChance(chance)) return result;

        Player taker = playerSelector.selectPenaltyTaker(activePlayers);

        double  conversionChance = 0.60
                + (taker.getShooting() / 100.0 * 0.30);
        boolean scored           = rng.rollChance(conversionChance);

        PenaltyOutcome outcome = scored
                ? PenaltyOutcome.SCORED
                : PenaltyOutcome.MISSED;

        result.add(new PenaltyEvent(minute, team, taker, outcome));

        if (scored) {
            if (team == context.getHomeTeam()) context.addHomeGoal();
            else                               context.addAwayGoal();

            result.add(new GoalEvent(minute, team, taker));
        }

        return result;
    }
}