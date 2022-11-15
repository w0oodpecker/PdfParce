package org.example;

public class LsGrouped {
    private char letter;
    private int startPos;
    private int endPos;


    LsGrouped(char letter, int startPos, int endPos){
        this.letter = letter;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public char getLetter() {
        return letter;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }
}
