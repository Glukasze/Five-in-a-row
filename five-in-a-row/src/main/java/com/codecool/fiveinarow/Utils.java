package com.codecool.fiveinarow;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Utils {

    private static Set<String> signs = new HashSet<>();
    private static Set<String> names = new HashSet<>();
    private static int gameRows;
    private static int gameCols;
    private static int howMany;

    public Utils() throws IOException {
    }

    public static String getUserInput(String message){
        System.out.print(message);
//        String input = System.console().readLine();
        Scanner myObj = new Scanner(System.in);

        return myObj.nextLine();
    }

    public static void clearScreen() {
        System.out.print("clear console");
        System.out.print("\033[H\033[2J");
        System.out.flush();
//        System.out.print('\f');
//        System.out.print('\u000C');
//        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//
    }

    public static int getRandomNumber(int upperboud){
        Random random = new Random();
        return random.nextInt(upperboud);
    }

    public static void setSigns(String sign) {
        signs.add(sign);
    }

    public static void clearSigns() {
        signs.clear();
    }

    public static Set<String> getSigns() {
        return signs;
    }

    public static void setNames(String name) {
        names.add(name);
    }

    public static void clearNames() {
        names.clear();
    }

    public static Set<String> getNames() {
        return names;
    }


    public static void setRows(int rows) {
        gameRows = rows;
    }

    public static int getRows() {
        return gameRows;
    }

    public static void setCols(int cols) {
        gameCols = cols;
    }

    public static int getCols() {
        return gameCols;
    }

    public static void setHowMany(int num) {
        howMany = num;
    }

    public static int getHowMany() {
        return howMany;
    }

    public static void sleepTime(int timer) {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void gameHandler() throws IOException {
        Utils.clearScreen();

        String gameName = readFromFile("gameName.txt");
        System.out.println(ConsoleColors.RED_BOLD + gameName + ConsoleColors.RESET);

//        System.out.println(ConsoleColors.RED_BOLD + "Welcome to the five-in-a-row !" + ConsoleColors.RESET);
        sleepTime(2000);

        System.out.println("Gomoku? What is Gomoku!?");
        sleepTime(4000);
        System.out.println("We have created something waaaay cooler");
        sleepTime(4000);
        System.out.println("BEHOLD tic-tac-toe on steroids!");
        sleepTime(4000);
        System.out.println("Create a board, choose win condition and become the new tic-tac-toe master!");
        sleepTime(5000);

        Scanner scan = new Scanner((System.in));

        System.out.print("How many rows do you want? ");
        int rows = scan.nextInt();
        setRows(rows);

        System.out.print("How many columns do you want? ");
        int cols = scan.nextInt();
        setCols(cols);

        System.out.print("How many in a row to win? ");
        int howMany = scan.nextInt();
        setHowMany(howMany);
    }

//for testing purpose  --------------
//        Game game = new Game(10,10,player1, player2);
//        int howMany = 5;

// End for testing purpose --------------


//    public static String readFile(String path, Charset encoding) throws IOException {
//            byte[] encoded = Files.readAllBytes(Paths.get(path));
//            return new String(encoded, encoding);
//        }

//    List<String> lines = Files.readAllLines(Paths.get("five-in-a-row-java-rivienne8/gameName.txt"), StandardCharsets.UTF_8);
//    String content = Files.readString("five-in-a-row-java-rivienne8/gameName.txt", StandardCharsets.US_ASCII);

    public static String readFromFile(String name) throws IOException {
        byte[] bytes = null;
        String str  = ConsoleColors.RED_BOLD + "Welcome to the five-in-a-row !" + ConsoleColors.RESET;
        try {
            bytes = Files.readAllBytes(Paths.get(name));
            str = new String(bytes, StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
        }
        return str;

    }

}
