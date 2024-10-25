package assignment1socket.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import assignment1socket.helper.Utilities;

public class GameManager {
    private Leaderboard leaderboard;

    public GameManager(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }
    // start game
    public boolean start(Player player, BufferedReader reader, PrintWriter writer) throws IOException {
    
        String gameChoice = "";
        int choice = -1;

        boolean validChoice = false;
        // ask user for their choice
        while (!validChoice) {
            writer.println("Please select an option from the list below:");
            // print menu
            writer.println("\t1: Play Rock-Paper-Scissors \t2: Play Tic-Tac-Toe \t3: View Leaderboard \t4: Quit");
            gameChoice = Utilities.cleanInput(reader.readLine());
            // validate choice
            if (gameChoice.length() != 1) {
                writer.println("Invalid choice. Please try again.");
                continue;
            }
            
            try {
                // convert to integer
                choice = Integer.parseInt(gameChoice);
                // validate choice range
                if (choice < 1 || choice > 4) {
                    writer.println("Invalid choice. Please try again.");
                    continue;
                }
            } catch (NumberFormatException nfe) {
                writer.println("Choice must be a number. Please try again.");
                continue;
            }

            validChoice = true;
        }
        writer.println("Good choice! You selected " + gameChoice);
        //execute selected game
        Game game;
        // if choice is 1, play rock-paper-scissors
        if (choice == 1) {
            game = new RockPaperScissors();
            game.playGame(player, reader, writer);
            leaderboard.updatePlayerStats(player);
            return false;
            // if choice is 2, play tic-tac-toe
        } else if (choice == 2) {
            game = new TicTacToe();
            game.playGame(player, reader, writer);
            leaderboard.updatePlayerStats(player);
            return false;
            // if choice is 3, show leaderboard
        } else if (choice == 3) {
            writer.println(leaderboard.showLeaderboard());
            return false;
            // if choice is 4, quit
        } else if (choice == 4) {
            writer.println("You chose to quit!");
            return true;
        } else {
            writer.println("Invalid choice. Please try again.");
            return false;
        }
    }
}