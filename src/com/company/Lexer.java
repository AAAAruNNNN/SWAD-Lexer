package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_SPACE = " ";
    private static final String KEYWORD = "KEYWORD";
    private static final String OPERATOR = "OPERATOR";
    private static final String DELIMITER = "DELIMITER";
    private static final String INTEGER = "INTEGER";
    private static final String BINARY = "BINARY";
    private static final String OCTAL = "OCTAL";
    private static final String FLOAT = "FLOAT";
    private static final String HEXADECIMAL = "HEXADECIMAL";
    private static final String STRING = "STRING";
    private static final String CHARACTER = "CHARACTER";
    private static final String IDENTIFIER = "IDENTIFIER";
    private static final String ERROR = "ERROR";
    private static final String HEXADECIMAL_PATTERN = "0[xX][0-9a-fA-F]+";
    private static final String INT_PATTERN = "\\d+";
    private static final String FLOAT_PATTERN = "[+-]?((\\d+\\.?\\d*)|(\\.\\d+))";
    private static final String OCTAL_PATTERN = "0[1-7][0-7]*";
    private static final String BINARY_PATTERN = "0[bB][0-1]+";
    private static final String STRING_PATTERN = "\"[0-9a-zA-Z]*\"";
    private static final String CHARACTER_PATTERN = "\'.\'";
    private static final String IDENTIFIER_PATTERN = "[a-zA-Z][_a-zA-Z0-9]*";
    private static final String[] DELIMITERS = { "(", ")", "{", "}", ";", ",", "." };
    private static final String[] KEYWORDS = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile",
            "while" };
    private static final String[] OPERATORS = { "+", "-", "*", "/", "%", "++", "--", "=", "+=", "-=", "=", "/=", "%=",
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
        Vector<IndexedString> linesFromInput = getLinesFromInput(inputText);
        List<IndexedString> wordList = splitWords(linesFromInput);
        getTokenFromWords(wordList);
    }

    private Vector<IndexedString> getLinesFromInput(String inputText) {
        Scanner scanner = new Scanner(inputText);
        Vector<IndexedString> lines = new Vector<>();
        int lineNumber = 1;
        while (scanner.hasNextLine()) {
            String content = scanner.nextLine();
            if(content.length()>0) {
                lines.add(new IndexedString(content, lineNumber));
            }
            lineNumber++;
        }
        scanner.close();
        return lines;
    }

    private List<IndexedString> splitWords(Vector<IndexedString> lines) {
        List<IndexedString> wordList = new ArrayList<>();
        for(IndexedString line : lines) {
            int startingIndex = 0;
            String lineContent = line.content;
            for(int currentIndex = 0; currentIndex < line.content.length(); currentIndex++) {
                if (substringFound(lineContent, currentIndex, startingIndex)) {
                    String currentWord = lineContent.substring(startingIndex, currentIndex);
                    if (!currentWord.equals(EMPTY_SPACE) && !currentWord.equals(EMPTY_STRING)) {
                        wordList.add(new IndexedString(currentWord, line.lineNumber));
                    }
                    startingIndex = currentIndex;
                }
            }
            wordList.add(new IndexedString(lineContent.substring(startingIndex, line.content.length()), line.lineNumber));
        }
        return wordList;
    }

    private boolean substringFound(String lineContent, int currentIndex, int startingIndex) {
        return (String.valueOf(lineContent.charAt(currentIndex)).equals(EMPTY_SPACE)
                || String.valueOf(lineContent.charAt(startingIndex)).equals(EMPTY_SPACE)
                || Arrays.asList(DELIMITERS).contains(String.valueOf(lineContent.charAt(currentIndex)))
                || Arrays.asList(OPERATORS).contains(String.valueOf(lineContent.charAt(currentIndex)))
                || Arrays.asList(DELIMITERS).contains(String.valueOf(lineContent.charAt(startingIndex)))
                || Arrays.asList(OPERATORS).contains(String.valueOf(lineContent.charAt(startingIndex))));
    }

    private void getTokenFromWords(List<IndexedString> wordList) {
        for(IndexedString word: wordList) {
            if (!word.content.equals(EMPTY_SPACE)) {
                Token token = new Token(word.content, getTokenType(word.content), word.lineNumber);
                addToken(token);
            }
        }
    }

    private void addToken(Token currentToken) {
        if (currentToken.getToken().equals(OPERATOR)) {
            if (tokens.size() > 0) {
                Token lastToken = tokens.lastElement();
                if (lastToken.getToken().equals(OPERATOR) && lastToken.getLineNumber() == currentToken.getLineNumber()
                        && lastToken.getWord().length() < 2) {
                    lastToken.setWord(lastToken.getWord() + currentToken.getWord());
                    return;
                }
            }
        }
        tokens.add(currentToken);
    }

    private String getTokenType(String word) {
        if (word.equals(EMPTY_STRING))
            return EMPTY_STRING;
        if (isKeyWord(word))
            return KEYWORD;
        if (isOperator(word))
            return OPERATOR;
        if (isDelimiter(word))
            return DELIMITER;
        if (isBinary(word))
            return BINARY;
        if (isOctal(word))
            return OCTAL;
        if (isInteger(word))
            return INTEGER;
        if (isFloat(word))
            return FLOAT;
        if (isHexadecimal(word))
            return HEXADECIMAL;
        if (isString(word))
            return STRING;
        if (isCharacter(word))
            return CHARACTER;
        if (isIdentifier(word))
            return IDENTIFIER;
        return ERROR;
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

    private class IndexedString {
        String content;
        int lineNumber;

        public IndexedString(String content, int lineNumber) {
            this.content = content;
            this.lineNumber = lineNumber;
        }
    }
}