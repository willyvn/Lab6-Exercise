package com.student.dao;

import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentmanagementmvc?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    // Lấy tổng số sinh viên (Dashboard)
    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy danh sách sinh viên (có filter và sort)
    public List<Student> getStudentsFiltered(String major, String sortBy, String sortOrder) {
        List<Student> students = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM students");

        if (major != null && !major.isEmpty()) {
            sql.append(" WHERE major = ?");
        }

        String validatedSortBy = validateSortBy(sortBy);
        String validatedSortOrder = validateOrder(sortOrder);
        sql.append(" ORDER BY ").append(validatedSortBy).append(" ").append(validatedSortOrder);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (major != null && !major.isEmpty()) {
                pstmt.setString(1, major);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Tìm kiếm sinh viên theo tên hoặc email
    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE full_name LIKE ? OR email LIKE ? ORDER BY id DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapRowToStudent(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET full_name = ?, email = ?, major = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getMajor());
            pstmt.setInt(4, student.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Helper methods ---
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setMajor(rs.getString("major"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        return student;
    }

    private String validateSortBy(String sortBy) {
        List<String> validColumns = Arrays.asList("id", "student_code", "full_name", "email", "major");
        if (sortBy != null && validColumns.contains(sortBy)) return sortBy;
        return "id";
    }

    private String validateOrder(String order) {
        if ("desc".equalsIgnoreCase(order)) return "DESC";
        return "ASC";
    }
}
