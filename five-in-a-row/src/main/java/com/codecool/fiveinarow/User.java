package com.codecool.fiveinarow;
import java.util.*;

public class User {

    private int userNumber;
// userMode:  AI or Human
    private String userMode;
    private String userSign;
    private String userName;
    private String userColor;


    public User(int userNumber){
        this.userNumber = userNumber;
        System.out.println("Hello new player! ");
        setUserName();
        setUserSign();
        setUSerMode();

//for testing  -------------
//        testSetUserSign();
//        testSetUserMode();
// end for testing -------------
        setUserColor();

    }

// for testing purpose --------------------
    private void testSetUserMode(){
        if (userNumber == 1){
            this.userMode = "Human";
        } else if (userNumber == 2){
            this.userMode = "AI";
        }
    }


    private void testSetUserSign(){
        if (userNumber == 1){
            this.userSign = "H";
        } else if (userNumber == 2){
            this.userSign = "A";
        }
    }
// end for testing -------------------------------

    private void setUserName() {
        boolean setting = true;
        while (setting) {
            String result = Utils.getUserInput("What is your name? ");
            if(Utils.getNames().contains(result)) {
                System.out.println(result + " already taken");
            }else {
                this.userName = result;
                Utils.setNames(result);
                setting = false;
            }
        }
    }


    public String getUserMode() {
        return userMode;
    }

    private void setUserColor(){
        if (userNumber == 1 && userMode.equals("Human")){
            this.userColor = ConsoleColors.GREEN_BOLD;
        }
        else if ((userNumber == 2) && userMode.equals("Human")){
            this.userColor = ConsoleColors.BLUE_BRIGHT;
        }
        else if (userNumber == 1 && userMode.equals("AI")){
            this.userColor = ConsoleColors.YELLOW_BOLD;
        }
        else if (userNumber ==2 && userMode.equals("AI")){
            this.userColor = ConsoleColors.CYAN_BOLD;
        }
    }


    private void setUserSign() {
        boolean setting = true;
        while (setting) {
            String result = Utils.getUserInput(this.userName + ", what is your sign? ");
            if(result.length() != 1 || result.contains(" ")) {
                System.out.println("Sign must have only one proper character");
            }else if(Utils.getSigns().contains(result)) {
                System.out.println(result + " already taken");
            }else {
                this.userSign = result;
                Utils.setSigns(result);
                setting = false;
            }
        }
    }

    private void setUSerMode() {
        boolean setting = true;
        while (setting) {
            String result = Utils.getUserInput(this.userName + ", are you human or AI? ");
            if (result.toLowerCase().equals("human") || result.toLowerCase().equals("h")) {
                this.userMode = "Human";
                setting = false;
            }else if (result.toLowerCase().equals("ai")) {
                this.userMode = "AI";
                setting = false;
            }else {
                System.out.println("Choose Human or AI ");
            }
        }
    }


    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getUserNumber(){
        return userNumber;
    }

    public String getUserSign() {
        return userSign;
    }

    public String getUserColor(){
        return userColor;
    }
}


