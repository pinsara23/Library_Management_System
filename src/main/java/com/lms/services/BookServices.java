package com.lms.services;

import com.lms.dto.BookDto;
import com.lms.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookServices {

    public int createBook(BookDto book){
        boolean result = false;
        Connection connection = null;
        PreparedStatement preSat = null;
        int id = 0;
        try {
            connection = JDBCUtil.getConnection();
            String query = "insert into books(name,author,publisher,isbn,copies) values(?,?,?,?,?)";
            preSat = connection.prepareStatement(query,preSat.RETURN_GENERATED_KEYS);
            preSat.setString(1,book.getBookName());
            preSat.setString(2,book.getBookAuthor());
            preSat.setString(3,book.getBookPublisher());
            preSat.setLong(4,book.getBookISBN());
            preSat.setInt(5,book.getNoOfCopies());
            preSat.execute();
            ResultSet rs = preSat.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public ArrayList<BookDto> getAllBooks(){
        Connection connection = null;
        PreparedStatement preSat = null;
        ArrayList<BookDto> bookList = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from books";
            preSat = connection.prepareStatement(query);
            ResultSet rs = preSat.executeQuery();
            while (rs.next()) {
                BookDto book = new BookDto();
                book.setBookId(rs.getInt("id"));
                book.setBookName(rs.getString("name"));
                book.setBookAuthor(rs.getString("author"));
                book.setBookPublisher(rs.getString("publisher"));
                book.setBookISBN(rs.getLong("isbn"));
                book.setNoOfCopies(rs.getInt("copies"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public boolean updateNoOfCopies(int amount, int bookId){
        boolean result = true;
        Connection connection = null;
        PreparedStatement preSat = null;
        try {
            connection = JDBCUtil.getConnection();
            String query = "update books set copies = ? where id = ?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, amount);
            preSat.setInt(2, bookId);
            result = preSat.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public BookDto getBookById(int bookId){
        BookDto book = new BookDto();
        Connection connection = null;
        PreparedStatement preSat = null;
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from books where id = ?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, bookId);
            ResultSet rs = preSat.executeQuery();
            if (rs.next()){
                book.setBookId(rs.getInt("id"));
                book.setBookName(rs.getString("name"));
                book.setBookAuthor(rs.getString("author"));
                book.setBookPublisher(rs.getString("publisher"));
                book.setBookISBN(rs.getLong("isbn"));
                book.setNoOfCopies(rs.getInt("copies"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public int getCopiesById(int bookId){
        Connection connection = null;
        PreparedStatement preSat = null;
        int noOfCopies = 0;
        try {
            connection = JDBCUtil.getConnection();
            String query = "select copies from books where id = ?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, bookId);
            ResultSet rs = preSat.executeQuery();
            if (rs.next()){
                noOfCopies = rs.getInt("copies");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return noOfCopies;
    }

    public boolean increaseCopies(int bookId){
        boolean result = true;
        Connection connection = null;
        PreparedStatement preSat = null;
        int currentCopies = getCopiesById(bookId);
        try {
            connection = JDBCUtil.getConnection();
            String query = "update books set copies = ? where id = ?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, ++currentCopies);
            preSat.setInt(2, bookId);
            result = preSat.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean decreaseCopies(int bookId){
        boolean result = true;
        Connection connection = null;
        PreparedStatement preSat = null;
        int currentCopies = getCopiesById(bookId);
        try {
            connection = JDBCUtil.getConnection();
            String query = "update books set copies = ? where id = ?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, --currentCopies);
            preSat.setInt(2, bookId);
            result = preSat.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}



