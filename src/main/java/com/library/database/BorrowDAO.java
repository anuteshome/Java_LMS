package com.library.database;

import com.library.model.BorrowRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BorrowDAO {

    // ===================== BORROW A BOOK =====================
    public static boolean borrowBook(int bookId, int memberId, String dueDate) {
        String insertSql = "INSERT INTO borrow_records (book_id, member_id, due_date, status) " +
                           "VALUES (?, ?, ?, 'BORROWED')";
        String updateSql = "UPDATE books SET available = available - 1 WHERE id=? AND available > 0";

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false); // Transaction

            try (PreparedStatement ps1 = conn.prepareStatement(insertSql);
                 PreparedStatement ps2 = conn.prepareStatement(updateSql)) {

                ps1.setInt(1, bookId);
                ps1.setInt(2, memberId);
                ps1.setString(3, dueDate);
                int r1 = ps1.executeUpdate();

                ps2.setInt(1, bookId);
                int r2 = ps2.executeUpdate();

                if (r1 > 0 && r2 > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("❌ borrowBook error: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // ===================== RETURN A BOOK =====================
    public static boolean returnBook(int recordId) {
        String updateRecord = "UPDATE borrow_records SET return_date=CURDATE(), status='RETURNED' WHERE id=?";
        String updateBook   = "UPDATE books SET available = available + 1 " +
                              "WHERE id = (SELECT book_id FROM borrow_records WHERE id=?)";

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(updateRecord);
                 PreparedStatement ps2 = conn.prepareStatement(updateBook)) {

                ps1.setInt(1, recordId);
                ps2.setInt(1, recordId);

                int r1 = ps1.executeUpdate();
                int r2 = ps2.executeUpdate();

                if (r1 > 0 && r2 > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("❌ returnBook error: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // ===================== SEARCH BORROW RECORDS =====================
    public static ObservableList<BorrowRecord> getAllBorrowRecords() {
        ObservableList<BorrowRecord> list = FXCollections.observableArrayList();
        String sql = "SELECT br.id, br.book_id, br.member_id, b.title, m.name, " +
                     "br.borrow_date, br.due_date, br.return_date, br.status " +
                     "FROM borrow_records br " +
                     "JOIN books b ON br.book_id = b.id " +
                     "JOIN members m ON br.member_id = m.id " +
                     "ORDER BY br.borrow_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new BorrowRecord(
                    rs.getInt("id"),
                    rs.getInt("book_id"),
                    rs.getInt("member_id"),
                    rs.getString("title"),
                    rs.getString("name"),
                    rs.getString("borrow_date") != null ? rs.getString("borrow_date") : "",
                    rs.getString("due_date")    != null ? rs.getString("due_date")    : "",
                    rs.getString("return_date") != null ? rs.getString("return_date") : "Not Returned",
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ getAllBorrowRecords error: " + e.getMessage());
        }
        return list;
    }

    public static ObservableList<BorrowRecord> getActiveBorrows() {
        ObservableList<BorrowRecord> list = FXCollections.observableArrayList();
        String sql = "SELECT br.id, br.book_id, br.member_id, b.title, m.name, " +
                     "br.borrow_date, br.due_date, br.return_date, br.status " +
                     "FROM borrow_records br " +
                     "JOIN books b ON br.book_id = b.id " +
                     "JOIN members m ON br.member_id = m.id " +
                     "WHERE br.status = 'BORROWED' ORDER BY br.due_date";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new BorrowRecord(
                    rs.getInt("id"),
                    rs.getInt("book_id"),
                    rs.getInt("member_id"),
                    rs.getString("title"),
                    rs.getString("name"),
                    rs.getString("borrow_date") != null ? rs.getString("borrow_date") : "",
                    rs.getString("due_date")    != null ? rs.getString("due_date")    : "",
                    "Not Returned",
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ getActiveBorrows error: " + e.getMessage());
        }
        return list;
    }
}
