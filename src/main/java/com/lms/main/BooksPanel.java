package com.lms.main;

import com.lms.dto.BookDto;
import com.lms.dto.RecordsDto;
import com.lms.services.BookServices;
import com.lms.services.RecordServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BooksPanel extends JPanel {

    private DefaultTableModel model = null;
    private Timer timer;
    private JScrollPane scrollPanel = null;


    BooksPanel(CardLayout cardLayout, JPanel mainPanel) {
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
        userPanelButton.addActionListener(e->{
            cardLayout.show(mainPanel,"userPanel");
        });
        upperPanel.add(userPanelButton,BorderLayout.EAST);

        JButton recordPanelButton  = new JButton("Record Panel");
        recordPanelButton.addActionListener(e->{
            cardLayout.show(mainPanel,"recordPanel");
        });
        upperPanel.add(recordPanelButton,BorderLayout.WEST);

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
                    refreshTable();
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null,"ISBN Or No of copies is incorrect");
            }
        });

        editBooklButton.addActionListener(e -> {

            int id = 0;
            try {
                id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID to edit:"));
            }catch (RuntimeException ex) {
                return;
            }
            BookDto book = new BookDto();
            book = new BookServices().getBookById(id);

            JTextField bookNameTextField = new JTextField(book.getBookName());
            JTextField bookAuthorTextField = new JTextField(book.getBookAuthor());
            JTextField bookPublisherTextField = new JTextField(book.getBookPublisher());
            JTextField bookISBNTextField = new JTextField(String.valueOf(book.getBookISBN()));
            JTextField noOfCopiesTextField = new JTextField(String.valueOf(book.getNoOfCopies()));

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
                    book.setBookName(bookNameTextField.getText());
                    book.setBookAuthor(bookAuthorTextField.getText());
                    book.setBookPublisher(bookPublisherTextField.getText());
                    book.setBookISBN(Long.parseLong(bookISBNTextField.getText()));
                    book.setNoOfCopies(Integer.parseInt(noOfCopiesTextField.getText()));
                    BookServices bookServices = new BookServices();
                    bookServices.updateBook(id,book);
                    refreshTable();
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null,"ISBN Or No of copies is incorrect");
            }
        });

        borrowBookButton.addActionListener(e -> {

            try {
                RecordsDto record = new RecordsDto();
                record.setUserId(Integer.parseInt(JOptionPane.showInputDialog("Enter User ID:")));
                record.setBookId(Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:")));
                record.setIssueDate(new java.util.Date());
                int id = RecordServices.borrowBook(record);
                if (id != 0) {
                    new BookServices().decreaseCopies(record.getBookId());
                    JOptionPane.showMessageDialog(null, "Book Borrowed Successfully. Record ID: " + id);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Book Borrowing Failed.");
                }

            }catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, "Book Borrowing Failed.");
                return;
            }
        });

        returnBookButton.addActionListener(e -> {

            try{

                int recordId = Integer.parseInt(JOptionPane.showInputDialog("Enter Record ID:"));
                RecordsDto record = new RecordServices().getRecordById(recordId);

                if (record.isReturned()){
                    JOptionPane.showMessageDialog(null, "This book is not borrowed or already returned.");
                    return;
                }

                boolean result = RecordServices.returnBook(recordId);
                if(result){
                    new BookServices().increaseCopies(record.getBookId());
                    refreshTable();
                    JOptionPane.showMessageDialog(null, "Book Returning Success.");
                }else {
                    JOptionPane.showMessageDialog(null, "Book Returning Failed.");
                }

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, "Book Return Failed.");
                return;
            }
        });


        add(buttonContainer, BorderLayout.SOUTH);
        revalidate();


    }

    public void createTable(){
        String [] columnNames = {"Book ID", "Book Name", "Author", "ISBN", "Publisher", "No of Copies"};
        model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){//prevent editing from frontend
                return false;
            }
        };
        JTable booksTable = new JTable(model);
        scrollPanel = new JScrollPane(booksTable);
        refreshTable();

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
