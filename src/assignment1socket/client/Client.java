package assignment1socket.client;
//imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final static String HOST = "localhost";
    private final static int PORT = 1234;
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        Scanner scanner = null;
        try {
            socket = new Socket(HOST, PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            scanner = new Scanner(System.in);
            
            System.out.println(reader.readLine()); // reading the request for username from server
            String username = scanner.nextLine();
            writer.println(username); // write the username to the server

            System.out.println(reader.readLine()); // read welcome message from server
            while (true) {

                String gameChoice = "";
                String choiceResponse = "";
                while (!choiceResponse.toLowerCase().contains("good choice")) {
                    System.out.println(reader.readLine()); // read server request to select from menu options
                    System.out.println(reader.readLine()); // read the menu options
                    
                    gameChoice = scanner.nextLine();
                    writer.println(gameChoice);
                    choiceResponse = reader.readLine();
                    System.out.println(choiceResponse);
                }

                // notify user of selection
                String serverResponse = reader.readLine();
                System.out.println(serverResponse);

                // exit the connection if quit selection was made
                if (gameChoice.toLowerCase().equals("4")) {
                    System.out.println(reader.readLine()); // print quit message
                    break;
                }

                // play the selected game
                while (true) {
                    serverResponse = reader.readLine();
                    System.out.println(serverResponse);

                    // if prompted for input, enter response
                    if (serverResponse.toLowerCase().contains("your turn") ||
                            serverResponse.toLowerCase().contains("enter")) {
                        String clientMove = scanner.nextLine();
                        writer.println(clientMove);
                    }

                    // reached end of game loop, we will exit here
                    if (serverResponse.toLowerCase().contains("game over") ||
                            serverResponse.toLowerCase().contains("eof")) {
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Client exception:" + ex.getMessage());
        } finally {
            if (scanner != null)
                scanner.close();
            if (writer != null)
                writer.close();
            if (reader != null)
                reader.close();
            if (socket != null)
                socket.close();
        }
    }
}