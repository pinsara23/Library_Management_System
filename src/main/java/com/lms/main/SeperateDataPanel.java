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

public class SeperateDataPanel extends JPanel{

    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public SeperateDataPanel(){

        requestFocusInWindow();
        setLayout(new GridLayout(1,2));

        int userId = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter User Id"));

        createBookTable();

        JPanel leftPanel = new JPanel(new GridLayout(2,1));

        JPanel leftTopPanel = new JPanel(new BorderLayout());

        JPanel leftTopData = new JPanel(new GridLayout(5,2,5,5));
        leftTopData.setBackground(Color.RED);

        JPanel leftTopName = new JPanel();
        leftTopName.setBackground(Color.RED);

        leftTopName.add(new JLabel("User Data",JLabel.CENTER){
            @Override
            public void setFont(Font font) {
                super.setFont(new Font("Arial", Font.BOLD, 30
                ));
            }
        });

        UserDTO user = UserServices.getUserById(userId);
        leftTopData.add(new JLabel("User Id", SwingConstants.CENTER));
        leftTopData.add(new JLabel(String.valueOf(user.getUserId())));

        leftTopData.add(new JLabel("Name",SwingConstants.CENTER));
        leftTopData.add(new JLabel(user.getUserName()));

        leftTopData.add(new JLabel("Email",SwingConstants.CENTER));
        leftTopData.add(new JLabel(user.getUserEmail()));

        leftTopData.add(new JLabel("Status",SwingConstants.CENTER));
        leftTopData.add(new JLabel(String.valueOf(user.getStatus())));

        leftTopPanel.add(leftTopData,BorderLayout.CENTER);
        leftTopPanel.add(leftTopName,BorderLayout.NORTH);

        JPanel leftBottomPanel = new JPanel(new BorderLayout());

        JPanel leftBottomData = new JPanel();

        leftBottomData.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftBottomData.setBackground(Color.GREEN);
        createBookTable();
        scrollPane.setPreferredSize(new Dimension(400,400));
        leftBottomData.add(scrollPane);


        JPanel leftBottomName = new JPanel(new BorderLayout());
        leftBottomName.setBackground(Color.GREEN);
        leftBottomName.add(new JLabel("Book Data",SwingConstants.CENTER){
            @Override
            public void setFont(Font font) {
                super.setFont(new Font("Arial", Font.BOLD, 30));
            }
        },BorderLayout.CENTER);

        JButton bookDataButton = new JButton("Book Data");
        bookDataButton.addActionListener(e->{

            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Book Id"));
                refreshBookTable(id);
            } catch (RuntimeException ex) {
                return;
            }
        });

        leftBottomName.add(bookDataButton,BorderLayout.EAST);

        leftBottomPanel.add(leftBottomData,BorderLayout.CENTER);
        leftBottomPanel.add(leftBottomName,BorderLayout.NORTH);


        leftPanel.add(leftTopPanel);
        leftPanel.add(leftBottomPanel);


        JPanel rightPanel = new JPanel(new GridLayout(5,2,5,5));
        rightPanel.setBackground(Color.BLUE);

        add(leftPanel);
        add(rightPanel);
    }

    public void createBookTable(){

        String [] columnNames = {"Book id","Book Name","Author","Publisher","ISBN","No of Copies"};
        model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return  false;}
        };
        JTable recordTable = new JTable(model);
        scrollPane = new JScrollPane(recordTable);

    }

    private void refreshBookTable(int id){

        model.setRowCount(0);
        BookDto book = new BookServices().getBookById(id);

        Object [] row = {book.getBookId(),book.getBookName(),book.getBookAuthor(),book.getBookPublisher(),book.getBookISBN(),book.getNoOfCopies()};
        model.addRow(row);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Record Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new SeperateDataPanel());
        frame.setVisible(true);
    }
}
