package assignment1socket.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RockPaperScissors extends Game {
    // List of possible choices for rock, paper, scissors
    private static List<String> choices = Arrays.asList("rock", "paper", "scissors");
    private Map<String, String> choiceMap = new HashMap<String, String>() {
        {
            // map user letter to full word
            put("r", "rock");
            put("p", "paper");
            put("s", "scissors");
        }
    };
    // Game stats
    private int serverWins, clientWins, ties, currentRound, totalRounds, winLimit;

    public RockPaperScissors() {
        // by default, we will play up to 5 rounds
        this.totalRounds = 5;
        this.winLimit = 3;
        this.serverWins = 0;
        this.clientWins = 0;
        this.ties = 0;
        this.currentRound = 0;
    }

    public RockPaperScissors(int rounds) {
        this.totalRounds = rounds;
        this.winLimit = (int) (this.totalRounds / 2) + 1;
        this.serverWins = 0;
        this.clientWins = 0;
        this.currentRound = 0;
    }
    //method to handle game logic
    @Override
    public void playGame(Player player, BufferedReader reader, PrintWriter writer) throws IOException {
        //welcome message
        writer.println("\nWelcome to Rock-Paper-Scissors Game! \nWin 3 out of 5 games to beat the server!");
        boolean isGameOver = false;
        while (!isGameOver) {
            // server selects first
            String serverChoice = choices.get((int) (Math.random() * choices.size()));
            String clientChoice = "";
            // get user input
            while (true) {
                writer.println("Please enter a value of (r)ock, (p)aper, or (s)cissors");
                clientChoice = reader.readLine();
                //validate single letter and map it to full word
                if (clientChoice.length() == 1 && (clientChoice.equals("r") ||
                        clientChoice.equals("p") ||
                        clientChoice.equals("s"))) {
                    isGameOver = determineRoundWinner(writer, clientChoice, serverChoice);
                    break;
                    //validate full word choice
                } else if (choices.indexOf(clientChoice.toLowerCase().trim()) >= 0) {
                    isGameOver = determineRoundWinner(writer, clientChoice, serverChoice);
                    break;
                } else {
                    //if user enters an invalid value
                    writer.println("Invalid choice. Please input a valid value and try again.");
                }
            }
            // check if game is over ouput result and update stats
            if (isGameOver) {
                String gameOverResponse = "Game over:";
                String gameParametersString = "won the best " + winLimit + " out of " + totalRounds + " rounds!";
                if (clientWins == winLimit) {
                    writer.println("\n" + gameOverResponse + " You " + gameParametersString);
                    player.setRpsWins(player.getRpsWins() + 1);
                } else if (serverWins == winLimit) {
                    writer.println("\n" + gameOverResponse + " Server " + gameParametersString);
                    player.setRpsLosses(player.getRpsLosses() + 1);
                } else {
                    writer.println("\n" + gameOverResponse + " No one " + gameParametersString);
                }

                player.setRpsTotalGames(player.getRpsTotalGames() + 1);
            }
        }
    }
    // determine round winner
    private boolean determineRoundWinner(PrintWriter writer, String clientChoice, String serverChoice) {
        String roundWinner = "";
        // user inputted letter, map it to full word
        if (clientChoice.length() == 1) {
            clientChoice = choiceMap.get(clientChoice);
        }
        // determine who the round winner is 
        if ((clientChoice.equals("rock") && serverChoice.equals("paper")) ||
                (clientChoice.equals("scissors") && serverChoice.equals("rock")) ||
                (clientChoice.equals("paper") && serverChoice.equals("scissors"))) {
            roundWinner = "Server";
            serverWins++;
        } else if ((serverChoice.equals("rock") && clientChoice.equals("paper")) ||
                (serverChoice.equals("scissors") && clientChoice.equals("rock")) ||
                (serverChoice.equals("paper") && clientChoice.equals("scissors"))) {
            roundWinner = "You";
            clientWins++;
        } else {
            roundWinner = "No one";
            ties++;
        }
        currentRound++;
        //displaying server choice and client choice
        writer.println("\nThe server chose " + serverChoice);
        writer.println("You chose " + clientChoice);

        // Send server's choice and result to client
        writer.println("\nRound " + currentRound + ": Winner is -> " + roundWinner);
        writer.println(
                "Here is the current score -> You: " + clientWins + ", Server: " + serverWins + ", Ties: " + ties);

        if (clientWins == winLimit || serverWins == winLimit || currentRound == totalRounds) {
            return true;
        }

        return false;
    }
}