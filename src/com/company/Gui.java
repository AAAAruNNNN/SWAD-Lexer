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
    private JTextArea editorTextArea;
    private JTable tokensTable;
    private JTextArea console;
    private DefaultTableModel tableModel;


    public Gui() {
        createGui();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        Action runAction = new AbstractAction("Run") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = editorTextArea.getText();
                Lexer lexer = new Lexer(s);
                lexer.run();
                Vector<Token> tokens = lexer.getTokens();
                writeTokenTable(tokens);
            }
        };

        JToggleButton runMenu = new JToggleButton(runAction);
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        mainFrame.add(menuBar);
        mainFrame.setJMenuBar(menuBar);
    }

    private void createGui() {
        mainFrame = new JFrame("LEXER");
        mainFrame.setSize(500, 500);
        createMenu();
        createMiddlePanel();
        createConsole();
        mainFrame.setLayout(new GridLayout(2, 1)); // 2 rows 1 column
        mainFrame.setVisible(true);
    }


    private void createMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 2));
        createEditor();
        middlePanel.add(editorTextArea);
        createTokensTable();
        JScrollPane scrollPane = new JScrollPane(tokensTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Token Table"));
        middlePanel.add(scrollPane);
        mainFrame.add(middlePanel);
    }

    private void createEditor() {
        editorTextArea = new JTextArea();
        editorTextArea.setLineWrap(true);
        editorTextArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Source Code"));

    }

    private void createTokensTable() {
        String[] columnNames = { "Line", "Token", "String or word" };
        tableModel = new DefaultTableModel(columnNames, 0);
        tokensTable = new JTable(tableModel);
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
            System.out.println("Non empty. Clearing rows now!");
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        } else {
            System.out.println("empty. So no clearing");
        }

    }

    private void createConsole() {
        console = new JTextArea();
        console.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Console"));
        console.setLineWrap(true);
        console.setBackground(Color.gray);
        mainFrame.add(console);
    }



    public static void main(String[] args) {
        Gui gui = new Gui();
    }
}
