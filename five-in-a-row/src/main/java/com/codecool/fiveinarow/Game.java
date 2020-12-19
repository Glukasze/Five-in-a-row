package com.codecool.fiveinarow;
import javax.print.DocFlavor;
import java.io.*;
import java.lang.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Game implements GameInterface {

    private int[][] board;
    private int boardWidth;
    private int maxRowNumber;
    private char[] rowChars;
    private User player1;
    private User player2;
    //    private User[] players = {player1, player2};
    private User[] players;
    private String endPassword;
    private int howMany;


    private User getUserForUserNumber(int userNum) {
        for (User player : players) {
            if (player.getUserNumber() == userNum) {
                return player;
            }
        }
        return null;
    }


    // tu przychodzą jako parametry gotowi playerzy (utworzeni w osobnej klasie  userFactory, create Huser, AIuser)
    public Game(int nRows, int nCols, User player1, User player2) {

        this.boardWidth = nCols;
        this.maxRowNumber = nRows;
        this.player1 = player1;
        this.player2 = player2;
        this.players = new User[]{player1, player2};
        this.endPassword = "quit";

//for testing ----------------------------
//        int[][] boardArray ;
//        boardArray = new int[][]{{0, 0, 0, 0, 0, 2,0, 0, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {0, 0, 0, 0, 2, 0,0, 2, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {2, 0, 0, 0, 0, 0,0, 0, 0, 0},
//                                {0, 0, 0, 0, 0, 0,0, 0, 0, 0}};
//
//        setBoard(boardArray);
// end for testing -------------------------

        int[][] boardArray = new int[nRows][nCols];
        for (int i = 0; i < nRows - 1; i++) {
            for (int j = 0; j < nCols - 1; j++) {
                boardArray[i][j] = 0;
            }
        }

        setBoard(boardArray);
    }

    public int[][] getBoard() {
        return this.board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[] getMove(int player) throws EndOfGameException {


        int[] result = {2};
        boolean rightMove = false;

        while (!rightMove) {
            String input = Utils.getUserInput("Enter coordinates: ").trim();
            if (input.equals(this.endPassword)) {
                throw new EndOfGameException();
            }

            if (isProperInput(input)) {
                char rowChar = Character.toUpperCase(input.charAt(0));
                int rowNum = (int) rowChar - (int) 'A';
                int colNum = Integer.parseInt(input.substring(1)) - 1;
                if (this.board[rowNum][colNum] == 0) {
                    rightMove = true;
                    result = new int[]{rowNum, colNum};
//                    System.out.println("result x,y: " + result[0] + " " + result[1]);
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return result;
    }

    private boolean isProperInput(String input) {
        boolean properInput = false;

        if (input.length() == 2 || input.length() == 3) {
            char rowChar = Character.toUpperCase(input.charAt(0));
            String joinedLetters = new String(Arrays.copyOfRange(this.rowChars, 0, maxRowNumber));
            if (joinedLetters.contains(String.valueOf(rowChar))) {
                if (input.length() == 2) {
                    try {
                        int col = Integer.parseInt(String.valueOf(input.charAt(1)));
                        if (col >= 0 && col <= boardWidth) {
                            properInput = true;
                        }
                    } catch (Exception NumberFormatException) {
                        return properInput;
                    }
                } else {
                    try {
                        int col = Integer.parseInt(input.substring(1));
                        if (col >= 0 && col <= boardWidth) {
                            properInput = true;
                        }
                    } catch (Exception NumberFormatException) {
                        return properInput;
                    }
                }

            }
        }

        return properInput;
    }


    public int[] getTestAiMove(int player) {
        int[] coordinatesAI = {-1, -1};

        Movement movement = new Movement();

        int playerSignsNum = 0;

        for (int i = 0; i < maxRowNumber; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (board[i][j] == player) {
//if there is space for howMany marks , so movement.getXY != -1, -1
                    if (winCoordsExist(checkForFieldIsSpace(i, j, player, movement))) {
                        playerSignsNum = getPlayerSignsNum(movement.getX(), movement.getY(), player);
//                            System.out.println("PlayerSignsNum: " + playerSignsNum);
//                            System.out.println("For i, j: " + i +" " + j);
//                            System.out.println("From movement: PlayerSings counter: " + movement.getCounter() + ", x: " + movement.getX() + ", y: " + movement.getY());
//                            System.out.println("Stare Movement.getHowManyMarks: " + movement.getHowManyMarks());

                        if (playerSignsNum > movement.getHowManyMarks()) {
                            movement.setHowManyMarks(playerSignsNum);
                            movement.setXToPutYtoPut(movement.getX(), movement.getY());
//                                System.out.println("If playerSignsNum > mov.getHowManyMarks: X,Y to put: " + movement.getxToPut() +" " + movement.getyToPut());
//                                System.out.println("Movement.getHowManyMarks: " + movement.getHowManyMarks());
//                                System.out.println(System.lineSeparator());
                        }
//                        else {
//                            if (movement.getCounter() > playerSignsNum) {
////                                    movement.setHowManyMarks();
////                                    jeśli jest więcej signów ale z przerwami
//                                movement.setXToPutYtoPut(movement.getX(), movement.getY());
//                            }
//                        }

                    } else {
                        continue;
                    }
                }
            }
        }
        coordinatesAI = movement.getXtoPutYtoPut();

        return coordinatesAI;
    }

    private int getPlayerSignsNum(int i, int j, int player) {


        for (int l = 0; l < howMany - 1; l++) {
            int[] resultCoords = checkCoords(i, j, player, howMany - 1 - l);
            if (winCoordsExist(resultCoords)) {
                int result = howMany - 1 - l - 1;
//                System.out.println("from getPlayerSignsNum: " + (result));
                return howMany - 1 - l - 1; // -1 bo howMany-1-l to liczba znaków sprawdzana, przy założeniu ze ze znakiem  w i,j tyle będzie
            }

        }
        return 0;
    }

    private int[] checkForFieldIsSpace(int i, int j, int player, Movement movement) {
//        Movement movement = new Movement();

        checkInRow(i, j, player, movement);
        checkInCol(i, j, player, movement);
        checkInRightDownDiagonal(i, j, player, movement);

        int[] bestField = movement.getXY();


        return bestField;
    }

    private void checkInCol(int i, int j, int player, Movement movement) {

        int spaceCounter = 0;
        int signCounter = 0;

        int[] firstFreeField = {-1, -1};
        int[] betterFreeField = {-1, -1};
        int[] bestFreeField = {-1, -1};
        for (int k = -howMany + 1; k <= howMany - 1; k++) {
            if (i + k >= 0 && i + k < maxRowNumber) {
                if (board[i + k][j] == 0 || board[i + k][j] == player) {
                    spaceCounter++;
                    if (board[i + k][j] == player) {
                        signCounter++;
//                        checkColNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);
                    }
                        checkColNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);

//                    }
                    if (spaceCounter >= howMany && signCounter > movement.getCounter()) {
//                        checkRightDownDiagonalNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);
                        setMovementCounter(bestFreeField, bestFreeField, firstFreeField, signCounter, movement);
//                        System.out.println("Sprawdza space in col, x y to put: " + firstFreeField[0] + " " +firstFreeField[1]);

                        signCounter = 0;
                        spaceCounter = 0;
                        restoreBestBetterFirstFreeField(firstFreeField,betterFreeField, bestFreeField);
                    }
                } else {
                    signCounter = 0;
                    spaceCounter = 0;
                    restoreBestBetterFirstFreeField(firstFreeField,betterFreeField, bestFreeField);
                }
            }
        }
    }

    private void setMovementCounter(int[] bestFreeField, int[] betterFreeField, int[] firstFreeField, int signCounter, Movement movement) {
        if (winCoordsExist(bestFreeField)) {
            movement.setCounter(signCounter, bestFreeField[0], bestFreeField[1]);
        } else if (winCoordsExist(betterFreeField)) {
            movement.setCounter(signCounter, betterFreeField[0], betterFreeField[1]);
        } else {
            movement.setCounter(signCounter, firstFreeField[0], firstFreeField[1]);
        }
    }


    private void setCoords(int[] coords, int x, int y) {
        coords[0] = x;
        coords[1] = y;
    }

    private void restoreBestBetterFirstFreeField(int[] firstFreeField, int[] betterFreeField, int[] bestFreeField) {
        setCoords(firstFreeField, -1,-1);
        setCoords(betterFreeField, -1,-1);
        setCoords(bestFreeField, -1,-1);

    }

    private void checkInRow(int i, int j, int player, Movement movement){
        int spaceCounter=0;
        int signCounter = 0;

        int[] firstFreeField = {-1,-1};
        int[] betterFreeField = {-1, -1};
        int[] bestFreeField = {-1, -1};
        for (int k = -howMany+1; k <= howMany -1; k++){
            if (j+k >= 0 && j+k < boardWidth ){
                if (board[i][j +k] == 0 || board[i][j+k] == player) {
                    spaceCounter++;
                    if (board[i][j+k] == player) {
                        signCounter++;
//                        checkColNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);
                    }
                        checkRowNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);

//                    }
                    if (spaceCounter >= howMany && signCounter > movement.getCounter() ){
//                        checkRightDownDiagonalNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);
                        setMovementCounter(bestFreeField, betterFreeField, firstFreeField, signCounter, movement);
//                        System.out.println("Sprawdza space in row, x y to put: " + firstFreeField[0] + " " +firstFreeField[1]);
                        signCounter = 0;
                        spaceCounter = 0;
                        restoreBestBetterFirstFreeField(firstFreeField,betterFreeField, bestFreeField);
                    }
                }
                else {
                    signCounter = 0;
                    spaceCounter = 0;
                    restoreBestBetterFirstFreeField(firstFreeField,betterFreeField, bestFreeField);
                }
            }
        }
    }

    private void checkInRightDownDiagonal(int i,int j,int player,Movement movement){
        int spaceCounter=0;
        int signCounter = 0;

        int[] firstFreeField = {-1,-1};
        int[] betterFreeField = {-1, -1};
        int[] bestFreeField = {-1, -1};
        for (int k = -howMany+1; k <= howMany -1; k++){
            if (j+k >= 0 && j+k < boardWidth && i+k >=0 && i+k < maxRowNumber ){
                if (board[i+k][j +k] == 0 || board[i+k][j+k] == player) {
                    spaceCounter++;
                    if (board[i+k][j+k] == player) {
                        signCounter++;
//                        checkColNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);
                    }
                        checkRightDownDiagonalNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);

//                    }
                    if (spaceCounter >= howMany && signCounter > movement.getCounter() ){
//                        checkRightDownDiagonalNeighboursField(i, j, k, player, firstFreeField, betterFreeField, bestFreeField);
                        setMovementCounter(bestFreeField, betterFreeField, firstFreeField, signCounter, movement);
//                        System.out.println("Sprawdza space in row, x y to put: " + firstFreeField[0] + " " +firstFreeField[1]);
                        signCounter = 0;
                        spaceCounter = 0;
                        restoreBestBetterFirstFreeField(firstFreeField,betterFreeField, bestFreeField);
                    }
                }
                else {
                    signCounter = 0;
                    spaceCounter = 0;
                    restoreBestBetterFirstFreeField(firstFreeField,betterFreeField, bestFreeField);
                }
            }
        }

    }

    private void checkRightDownDiagonalNeighboursField(int i, int j, int k, int player, int[] firstFreeField, int[]betterFreeField, int[] bestFreeField){
        if (!winCoordsExist(bestFreeField) && i+k+1 >=0 && i+k+1 < maxRowNumber
                && i+k+2 >=0 && i+k+2 < maxRowNumber && j+k+1 >=0 && j+k+1 < boardWidth
                && j+k+2 >=0 && j+k+2 < boardWidth) {
            if (board[i + k + 1][j+k+1] == 0 && board[i + k + 2][j+k+2] == player) {
                setCoords(bestFreeField, i + k + 1, j+k+1);
                return;
            }
        }
        if (!winCoordsExist(bestFreeField) && i+k-1 >=0 && i+k-1 < maxRowNumber
                && i+k-2 >=0 && i+k-2 < maxRowNumber && j+k-1 >=0 && j+k-1 < boardWidth
                && j+k-2 >=0 && j+k-2 < boardWidth ){
            if (board[i+k-1][j+k-1] == 0 && board[i+k-2][j+k-2]==player){
                setCoords(bestFreeField, i+k-1, j+k-1);
                return;
            }
        }

        if (!winCoordsExist(betterFreeField) && i+k+1 >=0 && i+k+1 < maxRowNumber &&
                j+k+1 >=0 && j+k+1 < boardWidth){
            if (board[i+k+1][j+k+1] == 0){
                setCoords(betterFreeField,i-k+1, j+k+1);
                return;
            }
        }
        if (!winCoordsExist(betterFreeField) && i+k-1 >=0 && i+k-1 < maxRowNumber &&
                j+k-1 >=0 && j+k-1 < boardWidth){
            if (board[i+k-1][j+k-1] == 0){
                setCoords(betterFreeField, i+k-1,j+k-1);
                return;
            }
        }

        if (!winCoordsExist(firstFreeField)) {
            if (board[i+k][j+k] == 0) {
                setCoords(firstFreeField, i + k, j + k);
                return;
            }

        }
    }

    private void checkColNeighboursField(int i, int j, int k, int player, int[] firstFreeField, int[]betterFreeField, int[] bestFreeField){
        if (!winCoordsExist(bestFreeField) && i+k+1 >=0 && i+k+1 < maxRowNumber
                && i+k+2 >=0 && i+k+2 < maxRowNumber) {
            if (board[i + k + 1][j] == 0 && board[i + k + 2][j] == player) {
                setCoords(bestFreeField, i + k + 1, j);
                return;
            }
        }
        if (!winCoordsExist(bestFreeField) && i+k-1 >=0 && i+k-1 < maxRowNumber
                && i+k-2 >=0 && i+k-2 < maxRowNumber ){
            if (board[i+k-1][j] == 0 && board[i+k-2][j]==player){
                setCoords(bestFreeField, i+k-1, j);
                return;
            }
        }

        if (!winCoordsExist(betterFreeField) && i+k+1 >=0 && i+k+1 < maxRowNumber){
            if (board[i+k+1][j] == 0){
                setCoords(betterFreeField,i-k+1, j);
                return;
            }
        }
        if (!winCoordsExist(betterFreeField) && i+k-1 >=0 && i+k-1 < maxRowNumber){
            if (board[i+k-1][j] == 0){
                setCoords(betterFreeField, i+k-1,j);
                return;
            }
        }

            if (!winCoordsExist(firstFreeField)) {
                if (board[i+k][j]==0){
                setCoords(firstFreeField, i+k, j);
                }
            }
    }


    private void checkRowNeighboursField(int i, int j, int k, int player, int[] firstFreeField, int[]betterFreeField, int[] bestFreeField){
        if (!winCoordsExist(bestFreeField) && j+k+1 >=0 && j+k+1 < boardWidth
                && j+k+2 >=0 && j+k+2 < boardWidth) {
            if (board[i][j + k + 1] == 0 && board[i][j + k + 2] == player) {
                setCoords(bestFreeField, i, j + k + 1);
                return;
            }
        }
        if (!winCoordsExist(bestFreeField) && j+k-1 >=0 && j+k-1 < boardWidth
                && j+k-2 >=0 && j+k-2 < boardWidth ){
            if (board[i][j+k-1] == 0 && board[i][j+k-2]==player){
                setCoords(bestFreeField, i, j+k-1);
                return;
            }
        }

        if (!winCoordsExist(betterFreeField) && j+k+1 >=0 && j+k+1 < boardWidth){
            if (board[i][j+k+1] == 0){
                setCoords(betterFreeField,i,j-k+1);
                return;
            }
        }
        if (!winCoordsExist(betterFreeField) && j+k-1 >=0 && j+k-1 < boardWidth){
            if (board[i][j+k-1] == 0){
                setCoords(betterFreeField, i, j+k-1);
                return;
            }
        }

        if (!winCoordsExist(firstFreeField)) {
            if (board[i][j+k]==0) {
                setCoords(firstFreeField, i, j + k);
            }
        }



    }

    public int[] getAiMove(int player) {



        int[] coordinatesAI = easyWin(player);
        if (winCoordsExist(coordinatesAI)){
            return coordinatesAI;
        }

        coordinatesAI = preventEasyWin(player);
        if (winCoordsExist(coordinatesAI)){
            return coordinatesAI;
        }

        coordinatesAI = getTestAiMove(player);
        if (winCoordsExist(coordinatesAI)){
            return coordinatesAI;
        }


        coordinatesAI = getRandomAIMove();

        return coordinatesAI;
    }



    private int[] getRandomAIMove(){
        boolean isRigthMove = false;
        int[] coordAI = new int[0];

        while (!isRigthMove) {
            int x = Utils.getRandomNumber(maxRowNumber);
            int y = Utils.getRandomNumber(boardWidth);
            if (board[x][y] == 0) {
                isRigthMove = true;
                coordAI = new int[]{x, y};
            }
        }
        return coordAI;
    }


    public void mark(int player, int row, int col) {
        this.board[row][col] = player;

    }

    public boolean hasWon(int player, int howMany) {
        int inRowCounter = 0;
        int inColCounter = 0;
        int diagonalCounter = 0;
//  Check for win horizontally
        for(int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            inRowCounter = 0;
            for(int colIndex = 0; colIndex < board[0].length; colIndex++) {
                if(board[rowIndex][colIndex] == player) {
                    inRowCounter += 1;
                    if(inRowCounter >= howMany) {
                        return true;
//  Check for win diagonally
                    }else{
//  to right
                        diagonalCounter = 0;
                        for(int diagonalCell = 0; diagonalCell < board.length - rowIndex && diagonalCell < board[0].length - colIndex; diagonalCell++) {
                            if(board[rowIndex + diagonalCell][colIndex + diagonalCell] == player) {
                                diagonalCounter += 1;
                                if(diagonalCounter >= howMany) {
                                    return true;
                                }
                            }else{
                                diagonalCounter = 0;
                            }
                        }
//  to left
                        diagonalCounter = 0;
                        for(int diagonalCell = 0; diagonalCell <= colIndex && diagonalCell < (board.length - rowIndex); diagonalCell++) {
                            if(board[rowIndex + diagonalCell][colIndex - diagonalCell] == player) {
                                diagonalCounter += 1;
                                if(diagonalCounter >= howMany) {
                                    return true;
                                }
                            }else{
                                diagonalCounter = 0;
                            }
                        }
                    }
//  Check for win vertically
                }else{
                    inRowCounter = 0;
                }for(int rowInColumn = 0; rowInColumn < board.length; rowInColumn++) {
                    if(board[rowInColumn][colIndex] == player) {
                        inColCounter += 1;
                        if(inColCounter >= howMany) {
                            return true;
                        }
                    }else{
                        inColCounter = 0;
                    }
                }
            }
        }
        return false;
    }

    public boolean isFull() {
        for(int[] row: board) {
            for(int col: row) {
                if(col == 0) {
                    return false;
                }
            }
        }
        return true;

    }

    public void printBoard() {
        char[]  letters = {'A','B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                            'P', 'Q','R','S', 'T', 'U', 'W', 'X', 'Y', 'Z'};
        this.rowChars = letters;
        String header = "header";

// printing header row
        System.out.print("  ");
        for (int i = 1; i <= this.boardWidth; i++){
            this.printRow(i, header);
        }
        System.out.print(System.lineSeparator());

// printing rows
        for (int j =0; j < this.board.length; j++){
            System.out.print(ConsoleColors.RED_BOLD + letters[j] + ConsoleColors.RESET + " ");

            for (int l = 0; l < this.board[j].length; l++){
                this.printRow(board[j][l], null );
            }
            System.out.print(System.lineSeparator());
        }
    }

    // prints rows with a proper number of " "
    private void printRow(int field, String header){
        for (int k=0; k <= String.valueOf(this.boardWidth).length() - String.valueOf(field).length(); k++){
            System.out.print(" ");
        }
        if (header != null){
            System.out.print(ConsoleColors.RED_BOLD + field + ConsoleColors.RESET);
        } else {
            if (field == 0){
                System.out.print( "." );
            } else {
                    User userForField = getUserForUserNumber(field);
//                    System.out.print(userForField.getUserSign() + ConsoleColors.RESET);
//  narazie zakomentowane bo daje null -odp. Tutaj  powinien być getter, bo on weźmie kolor, jaki bedzie aktualnie przypisany do playera
            System.out.print(userForField.getUserColor() + userForField.getUserSign() + ConsoleColors.RESET);

            }
        }
    }

    public void printResult(int player) {
    }

    public void enableAi(int player) {
//        int[] easyWinCoords = easyWin(player);
//        if (!Arrays.equals(new int[]{-1, -1}, easyWinCoords)){
//            return easyWinCoords;
//
    }


    public int[] easyWin(int player){
        int[] easyWinCoords = {-1, -1};

        for (int i = 0; i < maxRowNumber; i++){
            for (int j=0; j < boardWidth; j++) {
                int[] resultCoords = checkCoords(i, j, player, howMany);
                if (winCoordsExist(resultCoords)){
                    easyWinCoords = resultCoords;
                    return easyWinCoords;
                }

            }
        }
        return easyWinCoords;
    }

// bo to sprawdza dla całego boarda, a nie dla tego kawalka ze zmienioną daną
    private int[] checkCoords(int i, int j, int player, int howManySigns){
        int[] easyWinCoords = {-1, -1};

        if (board[i][j] == 0) {
            board[i][j] = player;
            if (hasWon(player, howManySigns)){
                easyWinCoords[0] = i;
                easyWinCoords[1] = j;

            }
            board[i][j] = 0;
        }

        return easyWinCoords;
    }


    private boolean winCoordsExist(int[] coords){
        return coords[0] != -1 && coords[1] != -1;
    }

    public int[] preventEasyWin(int player){

        int oponentPlayer = getOpponentNumber(player);
        int[] preventEasyWinCoords = {-1, -1};

        preventEasyWinCoords = easyWin(oponentPlayer);

        return preventEasyWinCoords;
    }


    private int getOpponentNumber(int playerNumber){
        int oponentNumber = 0;
        if (playerNumber == 1){
            oponentNumber = 2;
        } else {
            oponentNumber = 1;
        }
        return oponentNumber;
    }

    private User switchPlayers(User currentPlayer){
        if (currentPlayer == player2 ){
            currentPlayer = player1;
        }
        else {
            currentPlayer = player2;
        }
        return currentPlayer;
    }

    public void play(int howMany) throws EndOfGameException, IOException {
        // tutaj logika gry, i musimy miec w klasie Game dostep do graczy, boardu

        this.howMany = howMany;
        User player = player2;
        boolean isEnd = false;

        while (!isEnd){
            Utils.clearScreen();

            this.printBoard();

            player = switchPlayers(player);
            int[] markedFields = {2};

            if (player.getUserMode().equals("AI")) {
                markedFields = this.getAiMove(player.getUserNumber());

                Utils.sleepTime(1000);

            }else {
                markedFields = this.getMove(player.getUserNumber());
            }
            mark(player.getUserNumber(),markedFields[0], markedFields[1]);
//            this.printBoard();


            if (hasWon(player.getUserNumber(),howMany )){
                Utils.clearScreen();
                this.printBoard();

//                for (int[] row : board){
//                for (int i : row) {
//                    System.out.print(i);
//                }
//                System.out.print(System.lineSeparator());
//            }
                String whoWon;
                if (player.getUserNumber() == 1) {
                    whoWon = Utils.readFromFile("1_won.txt");
                } else {
                    whoWon = Utils.readFromFile("2_won.txt");
                }
                System.out.println(ConsoleColors.RED_BOLD + whoWon + ConsoleColors.RESET);
                isEnd = true;
            }else if(isFull()) {
                System.out.println("Its a tie!");
                isEnd = true;
            }

        }

    }
}
