package util;

import exceptions.InvalidPlayerException;
import exceptions.InvalidTeamException;
import model.Player;
import model.Position;
import model.Team;

public class PresetTeams {

    private PresetTeams() {}

    public static Team buildBarcelona() throws InvalidTeamException {
        try {
            Team team = new Team("FC Barcelona");
            team.addPlayer(new Player("ter Stegen",    1,  Position.GOALKEEPER, 55, 20, 65, 85, 80));
            team.addPlayer(new Player("Araujo",        4,  Position.DEFENDER,   75, 40, 60, 88, 85));
            team.addPlayer(new Player("Christensen",   5,  Position.DEFENDER,   65, 35, 68, 84, 82));
            team.addPlayer(new Player("Cubarsi",       3,  Position.DEFENDER,   65, 30, 72, 83, 84));
            team.addPlayer(new Player("Balde",         12, Position.DEFENDER,   85, 35, 70, 78, 86));
            team.addPlayer(new Player("Pedri",         8,  Position.MIDFIELDER, 78, 72, 90, 70, 88));
            team.addPlayer(new Player("Gavi",          6,  Position.MIDFIELDER, 72, 68, 88, 65, 85));
            team.addPlayer(new Player("De Jong",       21, Position.MIDFIELDER, 76, 65, 85, 72, 84));
            team.addPlayer(new Player("Lewandowski",   9,  Position.FORWARD,    76, 91, 75, 40, 82));
            team.addPlayer(new Player("Raphinha",      11, Position.FORWARD,    88, 80, 72, 45, 83));
            team.addPlayer(new Player("Yamal",         27, Position.FORWARD,    92, 78, 76, 35, 85));
            team.addPlayer(new Player("Inaki Pena",    13, Position.GOALKEEPER, 52, 18, 60, 80, 78));
            team.addPlayer(new Player("Kounde",        23, Position.DEFENDER,   82, 38, 68, 85, 83));
            team.addPlayer(new Player("Fermin",        16, Position.MIDFIELDER, 74, 66, 82, 60, 83));
            team.addPlayer(new Player("Olmo",          20, Position.MIDFIELDER, 80, 74, 86, 62, 82));
            team.addPlayer(new Player("Ferran Torres",  7, Position.FORWARD,    85, 76, 70, 38, 80));
            return team;
        } catch (InvalidPlayerException e) {
            throw new InvalidTeamException(
                "Failed to build Barcelona: " + e.getMessage());
        }
    }

    public static Team buildMonaco() throws InvalidTeamException {
        try {
            Team team = new Team("AS Monaco");
            team.addPlayer(new Player("Majecki",    1,  Position.GOALKEEPER, 50, 15, 55, 80, 78));
            team.addPlayer(new Player("Singo",      19, Position.DEFENDER,   82, 45, 65, 80, 82));
            team.addPlayer(new Player("Badiashile",  5, Position.DEFENDER,   70, 30, 60, 85, 80));
            team.addPlayer(new Player("Sarr",        6, Position.DEFENDER,   72, 28, 62, 83, 81));
            team.addPlayer(new Player("Vanderson",  14, Position.DEFENDER,   84, 40, 68, 76, 83));
            team.addPlayer(new Player("Fofana",     29, Position.MIDFIELDER, 80, 65, 82, 75, 85));
            team.addPlayer(new Player("Camara",     17, Position.MIDFIELDER, 75, 60, 78, 70, 83));
            team.addPlayer(new Player("Minamino",   18, Position.MIDFIELDER, 78, 68, 80, 60, 82));
            team.addPlayer(new Player("Ben Yedder", 10, Position.FORWARD,    72, 88, 70, 38, 84));
            team.addPlayer(new Player("Embolo",      7, Position.FORWARD,    80, 78, 65, 42, 80));
            team.addPlayer(new Player("Akliouche",  26, Position.FORWARD,    83, 74, 68, 35, 81));
            team.addPlayer(new Player("Nubel",       2, Position.GOALKEEPER, 54, 16, 58, 82, 79));
            team.addPlayer(new Player("Maripan",     3, Position.DEFENDER,   68, 25, 58, 82, 78));
            team.addPlayer(new Player("Mawissa",    24, Position.MIDFIELDER, 72, 58, 74, 65, 80));
            team.addPlayer(new Player("Zakaria",    28, Position.MIDFIELDER, 76, 62, 76, 72, 81));
            team.addPlayer(new Player("Balogun",     9, Position.FORWARD,    78, 80, 62, 30, 79));
            return team;
        } catch (InvalidPlayerException e) {
            throw new InvalidTeamException(
                "Failed to build Monaco: " + e.getMessage());
        }
    }

