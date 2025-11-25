package com.lms.main;

import com.lms.dto.UserDTO;
import com.lms.services.UserServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class UserPanel extends JPanel {

    private DefaultTableModel model = null;
    private Timer timer;
    private JScrollPane scrollPanel = null;

    UserPanel(CardLayout cardLayout, JPanel mainPanel){
        setFocusable(true);
        requestFocusInWindow();
        setLayout(new BorderLayout());

        //upper panel of the user panel

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBackground(new Color(34, 89, 242));

        JLabel titleLabel = new JLabel("User Panel",SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial",Font.BOLD,40));
        titleLabel.setForeground(Color.BLACK);
        upperPanel.add(titleLabel,BorderLayout.CENTER);

        JButton bookPanelButton = new JButton("Book Panel");
        bookPanelButton.addActionListener(e->{
            cardLayout.show(mainPanel,"booksPanel");
        });
        upperPanel.add(bookPanelButton,BorderLayout.WEST);

        add(upperPanel, BorderLayout.NORTH);

        //Bottom panel for user panel
        createTable();
        scrollPanel.setPreferredSize(new Dimension(1000, 600));//table size
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new FlowLayout(FlowLayout.CENTER));//table is in table container panel center
        tableContainer.setBackground(new Color(90, 132, 250));
        tableContainer.add(scrollPanel);

        add(tableContainer,BorderLayout.SOUTH);

        //Center panle of user panel(buttons)
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer,BoxLayout.X_AXIS));
        buttonContainer.setBackground(new Color(90, 132, 250));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));//hilighted

        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton blockUserButton = new JButton("Block User");
        JButton unBlockUserButton = new JButton("Unblock User");
        JButton filterButon  = new JButton("Filter User");
        JRadioButton allUsers =  new JRadioButton("All Users");
        JRadioButton blockedUsers =  new JRadioButton("Blocked Users");

        ButtonGroup buttonGroupForFilters = new ButtonGroup();
        buttonGroupForFilters.add(allUsers);
        buttonGroupForFilters.add(blockedUsers);

        JPanel filterButtonsPanel = new JPanel();
        filterButtonsPanel.setLayout(new BoxLayout(filterButtonsPanel,BoxLayout.Y_AXIS));
        filterButtonsPanel.add(new JLabel("Select one:"));
        filterButtonsPanel.add(Box.createVerticalGlue());
        filterButtonsPanel.add(allUsers);
        filterButtonsPanel.add(Box.createVerticalGlue());
        filterButtonsPanel.add(blockedUsers);
        filterButtonsPanel.add(Box.createVerticalGlue());


        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(addUserButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(editUserButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(blockUserButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(unBlockUserButton);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(filterButon);
        buttonContainer.add(Box.createHorizontalGlue());

        addUserButton.addActionListener(e ->{

            JTextField usernameField = new JTextField();
            JTextField emailField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JRadioButton trueButton = new JRadioButton("True");
            JRadioButton falseButton = new JRadioButton("False");

            ButtonGroup buttonGroup = new ButtonGroup(); // can select only one option
            buttonGroup.add(trueButton);
            buttonGroup.add(falseButton);

            JPanel stausButton = new JPanel(new FlowLayout(FlowLayout.LEFT));
            stausButton.add(trueButton);
            stausButton.add(falseButton);

            JPanel addUserPanel = new JPanel(new GridLayout(4,2));

            addUserPanel.add(new JLabel("User Name:"));
            addUserPanel.add(usernameField);

            addUserPanel.add(new JLabel("User Email:"));
            addUserPanel.add(emailField);

            addUserPanel.add(new JLabel("User Password:"));
            addUserPanel.add(passwordField);

            addUserPanel.add(new JLabel("User Status (true/false):"));
            addUserPanel.add(stausButton);

            int result = JOptionPane.showConfirmDialog(null,addUserPanel,"Add User",JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {

                boolean status = false;
                if (trueButton.isSelected()) {
                    status = true;
                }

                UserDTO newUser = new UserDTO();
                newUser.setUserName(usernameField.getText());
                newUser.setUserEmail(emailField.getText());
                newUser.setUserPassword(new String(passwordField.getPassword()));
                newUser.setStatus(status);

                int userId = UserServices.addUser(newUser);
                refreshTable();
                if (userId > 0) {
                    JOptionPane.showMessageDialog(null, "User added successfully with ID: " + userId);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add user.");
                }
            }
        });

        editUserButton.addActionListener(e ->{

            int id = Integer.parseInt(JOptionPane.showInputDialog("Please enter the ID of the user you want to edit"));
            UserDTO user  = new UserDTO();
            user  = UserServices.getUserById(id);

            JTextField usernameField = new JTextField(user.getUserName());
            JTextField emailField = new JTextField(user.getUserEmail());
            JTextField passwordField = new JTextField(user.getUserPassword());

            JPanel addUserPanel = new JPanel(new GridLayout(3,2));

            addUserPanel.add(new JLabel("User Name:"));
            addUserPanel.add(usernameField);

            addUserPanel.add(new JLabel("User Email:"));
            addUserPanel.add(emailField);

            addUserPanel.add(new JLabel("User Password:"));
            addUserPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(null,addUserPanel,"Update User",JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {

                UserDTO newUser = new UserDTO();
                newUser.setUserName(usernameField.getText());
                newUser.setUserEmail(emailField.getText());
                newUser.setUserPassword(passwordField.getText());
                UserServices.updateUser(newUser, id);
                refreshTable();

            }
        });

        blockUserButton.addActionListener(e ->{

            int id;
            try {
                id = Integer.parseInt(JOptionPane.showInputDialog("Please enter the ID of the user you want to block"));
            }catch (RuntimeException ex){
                return;
            }
            boolean result = UserServices.blockUser(id);
            refreshTable();

            if (!result){
                JOptionPane.showMessageDialog(null,"User blocked successfully");
            } else{
                JOptionPane.showMessageDialog(null,"Failed to block user");
            }

        });

        unBlockUserButton.addActionListener(e ->{

            int id;
            try {
                id = Integer.parseInt(JOptionPane.showInputDialog("Please enter the ID of the user you want to Unblock"));
            }catch (RuntimeException ex){
                return;
            }
            boolean result = UserServices.unblockUser(id);
            refreshTable();

            if (!result){
                JOptionPane.showMessageDialog(null,"User Unblocked successfully");
            } else{
                JOptionPane.showMessageDialog(null,"Failed to Unblock user");
            }

        });

        filterButon.addActionListener(e ->{

            int filterResult = JOptionPane.showConfirmDialog(null, filterButtonsPanel,"Filter Options",JOptionPane.OK_CANCEL_OPTION);
            if(filterResult == JOptionPane.OK_OPTION){
                if (allUsers.isSelected()){
                    refreshTable();
                }else if (blockedUsers.isSelected()){
                    refreshTableWithBlockedUsers();
                }
            }else {
                refreshTable();
            }
        });

        add(buttonContainer,BorderLayout.CENTER);

    }




    public void createTable(){
        String [] columnNames = {"User id","User Name","User Email","User Password","User Status"};
        model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return  false;}
        };
        JTable usertable = new JTable(model);
        scrollPanel = new JScrollPane(usertable);
        refreshTable();
    }

    private void refreshTable(){//change this function to add filters sql query for find blocked users
        model.setRowCount(0);
        ArrayList<UserDTO> userList = new ArrayList<>();
        userList = UserServices.getAllUsers();
        for (UserDTO user : userList) {

            Object [] row = {user.getUserId(), user.getUserName(), user.getUserEmail(),
                    user.getUserPassword(), user.getStatus()};

            model.addRow(row);
        }
    }

    private void refreshTableWithBlockedUsers(){//change this function to add filters sql query for find blocked users
        model.setRowCount(0);
        ArrayList<UserDTO> userList = new ArrayList<>();
        userList = UserServices.getAllBlockedUsers();
        for (UserDTO user : userList) {

            Object [] row = {user.getUserId(), user.getUserName(), user.getUserEmail(),
                    user.getUserPassword(), user.getStatus()};

            model.addRow(row);
        }
    }


}
