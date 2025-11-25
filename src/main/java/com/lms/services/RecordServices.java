package com.lms.services;

import com.lms.dto.RecordsDto;
import com.lms.util.JDBCUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class RecordServices {

    public static Date getReturnDate(java.util.Date issueDate){

        LocalDate localDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate returnDate = localDate.plusDays(7);
        return Date.valueOf(returnDate);
    }

    public static Date extendReturn(int days, int id){

        java.util.Date utilDate = RecordServices.getRecordById(id).getReturnDate();
        LocalDate localDate = ((java.sql.Date)utilDate).toLocalDate();

        LocalDate returnDate = localDate.plusDays(days);
        return Date.valueOf(returnDate);
    }

    public static int calculateOverdueDays(java.util.Date returnDate){

        LocalDate localReturnDate = ((java.sql.Date)returnDate).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(localReturnDate)){
            return (int) (currentDate.toEpochDay() - localReturnDate.toEpochDay());
        } else {
            return 0;
        }
    }

    public static int borrowBook(RecordsDto recordsDto){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int recordId = 0;

        try {
            connection = JDBCUtil.getConnection();
            String query = "INSERT INTO records (userid, bookid, issuedate, returndate, isreturned) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, recordsDto.getUserId());
            preparedStatement.setInt(2, recordsDto.getBookId());
            preparedStatement.setDate(3, new java.sql.Date(recordsDto.getIssueDate().getTime()));
            preparedStatement.setDate(4, getReturnDate(recordsDto.getIssueDate()));
            preparedStatement.setBoolean(5, false);
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                recordId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recordId;
    }

    public static boolean returnBook(int recordId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = false;

        try {
            connection = JDBCUtil.getConnection();
            String query = "UPDATE records SET isreturned = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, recordId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean addExtraDays(int recordId, int days){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = false;
        try {
            connection = JDBCUtil.getConnection();
            String query = "update records set returndate = ? where id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1,extendReturn(days,recordId));
            preparedStatement.setInt(2,recordId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static RecordsDto getRecordById(int id) {
        Connection connection = null;
        PreparedStatement preSat = null;
        RecordsDto record = null;
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from records where id = ?";
            preSat = connection.prepareStatement(query);
            preSat.setInt(1, id);
            ResultSet rs = preSat.executeQuery();
            if (rs.next()) {
                record = new RecordsDto();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("userid"));
                record.setBookId(rs.getInt("bookid"));
                record.setIssueDate(rs.getDate("issuedate"));
                record.setReturnDate(rs.getDate("returndate"));
                record.setOverdueDays(rs.getInt("overduedays"));
                record.setReturned(rs.getBoolean("isreturned"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }

    public static ArrayList<RecordsDto> getAllRecords() {
        Connection connection = null;
        PreparedStatement preSat = null;
        ArrayList<RecordsDto> recordList = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from records order by id";
            preSat = connection.prepareStatement(query);
            ResultSet rs = preSat.executeQuery();
            while (rs.next()) {
                RecordsDto record = new RecordsDto();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("userid"));
                record.setBookId(rs.getInt("bookid"));
                record.setIssueDate(rs.getDate("issuedate"));
                record.setReturnDate(rs.getDate("returndate"));
                record.setOverdueDays(rs.getInt("overduedays"));
                record.setReturned(rs.getBoolean("isreturned"));
                recordList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordList;
    }

    public static ArrayList<RecordsDto> getAllNotReturnedRecords() {
        Connection connection = null;
        PreparedStatement preSat = null;
        ArrayList<RecordsDto> recordList = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from records where isreturned = false order by id";
            preSat = connection.prepareStatement(query);
            ResultSet rs = preSat.executeQuery();
            while (rs.next()) {
                RecordsDto record = new RecordsDto();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("userid"));
                record.setBookId(rs.getInt("bookid"));
                record.setIssueDate(rs.getDate("issuedate"));
                record.setReturnDate(rs.getDate("returndate"));
                record.setOverdueDays(rs.getInt("overduedays"));
                record.setReturned(rs.getBoolean("isreturned"));
                recordList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordList;
    }

    public static ArrayList<RecordsDto> getAllOverduedRecords() {
        Connection connection = null;
        PreparedStatement preSat = null;
        ArrayList<RecordsDto> recordList = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String query = "select * from records where overduedays>0  order by id";
            preSat = connection.prepareStatement(query);
            ResultSet rs = preSat.executeQuery();
            while (rs.next()) {
                RecordsDto record = new RecordsDto();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("userid"));
                record.setBookId(rs.getInt("bookid"));
                record.setIssueDate(rs.getDate("issuedate"));
                record.setReturnDate(rs.getDate("returndate"));
                record.setOverdueDays(rs.getInt("overduedays"));
                record.setReturned(rs.getBoolean("isreturned"));
                recordList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordList;
    }

    //to find overdue days not returned books
    public static void addOverdue(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{

            connection = JDBCUtil.getConnection();
            ArrayList<RecordsDto> records = getAllNotReturnedRecords();
            for (RecordsDto rec : records){
                int overdueDays = calculateOverdueDays(rec.getReturnDate());
                String query = "update records set overduedays = ? where id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,overdueDays);
                preparedStatement.setInt(2,rec.getId());
                preparedStatement.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }




}
