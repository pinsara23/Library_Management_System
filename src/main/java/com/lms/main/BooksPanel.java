package com.lms.main;

import com.lms.dto.BookDto;
import com.lms.services.BookServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BooksPanel extends JPanel {

    private DefaultTableModel model = null;
    private Timer timer;
    private JScrollPane scrollPanel = null;


    BooksPanel() {
        setFocusable(true);
        requestFocusInWindow();
        //setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBackground(new Color(3, 123, 168));

        JLabel titleLabel = new JLabel("Book List",SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        upperPanel.add(titleLabel,BorderLayout.CENTER);

        JButton userPanelButton = new JButton("User Panel");
        upperPanel.add(userPanelButton,BorderLayout.EAST);

        add(upperPanel, BorderLayout.NORTH);

        createTable();

        scrollPanel.setPreferredSize(new Dimension(1000, 600));//table size

        //table is inside this pannel use for center the table
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new FlowLayout(FlowLayout.CENTER));//table is in table container panel center
        tableContainer.setBackground(new Color(119, 209, 242));
        tableContainer.add(scrollPanel);

        add(tableContainer, BorderLayout.CENTER);//main jpanal center

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer,BoxLayout.X_AXIS));
        buttonContainer.setBackground(new Color(119, 209, 242));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10,10,100,10));

        JButton addBookButton = new JButton("Add Book");
        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton editBooklButton = new JButton("Edit book");

        //buttonContainer.add(Box.createVerticalStrut(400));
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(addBookButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(borrowBookButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(returnBookButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(editBooklButton);
        buttonContainer.add(Box.createHorizontalGlue());

        addBookButton.addActionListener(e -> {

            //this is use for add multiple rows to joptionpane panel

            JTextField bookNameTextField = new JTextField();
            JTextField bookAuthorTextField = new JTextField();
            JTextField bookPublisherTextField = new JTextField();
            JTextField bookISBNTextField = new JTextField();
            JTextField noOfCopiesTextField = new JTextField();

            JPanel addBookPanel = new JPanel(new GridLayout(5,2));

            addBookPanel.add(new JLabel("Book Name:"));
            addBookPanel.add(bookNameTextField);

            addBookPanel.add(new JLabel("Author:"));
            addBookPanel.add(bookAuthorTextField);

            addBookPanel.add(new JLabel("Publisher:"));
            addBookPanel.add(bookPublisherTextField);

            addBookPanel.add(new JLabel("ISBN:"));
            addBookPanel.add(bookISBNTextField);

            addBookPanel.add(new JLabel("No of Copies:"));
            addBookPanel.add(noOfCopiesTextField);

            int result = JOptionPane.showConfirmDialog(null,addBookPanel,"Enter Details",JOptionPane.OK_CANCEL_OPTION);

            try {
                if (result == JOptionPane.OK_OPTION) {
                    BookDto book = new BookDto();
                    book.setBookName(bookNameTextField.getText());
                    book.setBookAuthor(bookAuthorTextField.getText());
                    book.setBookPublisher(bookPublisherTextField.getText());
                    book.setBookISBN(Long.parseLong(bookISBNTextField.getText()));
                    book.setNoOfCopies(Integer.parseInt(noOfCopiesTextField.getText()));
                    BookServices bookServices = new BookServices();
                    bookServices.createBook(book);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null,"ISBN Or No of copies is incorrect");
            }
        });

        add(buttonContainer, BorderLayout.SOUTH);
        revalidate();


    }



    public void createTable(){
        String [] columnNames = {"Book ID", "Book Name", "Author", "ISBN", "Publisher", "No of Copies"};
        model = new DefaultTableModel(columnNames, 0);
        JTable booksTable = new JTable(model);
        scrollPanel = new JScrollPane(booksTable);
        refreshTable();
        //add rows
        timer = new Timer(1000, e -> refreshTable());
        timer.start();


    }

    private void refreshTable(){
        model.setRowCount(0);
        ArrayList<BookDto> bookList = new ArrayList<>();
        bookList = new BookServices().getAllBooks();
        for (BookDto book : bookList) {

            Object [] row = {book.getBookId(), book.getBookName(), book.getBookAuthor(),
                    book.getBookISBN(), book.getBookPublisher(), book.getNoOfCopies()};

            model.addRow(row);
        }
    }
}
