package com.codecool.fiveinarow;

public class Movement {


    private int counter;
    private int x;
    private int y;
    private int howManyMarks;
    private int xToPut;
    private int yToPut;


    public Movement(){
        this.counter = -1;
        this.x = -1;
        this.y = -1;
        this.howManyMarks= -1;
        this.xToPut = -1;
        this.yToPut = -1;
   }

    public void setX(int x) {
        this.x = x;
    }

    public void setXToPutYtoPut(int x, int y) {
        this.xToPut = x;
        this.yToPut = y;
    }

    public void setYToPut(int y) {
        this.yToPut = y;
    }

    public void setHowManyMarks(int num){
        this.howManyMarks = num;
    }


    public void setCounter(int newCounter, int newX, int newY ) {
        if (this.counter < newCounter && newX != -1 && newY != -1){
            this.counter = newCounter;
            this.x = newX;
            this.y = newY;
        }

    }

    public int getCounter() {
        return counter;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getXY(){
        return new int[] {this.x, this.y};
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHowManyMarks(){
        return this.howManyMarks;
    }

    public int[] getXtoPutYtoPut(){
        return new int[] {this.xToPut, this.yToPut};
    }

    public int getxToPut(){
        return this.xToPut;
    }

    public int getyToPut(){
        return this.yToPut;
    }

}
