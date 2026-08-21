package event;

import model.Player;
import model.Team;

public class OffsideEvent extends MatchEvent {

    private final Player player;

    public OffsideEvent(int minute, Team team, Player player) {
        super(minute, team);
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        this.player = player;
    }

    public Player getPlayer() { return player; }

    @Override
    public String toString() {
        return String.format("[%d'] OFFSIDE! %s - %s caught offside",
                getMinute(), getTeam().getName(), player.getName());
    }
}