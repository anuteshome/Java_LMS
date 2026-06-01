package com.library.database;

import com.library.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Data Access Object for Members
 */
public class MemberDAO {

    // ===================== SAVE =====================
    public static boolean addMember(Member m) {
        String sql = "INSERT INTO members (name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getPhone());
            ps.setString(4, m.getAddress());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ addMember error: " + e.getMessage());
            return false;
        }
    }

    // ===================== SEARCH =====================
    public static ObservableList<Member> getAllMembers() {
        ObservableList<Member> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM members ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("❌ getAllMembers error: " + e.getMessage());
        }
        return list;
    }

    public static ObservableList<Member> searchMembers(String keyword) {
        ObservableList<Member> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM members WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("❌ searchMembers error: " + e.getMessage());
        }
        return list;
    }

    // ===================== UPDATE =====================
    public static boolean updateMember(Member m) {
        String sql = "UPDATE members SET name=?, email=?, phone=?, address=?, is_active=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getName());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getPhone());
            ps.setString(4, m.getAddress());
            ps.setBoolean(5, m.isActive());
            ps.setInt(6, m.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ updateMember error: " + e.getMessage());
            return false;
        }
    }

    // ===================== DELETE =====================
    public static boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ deleteMember error: " + e.getMessage());
            return false;
        }
    }

    private static Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getBoolean("is_active")
        );
    }
}
