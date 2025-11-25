package com.lms.services;

import com.lms.dto.UserDTO;
import com.lms.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserServices {

    public static int addUser(UserDTO user) {
        int userId = 0;
        Connection connection = null;
        PreparedStatement preSat = null;
        try {
            connection = JDBCUtil.getConnection();
            String query = "insert into users(name,email,password,status) values(?,?,?,?)";
            preSat = connection.prepareStatement(query, preSat.RETURN_GENERATED_KEYS);
            preSat.setString(1, user.getUserName());
            preSat.setString(2, user.getUserEmail());
            preSat.setString(3, user.getUserPassword());
            preSat.setBoolean(4, user.getStatus());
            preSat.execute();
            ResultSet rs = preSat.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static boolean updateUser(UserDTO user, int id) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preSat = null;
        try {
            connection = JDBCUtil.getConnection();
            String query = "update users set name=?, email=?, password=? where id=?";
            preSat = connection.prepareStatement(query);
            preSat.setString(1, user.getUserName());
            preSat.setString(2, user.getUserEmail());
            preSat.setString(3, user.getUserPassword());
            preSat.setInt(4, id);
            int rowsAffected = preSat.executeUpdate();
            if (rowsAffected > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<UserDTO> getAllUsers() {
        Connection connection = null;
        PreparedStatement preSat = null;
        ArrayList<UserDTO> userList = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from users order by id";
            preSat = connection.prepareStatement(query);
            ResultSet rs = preSat.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserId(rs.getInt(1));
                user.setUserName(rs.getString("name"));
                user.setUserEmail(rs.getString("email"));
                user.setUserPassword(rs.getString("password"));
                user.setStatus(rs.getBoolean("status"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static ArrayList<UserDTO> getAllBlockedUsers() {
        Connection connection = null;
        PreparedStatement preSat = null;
        ArrayList<UserDTO> userList = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from users where status = false order by id";
            preSat = connection.prepareStatement(query);
            ResultSet rs = preSat.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserId(rs.getInt(1));
                user.setUserName(rs.getString("name"));
                user.setUserEmail(rs.getString("email"));
                user.setUserPassword(rs.getString("password"));
                user.setStatus(rs.getBoolean("status"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static UserDTO getUserById(int userId) {
        Connection connection = null;
        PreparedStatement preSat = null;
        UserDTO user = null;
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from users where id=?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, userId);
            ResultSet rs = preSat.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setUserId(rs.getInt(1));
                user.setUserName(rs.getString("name"));
                user.setUserEmail(rs.getString("email"));
                user.setUserPassword(rs.getString("password"));
                user.setStatus(rs.getBoolean("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static boolean blockUser(int userId) {
        Connection connection = null;
        PreparedStatement preSat = null;
        boolean result = false;
        try {
            connection = JDBCUtil.getConnection();
            String query = "update users set status=? where id=?";
            preSat = connection.prepareStatement(query);
            preSat.setBoolean(1, false);
            preSat.setInt(2, userId);
            result = preSat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean unblockUser(int userId) {
        Connection connection = null;
        PreparedStatement preSat = null;
        boolean result = false;
        try {
            connection = JDBCUtil.getConnection();
            String query = "update users set status=? where id=?";
            preSat = connection.prepareStatement(query);
            preSat.setBoolean(1, true);
            preSat.setInt(2, userId);
            result = preSat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean deleteUser(int userId) {
        Connection connection = null;
        PreparedStatement preSat = null;
        boolean result = false;
        try {
            connection = JDBCUtil.getConnection();
            String query = "delete from users where id=?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, userId);
            result = preSat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
