package assignment1socket.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import assignment1socket.helper.Utilities;

public class TicTacToe extends Game {
    private int boardSize;
    private char[][] board;

    private boolean playerWon, serverWon;

    public TicTacToe() {

        playerWon = false;
        serverWon = false;

        boardSize = 3;
        board = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }
    }

    @Override
    public void playGame(Player player, BufferedReader reader, PrintWriter writer) throws IOException {
        // by default, server is 'X'
        char currentSymbol = 'X';
        writer.println("Welcome to Tic Tac Toe! The server is " + Character.toString(currentSymbol) + ". You are "
                + ((currentSymbol == 'X') ? "O" : "X") + ".");

        boolean isGameOver = false;
        // keep playing until game is over
        while (!isGameOver) {
            // server makes their move
            writer.println("Server is making their move.");

            makeServerTurn(currentSymbol);
            writer.println(printBoard());

            isGameOver = checkGameOver(currentSymbol);
            if (isGameOver) {
                break;
            }
            //switch symbols for the next turn
            currentSymbol = (currentSymbol == 'X') ? 'O' : 'X';
            // client makes their move
            makePlayerTurn(currentSymbol, reader, writer);
            // print board
            writer.println(printBoard());
            // check if game is over
            isGameOver = checkGameOver(currentSymbol);
            if (isGameOver) {
                break;
            }
            //switch symbols for the next turn
            currentSymbol = (currentSymbol == 'X') ? 'O' : 'X';
        }
        //show game result
        String gameOverResponse = "Game over: ";
        String winner = "No one";
        // check who won or if there was a tie
        if (serverWon) {
            winner = "The server";
            // update player's loss
            player.setTttLosses(player.getTttLosses() + 1);
        } else if (playerWon) {
            winner = "You";
            // update player's win
            player.setTttWins(player.getTttWins() + 1);
        }
        // update player's total games
        player.setTttTotalGames(player.getTttTotalGames() + 1);
        // orint the winner with a message that they won
        String gameParametersString = winner + " won the Tic-Tac-Toe match!";
        writer.println(gameOverResponse + gameParametersString);
    }

    // helper function that checks if board is full
    private boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != ' ') {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // helper function to check if game is over
    private boolean checkGameOver(char symbol) {
        boolean winnerFound = false;
        // check rows
        for (int i = 0; i < boardSize; i++) {
            if (board[i][0] == symbol && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winnerFound = true;
            }
        }

        // check columns
        for (int j = 0; j < boardSize; j++) {
            if (board[0][j] == symbol && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                winnerFound = true;
            }
        }

        // check diagonals
        if (board[0][0] == symbol && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winnerFound = true;
        }
        if (board[0][2] == symbol && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winnerFound = true;
        }

        // finally, check if board is full
        if (!winnerFound && isBoardFull()) {
            return true;
        }

        if (winnerFound) {
            if (symbol == 'X') {
                serverWon = true;
            } else if (symbol == 'O') {
                playerWon = true;
            }

            return true;
        }

        return false;
    }

    private void makeServerTurn(char symbol) {
        // choose a random row and column
        int randomRow = (int) (Math.random() * boardSize);
        int randomCol = (int) (Math.random() * boardSize);

        while (board[randomRow][randomCol] != ' ') {
            // choose another random row and column
            randomRow = (int) (Math.random() * boardSize);
            randomCol = (int) (Math.random() * boardSize);

            if (board[randomRow][randomCol] == ' ') {
                break;
            }
        }

        // Mark symbol on the board
        board[randomRow][randomCol] = symbol;
    }
    //method to handle players turn
    private void makePlayerTurn(char symbol, BufferedReader reader, PrintWriter writer) throws IOException {

        int row = -1;
        int col = -1;

        String[] clientMove = null;
        // loop until a valid move is entered
        do {
            try {
                //tells user how to enter their input in terms of the board
                writer.println("Your turn (enter row and column,separated by a comma [ex: 1,1 or 2,3]): ");
                clientMove = reader.readLine().split(",");
                //check if the move is valid
                if (clientMove == null || clientMove.length != 2) {
                    writer.println("Invalid move, please try again with valid values.");
                    continue;
                }
                row = Integer.parseInt(Utilities.cleanInput(clientMove[0]));
                col = Integer.parseInt(Utilities.cleanInput(clientMove[1]));
                //check if the move is out of bounds
                if (row < 1 || col < 1 || row > boardSize || col > boardSize) {
                    writer.println("Row and/or column are out of bounds. Please try again with valid values.");
                    continue;
                }
                if (row > 0 && col > 0 && row <= boardSize && col <= boardSize && board[row - 1][col - 1] != ' ') {
                    //check if the box is already filled
                    writer.println("Selection has already been filled. Please try again with a different row and col");
                    continue;
                }
                printBoard();
                // if selection is valid, put the "o" in the board
                if (board[row - 1][col - 1] == ' ') {
                    board[row - 1][col - 1] = 'O';
                    break;
                } else {
                    writer.println("Invalid move. Try again.");
                }
            } catch (NumberFormatException nfe) {
                writer.println("Invalid selection, please try again.");
            }
        } while (row < 1 || row > boardSize || col < 1 || col > boardSize || board[row - 1][col - 1] != ' '
                || clientMove == null || clientMove.length != 2);
    }
    //method to print the board
    private String printBoard() {
        String boardString = "";
        boardString += "\n  ";

        // label each column
        for (int k = 0; k < boardSize; k++) {
            boardString += String.valueOf(k + 1) + " ";
        }
        boardString += "\n";

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // label each row
                if (j == 0) {
                    boardString += String.valueOf(i + 1) + " ";
                }
                boardString += board[i][j];
                if (j < 2)
                    boardString += "|";
            }

            boardString += "\n";

            if (i < 2)
                boardString += "  -----\n";
        }

        return boardString;
    }
}