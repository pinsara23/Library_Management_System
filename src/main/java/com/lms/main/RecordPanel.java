package com.lms.main;

import com.lms.dto.BookDto;
import com.lms.dto.RecordsDto;
import com.lms.dto.UserDTO;
import com.lms.services.BookServices;
import com.lms.services.RecordServices;
import com.lms.services.UserServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class RecordPanel extends JPanel {

    private DefaultTableModel model;
    private JScrollPane scrollPanel;

    public RecordPanel(CardLayout cardLayout, JPanel mainPanel){

        setFocusable(true);
        requestFocusInWindow();
        setLayout(new BorderLayout());

        //upper panel of the record panel
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBackground(new Color(110, 47, 250));

        JLabel titleLabel = new JLabel("Not Returned Records",SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        upperPanel.add(titleLabel,BorderLayout.CENTER);

        JButton bookPanelButton = new JButton("Book Panel");
        bookPanelButton.addActionListener(e->{
            cardLayout.show(mainPanel,"booksPanel");
        });
        upperPanel.add(bookPanelButton,BorderLayout.EAST);

        add(upperPanel, BorderLayout.NORTH);

        //center panel of the record panel

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer,BoxLayout.X_AXIS));
        buttonContainer.setBackground(new Color(133, 81, 245));

        JButton extendDateButton = new JButton("Extend Date");
        JRadioButton getAllRecordsButton = new JRadioButton("Get All Records");
        JRadioButton getOverdueRecordsButton = new JRadioButton("Get Overdue Records");
        JRadioButton getNotReturnedRecordsButton = new JRadioButton("Get Not Returned Records");
        JButton recordDetailsButton = new JButton("Record Details");
        JButton filterButton = new JButton("Filter");

        ButtonGroup buttonGroupForFilters = new ButtonGroup();
        buttonGroupForFilters.add(getAllRecordsButton);
        buttonGroupForFilters.add(getOverdueRecordsButton);
        buttonGroupForFilters.add(getNotReturnedRecordsButton);

        // filter process
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel,BoxLayout.Y_AXIS));
        filterPanel.add(new JLabel("Select one:"));
        filterPanel.add(Box.createVerticalGlue());
        filterPanel.add(getAllRecordsButton);
        filterPanel.add(Box.createVerticalGlue());
        filterPanel.add(getOverdueRecordsButton);
        filterPanel.add(Box.createVerticalGlue());
        filterPanel.add(getNotReturnedRecordsButton);
        filterPanel.add(Box.createVerticalGlue());

        filterButton.addActionListener(e->{

            int filterResult = JOptionPane.showConfirmDialog(null,filterPanel,"Filter Options",JOptionPane.OK_CANCEL_OPTION);

            if(filterResult == JOptionPane.OK_OPTION){

                if(getAllRecordsButton.isSelected()){
                    titleLabel.setText("All Records");
                    refreshTableWithAllrecords();

                } else if (getOverdueRecordsButton.isSelected()) {
                    titleLabel.setText("Overdue Records");
                    refreshTablewithOverduedRecords();

                } else if (getNotReturnedRecordsButton.isSelected()) {
                    titleLabel.setText("Not Returned Records");
                    refreshTable();

                }else {

                    JOptionPane.showMessageDialog(null,"Please select a filter option","No Option Selected",JOptionPane.WARNING_MESSAGE);
                    titleLabel.setText("Not Returned Records");
                    refreshTable();
                }

            }
        });

        recordDetailsButton.addActionListener(e->{recordDetails();});//print both user and book details using JTextFields

        extendDateButton.addActionListener(e->{addExtraDays();});

        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(extendDateButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(filterButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(recordDetailsButton);
        buttonContainer.add(Box.createHorizontalGlue());

        add(buttonContainer, BorderLayout.CENTER);

        //Bottom panel for user panel
        createTable();
        scrollPanel.setPreferredSize(new Dimension(1000, 600));//table size
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new FlowLayout(FlowLayout.CENTER));//table is in table container panel center
        tableContainer.setBackground(new Color(133, 81, 245));
        tableContainer.add(scrollPanel);

        add(tableContainer,BorderLayout.SOUTH);

    }

    public void createTable(){

        String [] columnNames = {"Record id","Book id","User id","Issued Date ","receivable Date","Overdue Days","Is Returned"};
        model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return  false;}
        };
        JTable recordTable = new JTable(model);
        scrollPanel = new JScrollPane(recordTable);
        refreshTable();
    }

    private void refreshTable(){//non returned books records

        model.setRowCount(0);
        ArrayList<RecordsDto> recordList = new ArrayList<>();
        recordList = RecordServices.getAllNotReturnedRecords();

        for (RecordsDto record : recordList) {

            Object [] row = {record.getId(), record.getBookId(), record.getUserId(),
                    record.getIssueDate(), record.getReturnDate(), record.getOverdueDays(), record.isReturned(),};

            model.addRow(row);
        }
    }

    private void refreshTableWithAllrecords(){

        model.setRowCount(0);
        ArrayList<RecordsDto> recordList = new ArrayList<>();
        recordList = RecordServices.getAllRecords();

        for (RecordsDto record : recordList) {

            Object [] row = {record.getId(), record.getBookId(), record.getUserId(),
                    record.getIssueDate(), record.getReturnDate(), record.getOverdueDays(), record.isReturned(),};

            model.addRow(row);
        }
    }

    private void refreshTablewithOverduedRecords(){

        model.setRowCount(0);
        ArrayList<RecordsDto> recordList = new ArrayList<>();
        recordList = RecordServices.getAllOverduedRecords();

        for (RecordsDto record : recordList) {

            Object [] row = {record.getId(), record.getBookId(), record.getUserId(),
                    record.getIssueDate(), record.getReturnDate(), record.getOverdueDays(), record.isReturned(),};

            model.addRow(row);
        }
    }

    private void recordDetails(){

        int recordId = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Record ID:"));
        RecordsDto record = RecordServices.getRecordById(recordId);
        UserDTO user = UserServices.getUserById(record.getUserId());
        BookDto book = new BookServices().getBookById(record.getBookId());

        JTextField usernameField = new JTextField(user.getUserName()){
            @Override
            public boolean isEditable() {
                return false;
            }
        };
        JTextField emailField = new JTextField(user.getUserEmail()){
            @Override
            public boolean isEditable() {
                return false;
            }
        };
        JTextField ststusField = new JTextField(String.valueOf(user.getStatus())){
            @Override
            public boolean isEditable() {
                return false;
            }
        };
        JTextField userIdField = new JTextField(String.valueOf(user.getUserId())){
            @Override
            public boolean isEditable() {
                return false;
            }
        };

        JTextField bookIdField = new JTextField(String.valueOf(book.getBookId())){
            @Override
            public boolean isEditable() {
                return false;
            }
        };
        JTextField bookNameField = new JTextField(book.getBookName()){
            @Override
            public boolean isEditable() {
                return false;
            }
        };
        JTextField isbnField = new JTextField(String.valueOf(book.getBookISBN())){;
            @Override
            public boolean isEditable() {
                return false;
            }
        };

        JPanel userDetailsPanel = new JPanel(new GridLayout(4,2));
        JPanel bookDetailsPanel = new JPanel(new GridLayout(3,2));

        userDetailsPanel.add(new JLabel("User ID:"));
        userDetailsPanel.add(userIdField);
        userDetailsPanel.add(new JLabel("User Name:"));
        userDetailsPanel.add(usernameField);
        userDetailsPanel.add(new JLabel("User Email:"));
        userDetailsPanel.add(emailField);
        userDetailsPanel.add(new JLabel("User Status:"));
        userDetailsPanel.add(ststusField);

        bookDetailsPanel.add(new JLabel("Book ID:"));
        bookDetailsPanel.add(bookIdField);
        bookDetailsPanel.add(new JLabel("Book Name:"));
        bookDetailsPanel.add(bookNameField);
        bookDetailsPanel.add(new JLabel("Book ISBN:"));
        bookDetailsPanel.add(isbnField);

        JPanel detailsContainer = new JPanel(new GridLayout(1,2,10,0));
        detailsContainer.add(userDetailsPanel);
        detailsContainer.add(bookDetailsPanel);

        JOptionPane.showConfirmDialog(null,detailsContainer,"Record Details",JOptionPane.CANCEL_OPTION);


    }

    private void addExtraDays(){

        JTextField recordId = new JTextField();
        JTextField extraDays = new JTextField();

        JPanel extendDatePanel = new JPanel(new GridLayout(2,2));
        extendDatePanel.add(new JLabel("Record ID:"));
        extendDatePanel.add(recordId);

        extendDatePanel.add(new JLabel("Extra Days:"));
        extendDatePanel.add(extraDays);

        int result = JOptionPane.showConfirmDialog(null,extendDatePanel,"Extend Due Date",JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){

            int recId = Integer.parseInt(recordId.getText());
            int days = Integer.parseInt(extraDays.getText());
            boolean isAdded = RecordServices.addExtraDays(recId,days);

            if (isAdded) {
                JOptionPane.showMessageDialog(null, "Due date extended successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to extend due date.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}
