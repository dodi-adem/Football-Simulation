package simulation;

import event.MatchEvent;
import model.Team;

import java.util.List;

public interface EventGenerator {

    List<MatchEvent> generate(MatchContext context, Team team, int minute);
}
