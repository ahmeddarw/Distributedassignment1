package assignment1socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import assignment1socket.game.GameManager;
import assignment1socket.game.Leaderboard;
import assignment1socket.game.Player;
import assignment1socket.helper.Utilities;

public class ClientHandler implements Runnable {
    private Socket clientSocket; 
    private Leaderboard leaderboard;

    public ClientHandler(Socket clientSocket, Leaderboard leaderboard) {
        this.clientSocket = clientSocket;
        this.leaderboard = leaderboard;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            // get input and output streams
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // ask user for their name
            writer.println("Enter your username: ");

            String username = Utilities.cleanInput(reader.readLine());
            Player player = new Player(username);
            writer.println("Welcome " + username + "!");

            // attempt to set existing leaderboard statistics for user
            leaderboard.setExistingStats(player);

            System.out.println("User '" + username + "' is connected.");
            boolean isConnectionTerminated = false;
            while (!isConnectionTerminated) {
                // delegate to game manager for handling selection
                GameManager gameManager = new GameManager(leaderboard);
                isConnectionTerminated = gameManager.start(player, reader, writer);
            }

            // close connection if terminated
            if (isConnectionTerminated) {
                writer.println("Closing connection. Good bye " + username + "!");
                clientSocket.close();
            }
    // catch exceptions
        } catch (IOException e) {
            System.out.println("Client handler exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing reader:" + e.getMessage());;
                }
            }
            // close connection
            if (writer != null) {
                writer.close();
            }
        } 
    }
}