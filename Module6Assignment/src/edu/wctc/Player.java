package edu.wctc;

public class Player {
    // Static field tracks ID to be assigned to next Player object
    // that is created
    private static int nextPlayerNum = 1;
    private final int playerNum;
    private int countWin;
    private int countLoss;
    private int score;
    private int rollsUsed;
    // Adding variables for betting
    private int money = 100;
    private int bet;

    public Player() {
        playerNum = nextPlayerNum++;
    }

    public void addLoss() {
        this.countLoss++;
    }

    public void addWin() {
        this.countWin++;
    }

    public int getPlayerNumber() {
        return playerNum;
    }

    public int getRollsUsed() {
        return this.rollsUsed;
    }

    public int getScore() {
        return score;
    }

    public int getWins() {
        return countWin;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void resetPlayer() {
        score = 0;
        rollsUsed = 0;
    }

    public void roll() {
        rollsUsed++;
    }

    // Methods for betting
    public int getMoney() {
        return money;
    }

    public void placeBet(int amount) {

        if (amount > money) {
            throw new IllegalArgumentException("Not enough money.");
        }

        bet = amount;
        money -= amount;
    }

    public void winBet(int pot) {
        money += pot;
    }

    public void loseBet() {
        money -= bet;
    }

    // Updated toString to include betting info
    public String toString() {
        return String.format(
                "Player %d: Score %d | Money %d | (Won: %d, Lost: %d)",
                playerNum, score, money, countWin, countLoss
        );
    }
}
