package com.company;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class Gui extends JFrame {
    private JFrame mainFrame;
    private JPanel middlePanel;
    private JTextArea editor;
    private JTable tokensTable;
    private JTextArea console;
    private DefaultTableModel tableModel;

    private static final String FRAME_TITLE = "LEXICAL ANALYZER ";
    private static final int FRAME_DIMENSION = 500;
    private static final String FILE_MENU_LABEL = "File";
    private static final String RUN_MENU_LABEL = "Run";
    private static final String[] COLUMN_NAMES = {"Line", "Token", "String or word"};
    private static final String EDITOR_PANEL_TITLE = "Source Code";
    private static final String TOKEN_TABLE_TITLE = "Token Table";
    private static final String CONSOLE_PANEL_TITLE = "Console";
    private static final String ERROR = "ERROR";

    public Gui() {
        createGui();
    }

    private void createGui() {
        mainFrame = new JFrame(FRAME_TITLE);
        mainFrame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        createMenu();
        createMiddlePanel();
        createConsole();
        mainFrame.setLayout(new GridLayout(2, 1)); // 2 rows 1 column
        mainFrame.setVisible(true);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(FILE_MENU_LABEL);
        Action runAction = new AbstractAction(RUN_MENU_LABEL) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Lexer lexer = new Lexer(editor.getText());
                lexer.run();
                Vector<Token> tokens = lexer.getTokens();
                writeLexicalAnalysisResults(tokens);
                writeConsole(tokens);
            }
        };
        JToggleButton runMenu = new JToggleButton(runAction);
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        mainFrame.add(menuBar);
        mainFrame.setJMenuBar(menuBar);
    }

    private void createMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 2));
        createEditor();
        createTokensTable();
        mainFrame.add(middlePanel);
    }

    private void createEditor() {
        editor = new JTextArea();
        editor.setLineWrap(true);
        JScrollPane editorScrollPane = new JScrollPane(editor);
        editorScrollPane.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), EDITOR_PANEL_TITLE));
        middlePanel.add(editorScrollPane);
    }

    private void createTokensTable() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
        tokensTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(tokensTable);
        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), TOKEN_TABLE_TITLE));
        middlePanel.add(tableScrollPane);
    }

    private void writeLexicalAnalysisResults(Vector<Token> tokens) {
        writeTokenTable(tokens);
        writeConsole(tokens);
    }

    private void writeTokenTable(Vector<Token> tokens) {
        Object[] tokenData;
        clearTokenTable(tableModel);
        for (Token token : tokens) {
            tokenData = new Object[]{token.getLineNumber(), token.getToken(), token.getWord()};
            tableModel.addRow(tokenData);
        }
    }

    private void clearTokenTable(DefaultTableModel tableModel) {
        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
    }

    private void createConsole() {
        console = new JTextArea();
        console.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), CONSOLE_PANEL_TITLE));
        console.setLineWrap(true);
        console.setBackground(Color.gray);
        mainFrame.add(console);
    }

    private void writeConsole(Vector<Token> tokens) {
        int numberOfStrings = tokens.size();
        if (numberOfStrings > 0) {
            int numberOfLines = tokens.lastElement().getLineNumber();
            int numberOfErrors = getNumberOfErrors(tokens);
            String plural;
            if (numberOfErrors == 1) {
                plural = " does";
            } else {
                plural = "s do";
            }
            console.setText(numberOfStrings + " strings found in " + numberOfLines + " lines.\n" +
                    numberOfErrors + " string" + plural + " not match any rule");
        } else {
            console.setText("No input in editor");
        }
    }

    private int getNumberOfErrors(Vector<Token> tokens) {
        int numberOfErrors = 0;
        for (Token token : tokens) {
            if (token.getToken().equals(ERROR)) {
                numberOfErrors++;
            }
        }
        return numberOfErrors;
    }

    public static void main(String[] args) {
        new Gui();
    }
}
