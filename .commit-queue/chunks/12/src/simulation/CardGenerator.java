package simulation;

import event.CardEvent;
import event.CardType;
import event.MatchEvent;
import event.RedCardReason;
import model.Player;
import model.Team;
import service.PlayerSelector;
import service.ProbabilityService;
import util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class CardGenerator extends AbstractEventGenerator {

    private final ProbabilityService    probabilityService;
    private final PlayerSelector        playerSelector;
    private final RandomNumberGenerator rng;

    public CardGenerator(ProbabilityService probabilityService, PlayerSelector playerSelector, RandomNumberGenerator rng) {
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
    protected List<MatchEvent> tryGenerate(MatchContext context, Team team, int minute, List<Player> activePlayers) {
        List<MatchEvent> result = new ArrayList<>();

        if (rng.rollChance(probabilityService.yellowCardProbability(
                minute, context.getTactic(team)))) {

            Player  recipient      = playerSelector.selectCardRecipient(
                    activePlayers);
            boolean isSecondYellow = context.recordYellowCard(
                    recipient, team);

            if (isSecondYellow) {
                result.add(new CardEvent(minute, team, recipient, CardType.YELLOW));
                result.add(new CardEvent(minute, team, recipient, CardType.RED, RedCardReason.SECOND_YELLOW));
            } else {
                result.add(new CardEvent(minute, team, recipient, CardType.YELLOW));
            }

        } else if (rng.rollChance(probabilityService.redCardProbability(
                minute, context.getTactic(team)))) {

            Player recipient = playerSelector.selectCardRecipient(
                    activePlayers);
            context.recordRedCard(recipient);
            result.add(new CardEvent(minute, team, recipient,
                    CardType.RED, RedCardReason.DIRECT_RED));
        }

        return result;
    }
}