package edu.wctc;

import java.util.*;
import java.util.stream.Collectors;

public class DiceGame {

    private final List<Player> players;
    private final List<Die> dice;
    private final int maxRolls;
    private Player currentPlayer;

    public DiceGame(int countPlayers, int countDice, int maxRolls) {

        if (countPlayers < 2) {
            throw new IllegalArgumentException("Must have at least two players.");
        }

        players = new ArrayList<>();
        dice = new ArrayList<>();
        this.maxRolls = maxRolls;

        for (int i = 0; i < countPlayers; i++) {
            players.add(new Player());
        }

        for (int i = 0; i < countDice; i++) {
            dice.add(new Die(6));
        }
    }

    private boolean allDiceHeld() {
        return dice.stream().allMatch(Die::isBeingHeld);
    }

    private boolean isHoldingDie(int faceValue) {
        return dice.stream()
                .filter(d -> d.isBeingHeld() && d.getFaceValue() == faceValue)
                .findFirst()
                .isPresent();
    }

    public boolean autoHold(int faceValue) {

        if (isHoldingDie(faceValue)) {
            return true;
        }

        Optional<Die> die = dice.stream()
                .filter(d -> !d.isBeingHeld() && d.getFaceValue() == faceValue)
                .findFirst();

        if (die.isPresent()) {
            die.get().holdDie();
            return true;
        }

        return false;
    }

    public boolean currentPlayerCanRoll() {
        return currentPlayer.getRollsUsed() < maxRolls && !allDiceHeld();
    }

    public int getCurrentPlayerNumber() {
        return currentPlayer.getPlayerNumber();
    }

    public int getCurrentPlayerScore() {
        return currentPlayer.getScore();
    }

    public String getDiceResults() {
        return dice.stream()
                .map(Die::toString)
                .collect(Collectors.joining("  "));
    }

    public String getFinalWinner() {
        Player winner = Collections.max(players,
                Comparator.comparingInt(Player::getWins));

        return winner.toString();
    }

    public String getGameResults() {

        players.sort(Comparator.comparingInt(Player::getScore).reversed());

        int topScore = players.get(0).getScore();

        players.forEach(p -> {
            if (p.getScore() == topScore) {
                p.addWin();
            } else {
                p.addLoss();
            }
        });

        return players.stream()
                .map(Player::toString)
                .collect(Collectors.joining("\n"));
    }

    public boolean nextPlayer() {

        int index = players.indexOf(currentPlayer);

        if (index + 1 < players.size()) {
            currentPlayer = players.get(index + 1);
            return true;
        }

        return false;
    }

    public void playerHold(char dieNum) {

        Optional<Die> die = dice.stream()
                .filter(d -> d.getDieNum() == dieNum)
                .findFirst();

        die.ifPresent(Die::holdDie);
    }

    public void resetDice() {
        dice.forEach(Die::resetDie);
    }

    public void resetPlayers() {
        players.forEach(Player::resetPlayer);
    }

    public void rollDice() {
        currentPlayer.roll();
        dice.forEach(Die::rollDie);
    }

    public void scoreCurrentPlayer() {

        if (isHoldingDie(6) && isHoldingDie(5) && isHoldingDie(4)) {

            int cargo = dice.stream()
                    .mapToInt(Die::getFaceValue)
                    .sum();

            currentPlayer.setScore(cargo - 15);

        } else {
            currentPlayer.setScore(0);
        }
    }

    public void startNewGame() {
        currentPlayer = players.get(0);
        resetPlayers();
    }
}