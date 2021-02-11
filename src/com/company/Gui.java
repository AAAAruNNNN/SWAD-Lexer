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
    private JTextArea editor;
    private JTable tokensTable;
    private JTextArea console;
    private DefaultTableModel tableModel;

    private static final String FRAMETITLE = "LEXICAL ANALYZER ";
    private static final int FRAMEWIDTH = 500;
    private static final int FRAMEHEIGHT = 500;
    private static final String FILEMENULABEL = "File";
    private static final String RUNMENULABEL = "Run";
    private static final String[] columnNames = { "Line", "Token", "String or word" };

    public Gui() {
        createGui();
    }

    private void createGui() {
        mainFrame = new JFrame(FRAMETITLE);
        mainFrame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        createMenu();
        createMiddlePanel();
        createConsole();
        mainFrame.setLayout(new GridLayout(2, 1)); // 2 rows 1 column
        mainFrame.setVisible(true);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(FILEMENULABEL);
        Action runAction = new AbstractAction(RUNMENULABEL) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = editor.getText();
                Lexer lexer = new Lexer(s);
                lexer.run();
                Vector<Token> tokens = lexer.getTokens();
                writeLexicalAnalysis(tokens);
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
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 2));
        createEditor();
        JScrollPane editorScrollPane = new JScrollPane(editor);
        editorScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Source Code"));
        middlePanel.add(editorScrollPane);
        createTokensTable();
        JScrollPane tableScrollPane = new JScrollPane(tokensTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Token Table"));
        middlePanel.add(tableScrollPane);
        mainFrame.add(middlePanel);
    }

    private void createEditor() {
        editor = new JTextArea();
        editor.setLineWrap(true);
    }

    private void createTokensTable() {
        tableModel = new DefaultTableModel(columnNames, 0);
        tokensTable = new JTable(tableModel);
    }

    private void writeLexicalAnalysis(Vector<Token> tokens) {
        writeTokenTable(tokens);
        writeConsole(tokens);
    }

    private void writeTokenTable(Vector<Token> tokens) {
        Object[] tokenData;
        clearTokenTable(tableModel);
        for(Token token: tokens) {
            tokenData = new Object[]{token.getLine(), token.getToken(), token.getWord()};
            tableModel.addRow(tokenData);
        }
    }

    private void clearTokenTable(DefaultTableModel tableModel) {
        if(tableModel.getRowCount()>0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
    }

    private void createConsole() {
        console = new JTextArea();
        console.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Console"));
        console.setLineWrap(true);
        console.setBackground(Color.gray);
        mainFrame.add(console);
    }

    private void writeConsole(Vector<Token> tokens) {
        int numberOfStrings = tokens.size();
        if(numberOfStrings>0){
            int numberOfLines = tokens.lastElement().getLine();
            int numberOfErrors = getNumberOfErrors(tokens);
            String plural;
            if(numberOfErrors==1) {
                plural = " does";
            } else {
                plural = "s do";
            }
            console.setText(numberOfStrings+" strings found in "+numberOfLines+" lines.\n"+
                    numberOfErrors+" string"+plural+" not match any rule");
        } else {
            console.setText("No input in editor");
        }

    }

    private int getNumberOfErrors(Vector<Token> tokens) {
        int numberOfErrors = 0;
        for(Token token : tokens) {
            if(token.getToken().equals("ERROR")) {
                numberOfErrors++;
            }
        }
        return numberOfErrors;
    }

    public static void main(String[] args) {
        new Gui();
    }
}
