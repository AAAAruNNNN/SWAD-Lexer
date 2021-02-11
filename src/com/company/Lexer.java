package com.company;

import java.util.Arrays;
import java.util.Vector;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Performs lexical analysis on the input text and outputs the translated text
 * on console.
 *
 * @author Arun Kumar Kumarasamy (ASU ID: 1219494367)
 * @author Chandrasekhara Bharathi Narasimhan (ASU ID: 1217504873)
 */
public class Lexer {
    private static final String HEXADECIMAL_PATTERN = "0[xX][0-9a-fA-F]+"; //"(?:0[xX])?[0-9a-fA-F]+";///(0x)?[0-9a-f]+/i
    private static final String INT_PATTERN = "\\d+";
    private static final String FLOAT_PATTERN = "[+-]?((\\d+\\.?\\d*)|(\\.\\d+))";
    private static final String OCTAL_PATTERN = "0[1-7][0-7]*";// "[0-7]$"
    private static final String BINARY_PATTERN = "0[bB][0-1]+";// "(?:0[bB])?[0-1]+";
    private static final String STRING_PATTERN = "\"[0-9a-zA-Z]+\"";
    private static final String CHARACTER_PATTERN = "\'.\'";
    private static final String IDENTIFIER_PATTERN = "[a-zA-Z][_a-zA-Z0-9]*";// "(?:)?[_][a-zA-Z]+";
    private static final String[] DELIMITERS = { "(", ")", "{", "}", ";", ",", "." };
    private static final String[] KEYWORDS = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile",
            "while" };
    private static final String[] OPERATORS = { "+", "-", "", "/", "%", "++", "--", "=", "+=", "-=", "=", "/=", "%=",
            "&=", "|=", "^=", ">>=", "<<=", "==", "!=", ">", "<", ">=", "<=", "&&", "||", "!", "&", "|" };
    private Vector<Token> tokens;
    private String inputText;

    /**
     * Lexer class constructor
     */
    public Lexer(String inputText) {
        this.inputText = inputText;
        tokens = new Vector<Token>();
    }

    public void run() {
        splitLines(inputText);
    }

    private void splitLines(String editorText) {
        Scanner scanner = new Scanner(editorText);
        Vector<Line> lines = new Vector<>();
        int lineNumber = 1;
        while (scanner.hasNextLine()) {
            String lineContent = scanner.nextLine();
            lines.add(new Line(lineContent, lineNumber++));
        }
        splitWords(lines);
        scanner.close();
    }

    public void splitWords(Vector<Line> lines) {
        for(Line line: lines) {
            int startingIndex = 0;
            String lineContent = line.lineContent;
            for(int currentIndex = 0; currentIndex < line.lineContent.length(); currentIndex++) {
                if (String.valueOf(lineContent.charAt(currentIndex)).equals(" ")
                        || String.valueOf(lineContent.charAt(startingIndex)).equals(" ")
                        || Arrays.asList(DELIMITERS).contains(String.valueOf(lineContent.charAt(currentIndex)))
                        || Arrays.asList(OPERATORS).contains(String.valueOf(lineContent.charAt(currentIndex)))
                        || Arrays.asList(DELIMITERS).contains(String.valueOf(lineContent.charAt(startingIndex)))
                        || Arrays.asList(OPERATORS).contains(String.valueOf(lineContent.charAt(startingIndex)))) {
                    String currentWord = lineContent.substring(startingIndex, currentIndex);
                    if (!currentWord.equals(" ") && !currentWord.equals("")) {
                        createToken(currentWord, line.lineNumber);
                    }
                    startingIndex = currentIndex;
                }
            }
            createToken(lineContent.substring(startingIndex, line.lineContent.length()), line.lineNumber);
        }
    }

    private void createToken(String word, int lineNumber) {
        if (word.equals(" "))
            return;
        Token token = new Token(word, checkTokenType(word), lineNumber);
        addTokens(token);
    }

    private void addTokens(Token currentToken) {
        if (currentToken.getToken().equals("OPERATOR")) {
            if (tokens.size() > 0) {
                Token lastToken = tokens.lastElement();
                if (lastToken.getToken().equals("OPERATOR") && lastToken.getLine() == currentToken.getLine()
                        && lastToken.getWord().length() < 2) {
                    lastToken.setWord(lastToken.getWord() + currentToken.getWord());
                    return;
                }
            }
        }
        tokens.add(currentToken);
    }

    private String checkTokenType(String word) {
        if (word.equals(""))
            return "";
        if (isKeyWord(word))
            return "KEYWORD";
        if (isOperator(word))
            return "OPERATOR";
        if (isDelimiter(word))
            return "DELIMITER";
        if (isBinary(word))
            return "BINARY";
        if (isOctal(word))
            return "OCTAL";
        if (isInteger(word))
            return "INTEGER";
        if (isFloat(word))
            return "FLOAT";
        if (isHexadecimal(word))
            return "HEXADECIMAL";
        if (isString(word))
            return "STRING";
        if (isCharacter(word))
            return "CHARACTER";
        if (isIdentifier(word))
            return "IDENTIFIER";
        return "ERROR";
    }

    private boolean isKeyWord(String word) {
        if (Arrays.asList(KEYWORDS).contains(word))
            return true;
        return false;
    }

    private boolean isDelimiter(String word) {
        if (Arrays.asList(DELIMITERS).contains(word))
            return true;
        return false;
    }

    private boolean isOperator(String word) {
        if (Arrays.asList(OPERATORS).contains(word))
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

    private boolean isIdentifier(String word) {
        return word.matches(IDENTIFIER_PATTERN);
    }

    public Vector<Token> getTokens() {
        return tokens;
    }

    private class Line{
        String lineContent;
        int lineNumber;

        public Line(String lineContent, int lineNumber) {
            this.lineContent = lineContent;
            this.lineNumber = lineNumber;
        }
    }

}