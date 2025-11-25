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

        //main  layout
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        //screens in program
        BooksPanel booksPanel = new BooksPanel(cardLayout, mainPanel);
        UserPanel userPanel = new UserPanel(cardLayout, mainPanel);

        //userdefined screen names
        mainPanel.add(booksPanel,"booksPanel");
        mainPanel.add(userPanel,"userPanel");

        mainFrame.add(mainPanel);
        cardLayout.show(mainPanel,"booksPanel");

        mainFrame.setVisible(true);
    }


}