    public static Team buildRealMadrid() throws InvalidTeamException {
        try {
            Team team = new Team("Real Madrid");
            team.addPlayer(new Player("Courtois",     1,  Position.GOALKEEPER, 58, 15, 68, 88, 82));
            team.addPlayer(new Player("Carvajal",     2,  Position.DEFENDER,   83, 42, 72, 82, 83));
            team.addPlayer(new Player("Militao",       3, Position.DEFENDER,   78, 35, 65, 88, 84));
            team.addPlayer(new Player("Alaba",         4, Position.DEFENDER,   72, 38, 76, 86, 82));
            team.addPlayer(new Player("Mendy",        23, Position.DEFENDER,   86, 32, 68, 80, 83));
            team.addPlayer(new Player("Valverde",     15, Position.MIDFIELDER, 85, 74, 80, 75, 88));
            team.addPlayer(new Player("Camavinga",    12, Position.MIDFIELDER, 82, 65, 78, 72, 86));
            team.addPlayer(new Player("Bellingham",    5, Position.MIDFIELDER, 80, 82, 84, 70, 88));
            team.addPlayer(new Player("Vinicius Jr.",  7, Position.FORWARD,    95, 82, 74, 32, 86));
            team.addPlayer(new Player("Mbappe",        9, Position.FORWARD,    97, 88, 78, 35, 85));
            team.addPlayer(new Player("Rodrygo",      11, Position.FORWARD,    88, 78, 72, 36, 83));
            team.addPlayer(new Player("Lunin",        13, Position.GOALKEEPER, 55, 14, 60, 82, 78));
            team.addPlayer(new Player("Nacho",         6, Position.DEFENDER,   74, 36, 68, 83, 80));
            team.addPlayer(new Player("Tchouameni",   18, Position.MIDFIELDER, 78, 62, 76, 78, 83));
            team.addPlayer(new Player("Ceballos",     24, Position.MIDFIELDER, 74, 64, 82, 62, 80));
            team.addPlayer(new Player("Brahim",       21, Position.FORWARD,    84, 74, 72, 34, 80));
            return team;
        } catch (InvalidPlayerException e) {
            throw new InvalidTeamException(
                "Failed to build Real Madrid: " + e.getMessage());
        }
    }

    public static Team buildManCity() throws InvalidTeamException {
        try {
            Team team = new Team("Manchester City");
            team.addPlayer(new Player("Ederson",        31, Position.GOALKEEPER, 62, 20, 72, 85, 82));
            team.addPlayer(new Player("Walker",          2, Position.DEFENDER,   88, 38, 70, 82, 83));
            team.addPlayer(new Player("Ruben Dias",      3, Position.DEFENDER,   70, 30, 72, 90, 85));
            team.addPlayer(new Player("Akanji",         25, Position.DEFENDER,   74, 28, 68, 87, 83));
            team.addPlayer(new Player("Gvardiol",       24, Position.DEFENDER,   80, 35, 72, 84, 84));
            team.addPlayer(new Player("Rodri",          16, Position.MIDFIELDER, 72, 62, 84, 82, 86));
            team.addPlayer(new Player("De Bruyne",      17, Position.MIDFIELDER, 78, 82, 93, 62, 84));
            team.addPlayer(new Player("Bernardo Silva", 20, Position.MIDFIELDER, 80, 74, 88, 65, 86));
            team.addPlayer(new Player("Doku",           11, Position.FORWARD,    94, 76, 70, 30, 83));
            team.addPlayer(new Player("Haaland",         9, Position.FORWARD,    89, 95, 65, 35, 84));
            team.addPlayer(new Player("Foden",          47, Position.FORWARD,    82, 80, 82, 42, 84));
            team.addPlayer(new Player("Ortega",         18, Position.GOALKEEPER, 54, 16, 60, 80, 78));
            team.addPlayer(new Player("Stones",          5, Position.DEFENDER,   72, 32, 72, 85, 81));
            team.addPlayer(new Player("Kovacic",         8, Position.MIDFIELDER, 76, 62, 82, 68, 82));
            team.addPlayer(new Player("Nunes",          27, Position.MIDFIELDER, 80, 65, 78, 65, 82));
            team.addPlayer(new Player("Grealish",       10, Position.FORWARD,    82, 72, 78, 38, 80));
            return team;
        } catch (InvalidPlayerException e) {
            throw new InvalidTeamException(
                "Failed to build Man City: " + e.getMessage());
        }
    }
}