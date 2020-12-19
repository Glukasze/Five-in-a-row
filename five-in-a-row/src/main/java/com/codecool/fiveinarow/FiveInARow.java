package com.codecool.fiveinarow;

import java.io.IOException;
import java.util.Locale;

public class FiveInARow {

    public static void main(String[] args) throws IOException {

        String playing = "y";
        while (playing.equals("y")) {

            Utils.gameHandler();

            User player1 = new User(1);
            User player2 = new User(2);

            Game game = new Game(Utils.getRows(), Utils.getCols(), player1, player2);
//            Game game = new Game(14, 12
//                    , player1, player2);

            game.enableAi(1);
            game.enableAi(2);

            try {
                game.play(Utils.getHowMany());
//                game.play(4);
            } catch (EndOfGameException e) {
//            System.out.println(EndOfGame);
                System.out.println("Good Bye");
                return;
            }
            boolean playingAgain = true;
            while (playingAgain) {
                String playAgain = Utils.getUserInput("Do you wanna play again? (Y/N) ");
                if (!playAgain.toLowerCase().equals("y") && !playAgain.toLowerCase().equals("n")) {
                    System.out.println("Please choose 'Y' or 'N'");
                } else {
                    playing = playAgain.toLowerCase();
                    Utils.clearNames();
                    Utils.clearSigns();
                    playingAgain = false;
                }
            }
        }
    }
}
