package assignment1socket.helper;

import assignment1socket.game.Player;

public class Utilities {
    public static String cleanInput(String input) {
        if (input == null) {
            return input;
        }
        return input.trim();
    }
    // convert player object to csv string
    public static String parseToCSVString(Player player) {
        //wins, losses, total games for tic-tac-toe and rock-paper-scissors
        return player.getUserName() + "," +
                player.getTttWins() + "," +
                player.getTttLosses() + "," +
                player.getTttTotalGames() + "," +
                player.getRpsWins() + "," +
                player.getRpsLosses() + "," +
                player.getRpsTotalGames();
    }

}