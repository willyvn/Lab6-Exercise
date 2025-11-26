package controller;

import com.student.dao.StudentDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO(); // initialize your DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login?message=Please login first");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get total students using the new DAO method
        int totalStudents = studentDAO.getTotalStudents();

        request.setAttribute("user", user);
        request.setAttribute("totalStudents", totalStudents);

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
