package event;

import model.Player;
import model.Team;

public class CardEvent extends MatchEvent {

    private final Player        player;
    private final CardType      cardType;
    private final RedCardReason redCardReason;

    public CardEvent(int minute, Team team, Player player, CardType cardType) {
        super(minute, team);
        validate(player, cardType);
        this.player        = player;
        this.cardType      = cardType;
        this.redCardReason = null;
    }

    public CardEvent(int minute, Team team, Player player, CardType cardType, RedCardReason reason) {
        super(minute, team);
        validate(player, cardType);
        if (reason == null) {
            throw new IllegalArgumentException(
                    "Red card reason cannot be null.");
        }
        this.player        = player;
        this.cardType      = cardType;
        this.redCardReason = reason;
    }

    public Player        getPlayer()        { return player; }
    public CardType      getCardType()      { return cardType; }
    public RedCardReason getRedCardReason() { return redCardReason; }

    @Override
    public boolean isYellowCard() { return cardType == CardType.YELLOW; }

    @Override
    public boolean isRedCard() { return cardType == CardType.RED; }

    public boolean isSecondYellow() {
        return redCardReason == RedCardReason.SECOND_YELLOW;
    }

    @Override
    public String toString() {
        if (cardType == CardType.RED) {
            return String.format("[%d'] %s - %s receives a RED CARD (%s)",
                    getMinute(), getTeam().getName(),
                    player.getName(), redCardReason);
        }
        return String.format("[%d'] %s - %s receives a YELLOW CARD",
                getMinute(), getTeam().getName(), player.getName());
    }

    private static void validate(Player player, CardType cardType) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (cardType == null) {
            throw new IllegalArgumentException("CardType cannot be null.");
        }
    }
}