package assignment1socket.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
//abstract class for all games
public abstract class Game {
    public abstract void playGame(Player player, BufferedReader reader, PrintWriter writer) throws IOException;
}

