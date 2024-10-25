package assignment1socket.game;

public class Player {

    String userName;
    String displayName;

    private int tttWins;
    private int tttLosses;
    private int tttTotalGames;

    private int rpsWins;
    private int rpsLosses;
    private int rpsTotalGames;

    public Player(String userName) {
        this.displayName = userName;
        // same username will be stored as same leaderboard record, regardless of capitalization
        this.userName = userName.toLowerCase();

        this.tttWins = 0;
        this.tttLosses = 0;
        this.tttTotalGames = 0;

        this.rpsWins = 0;
        this.rpsLosses = 0;
        this.rpsTotalGames = 0;
    }
// getters and setters
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTttWins(int tttWins) {
        this.tttWins = tttWins;
    }

    public void setTttLosses(int tttLosses) {
        this.tttLosses = tttLosses;
    }

    public void setTttTotalGames(int tttTotalGames) {
        this.tttTotalGames = tttTotalGames;
    }
    public void setRpsWins(int rpsWins) {
        this.rpsWins = rpsWins;
    }

    public void setRpsLosses(int rpsLosses) {
        this.rpsLosses = rpsLosses;
    }

    public void setRpsTotalGames(int rpsTotalGames) {
        this.rpsTotalGames = rpsTotalGames;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserName() {
        return userName;
    }

    public int getTttWins() {
        return tttWins;
    }

    public int getTttLosses() {
        return tttLosses;
    }

    public int getTttTotalGames() {
        return tttTotalGames;
    }
    public int getRpsWins() {
        return rpsWins;
    }

    public int getRpsLosses() {
        return rpsLosses;
    }

    public int getRpsTotalGames() {
        return rpsTotalGames;
    }
}