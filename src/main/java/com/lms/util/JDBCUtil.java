package com.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "2003";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/LMS";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }

        try {
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (SQLException e) {

            System.out.println(e.getMessage());
            return null;
        }
//		if (connection != null) {
//			System.out.println("Connection is running");
//		}
        return connection;
    }





}
