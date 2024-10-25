package assignment1socket.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Pipe.SourceChannel;
import java.util.ArrayList;
import java.util.List;

import assignment1socket.helper.Utilities;

// Leaderboard class
public class Leaderboard {
    // List of players
    private List<Player> leaderboard;
    // File path
    private final String leaderboardFile = "src" + File.separator + "assignment1socket" + File.separator + "server"
            + File.separator + "leaderboard.csv";

    public Leaderboard() {
        // Load leaderboard from file
        leaderboard = new ArrayList<Player>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(leaderboardFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] playerData = line.split(",");

                // get username for each player
                String username = playerData[0];
                Player player = new Player(username);

                // Load stats from list of player data
                player.setTttWins(Integer.parseInt(playerData[1]));
                player.setTttLosses(Integer.parseInt(playerData[2]));
                player.setTttTotalGames(Integer.parseInt(playerData[3]));
                player.setRpsWins(Integer.parseInt(playerData[4]));
                player.setRpsLosses(Integer.parseInt(playerData[5]));
                player.setRpsTotalGames(Integer.parseInt(playerData[6]));

                leaderboard.add(player);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error loading leaderboard: " + e.getMessage());
        }
    }

    public void updatePlayerStats(Player player) {

        boolean foundExistingPlayer = false;
        // check if player already exists in leaderboard list
        for (int i = 0; i < leaderboard.size(); i++) {
            Player p = leaderboard.get(i);
            if (p.getUserName().equals(player.getUserName())) {
                leaderboard.set(i, player);
                foundExistingPlayer = true;
                break;
            }
        }

        // add new player to leaderboard
        if (!foundExistingPlayer) {
            leaderboard.add(player);
        }

        savePlayersToLeaderboard(); // Save to file after every update
    }

    // helper function to set existing statistics for a new user
    public void setExistingStats(Player player) {
        for (int i = 0; i < leaderboard.size(); i++) {
            Player p = leaderboard.get(i);
            if (p.getUserName().equals(player.getUserName())) {
                player.setTttWins(p.getTttWins());
                player.setTttLosses(p.getTttLosses());
                player.setTttTotalGames(p.getTttTotalGames());
                player.setRpsWins(p.getRpsWins());
                player.setRpsLosses(p.getRpsLosses());
                player.setRpsTotalGames(p.getRpsTotalGames());
            }
        }
    }

    // Save players to leaderboard
    private void savePlayersToLeaderboard() {
        try {
            // Write to file
            PrintWriter pw = new PrintWriter(new FileWriter(leaderboardFile));
            for (Player p : leaderboard) {
                pw.println(Utilities.parseToCSVString(p));
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error saving the leaderboard: " + e.getMessage());
        }
    }

    public String showLeaderboard() {
        String playerStats = "Leaderboard (Player stats per game):\n";

        // Print player stats
        for (Player p : leaderboard) {
            playerStats += "**************************************************\n";
            playerStats += "USER: " + p.getDisplayName() + "\n";
            playerStats += "\tTic-Tac-Toe:\n";
            playerStats += "\t\tWins: " + p.getTttWins();
            playerStats += ", Losses: " + p.getTttLosses();
            playerStats += ", Total Games: " + p.getTttTotalGames() + "\n";
            playerStats += "\tRock-Paper-Scissors\n";
            playerStats += "\t\tWins: " + p.getRpsWins();
            playerStats += ", Losses: " + p.getRpsLosses();
            playerStats += ", Total Games: " + p.getRpsTotalGames() + "\n";
            playerStats += "**************************************************\n";
        }

        playerStats += "EOF";
        return playerStats;
    }
}
