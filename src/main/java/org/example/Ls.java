package org.example;

public class Ls {

    private char letter; //Первая буква фамилии
    private int startLS; //номер первой страницы ЛС
    private String name; //Имя
    private int endLS; //номер последней страницы ЛС


    Ls(char letter, int startLS, String name){
        this.letter = letter;
        this.startLS = startLS;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public char getLetter() {
        return letter;
    }

    public int getStartLS() {
        return startLS;
    }

    public void setEndLS(int endLS){
        this.endLS = endLS;
    }

    public int getEndLS(){
        return endLS;
    }

}


