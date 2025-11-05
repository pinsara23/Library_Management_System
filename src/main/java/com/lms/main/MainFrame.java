package com.lms.main;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    public static void main(String[] args) {

        JFrame mainFrame = new JFrame("Library Management System");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(screenSize.width, screenSize.height);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        BooksPanel booksPanel = new BooksPanel();
        mainFrame.add(booksPanel);
        mainFrame.setVisible(true);
    }


}
