package event;

import exceptions.InvalidSubstitutionException;
import model.Player;
import model.Team;

public class SubstitutionEvent extends MatchEvent {

    private final Player playerOut;
    private final Player playerIn;

    public SubstitutionEvent(int minute, Team team,
                             Player playerOut, Player playerIn)
            throws InvalidSubstitutionException {
        super(minute, team);
        if (playerOut == null || playerIn == null) {
            throw new InvalidSubstitutionException(
                "Players cannot be null.");
        }
        if (playerOut == playerIn) {
            throw new InvalidSubstitutionException(
                "A player cannot substitute themselves.");
        }
        this.playerOut = playerOut;
        this.playerIn  = playerIn;
    }

    public Player getPlayerOut() { return playerOut; }
    public Player getPlayerIn()  { return playerIn; }

    @Override
    public String toString() {
        return String.format("[%d'] SUBSTITUTION! %s - %s comes on for %s",
                getMinute(), getTeam().getName(),
                playerIn.getName(), playerOut.getName());
    }
}