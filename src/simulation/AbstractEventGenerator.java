package simulation;

import event.MatchEvent;
import model.Player;
import model.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventGenerator implements EventGenerator {

    @Override
    public final List<MatchEvent> generate(MatchContext context, Team team, int minute) {
        List<Player> activePlayers = context.getActivePlayers(team);

        if (activePlayers.isEmpty()) {
            return new ArrayList<>();
        }

        return tryGenerate(context, team, minute, activePlayers);
    }

    protected abstract List<MatchEvent> tryGenerate(MatchContext context, Team team, int minute, List<Player> activePlayers);
}