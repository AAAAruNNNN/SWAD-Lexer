package com.company;

public class Token {

    private String word;
    private String token;
    private int lineNumber;

    public Token(String word, String token, int lineNumber){
        this.word = word;
        this.token = token;
        this.lineNumber = lineNumber;
    }

    public String getWord(){
        return word;
    }

    public void setWord(String word){
        this.word = word;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public int getLineNumber(){
        return lineNumber;
    }

    public void setLineNumber(int lineNumber){
        this.lineNumber = lineNumber;
    }

}
