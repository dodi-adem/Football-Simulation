package simulation;

import event.GoalEvent;
import event.MatchEvent;
import model.Player;
import model.Team;
import service.PlayerSelector;
import service.ProbabilityService;
import service.TeamStrengthCalculator;
import util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class GoalGenerator extends AbstractEventGenerator {

    private final TeamStrengthCalculator strengthCalculator;
    private final ProbabilityService     probabilityService;
    private final PlayerSelector         playerSelector;
    private final RandomNumberGenerator  rng;

    public GoalGenerator(TeamStrengthCalculator strengthCalculator,
                         ProbabilityService probabilityService,
                         PlayerSelector playerSelector,
                         RandomNumberGenerator rng) {
        if (strengthCalculator == null) {
            throw new IllegalArgumentException(
                "StrengthCalculator cannot be null.");
        }
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
        this.strengthCalculator = strengthCalculator;
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

        Team opponent = (team == context.getHomeTeam())
                ? context.getAwayTeam()
                : context.getHomeTeam();

        double attack  = strengthCalculator.calculateAttackStrength(team)
                       * context.getAttackModifier(team)
                       + context.getAttackBoost(team);

        double defense = strengthCalculator.calculateDefenseStrength(opponent)
                       * context.getDefenseModifier(opponent);

        double chance = probabilityService.goalProbability(
                attack, defense,
                context.getTactic(team),
                context.getTactic(opponent));

        if (rng.rollChance(chance)) {
            Player scorer    = playerSelector.selectScorer(activePlayers);
            Player assistant = playerSelector.selectAssistant(
                    activePlayers, scorer);

            if (team == context.getHomeTeam()) context.addHomeGoal();
            else                               context.addAwayGoal();

            result.add(new GoalEvent(minute, team, scorer, assistant));
        }

        return result;
    }
}