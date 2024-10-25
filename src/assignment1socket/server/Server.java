package assignment1socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import assignment1socket.client.ClientHandler;
import assignment1socket.game.Leaderboard;

public class Server {
    
    private final static int PORT = 1234; //port number
    private static Leaderboard leaderboard = new Leaderboard(); //leaderboard

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            //create server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("listening on port " + PORT);

            while (true) {
                //accept client
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket, leaderboard);
                //start a new thread
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}