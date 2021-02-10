package com.company;

import java.util.Arrays;
import java.util.Vector;
import java.util.Scanner;


/**
 * Performs lexical analysis on the input text and outputs the translated text
 * on console.
 *
 * @author Arun Kumar Kumarasamy (ASU ID: 1219494367)
 * @author Chandrasekhara Bharathi Narasimhan (ASU ID: 1217504873)
 */
public class Lexer {
    private static final String HEXADECIMAL_PATTERN = "(?:0[xX])?[0-9a-fA-F]+";///(0x)?[0-9a-f]+/i
    private static final String INT_PATTERN = "\\d+";
    private static final String FLOAT_PATTERN = "[+-]?((\\d+\\.?\\d*)|(\\.\\d+))" ;
    private static final String OCTAL_PATTERN = "^0[1-7][0-7]*$";//"[0-7]*$"
    private static final String BINARY_PATTERN = "(?:0[bB])?[0-1]+";
    private static final String STRING_PATTERN = "\"[0-9a-zA-Z]+\"";
    private static final String CHARACTER_PATTERN = "\'.\'";

    final String[] delimiters = {"(", ")", "{", "}", ";"};
    final String[] keyWords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
            "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"};
    final String[] operators = { "+", "-", "*", "/", "%", "++", "--", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=",
            "^=", ">>=", "<<=", "==", "!=", ">", "<", ">=", "<=", "&&", "||", "!" };

    static Vector<Token> tokens;
    String inputText;


    /**
     * Lexer class constructor
     */
    Lexer(String inputText) {
        this.inputText = inputText;
        tokens = new Vector<Token>();
    }

    public void run() {
        splitLines(inputText);
//        printConsole();
    }

    public String splitLines(String editorText){
        Scanner scanner = new Scanner(editorText);
        String text = "";
        int lineNumber = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            splitWords(line, lineNumber++);
            text = text + " " + line.trim();
        }
        scanner.close();
        return text.trim();
    }

    private void splitWords(String text, int lineNumber) {
        String[] wordsArray = text.split(" ");
//        System.out.println(Arrays.toString(wordsArray));
        for (String s : wordsArray)
            createToken(s, lineNumber);
    }

    public void createToken(String word, int lineNumber) {
        int start = 0, end = word.length();
        String tokenType = "";
        Token token;
        for (int i = 0; i < word.length(); i++) {
            for(String s: delimiters){
                if(s.equals(String.valueOf(word.charAt(i)))){
                    tokenType = checkTokenType(word.substring(start, i));
                    if(tokenType.equals(""))
                        continue;
                    token = new Token(word.substring(start, i), tokenType, lineNumber);
                    tokens.add(token);
                    start = i;
                }
            }
        }
        tokenType = checkTokenType(word.substring(start, end));
        token = new Token(word.substring(start, end), tokenType, lineNumber);
        tokens.add(token);
    }

    public String checkTokenType(String word){
        if(word.equals(""))
            return "";
        if(isKeyWord(word))
            return "KEYWORD";
        if(isOperator(word))
            return "OPERATOR";
        if(isDelimiter(word))
            return "DELIMITER";
        if(isBinary(word))
            return "BINARY";
        if(isOctal(word))
            return "OCTAL";
        if(isInteger(word))
            return "INTEGER";
        if(isFloat(word))
            return "FLOAT";
        if(isHexadecimal(word))
            return "HEXADECIMAL";
        if(isString(word))
            return "STRING";
        if(isCharacter(word))
            return "CHARACTER";
        if(isIdentifier(word))
            return "IDENTIFIER";
        return "ERROR";
    }

    public boolean isKeyWord(String word){
        if(Arrays.asList(keyWords).contains(word))
            return true;
        return false;
    }

    public boolean isDelimiter(String word){
        if(Arrays.asList(delimiters).contains(word))
            return true;
        return false;
    }

    public boolean isOperator(String word){
        if(Arrays.asList(operators).contains(word))
            return true;
        return false;
    }

    private boolean isHexadecimal(String word) {
        return word.matches(HEXADECIMAL_PATTERN);
    }

    private boolean isInteger(String word) {
        return word.matches(INT_PATTERN);
    }

    private boolean isFloat(String word) {
        return word.matches(FLOAT_PATTERN);
    }

    private boolean isOctal(String word) {
        return word.matches(OCTAL_PATTERN);
    }

    private boolean isBinary(String word) {
        return word.matches(BINARY_PATTERN);
    }

    private boolean isString(String word) {
        return word.matches(STRING_PATTERN);
    }

    private boolean isCharacter(String word) {
        return word.matches(CHARACTER_PATTERN);
    }

    public boolean isIdentifier(String word){
        if (!((word.charAt(0) >= 'a' && word.charAt(0) <= 'z')
                || (word.charAt(0)>= 'A' && word.charAt(1) <= 'Z')
                || word.charAt(0) == '_'))
            return false;

        for (int i = 1; i < word.length(); i++)
        {
            if (!((word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
                    || (word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    || (word.charAt(i) >= '0' && word.charAt(i) <= '9')
                    || word.charAt(i) == '_'))
                return false;
        }

        return true;
    }

    public Vector<Token> getTokens(){
        return tokens;
    }

//    public void printConsole(){
//        for(Token token: tokens){
//            System.out.println("-----"+token.getLine() + " " + token.getToken()+" " + token.getWord()+"-----");
//        }
//    }

}




