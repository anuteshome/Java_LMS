package com.library.database;

import com.library.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Data Access Object for Books
 * Handles all database operations: Save, Search, Update, Delete
 */
public class BookDAO {

    // ===================== SAVE (INSERT) =====================
    public static boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, category, quantity, available) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getQuantity());
            ps.setInt(6, book.getQuantity()); // available = quantity on insert
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ addBook error: " + e.getMessage());
            return false;
        }
    }

    // ===================== SEARCH =====================
    public static ObservableList<Book> getAllBooks() {
        ObservableList<Book> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM books ORDER BY title";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ getAllBooks error: " + e.getMessage());
        }
        return list;
    }

    public static ObservableList<Book> searchBooks(String keyword) {
        ObservableList<Book> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? OR category LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ searchBooks error: " + e.getMessage());
        }
        return list;
    }

    // ===================== UPDATE =====================
    public static boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, category=?, quantity=?, available=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getQuantity());
            ps.setInt(6, book.getAvailable());
            ps.setInt(7, book.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ updateBook error: " + e.getMessage());
            return false;
        }
    }

    // ===================== DELETE =====================
    public static boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ deleteBook error: " + e.getMessage());
            return false;
        }
    }

    // ===================== HELPER =====================
    private static Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("isbn"),
            rs.getString("category"),
            rs.getInt("quantity"),
            rs.getInt("available")
        );
    }
}
