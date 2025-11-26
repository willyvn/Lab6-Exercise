package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new": showNewForm(request, response); break;
            case "edit": showEditForm(request, response); break;
            case "delete": deleteStudent(request, response); break;
            case "search": searchStudents(request, response); break;
            case "filter": filterStudents(request, response); break;
            default: listStudents(request, response); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertStudent(request, response);
        } else if ("update".equals(action)) {
            updateStudent(request, response);
        } else {
            response.sendRedirect("student?action=list");
        }
    }

    // --- List all students ---
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy sort params
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        List<Student> students = studentDAO.getStudentsFiltered(null, sortBy, sortOrder);

        request.setAttribute("students", students);
        request.setAttribute("selectedMajor", "");
        request.setAttribute("sortBy", sortBy != null ? sortBy : "id");
        request.setAttribute("sortOrder", sortOrder != null ? sortOrder : "ASC");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // --- Filter by major + sort ---
    private void filterStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        List<Student> students = studentDAO.getStudentsFiltered(major, sortBy, sortOrder);

        request.setAttribute("students", students);
        request.setAttribute("selectedMajor", major != null ? major : "");
        request.setAttribute("sortBy", sortBy != null ? sortBy : "id");
        request.setAttribute("sortOrder", sortOrder != null ? sortOrder : "ASC");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // --- Search students ---
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<Student> students = studentDAO.searchStudents(keyword);

        request.setAttribute("students", students);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("selectedMajor", "");
        request.setAttribute("sortBy", "id");
        request.setAttribute("sortOrder", "ASC");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // --- Show form ---
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("isNew", true);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDAO.getStudentById(id);

        request.setAttribute("student", student);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // --- Insert/Update/Delete ---
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Student student = extractStudentFromRequest(request);

        if (!validateStudent(student, request)) {
            request.setAttribute("student", student);
            request.setAttribute("isNew", true);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.addStudent(student)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student student = extractStudentFromRequest(request);
        student.setId(id);

        if (!validateStudent(student, request)) {
            request.setAttribute("student", student);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }

    // --- Helpers ---
    private Student extractStudentFromRequest(HttpServletRequest request) {
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");
        return new Student(studentCode, fullName, email, major);
    }

    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;

        if (student.getStudentCode() == null || student.getStudentCode().isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        }

        if (student.getFullName() == null || student.getFullName().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        }

        if (student.getMajor() == null || student.getMajor().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return isValid;
    }
}
