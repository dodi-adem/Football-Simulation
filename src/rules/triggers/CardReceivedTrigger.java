package rules.triggers;

import event.CardType;
import event.MatchEvent;
import model.Team;
import rules.RuleTrigger;
import simulation.MatchContext;

public class CardReceivedTrigger implements RuleTrigger {

    private final CardType cardType;

    public CardReceivedTrigger(CardType cardType) {
        if (cardType == null) {
            throw new IllegalArgumentException("CardType cannot be null.");
        }
        this.cardType = cardType;
    }

    @Override
    public boolean isMet(MatchEvent event, MatchContext context, Team team) {
        if (cardType == CardType.YELLOW) return event.isYellowCard();
        return event.isRedCard();
    }

    @Override
    public String describe() {
        return "When team receives a " + cardType.getLabel();
    }
}